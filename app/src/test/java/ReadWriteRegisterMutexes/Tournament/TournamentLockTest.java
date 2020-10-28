/** TournamentLock tests
 */
package ReadWriteRegisterMutexes.Tournament;

import org.junit.Test;
import static org.junit.Assert.*;

import java.lang.Runnable;
import java.util.concurrent.locks.ReentrantLock;
import ReadWriteRegisterMutexes.Tournament.TournamentLock;

public class TournamentLockTest {

    /** Test the TournamentLock by incrementing the c shared variable 1,000,000
     * times while concurrently decrementing it another 1,000,000 times. There
     * is no guarantee of the atomicity of the increments or decrements except
     * if the lock works.
     */
    @Test
    public void testTournamentLockIncrement() {
        int numWorkers = 8;
        TournamentLock lock = new TournamentLock(numWorkers);
        Runnable[] workers = new Runnable[numWorkers];
        Thread[] threads = new Thread[numWorkers];

        // Initialize workers
        for (int i=0; i< numWorkers; i++) {
            // Even workers add, odd workers substract
            workers[i] = new WorkerThread((((i%2) == 0) ? true : false), i);
        }

        WorkerThread.c = 0;
        WorkerThread.increments = 100000;
        WorkerThread.lock = lock;

        // Spawn threads
        for (int i=0; i<numWorkers; i++) {
            threads[i] = new Thread(workers[i], "T" + i);
        }

        // Start threads
        for (int i=0; i<numWorkers; i++) {
            threads[i].start();
        }

        // Wait for threads to terminate
        for (int i=0; i<numWorkers; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                assertTrue("Exception caught for T" + i + ": " + e, false);
            }
        }

        // Check we got the right result
        assertEquals("Synchronization error: ", 0, WorkerThread.c);
        System.out.println("Success: c = " + WorkerThread.c + " expected 0");
    }
}

class WorkerThread implements Runnable {
    public volatile static int c;
    public volatile static int increments;
    public volatile static TournamentLock lock;
    public boolean add;
    public int tid;

    public WorkerThread(boolean add, int tid) {
        this.add = add;
        this.tid = tid;
    }

    public void run() {
        int workerIncs = 0; // Number of incs/decs this worker did

        // Increment instance variable c the configured number of times
        for (int i=0; i<WorkerThread.increments; i++) {
            if (this.add) {
                WorkerThread.lock.lock(this.tid);
                WorkerThread.c++;
                //System.out.println(Thread.currentThread().getName() + ": Incremented c: " + c);
                WorkerThread.lock.unlock(this.tid);
            } else {
                WorkerThread.lock.lock(this.tid);
                WorkerThread.c--;
                //System.out.println(Thread.currentThread().getName() + ": Decremented c: " + c);
                WorkerThread.lock.unlock(this.tid);
            }
            workerIncs++;
        }

        System.out.println("T" + this.tid
            + ((this.add) ? " increments: " : " decrements: ")
            + workerIncs);
    }
}

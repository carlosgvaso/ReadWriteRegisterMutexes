/*
 * Comparison of mutual exclusion algorithms based on read-write registers
 */

package ReadWriteRegisterMutexes;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
//import org.openjdk.jmh.annotations.Warmup;

import ReadWriteRegisterMutexes.Tournament.TournamentLock;

/** App class is the main class of the application
 * 
 * It runs benchmarks for all the implemented read-write register locks.
 */
public class App {
    /** Benchmark the TournamentLock for threads incrementing a shared variable
     * 
     * The benchmark measures the average time that 8 worker threads take to
     * increment/decrement a shared variable 50,000,000 times. Half of the
     * threads will increment the shared variable by 1 each time in a loop, and
     * the other half will decrement it by 1 each time in a loop. Each time that
     * any of the threads wants to increment/decrement the shared variable, they
     * must request the lock, and they release the lock immediately after. 
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void incrementTournamentLock() {
        int numWorkers = 8;
        TournamentLock lock = new TournamentLock(numWorkers);
        Runnable[] workers = new Runnable[numWorkers];
        Thread[] threads = new Thread[numWorkers];

        // Initialize workers
        for (int i=0; i< numWorkers; i++) {
            // Even workers add, odd workers subtract
            workers[i] = new WorkerThread((((i%2) == 0) ? true : false), i);
        }

        WorkerThread.c = 0;
        WorkerThread.increments = 50000000;
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
                System.out.println("ERROR: T" + i + ": " + e);
            }
        }

        // Check we got the right result
        System.out.println("Finished: c = " + WorkerThread.c + " expected 0");
    }

    /** Entry point of the App class
     * 
     * It runs benchmarks for all the implemented read-write register locks.
     */
    public static void main(String[] args) {
        // Run the benchmarks
        try {
            org.openjdk.jmh.Main.main(args);
        } catch (IOException e) {
            System.out.println("ERROR: " + e);
        }
    }
}

/** Worker thread class to increment/decrement a shared counter
 */
class WorkerThread implements Runnable {
    public volatile static int c;
    public volatile static int increments;
    public volatile static TournamentLock lock;
    public boolean add;
    public int tid;

    /** Constructor
     */
    public WorkerThread(boolean add, int tid) {
        this.add = add;
        this.tid = tid;
    }

    /** Increment/decrement shared counter
     */
    public void run() {
        // Increment instance variable c the configured number of times
        for (int i=0; i<WorkerThread.increments; i++) {
            if (this.add) {
                WorkerThread.lock.lock(this.tid);
                WorkerThread.c++;
                WorkerThread.lock.unlock(this.tid);
            } else {
                WorkerThread.lock.lock(this.tid);
                WorkerThread.c--;
                WorkerThread.lock.unlock(this.tid);
            }
        }
    }
}

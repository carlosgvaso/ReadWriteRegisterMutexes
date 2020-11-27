/*
 * Comparison of mutual exclusion algorithms based on read-write registers
 */

package ReadWriteRegisterMutexes;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
//import org.openjdk.jmh.annotations.Warmup;

import ReadWriteRegisterMutexes.Tournament.TournamentLock;
import ReadWriteRegisterMutexes.OneBit.OneBitLock;

/** App class is the main class of the application
 * 
 * It runs benchmarks for all the implemented read-write register locks.
 */
public class App {
    /** Global setting for increments in the benchmarks
     */
//    private static int gIncrements = 50000000;
    private static int gIncrements = 50;

    /** Global setting for number of threads on benchmarks with heavy contention
     */
    private static int gHeavyContentionThreadNum = 8;

    /** Global setting for number of threads on benchmarks with no contention
     */
    private static int gNoContentionThreadNum = 1;

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

    /** Benchmark the ReentrantLock for threads incrementing a shared variable
     *
     * The benchmark measures the average time that 8 worker threads take to
     * increment/decrement a shared variable 50,000,000 times. Half of the
     * threads will increment the shared variable by 1 each time in a loop, and
     * the other half will decrement it by 1 each time in a loop. Each time that
     * any of the threads wants to increment/decrement the shared variable, they
     * must request the lock, and they release the lock immediately after.
     *
     * This benchmark is designed to measure how well this lock implementation
     * performs under heavy contention.
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void heavyContentionReentrantLock() {
        ReentrantLock lockBench = new ReentrantLock();
        runIncrementBenchmark(gHeavyContentionThreadNum, gIncrements,
            lockBench);
    }

    /** Benchmark the TournamentLock for threads incrementing a shared variable
     *
     * The benchmark measures the average time that 8 worker threads take to
     * increment/decrement a shared variable 50,000,000 times. Half of the
     * threads will increment the shared variable by 1 each time in a loop, and
     * the other half will decrement it by 1 each time in a loop. Each time that
     * any of the threads wants to increment/decrement the shared variable, they
     * must request the lock, and they release the lock immediately after.
     *
     * This benchmark is designed to measure how well this lock implementation
     * performs under heavy contention.
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void heavyContentionTournamentLock() {
        TournamentLock lockBench = new TournamentLock(gHeavyContentionThreadNum);
        runIncrementBenchmark(gHeavyContentionThreadNum, gIncrements,
            lockBench);
    }

    /** Benchmark without a lock for 1 thread incrementing a shared variable
     *
     * The benchmark measures the average time that 1 worker thread takes to
     * increment a shared variable 50,000,000 times. This benchmark doesn't use
     * a lock.
     *
     * This benchmark is designed to measure how much overhead other lock
     * implementations add to the operation without any contention. This is the
     * reference benchmark of the operation without using a lock.
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void noContentionNoLock() {
        runIncrementBenchmark(gNoContentionThreadNum, gIncrements, null);
    }

    /** Benchmark the ReentrantLock for 1 thread incrementing a shared variable
     *
     * The benchmark measures the average time that 1 worker thread takes to
     * increment a shared variable 50,000,000 times. Each time that the thread
     * wants to increment the shared variable, it musts request the lock, and it
     * releases the lock immediately after.
     *
     * This benchmark is designed to measure how much overhead this lock
     * implementation adds to the operation without any contention.
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void noContentionReentrantLock() {
        ReentrantLock lockBench = new ReentrantLock();
        runIncrementBenchmark(gNoContentionThreadNum, gIncrements,
            lockBench);
    }

    /** Benchmark the TournamentLock for 1 thread incrementing a shared variable
     *
     * The benchmark measures the average time that 1 worker thread takes to
     * increment a shared variable 50,000,000 times. Each time that the thread
     * wants to increment the shared variable, it musts request the lock, and it
     * releases the lock immediately after.
     *
     * This benchmark is designed to measure how much overhead this lock
     * implementation adds to the operation without any contention.
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void noContentionTournamentLock() {
        TournamentLock lockBench = new TournamentLock(gNoContentionThreadNum);
        runIncrementBenchmark(gNoContentionThreadNum, gIncrements,
            lockBench);
    }

    /** Benchmark the OneBit for 1 thread incrementing a shared variable
     *
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void noContentionOneBitLock() {
        OneBitLock lockBench = new OneBitLock(gNoContentionThreadNum);
        runIncrementBenchmark(gNoContentionThreadNum, gIncrements, lockBench);
    }

    /** Benchmark the OneBit for threads incrementing a shared variable
     *
     * The benchmark measures the average time that 8 worker threads take to
     * increment/decrement a shared variable 50,000,000 times. Half of the
     * threads will increment the shared variable by 1 each time in a loop, and
     * the other half will decrement it by 1 each time in a loop. Each time that
     * any of the threads wants to increment/decrement the shared variable, they
     * must request the lock, and they release the lock immediately after.
     *
     * This benchmark is designed to measure how well this lock implementation
     * performs under heavy contention.
     */
    @Benchmark
    @OutputTimeUnit(TimeUnit.NANOSECONDS) // Use nanoseconds for output
    @Fork(value = 1) // Run 1 fork with no warmup forks
    //@Warmup(iterations=2) // Run that number of warmup iterations
    @BenchmarkMode(Mode.AverageTime) // Measure average time in benchmarks
    public void heavyContentionOneBitLock() {
        OneBitLock lockBench = new OneBitLock(gHeavyContentionThreadNum);
        runIncrementBenchmark(gHeavyContentionThreadNum, gIncrements, lockBench);
    }

    /** Run the increment a shared counter a set number of times per thread
     *  operation to benchmark
     * 
     * This benchmark operation consists of incrementing/decrementing by one a
     * shared counter by one or multiple threads. Even threads (starting from 0)
     * increment the counter, and odd threads decrement the counter. Depending
     * on the lock object passed, each increment/decrement operation is
     * synchronized by locking before incrementing/decrementing, and locking
     * immediately after it. This increment/decrement operation is repeated a
     * configured number of times.
     * 
     * @param numWorkers    Number of worker threads
     * @param increments    Number of increments/decrements per thread
     * @param lockObj   Lock object of type Lock or ReentrantLock, or null
     */
    private void runIncrementBenchmark(int numWorkers, int increments, Object lockObj) {
        Runnable[] workers = new Runnable[numWorkers];
        Thread[] threads = new Thread[numWorkers];

        if (lockObj == null) {
            // No lock
            // Initialize workers
            for (int i=0; i< numWorkers; i++) {
                // Even workers add, odd workers subtract
                workers[i] = new Worker(i, (((i%2) == 0) ? true : false),
                    increments);
            }
        } else if (lockObj instanceof Lock) {
            // Lock interface
            Lock lock = (Lock) lockObj;

            // Initialize workers
            for (int i=0; i< numWorkers; i++) {
                // Even workers add, odd workers subtract
                workers[i] = new Worker(i, (((i%2) == 0) ? true : false),
                    increments, lock);
            }
        } else if (lockObj instanceof ReentrantLock) {
            // ReentrantLock
            ReentrantLock lock = (ReentrantLock) lockObj;

            // Initialize workers
            for (int i=0; i< numWorkers; i++) {
                // Even workers add, odd workers subtract
                workers[i] = new Worker(i, (((i%2) == 0) ? true : false),
                    increments, lock);
            }
        } else {
            throw new IllegalArgumentException("ERROR: Unknown type of lock");
        }

        // Initialize the shared counter c
        ((Worker)workers[0]).setC(0);

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
        //System.out.println("Finished: c = " + ((Worker)workers[0]).getC()
        //    + " expected 0");
    }
}

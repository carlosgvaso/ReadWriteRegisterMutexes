/** AdaptiveLock is a lock implementation of the Adaptive Simple Algorithm by
 * M. Merritt and G. Taubenfeld.
 * 
 * This implementation is based on the pseudo-code from the Synchronization
 * Algorithms and Concurrent Programming textbook by Gadi Taubenfeld, p. 109.
 */
package ReadWriteRegisterMutexes.Adaptive;

/** TournamentLock class implements the Peterson's Tournament algorithm for locks
 */
public class AdaptiveLock implements ReadWriteRegisterMutexes.Lock {
    /** Number of threads or leaves of the tournament tree
     * 
     * This must be a power of 2. If n is not a power of 2, "dummy" threads that
     * do nothing must be added to make it a power of 2.
     */
    private int n;

    /** Constructor
     * 
     * @param numThreads    Number of threads using the lock
     */
    public AdaptiveLock(int numThreads) {
        this.n = numThreads;
        //System.out.println("TournamentLock: n = " + this.n);
    }

    /** Lock or entry protocol method
     * 
     * @param tid Thread ID
     */
    public void lock(int tid) {
        //System.out.println("Thread-" + tid + ": locking...");
    }

    /** Unlock or exit protocol method
     * 
     * @param   tid Thread ID
     */
    public void unlock(int tid) {
        //System.out.println("Thread-" + tid + ": unlocking...");
    }
}

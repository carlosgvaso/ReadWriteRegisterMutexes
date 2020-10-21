package ReadWriteRegisterMutexes.Tournament;

import java.lang.Math;
import java.util.Arrays;

/**
 * TournamentLock class implements the Peterson's Tournament algorithm
 * 
 * This implementation is based on the pseudo-code from the Synchronization
 * Algorithms and Concurrent Programming textbook by Gadi Taubenfeld at page 38.
 */
public class TournamentLock implements ReadWriteRegisterMutexes.Lock {
    // Shared variables
    /**
     * Number of threads or leaves of the tournament tree
     * 
     * This must be a power of 2. If n is not a power of 2, "dummy" threads that do
     * nothing must be added to make it a power of 2.
     */
    private int n;
    /**
     * Height of the tournament tree
     * 
     * It is obtained from n.
     */
    private int hTree;
    /**
     * Shared variable that indicates if a process wants to access the CS in each
     * node contest
     * 
     * Both registers for process 0 and 1 in each tournament are stored in the same
     * array. This way each node in the tree will have 2 entries: b[level][2*node]
     * and b[level][2*node+1], where the size of b is the height of the tree (for
     * the level dimension) by the number of leaves in the tree (for the node
     * dimension).
     */
    private volatile boolean[][] wantCS;
    /**
     * Shared variable that indicates the turn in each node contest
     * 
     * Each node in the tree will have an entry: turn[level][node], where the size
     * of turn is the height of the tree (for level dimension) by half of the number
     * of leaves of the tree (for the node dimension).
     */
    private volatile int[][] turn;

    public TournamentLock(int numThreads) {
        // your implementation goes here.

        // Initialize n to the smallest power of 2 larger or equal to numThreads
        double p = Math.ceil(Math.log((double) numThreads) / Math.log(2.0));
        this.n = (int) Math.pow(2.0, p);
        // System.out.println("TournamentLock: n = " + this.n);

        // Initialize hTree
        this.hTree = (int) Math.floor(Math.log((double) this.n) / Math.log(2.0)); // Binary tree height
        // System.out.println("TournamentLock: hTree = " + hTree);

        // Initialize b array
        this.wantCS = new boolean[this.hTree][this.n];
        for (boolean[] row : this.wantCS) {
            Arrays.fill(row, false);
        }

        // Initialize turn array
        this.turn = new int[this.hTree][this.n / 2];
    }

    /**
     * Lock or entry protocol method
     * 
     * @param tid Thread ID
     */
    public void lock(int tid) {
        // System.out.println("Thread-" + tid + ": locking...");
        int level, id, idJ;
        int node = tid; // Starting node (leave) is the thread ID

        // Iterate over all the levels of the tree to contest other threads
        for (level = 0; level < this.hTree; level++) {
            id = node % 2; // Find if process 0 or 1 for Peterson's contest
            idJ = 1 - id; // Id of other thread in the contest
            node = Math.floorDiv(node, 2); // Find next node

            this.wantCS[level][2 * node + id] = true; // Say we want to enter the CS
            this.turn[level][node] = idJ; // Set the turn to the other thread in the contest

            // Busy wait until we win the contest
            while (this.wantCS[level][2 * node + idJ] && (this.turn[level][node] == idJ)) {
                // Do nothing

                // DO NOT DELETE!!! For some reason the lock does not work
                // properly without this print statement.
                System.out.print("");
            }
        }
        
        // DO NOT DELETE!!! For some reason the lock does not work properly
        // without this print statement.
        System.out.print("");
    }

    /** Unlock or exit protocol method
     * 
     * @param   tid Thread ID
     */
    public void unlock(int tid) {
        //System.out.println("Thread-" + tid + ": unlocking...");
        int level, node, id;

        // Iterate the tree backwards to reset the values set by the thread
        for (level=this.hTree-1; level>=0; level--) {
            // Calculate the id from level and thread. For this, find previous
            // node in tree then id = nodePrev mod 2.
            id = Math.floorDiv(tid, (int)Math.pow(2.0, (double)(level))) % 2;

            // Calculate current node from level and thread
            node = Math.floorDiv(tid, (int)Math.pow(2.0, (double)(level+1)));

            // Reset wantCS entry
            this.wantCS[level][2*node+id] = false;
        }
    }
}

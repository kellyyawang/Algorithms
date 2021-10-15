/*
 * Name: Kelly Wang
 * EID: kaw4256
 */

// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.*;

public class Student {
    private int minCost;
    private int name;
    private ArrayList<Student> neighbors;
    private ArrayList<Integer> prices;
    private int minHeapIndex;		// stores the current index in the heap for changeKey() function
    public int previous;			// holds the previous node for debugging purposes in Program2
    public Student(int x) {
        name = x;
        minCost = Integer.MAX_VALUE;
        neighbors = new ArrayList<Student>();
        prices = new ArrayList<Integer>();
        previous=-1;				// initialize previous to -1
    }
    public void setIndex(Integer i) {
    	minHeapIndex=i;
    }
    public int getIndex() {
    	return minHeapIndex;
    }
    public void setNeighborAndPrice(Student n, Integer w) {
        neighbors.add(n);
        prices.add(w);
    }

    public ArrayList<Student> getNeighbors() {
        return neighbors;
    }

    public ArrayList<Integer> getPrices() {
        return prices;
    }

    public int getminCost() { return minCost; }

    public void setminCost(int x) {
        minCost = x;
    }

    public void resetminCost() {
        minCost = Integer.MAX_VALUE;
    }

    public int getName() {
        return name;
    }
}

/*
 * Name: Kelly Wang
 * EID: kaw4256
 */

// Implement your algorithms here
// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.ArrayList;

public class Program2 {
    private ArrayList<Student> students;    // this is a list of all Students, populated by Driver class
    private Heap minHeap;
    private int numStudents;
    // additional constructor fields may be added, but don't delete or modify anything already here
    public Program2(int numStudents) {
        minHeap = new Heap();
        students = new ArrayList<Student>();
        this.numStudents=numStudents;
    }

    /**
     * findMinimumStudentCost(Student start, Student dest)
     *
     * @param start - the starting Student.
     * @param dest  - the end (destination) Student.
     * @return the minimum cost possible to get from start to dest.
     * Assume the given graph is always connected.
     */
    public int findMinimumStudentCost(Student start, Student dest) {
    	int [] distance=new int[numStudents];
    	boolean [] visited=new boolean[numStudents];
    	for(Student s: students) {
    		s.resetminCost();			// initialize all students to max minimum cost
    		s.previous=-1;
    	}	
        start.setminCost(0);			// set the start student's min cost to itself as 0
        start.previous=start.getName();
        distance[start.getName()]=0;	
        minHeap.buildHeap(students);       //build the minimum heap
        while(!minHeap.isHeapEmpty()) {			
        	Student minStd=minHeap.extractMin();		// extract the minimum student from heap
        	int minStdName=minStd.getName();
        	int minStdCost=minStd.getminCost();
        	if (visited[minStdName] !=true) {			// continue if this student has not been explored already
        		ArrayList<Student> minStdNeighbors=minStd.getNeighbors();	
        		ArrayList<Integer> neighborPrices=minStd.getPrices();
        		for(int i=0; i<minStdNeighbors.size(); i++) {
        			Student neighbor =minStdNeighbors.get(i);	//examine neighbors of minStd
        			int initialCost=neighbor.getminCost();
        			int price=neighborPrices.get(i);
        			int newCost=minStdCost+price;			// newCost is potential newCost of getting to neighbor from minStd
        			if(newCost<initialCost) {
        				minHeap.changeKey(neighbor, newCost);		// update neighbor with newCost if it is less than initialCost
        				distance[neighbor.getName()]=newCost;
        				neighbor.previous=minStd.getName();
        			}	
        		
        	}
        	}
        }
        int result=distance[dest.getName()];
        return result;				// return result
    }

    /**
     * findMinimumClassCost()
     *
     * @return the minimum total cost required to connect (span) each student in the class.
     * Assume the given graph is always connected.
     */
    public int findMinimumClassCost(){
    	int mstCost=0;					// initialize mstCost to zero
    	boolean [] visited=new boolean[numStudents];
    	for(Student s: students) {
    		s.resetminCost();			// initialize all students to max minimum cost
    		s.previous=-1;
    	}
    	Student start=students.get(0);  // choose the first student as arbitrary student 
        start.setminCost(0);			
        start.previous=start.getName();
    	minHeap.buildHeap(students);	// build minimum heap
    	while( !minHeap.isHeapEmpty()) {
    		Student minEdge=minHeap.extractMin();		// extract minimum student from heap
        	int minEdgeDest=minEdge.getName();			// minEdgeDest is the node that the minEdge ends up at
        	int minEdgeCost=minEdge.getminCost();
    		if(!visited[minEdgeDest]) {
    			visited[minEdgeDest]=true;				// mark minEdge as already connected in MST
    			mstCost=mstCost+minEdgeCost;			// add the cost to mstCost
    			ArrayList<Student> minEdgeNeighbors=minEdge.getNeighbors();
        		ArrayList<Integer> neighborPrices=minEdge.getPrices();
        		for(int i=0; i<minEdgeNeighbors.size(); i++) {
        			int neighborName=minEdgeNeighbors.get(i).getName();
        			if (visited[neighborName] != true) {
        				int currentCost=minEdgeNeighbors.get(i).getminCost();
        				int potentialCost=neighborPrices.get(i);
        				if(currentCost>potentialCost) {				// if new cost is less than current cost to this node
        					minHeap.changeKey(minEdgeNeighbors.get(i), potentialCost);		//update key value in minHeap
        				}
        			}
        		}
    		}
    	}
    	return mstCost;
    }
   
    //returns edges and prices in a string.
    public String toString() {
        String o = "";
        for (Student v : students) {
            boolean first = true;
            o += "Student ";
            o += v.getName();
            o += " has neighbors ";
            ArrayList<Student> ngbr = v.getNeighbors();
            for (Student n : ngbr) {
                o += first ? n.getName() : ", " + n.getName();
                first = false;
            }
            first = true;
            o += " with prices ";
            ArrayList<Integer> wght = v.getPrices();
            for (Integer i : wght) {
                o += first ? i : ", " + i;
                first = false;
            }
            o += System.getProperty("line.separator");

        }

        return o;
    }
    

///////////////////////////////////////////////////////////////////////////////
//                           DANGER ZONE                                     //
//                everything below is used for grading                       //
//                      please do not change :)                              //
///////////////////////////////////////////////////////////////////////////////

    public Heap getHeap() {
        return minHeap;
    }

    public ArrayList<Student> getAllstudents() {
        return students;
    }

    // used by Driver class to populate each Student with correct neighbors and corresponding prices
    public void setEdge(Student curr, Student neighbor, Integer price) {
        curr.setNeighborAndPrice(neighbor, price);
    }

    // used by Driver.java and sets students to reference an ArrayList of all Students
    public void setAllNodesArray(ArrayList<Student> x) {
        students = x;
    }
}

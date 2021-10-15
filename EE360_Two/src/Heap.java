/*
 * Name: Kelly Wang
 * EID: kaw4256
 */

// Implement your heap here
// Methods may be added to this file, but don't remove anything
// Include this file in your final submission

import java.util.ArrayList;

public class Heap {
    private ArrayList<Student> minHeap;
    public Heap() {
        minHeap = new ArrayList<Student>();
    }
    public ArrayList<Student> getminHeap(){
    	return minHeap;
    }
    // isHeapEmpty() checks if the heap has any elements left in it
    // Returns true if heap is empty, false otherwise
    public boolean isHeapEmpty() {
    	if(minHeap.size()==0) {
    		return true;
    	}
		return false;
    	
    }

    /**
     * buildHeap(ArrayList<Student> students)
     * Given an ArrayList of Students, build a min-heap keyed on each Student's minCost
     * Time Complexity - O(nlog(n)) or O(n)
     *
     * @param students
     */
    public void buildHeap(ArrayList<Student> students) {
       int heapSize=students.size();
       for(int i=0; i<heapSize;i++) {
    	   Student s=students.get(i);		// set the student indices according to the order they were added to arraylist
    	   s.setIndex(i);
       }
       minHeap=new ArrayList<Student>(students);
       for(int i=(heapSize/2)-1; i>=0; i--) {
    	   heapifyDown(minHeap, i);			// call heapifyDown on non leaf nodes
       }
       
        
    }
    
    private void heapifyDown(ArrayList<Student> students, int node) {
    	int size=students.size();
    	int minIdx=node;			// index to heapifyDown from
    	int leftIdx=(2*node)+1;		// get indicies of children of node
    	int rightIdx=(2*node)+2;
    	int minimum=students.get(node).getminCost();  // initialize node's minCost as the minimum
    	int leftVal = -1;
    	int rightVal=-1;
    	
    	if(leftIdx<size) {			// check if index exists in tree
    		leftVal=students.get(leftIdx).getminCost();			
    		if((leftVal <minimum || (leftVal==minimum && students.get(leftIdx).getName()< students.get(minIdx).getName()))) {
    			minimum=leftVal;	// set minimum to leftVal
    			minIdx=leftIdx;
    		}
    	}
    	if(rightIdx<size) {			
    		rightVal=students.get(rightIdx).getminCost();
    		if((rightVal<minimum || (rightVal==minimum && students.get(rightIdx).getName() < students.get(minIdx).getName()))) {
    			minimum=rightVal; 	//set minimum to rightVal
    			minIdx=rightIdx;
    		}
    	}
    	if(minimum != students.get(node).getminCost() || minIdx !=node ) {
    		Student nodeStd=students.get(node);		// swap node student and the new minimum 
    		students.set(node, students.get(minIdx));
    		students.get(minIdx).setIndex(node);
    		students.set(minIdx, nodeStd);
    		nodeStd.setIndex(minIdx);
    		heapifyDown(students, minIdx);  // call heapifyDown on the new minIdx
    	}
    	
		// TODO Auto-generated method stub
		
	}
    

	/**
     * insertNode(Student in)
     * Insert a Student into the heap.
     * Time Complexity - O(log(n))
     *
     * @param in - the Student to insert.
     */
    public void insertNode(Student in) {
    	int initialSize=minHeap.size();   	
		in.setIndex(initialSize);	// set the index to the last position in the heap
        minHeap.add(in);			// add to heap
        heapifyUp(initialSize);		//heapifyUp at the index where in was added
    }

    private void heapifyUp(int current) {
		if(current>0) {				// can only heapify up if the current is not the root node
			int parentIdx=(current-1)/2;		// get current's parent index 
			int parentName=minHeap.get(parentIdx).getName();
			int currentName=minHeap.get(current).getName();
			int currentCost=minHeap.get(current).getminCost();
			int parentCost=minHeap.get(parentIdx).getminCost();
			if((parentCost > currentCost) || (parentCost==currentCost && currentName<parentName)) { // if the parent is greater than current
				Student temp=minHeap.get(parentIdx);
				minHeap.set(parentIdx, minHeap.get(current));
				minHeap.get(current).setIndex(parentIdx);
				minHeap.set(current, temp);
				temp.setIndex(current);
				heapifyUp(parentIdx);		// call heapifyUp on the parent index, this is where current element has been swapped to 
				
			}
		}
		
	}

	/**
     * findMin()
     * Time Complexity - O(1)
     *
     * @return the minimum element of the heap.
     */
    public Student findMin() {
        // TODO: implement this method
        return minHeap.get(0);
    }

    /**
     * extractMin()
     * Time Complexity - O(log(n))
     *
     * @return the minimum element of the heap, AND removes the element from said heap.
     */
    public Student extractMin() {
        Student minStudent=minHeap.get(0);		// get the first element in heap, this is the minimum
        int size=minHeap.size();
        if(size>1) {
        	minHeap.set(0, minHeap.get(size-1));	// set root as the last element in the heap
        	minHeap.get(size-1).setIndex(0);
        	minHeap.remove(size-1);
        	heapifyDown(minHeap,0);}				//heapifyDown the student at index 0 to fix the heap 
        if(size==1) {
        	minHeap.remove(size-1);				
        }
        return minStudent;
    }

    /**
     * delete(int index)
     * Deletes an element in the min-heap given an index to delete at.
     * Time Complexity - O(log(n))
     *
     * @param index - the index of the item to be deleted in the min-heap.
     */
    public void delete(int index) {
    	int heapSize=minHeap.size();
    	Student lastStudent=minHeap.get(heapSize-1);
    	int lastStdCost=lastStudent.getminCost();
    	int lastStudentID=lastStudent.getName();    	
        minHeap.set(index, lastStudent);		// replace the index you want to delete with the last student in heap
        minHeap.remove(heapSize-1);				// remove the last student from end of heap 
        lastStudent.setIndex(index);			//update index
        int parentIdx=(index-1)/2;
        int parentCost=minHeap.get(parentIdx).getminCost();
        int parentID=minHeap.get(parentIdx).getName();
        
        if((lastStdCost< parentCost) ||(lastStdCost==parentCost && lastStudentID<parentID)) {
        	heapifyUp(index);		// if parentCost> lastStdCost, need to heapify up that lastStudent element
        }
        else {
        	if(lastStdCost>parentCost || (lastStdCost==parentCost && lastStudentID>parentID)) {
        		heapifyDown(minHeap, index); 	// if parentCost<lastStdCost, need to heapify down that lastStudent element
        	}
        }
        
        
    }

    /**
     * changeKey(Student r, int newCost)
     * Changes minCost of Student s to newCost and updates the heap.
     * Time Complexity - O(log(n))
     *
     * @param r       - the Student in the heap that needs to be updated.
     * @param newCost - the new cost of Student r in the heap (note that the heap is keyed on the values of minCost)
     */
    public void changeKey(Student r, int newCost) {
        int stdIdx=r.getIndex();	// get index stored in Student
        int initialCost=r.getminCost();
        minHeap.get(stdIdx).setminCost(newCost);
        if(initialCost<newCost) {			// fix the heap with either heapify up or heapify down
        	heapifyDown(minHeap,stdIdx);
        }
        else {
        	if(initialCost>newCost) {
        		heapifyUp(stdIdx);
        	}
        }
    }

    public String toString() {
        String output = "";
        for (int i = 0; i < minHeap.size(); i++) {
            output += minHeap.get(i).getName() + " ";
        }
        return output;
    }
    

///////////////////////////////////////////////////////////////////////////////
//                           DANGER ZONE                                     //
//                everything below is used for grading                       //
//                      please do not change :)                              //
///////////////////////////////////////////////////////////////////////////////

    public ArrayList<Student> toArrayList() {
        return minHeap;
    }
}

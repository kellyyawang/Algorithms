
/*
 * Name: Kelly Wang
 * EID: kaw4256
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Your solution goes in this class.
 *
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 *
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {


    /**
     * Determines whether a candidate Matching represents a solution to the stable matching problem.
     * Study the description of a Matching in the project documentation to help you with this.
     */
    @Override
    public boolean isStableMatching(Matching problem) {
    	int hsCount=problem.getHighSchoolCount();
    	ArrayList<Integer> sMatching=problem.getStudentMatching();
    	ArrayList<ArrayList<Integer>> hsMatching =new ArrayList<ArrayList<Integer>>();
    	ArrayList<ArrayList<Integer>> hsPref=problem.getHighSchoolPreference();
    	ArrayList<Integer> studentswMatch=new ArrayList<Integer>();
    	ArrayList<Integer> studentsNoMatch=new ArrayList<Integer>();
    	for (int i=0; i<hsCount; i++) {
    		hsMatching.add(new ArrayList<Integer>()); // initialize hsMatching with empty arraylists
    	}
    	
    	for (int i=0; i< sMatching.size(); i++) {
    		int currHS=sMatching.get(i);
    		if (currHS==-1) {
    			studentsNoMatch.add(i); 		// add all students without a matched high school to a list
    		}
    		else {
    			hsMatching.get(currHS).add(i);
    			studentswMatch.add(i);				// add students with a matched high school to another list
    		}
    	}
    	for (int i=0; i<hsMatching.size(); i++) {
    		int count=0; 			// count keeps track of how many of the matched students have appeared so far in preference list
    		ArrayList<Integer> currHSMatches=hsMatching.get(i);
    		if(!currHSMatches.isEmpty()) {
    			int numStudents=currHSMatches.size();
    			ArrayList<Integer> currHSPref=hsPref.get(i);
    			for(int j=0; j<currHSPref.size(); j++) {
    				if (numStudents != count && studentsNoMatch.contains(currHSPref.get(j))) {
    					return false;				// instability because an unmatched student appeared before all the matched students in pref list
    				}
    				if (studentswMatch.contains(currHSPref.get(j)) && sMatching.get(currHSPref.get(j))==i){
    					count++;
    					if(count ==numStudents) {		
    						break;					// students who got matched to this hs, appeared earlier in pref list than unmatched students
    					}
    				}
    			}
    		}
    		
    	}				

    	return isStableMatchingCaseTwo(problem, hsMatching);		// checks for second type of instability
    	    
    }
    
    private boolean isStableMatchingCaseTwo(Matching problem, ArrayList<ArrayList<Integer>> hsMatching) {
    	ArrayList<Integer> stMatching=problem.getStudentMatching();
    	ArrayList<ArrayList<Integer>> hsPref=problem.getHighSchoolPreference();
    	ArrayList<ArrayList<Integer>> stPref=problem.getStudentPreference();
    	for(int i=0; i<stMatching.size(); i++) {
    		int hsID=stMatching.get(i);			// hsID is the student's current matched high school 
    		ArrayList<Integer> currStPref=stPref.get(i); 
    		if (hsID != -1) {
    			int k=currStPref.indexOf(hsID);		// k is an upper index bound, used to check indices in pref list before k
    			for (int j=k-1; j>=0; j--) {        
    				int preferredHS=currStPref.get(j);			// preferredHS is a school that student i prefers over its current match
    				ArrayList<Integer> currHSMatching=hsMatching.get(preferredHS);
    				ArrayList<Integer> currHSPref=hsPref.get(preferredHS);
    				int stIndex=currHSPref.indexOf(i); 		// i is current student we are checking for instabilities
    				for(int l=0; l<currHSMatching.size();l++) {
    					int matchIndex=currHSPref.indexOf(currHSMatching.get(l));
    					if (stIndex<matchIndex) {
    						return false;			// instability if the high school prefers student i over any of its current students
    					}
    				}
    			}
    		}
    	}
    	return true;							// return true if matching passed both cases
    	
    	
    }

    /**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_studentoptimal(Matching problem) {
    	int spotsCount=0;
    	int spotsTotal=0;
    	ArrayList<Integer> stdMatching=new ArrayList<Integer>();  //hold matchings for students
    	ArrayList<ArrayList<Integer>> hsMatching =new ArrayList<ArrayList<Integer>>(); // holds matchings for high schools
    	ArrayList<ArrayList<Integer>> hsPreferences=problem.getHighSchoolPreference();
    	ArrayList<ArrayList<Integer>> stPreferences=problem.getStudentPreference();
    	ArrayList<ArrayList<Integer>> inverseHSPrefs=convertToInverse(hsPreferences); //inverse pref lists
    	int numOfHS=problem.getHighSchoolCount();
    	int numOfSt=problem.getStudentCount();
    	ArrayList<Integer> hsSpots=new ArrayList<Integer>(problem.getHighSchoolSpots());
    	int [] nextPrefIdx=new int[numOfSt];
    	Queue <Integer> stdProposers=new LinkedList<Integer>();
    	for (int i=0; i<numOfHS; i++) {
    		hsMatching.add(new ArrayList<Integer>()); // initialize hsMatching with empty array lists
    	}
    	for(int i=0; i<numOfSt; i++) {
    		stdProposers.add(i);					// add students to queue
    		stdMatching.add(-1);
    	}
    	for(int i=0; i< numOfHS; i++) {
    		spotsCount += hsSpots.get(i);  		   // add up total spots 
    	}
    	spotsTotal=spotsCount;						// spotsTotal won't change after this, but spotsCount will
    	while (!stdProposers.isEmpty() && spotsCount>0) {   
    		int currProposer=stdProposers.remove();   
    		ArrayList<Integer> rankedHS=stPreferences.get(currProposer);  
    		int nextIndex=nextPrefIdx[currProposer];   		// get index of next preferred high school
    		int preferredHS=rankedHS.get(nextIndex);		// get next preferred high school 
    		int spotsFree=hsSpots.get(preferredHS);          
    		if(spotsFree >0) {
    			stdMatching.set(currProposer, preferredHS);  // assign student to preferredHS if it has spots
    			spotsCount--;
    			spotsFree--;	
    			hsSpots.set(preferredHS, spotsFree); 		// update spots
    			hsMatching.get(preferredHS).add(currProposer);  // update preferredHS' assigned students
    		}
    		else {
    			ArrayList <Integer> hsMatchList=hsMatching.get(preferredHS);
    			ArrayList<Integer>  rankedStds=hsPreferences.get(preferredHS);
    			ArrayList <Integer> hsMatchesRanks=new ArrayList<Integer>();
    			ArrayList<Integer> invRankedStds=inverseHSPrefs.get(preferredHS);
    			for(int i=0; i<hsMatchList.size(); i++) {
    				hsMatchesRanks.add(invRankedStds.get((hsMatchList.get(i))));	// add the ranking/index of each std to an arraylist
    			}
    			Collections.sort(hsMatchesRanks);    					// sort in ascending order to determine lowest ranked student (higher index)
    			int proposerRank=invRankedStds.get(currProposer);
    			for(int i=0; i<hsMatchList.size(); i++) {
    				int std=hsMatchList.get(i);
    				int matchedStdRank=invRankedStds.get(std);
    				if(proposerRank<matchedStdRank) { 				// if proposer appears earlier in pref list than matched student
    					int lowestRank=hsMatchesRanks.get(hsMatchesRanks.size()-1); // get the lowest assigned student's index
    					int lowestStd=rankedStds.get(lowestRank);					// get the lowest assigned student
    					hsMatchList.remove(hsMatchList.indexOf(lowestStd));		   
    					hsMatchList.add(currProposer);   				// add new assigned student
    					stdMatching.set(lowestStd, -1);
    					stdMatching.set(currProposer, preferredHS);		// add assigned high school
    					if(nextPrefIdx[lowestStd]<numOfHS) {
    						stdProposers.add(lowestStd);}		// add lowest student back to queue
    					break;					// break from for loop
    				}
    			}		
    		}
    		nextIndex++;			
    		nextPrefIdx[currProposer]=nextIndex;	//update next index on preference list
			if(stdMatching.get(currProposer) ==-1 && nextPrefIdx[currProposer] < numOfHS) {
				stdProposers.add(currProposer);  				
			}
    		
    	}
    	if(spotsTotal<numOfSt) {		// if # of spots is less than # of students, must resolve any instabilites
    		while(!stdProposers.isEmpty()) {		// repeats same process as above until all students are able to propose
    			int currProp=stdProposers.remove();
    			ArrayList<Integer> rankedHS=stPreferences.get(currProp);
    			int nextIdx=nextPrefIdx[currProp];
    			int nextHS=rankedHS.get(nextIdx);
    			ArrayList <Integer> hsMatchList=hsMatching.get(nextHS);
    			ArrayList<Integer>  rankedStds=hsPreferences.get(nextHS);
    			ArrayList <Integer> hsMatchesRanks=new ArrayList<Integer>();
    			ArrayList<Integer> invRankedStds=inverseHSPrefs.get(nextHS);
    			for(int i=0; i<hsMatchList.size(); i++) {
    				hsMatchesRanks.add(invRankedStds.get((hsMatchList.get(i))));
    			}
    			Collections.sort(hsMatchesRanks);
    			int propRank=invRankedStds.get(currProp);
    			for(int i=0; i<hsMatchList.size(); i++) {
    				int matchedStd=hsMatchList.get(i);
    				int matchedStdRank=invRankedStds.get(matchedStd);
    				if(propRank<matchedStdRank) {
    					int lowestRank=hsMatchesRanks.get(hsMatchesRanks.size()-1);
    					int lowestStd=rankedStds.get(lowestRank);
    					hsMatchList.remove(hsMatchList.indexOf(lowestStd));
    					hsMatchList.add(currProp);
    					stdMatching.set(lowestStd, -1);
    					stdMatching.set(currProp, nextHS);
    					if(nextPrefIdx[lowestStd] < numOfHS) {
    						stdProposers.add(lowestStd);
    					}
    					break;
    					
    				}
    			}
    			nextIdx++;
        		nextPrefIdx[currProp]=nextIdx;	
    			if(stdMatching.get(currProp) ==-1 && nextPrefIdx[currProp] < numOfHS) {
    				stdProposers.add(currProp);  				
    			}
    		}
    	}
    	problem.setStudentMatching(stdMatching);		// set the student matching
        return problem;							
    }



	/**
     * Determines a solution to the stable matching problem from the given input set. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return A stable Matching.
     */
    @Override
    public Matching stableMatchingGaleShapley_highschooloptimal(Matching problem) {
    	ArrayList <Integer> stdMatching=new ArrayList<Integer>();
    	ArrayList<ArrayList<Integer>> hsPreferences=problem.getHighSchoolPreference();
    	ArrayList<ArrayList<Integer>> stPreferences=problem.getStudentPreference();
    	ArrayList<ArrayList<Integer>> inverseStPrefs=convertToInverse(stPreferences);
    	ArrayList<Integer> hsSpots=new ArrayList<Integer>(problem.getHighSchoolSpots());
    	int numOfHS=problem.getHighSchoolCount();
    	int numOfSt=problem.getStudentCount();
    	int [] nextPrefIdx=new int[numOfHS];
    	Queue <Integer> proposersQueue=new LinkedList<Integer>();
    	for(int i=0; i<numOfSt; i++) {
    		stdMatching.add(-1);			// initialize all students as not having a school 
    	}
    	for( int i=0; i<numOfHS; i++) {
    		if (hsSpots.get(i) >0) {
    			proposersQueue.add(i);				// add highSchools that have spots to proposersQueue
    		}
    	}
    	while(!proposersQueue.isEmpty()) {
    		int hsProposer=proposersQueue.remove();   	//remove a high school from the queue
    		int spotsLeft=hsSpots.get(hsProposer);		
    		ArrayList<Integer> rankedStudents=hsPreferences.get(hsProposer); // hsProposer's pref list with ranked students
    		for(int i=0; i< spotsLeft; i++) {
    			int nextStd=rankedStudents.get(nextPrefIdx[hsProposer]);	// gets the high schools next preferred student
    			int stdsCurrHS=stdMatching.get(nextStd);
    			ArrayList<Integer> stdPrefList=inverseStPrefs.get(nextStd);
    			if(stdsCurrHS == -1) {
    				stdMatching.set(nextStd, hsProposer);		// if student is not assigned, assign to hsProposer
    				int spotsTemp=hsSpots.get(hsProposer);
    				spotsTemp--;
    				hsSpots.set(hsProposer, spotsTemp);
    			}
    			else {
    				int proposerHSRank=stdPrefList.get(hsProposer);		// if student is assigned, check if it will give up current hs
    				int currHSRank=stdPrefList.get(stdsCurrHS);
    				if(proposerHSRank < currHSRank) {					//if proposing high school appears earlier in preference list
    					stdMatching.set(nextStd, hsProposer);			// student will accept hsProposer's offer
    					int decrementTemp=hsSpots.get(hsProposer);
    					decrementTemp--;								// decrement # of high school spots
    					hsSpots.set(hsProposer, decrementTemp);
    					int incrementTemp=hsSpots.get(stdsCurrHS);
    					incrementTemp++;
    					hsSpots.set(stdsCurrHS, incrementTemp);
    					if(incrementTemp==1) {							// if students previous high school was full, but now opened a spot
    						proposersQueue.add(stdsCurrHS); 		   // add high school back to queue
    					}
    				}
    			}
    			int prefIndex= nextPrefIdx[hsProposer] +1;			
	    		nextPrefIdx[hsProposer]=prefIndex;						// increment and update nextPrefIdx for current proposer
    		}
    		if(hsSpots.get(hsProposer) > 0 && nextPrefIdx[hsProposer] < numOfSt) {		// if spots still remain and hs still has students to propose to
    			proposersQueue.add(hsProposer);							// add high school back to queue
    		}
    	}
    	problem.setStudentMatching(stdMatching);
        return problem;
    }
    private ArrayList<ArrayList<Integer>> convertToInverse(ArrayList<ArrayList<Integer>> listOfPrefs) {
		ArrayList<ArrayList<Integer>> inverseList=new ArrayList<ArrayList<Integer>>();
		for (int i=0; i<listOfPrefs.size(); i++) {
			ArrayList<Integer> ranked=listOfPrefs.get(i);
			ArrayList<Integer> invRanked=new ArrayList<Integer>(listOfPrefs.get(i));
			for(int j=0; j< ranked.size(); j++) {
				int currHSOrStd=ranked.get(j);
				invRanked.set(currHSOrStd, j);	// swaps index with the current HS or student
			}	
			inverseList.add(invRanked);	 // add to inverseList when this preference list is inversed
		}
		
		return inverseList;
	}
}
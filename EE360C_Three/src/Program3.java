/*
 * Name: Kelly Wang
 * EID: kaw4256
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;

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
public class Program3 extends AbstractProgram3 {

    /**
     * Determines the solution of the optimal response time for the given input TownPlan. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return Updated TownPlan town with the "responseTime" field set to the optimal response time
     */
    @Override
    public TownPlan findOptimalResponseTime(TownPlan town) {
    	int numHouses=town.getHouseCount();
    	int numStations=town.getStationCount();
    	int[][] optRT = new int[numHouses][numStations];	// optRT[i][j] holds optimal response times for first i houses and j stations
    	ArrayList<Integer> housePositions=town.getHousePositions();
    	// Initialize first column as base case
    	int firstHouse=housePositions.get(0);
    	for(int i=0; i<numHouses; i++) {
    		int houseLocation=housePositions.get(i);	
    		optRT[i][0]=(houseLocation-firstHouse)/2;  //optRT is the median
    		
    	} 	
    	for(int j=1; j<numStations; j++) {   // iterate through each column
    		for(int i=j; i<numHouses; i++ ) {	// go down each row of each column
    			int minimum=Integer.MAX_VALUE;	// minimum will hold the minimum response time for optRT[i][j]
    			for(int a=j; a<=i; a++) { 		// a represents the cutoff to partition the locations of houses into
    				int localMax;
    				int prevOpt=optRT[a-1][j-1];	// prevOpt is optimal response time of corresponding previous partition
   					int leftmostLocation=housePositions.get(a);		
   					int rightmostLocation=housePositions.get(i);
   					int minRT=(rightmostLocation-leftmostLocation)/2;	// station that gives minRT is median between right house and left house
   					if(prevOpt>minRT) {
   						localMax=prevOpt;	// localMax is the max between prevOpt and minRT
    				}
    				else {
    					localMax=minRT;
    				}
    				if(localMax<minimum) {		// if localMax is less than current minimum
   						minimum=localMax;		// localMax becomes the new minimum
   					}
    				}
   				optRT[i][j]=minimum;		
    		}
    	}
    	town.setResponseTime(optRT[numHouses-1][numStations-1]);
        return town;
    }
    
    

    /**
     * Determines the solution of the set of police station positions that optimize response time for the given input TownPlan. Study the
     * project description to understand the variables which represent the input to your solution.
     *
     * @return Updated TownPlan town with the "policeStationPositions" field set to the optimal police station positions
     */
    @Override
    public TownPlan findOptimalPoliceStationPositions(TownPlan town) {
    	int numHouses=town.getHouseCount();
    	int numStations=town.getStationCount();
    	int[][] optRT = new int[numHouses][numStations];
    	int [][] optStations=new int[numHouses][numStations];		// holds the newly added station that gives optimal response
    	int [][] optStationsRow=new int[numHouses][numStations];	// holds the row in the previous column to trace path back to
    	ArrayList<Integer> housePositions=town.getHousePositions();
    	// Initialize first column as base case
    	int firstHouse=housePositions.get(0);
    	for(int i=0; i<numHouses; i++) {
    		int houseLocation=housePositions.get(i); 			
    		optStations[i][0]=(houseLocation+firstHouse)/2;		// station location will be midpoint between houseLocation and firstHouse
    		optRT[i][0]=(houseLocation-firstHouse)/2;
    		
    	}
    	
    	for(int j=1; j<numStations; j++) {
    		for(int i=j; i<numHouses; i++ ) {
    			int minimum=Integer.MAX_VALUE;
    			int aCut=j;  		// initialize aCut as the leftmost bound, it reprsents where we are splitting the houses
    			for(int a=j; a<=i; a++) {
    				int localMax;
   					int prevOpt=optRT[a-1][j-1];
   					int leftmostLocation=housePositions.get(a);
   					int rightmostLocation=housePositions.get(i);
   					int minRT=(rightmostLocation-leftmostLocation)/2; // minRT is minimum response time
   					if(prevOpt>minRT) {
    					localMax=prevOpt;
    				}
    				else {
    					localMax=minRT;						
    				}
    				if(localMax<minimum) {
    					minimum=localMax;
    					aCut=a;					//if minimum is updated, update aCut as well
    				}
    				}
    			optRT[i][j]=minimum;
    			int leftmostLocation=housePositions.get(aCut); // use aCut to get leftmostLocation of optimal partition
				int rightmostLocation=housePositions.get(i);
				int minRTStation=(rightmostLocation+leftmostLocation)/2;	// location of optimal station is midpoint
   				optStations[i][j]=minRTStation;			// set station location for this i and j optStation
   				optStationsRow[i][j]=aCut-1;			// optStationsRow has the row of the previous column to trace to
    			
    			
    		}
    	}
    	int row=numHouses-1;
    	ArrayList<Integer> stationsList=new ArrayList<Integer>(); // stationsList holds police station locations
    	stationsList.add(optStations[row][numStations-1]);
    	for(int col=numStations-1; col>0; col--) {
    		int previousRow=optStationsRow[row][col];   
    		stationsList.add(optStations[previousRow][col-1]);  // add station from previous column and corresponding row
    		row=previousRow;				// update row
    	}
    	Collections.reverse(stationsList); // reverse stationsList to get it in increasing order
    	town.setPoliceStationPositions(stationsList);
        return town;
    }
}

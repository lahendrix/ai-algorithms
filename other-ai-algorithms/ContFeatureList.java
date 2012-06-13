///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            k-NN and id3 algorithm
// Files:            HW1.java, MyFileReader.java, ID3.java, 
//					 ContFeatureList.java, Feature.java, KNN.java,
//					 DecisionTree.java, Node.java
//
// Semester:         Fall 2007
//
// Author:           Larry A. Hendrix
// CS Login:         lhendrix
// TA's Name:        Dan Wong
//
//////////////////////////// 80 columns wide //////////////////////////////////

import java.util.*;

public class ContFeatureList 
{
	private Feature currentFeature;
	private ArrayList<Example> sortedList = new ArrayList<Example>();
	private ArrayList<Double> thresholds = new ArrayList<Double>();
	private ArrayList<String> outputValues;
	private double bestThreshold;
	private int index;
	
	
	public ContFeatureList(ArrayList<Example> examples, int ind, Feature f, ArrayList<String> out)
	{
		index = ind;
		currentFeature = f;
		 outputValues = out;
		sortExamples(examples);
		determineThresholds();
		setBestThreshold();
		
	}
	
	/**
	 * Sorts the example list by increasing values of the current
	 * continuous feature
	 * @param unsortedList original list of unsorted examples
	 */
	private void sortExamples(ArrayList<Example> unsortedList)
	{
		double minValue = Double.parseDouble(unsortedList.get(0).getValues().get(currentFeature.getIndex()));
		
		for(Example e: unsortedList)
		{
			double tmp = Double.parseDouble(e.getValues().get(currentFeature.getIndex()));
			
			if(tmp <= minValue && !sortedList.contains(e))
			{
				minValue = tmp;
				sortedList.add(e);
			}
		}
	}
	
	/**
	 * Calculates the boundary values by searching through the list of 
	 * examples and finding the mid point between adjacent examples
	 * with differing classifications
	 *
	 */
	private void determineThresholds()
	{
		for(int i = 1; i < sortedList.size(); i++)
		{
			Example previous = sortedList.get(i - 1);
			Example current = sortedList.get(i);
			
			if(!previous.getClassification().equalsIgnoreCase(current.getClassification()))
			{
				
				double currentVal = Double.parseDouble(current.getValues().get(index));
				double previousVal = Double.parseDouble(previous.getValues().get(index));
				double thresh = (currentVal + previousVal)/2.0;
				thresholds.add(thresh);
			}
		}
		
	}
	
	/**
	 * Determines the best threshold for a particular continuous feature
	 *
	 */
	private void setBestThreshold()
	{
		if(thresholds.size() == 0)
		{
			double total = 0.0;
			for(Example e: sortedList)
			{
				total += Double.parseDouble(e.getValues().get(index));
			}
			bestThreshold = total/sortedList.size();
		}
		else
		{	
			double bestScore = thresholds.get(0);
			for(double d: thresholds)
			{
				double currentScore = getThresholdScore(d);
				if(currentScore > bestScore)
				{
					bestScore = currentScore;
					
				}
			}
			 bestThreshold = bestScore;
		}
	}
	
	/**
	 * Determines the boundary values for adjacent values
	 * @param threshold
	 * @return
	 */
	private double getThresholdScore(double threshold)
	{
		double categoryA = 0;
		double categoryB = 0;
		double totalNumOfExamples = 0;
		//Count the number of "positive" and "negative" examples
		for(Example e: sortedList)
		{
			double tmp = Double.parseDouble(e.getValues().get(index));
			if(tmp > threshold)
			{
				totalNumOfExamples++;
				if(e.getClassification().equals(outputValues.get(0)))
					categoryA++;
				else
					categoryB++;
			}
		}
	
		double posFraction = categoryA/totalNumOfExamples;
		double negFraction = categoryB/totalNumOfExamples;
		
		//Calculate total entropy
		double totalEntropy = -posFraction*(Math.log(posFraction)/Math.log(2)) - negFraction*(Math.log(negFraction)/Math.log(2));
		
		return totalEntropy;
	}

	/**
	 * Returns the index of the continuous feature
	 * @return
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * Returns the sorted example list from smallest to largest
	 * @return
	 */
	public ArrayList<Example> getSortedList()
	{
		return sortedList;
	}
	
	/**
	 * Returns an arraylist of plausible thresholds
	 * @return
	 */
	public ArrayList<Double> getThresholds()
	{
		return thresholds;
	}
	
	/**
	 * Returns the particular continuous feature 
	 * @return
	 */
	public Feature getFeature()
	{
		return currentFeature;
	}
	
	/**
	 * Returns the best threshold
	 * @return
	 */
	public double getBestThreshold()
	{
		return bestThreshold;
	}
}

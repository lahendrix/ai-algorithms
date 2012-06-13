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
/**
 * This class defines a tree node
 * @author lhendrix
 *
 */
public class Node 
{

	private Feature splitFeature;
	private String splitValue;
	private int nodeNumber;
	private ArrayList<Example> exampleList;
	private String classification;
	private ArrayList<String> outputValues;
	private boolean isLeaf = true;
	private boolean toBeDeleted = false;
	
	/**
	 * COnstructor that creates a Node object and sets its classification value
	 * and node type (leaf or non-leaf)
	 * @param ex
	 * @param out
	 */
	public Node( ArrayList<Example> ex, ArrayList<String> out, int number)
	{
		
		exampleList = ex;
		outputValues = out;
		nodeNumber = number;
		setNodeType();
		setClassification();
		
	}
	
	/**
	 * Prunes this node by setting its classification and the boolean
	 * value that signifies it is a leaf node
	 */
	public void prune()
	{
		isLeaf = true;
		classification = getMajorityClassification();
		toBeDeleted = false;
		
	}
	
	/**
	 * Returns the node number of this node
	 * @return int nodeNumber
	 */
	public int getNumber()
	{
		return nodeNumber;
	}
	
	
	/**
	 * Automatically sets classification if this is a leaf node
	 *
	 */
	private void setClassification()
	{
		if(exampleList.isEmpty())
		{
			classification = getMajorityClassification();
			return;
		}
		
		if(isLeaf)
			classification = exampleList.get(0).getClassification();
	}
	
	/**
	 * Marks a node to be deleted
	 *
	 */
	public void setToBeDeleted()
	{
		toBeDeleted = true;
	}
	
	/**
	 * Returns a boolean value representing whether this node is marked to
	 * be deleted or not
	 * @return
	 */
	public boolean getDeleteStatus()
	{
		return toBeDeleted;
	}
	
	
	/**
	 * Allows user to set the classification values of this node
	 * @param s classification value
	 */
	public void setClassification(String s)
	{
		classification = s;
	}
	
	/**
	 * Returns the classification of this node
	 * @return
	 */
	public String getClassification()
	{
		return classification;
	}
	
	
	/**
	 * Returns the split value for the feature contained within this node
	 * @return
	 */
	public String getSplitValue()
	{
		return splitValue;
	}
	
	/**
	 * Returns the feature contained within this node
	 * @return
	 */
	public Feature getFeature()
	{
		return splitFeature;
	}
	
	/**
	 * Sets the boolean variable that tells whether or not this is 
	 * a leaf node
	 *
	 */
	private void setNodeType()
	{
		if(exampleList.isEmpty())
		{
			isLeaf = true;
		}
		
		else
		{	
			String classValue = exampleList.get(0).getClassification();
			for(Example e: exampleList)
			{
				if(!e.getClassification().equalsIgnoreCase(classValue))
					isLeaf = false;
			}
		}
	}
	
	/**
	 * Returns a boolean value that tells if this is a leaf node or not
	 * @return
	 */
	public boolean isLeaf()
	{
		return isLeaf;
	}
	
	
	
	/**
	 * Method that allows the user to set the split feature for this node
	 * @param f split feature
	 */
	public void setFeature(Feature f)
	{
		splitFeature = f;
	}
	
	/**
	 * Method that allows the user to set the split feature value for the
	 * feature contained within this node
	 * @param val
	 */
	public void setFeatureValue(String val)
	{
			splitValue = val;
	}
	
	/**
	 * Method that returns the examples in this node
	 * @return
	 */
	public ArrayList<Example> getExamples()
	{
		return exampleList;
	}
	
	/**
	 * Determines the majority classification for the examples within
	 * this node
	 * @return classification
	 */
	public String getMajorityClassification()
	{
		int categoryA = 0;
		int categoryB = 0;
		for(Example e: exampleList)
		{
			if(e.getClassification().equalsIgnoreCase(outputValues.get(0)))
				categoryA++;
			else
				categoryB++;
		}
		
		if(categoryA > categoryB)
			return outputValues.get(0);
		
		else
			return outputValues.get(1);

	}
	
}

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
 * This class defines type feature. Feature holds a particular feature's
 * name, type and possible values
 * @author lhendrix
 *
 */
public class Feature 
{

	private String name;
	private String type;
	private ArrayList<String> possibleValues = new ArrayList<String>();
	private String commaDelimitedValues;
	private double range;
	private double min;
	private boolean isContinuous;
	private int index;

	
	/**
	 * Constructor for creating an object of type Feature
	 * @param featureName name of the feature
	 * @param featureType type of feature 
	 * @param values legal values the feature can assume
	 */
	public Feature(String featureName, String featureType, String values)
	{
		name = featureName;
		type = featureType;
		commaDelimitedValues = values;
		setType(featureType);
		parseValues(values);
	}
	
	/**
	 * Determines if a given feature is equal to this feature
	 * @param f
	 * @return
	 */
	public boolean same(Feature f)
	{
		if(f.getFeatureName().equalsIgnoreCase(name) && f.getFeatureType().equalsIgnoreCase(type)
				&& f.getPossibleValues().equals(possibleValues))
			return true;
		else
			return false;
	}
	
	/**
	 * Sets the index of this feature within each example's list of values
	 * @param i
	 */
	public void setIndex(int i)
	{
		index = i;
	}
	
	/**
	 * Returns the index of this feature
	 * @return
	 */
	public int getIndex()
	{
		return index;
	}
	
	/**
	 * Sets a boolean variable that tells whether the feature is continuous
	 * @param featureType -the type of the feature
	 */
	private void setType(String featureType)
	{
		if(featureType.equalsIgnoreCase("continuous"))
		{
			isContinuous = true;
		}
		else
			isContinuous = false;
	}
	
	
	/**
	 * Parses the possible feature values 
	 * @param vals -comma delimited values read from the file
	 */
	private  void parseValues(String vals)
	{
		String tmp[] = vals.split(",");
			for(int i = 0; i < tmp.length; i++)
			{
				possibleValues.add(tmp[i]);
			}
			
			if(isContinuous)
			{
				min = Double.parseDouble(tmp[0]);
				range = Double.parseDouble(tmp[1]);
			}
	}
	
	/**
	 * Checks the validity of a feature value
	 * @param val -value to test
	 * @return -returns false if an illegal value was given
	 */
	public boolean correctValue(String val)
	{
		
		if(isContinuous)
		{
			double d = Double.parseDouble(val);
			return (d >= Double.parseDouble(possibleValues.get(0)) && 
					(d <= Double.parseDouble(possibleValues.get(1))));
		}
		
		else
		{
			boolean result = false;
			int i = 0;
			while(!result && i < possibleValues.size())
			{
				if(possibleValues.get(i).equalsIgnoreCase(val.trim()))
					result = true;
				i++;
			}
			return result;
		}
		
		
	}
	
	/**
	 * Set to true if this is a continuous function
	 * @return
	 */
	public boolean isContinuous()
	{
		return isContinuous;
	}
	
	/**
	 * Returns the smallest allowable value for a continuous feature
	 * @return
	 */
	public double getMin()
	{
		return min;
	}
	
	/**
	 * Returns the largeest allowable value for a continuous feature
	 * @return
	 */
	public double getRange()
	{
		return range;
	}
	
	public String getFeatureName()
	{
		return name;
	}
	
	/**
	 * Returns the type of a feature
	 * @return
	 */
	public String getFeatureType()
	{
		return type;
	}
	/**
	 * Returns an arraylist of possible values
	 * @return
	 */
	public ArrayList<String> getPossibleValues()
	{
		return possibleValues;
	}
	
	/**
	 * Return a long string of the possible values
	 * @return
	 */
	public String getValuesInPrintFormat()
	{
		return name + " " + type + " " + commaDelimitedValues;
	}
	
	
}

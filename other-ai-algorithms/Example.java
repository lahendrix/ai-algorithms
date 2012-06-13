import java.util.*;

public class Example 
{

	private ArrayList<String> values = new ArrayList<String>();
	private double distance;
	private String valsInPrintFormat;
	private String classification;
	
	public Example(String line, int outputIndex)
	{
		valsInPrintFormat = line;
		parseValues(line, outputIndex);
	}
	
	private  void parseValues(String vals, int outputIndex)
	{
		String tmp[] = vals.split(",");
		for(int i = 0; i < tmp.length; i++)
		{
			values.add(tmp[i].trim());
		}
		classification = values.get(outputIndex).trim();
	}
	
	/**
	 * Calculates the Euclidean distance between two data points
	 * @param e the query example
	 * @param features list of features
	 */
	public void setDistance(Example e, ArrayList<Feature> features)
	{
		double  sum = 0.0;
		
		//Calculate the distance between each feature of the two examples
		for(int i = 0; i < features.size(); i++)
		{
			String queryValue = e.getValues().get(i);
			Feature currentFeature = features.get(i);
			
			//For continuous features sum the square of the difference
			if(currentFeature.getFeatureType().equalsIgnoreCase("continuous"))
			{
				sum += Math.pow(((Double.parseDouble(queryValue) 
						- Double.parseDouble(values.get(i)))/currentFeature.getRange()), 2);
			}
			
			//For discrete features turn into boolean result
			else if(currentFeature.getFeatureType().equalsIgnoreCase("discrete"))
			{
				if(!queryValue.equalsIgnoreCase(values.get(i)))
				{
					sum += 1;
				}
			}
		}
		
		distance = Math.sqrt(sum);
		
	}
	
	/**
	 * Returns the stored distance
	 * @return
	 */
	public double getDistance()
	{
		return distance;
	}
	
	public String getClassification()
	{
		return classification;
	}
	
	public void printExample()
	{
		System.out.println(valsInPrintFormat);
	}
	
	/**
	 * Returns an ArrayList of feature values in String format
	 * @return
	 */
	public ArrayList<String> getValues()
	{
		return values;
	}
}

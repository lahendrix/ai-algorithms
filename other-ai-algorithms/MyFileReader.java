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
import java.io.*;

public class MyFileReader 
{
	
		public MyFileReader()
		{
			// do nothing
		}
		
		
		/**
		 * This method reads the names file and creates an ArrayList of
		 * type Feature; this is a list of all the features
		 * @param fileName -file containing the example names
		 * @return -an arraylist of features
		 */
		public ArrayList<Feature> createFeatures(String fileName)
		{
			final int FEATURE_FIELDS = 3;
			ArrayList<Feature> features = new ArrayList<Feature>();
			//Read the names file
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(fileName));
				String line;
			
				int index = 0;
				while((line = in.readLine())  != null) 
				{
					//Create features skipping the comment lines 
					//and blank lines
					 if(!line.startsWith("//") && (line.length() != 0))
					 {
						 Scanner scanner = new Scanner(line);
						
						 //Each feature will have three fields: (name, type, values)
						 String[] tmp = new String[FEATURE_FIELDS];
						 
				         for(int i = 0; i < FEATURE_FIELDS; i++)
				         {
				        	 tmp[i] = scanner.next();
						 }
				         
				         Feature current = new Feature(tmp[0], tmp[1], tmp[2]);
				         current.setIndex(index);
						 features.add(current); 
						 index++;
					 }
					
				}
				in.close();
			}
			
			//If file not found, catch the appropriate exception
			catch(FileNotFoundException e)
			{
				System.out.println("File " + fileName + " not found");
				System.exit(0);
			}
			
			catch(IOException e)
			{
				System.out.println("IO Exception!");
				System.exit(0);
			}
			return features;
		}
		
		/**
		 * This method reads from the examples.data file and prints out illegal feature values.
		 * It also prints out the count of output values in category A and category B
		 * @param fileName -name of the examples file
		 * @param features -array list of the features
		 * @param outputValues -arraylist of the possible output values
		 */
		public ArrayList<Example> createExampleList(String fileName, int outputIndex)
		{
			
			ArrayList<Example> exampleList = new ArrayList<Example>();
			try
			{
				BufferedReader in = new BufferedReader(new FileReader(fileName));
			
				String line;
				while((line = in.readLine())  != null ) 
				{
					
					 if(!line.startsWith("//") && (line.length() != 0))
					 {
						 exampleList.add(new Example(line, outputIndex));
					 }
					 
				}	 
				in.close();
				
				
			}
			
			//If file not found, catch the appropriate exception
			catch(FileNotFoundException e)
			{
				System.out.println("File " + fileName + " not found");
				System.exit(0);
			}
			
			catch(IOException e)
			{
				System.out.println("IO Exception!");
				System.exit(0);
			}
			
			return exampleList;
						 
		}
		
		
		public static void main(String[] args) 
		{
			
			
		}

	

	
	
}

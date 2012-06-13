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

import java.util.ArrayList;

public class KNN 
{
	private ArrayList<Feature> features;
	private ArrayList<Example>trainingExamples;
	private ArrayList<Example>testExamples;
	private int outputIndex;
	private ArrayList<String> outputValues;
	private int bestKValue;
	
	/**
	 * Constructor that reads the files and stores the data sets
	 * @param namesFile file containing feature names
	 * @param trainingFile file containing the training examples
	 * @param testFile file containing the test exampless
	 */
	public KNN(String namesFile, String trainingFile, String testFile)
	{
		
		readFiles( namesFile,  trainingFile,  testFile);
	}
	
	private void readFiles(String namesFile, String trainingFile, String testFile)
	{
		
		//Create MyFileReader object
		MyFileReader reader = new MyFileReader();
		
		//Create feature list
		features = reader.createFeatures(namesFile);
		
		//Determine the index for the output feature value
		for(int j = 0; j < features.size(); j++)
		{
			if(features.get(j).getFeatureType().equalsIgnoreCase("output"));
			{
				outputIndex = j;
				outputValues = features.get(j).getPossibleValues();
			}
		}
		
		//create list of training examples
		trainingExamples = reader.createExampleList(trainingFile, outputIndex);
		
		//create list of test examples
		testExamples = reader.createExampleList(testFile, outputIndex);
	
	}
	
	
	/**
	 * This method creates a list of the k nearest neighbors
	 * @param query Quesr example of which to find the nearest neighbors
	 * @param trainingSet 
	 * @param k number of nearest neighbors to find
	 * @return
	 */
	private  ArrayList<Example> getNearestNeighbors(Example query, ArrayList<Example> trainingSet, int k)
	{
		ArrayList<Example> nearestNeighbors = new ArrayList<Example>();
		
		
		Example neighbor = null;
		
		
		while(nearestNeighbors.size() <= k)
		{
			double minDistance = Double.MAX_VALUE;
			for(Example current: trainingSet)
			{
				
					//Make sure u leave the query example out
					if(!current.equals(query) && !nearestNeighbors.contains(current) 
							&& current.getDistance() < minDistance )
					{
						minDistance = current.getDistance();
						neighbor = current;
					}
				
			}
			nearestNeighbors.add(neighbor);
			
		}
		
		return nearestNeighbors;
	}
	
	/**
	 * This method returns the best value for k 
	 * @param accuracies ArrayList containing the accuracies for 
	 * the values of k = 1,3,5, and 9
	 * @return most accurate value of k
	 */
	private int getBestKValue(ArrayList<Double> accuracies)
	{
		int index = 0;
		double maxAccuracy = accuracies.get(0);
		for(int i = 1; i < accuracies.size(); i++)
		{
			if(accuracies.get(i) >= maxAccuracy)
			{
				maxAccuracy = accuracies.get(i);
				index = i;
			}
		}
		
		if(index == 1)
			return 3;
		else if(index == 2)
			return 5;
		else if(index == 3)
			return 9;
		else
			return 1;
	}
	
	/**
	 * Returns a value of 1 if the value of k predicted the correct class
	 * @param heldOutExample the query example
	 * @param nearestNeighbors list of the nearest neighbors
	 * @param outputValues the classification values (always boolean)
	 * @param k the number of nearest neighbors to test
	 * @return 1 if classified correctly, 0 if classified incorrectly
	 */
	private double checkClassification(Example heldOutExample, ArrayList<Example> nearestNeighbors, 
									ArrayList<String> outputValues, int k)
	{
		int classA = 0;
		int classB = 0;
		String predictedClass;
		//Count the classification values for the nearest neighbors
		for(int i = 0; i < k; i++)
		{
			if(nearestNeighbors.get(i).getClassification().equalsIgnoreCase(outputValues.get(0)))
				classA++;
			else
				classB++;
		}
		
		
		if(classA >= classB)
		{
			predictedClass = outputValues.get(0);
		}
		else
			predictedClass = outputValues.get(1);
		
		if(predictedClass.equalsIgnoreCase(heldOutExample.getClassification()))
		{
			return 1.0;
		}
		else
			return 0.0;
	}
	
	/**
	 * This method runs the k-NN algorithm and prints its statistics such as
	 * the accuracy of each k value on the tuning set, the best k value,
	 * and the accuracy of k-NN on the test set using the best k value
	 *
	 */
	public void runNearestNeighbor()
	{
		double correctTestExamples = 0;
	
		int k1 = 0;
		int k3 = 0;
		int k5 = 0;
		int k9 = 0;
	
		//Tune the value ok k using leave-one-out testing 
		for(int i = 0; i < trainingExamples.size(); i++)
		{
			//create the tuning set and calculate the distance between the
			//held out example and the tuning set
			for(Example current: trainingExamples)
			{
				current.setDistance(trainingExamples.get(i), features);
			
			}
		
			//Get the nearest neighbors for this held out example
			ArrayList<Example> nearestNeighbors = getNearestNeighbors(trainingExamples.get(i), 
																	trainingExamples, 9);
		
			//Update the count for correct classifications for each k value
			k1 += checkClassification(trainingExamples.get(i), nearestNeighbors, outputValues, 1);
			k3 += checkClassification(trainingExamples.get(i), nearestNeighbors, outputValues, 3);
			k5 += checkClassification(trainingExamples.get(i), nearestNeighbors, outputValues, 5);
			k9 += checkClassification(trainingExamples.get(i), nearestNeighbors, outputValues, 9);
		
		}
	
		//Caculate percentage of correctly classified examples for each k value
		ArrayList<Double> accuracies = new ArrayList<Double>();
		accuracies.add((double)k1/trainingExamples.size());
		accuracies.add((double)k3/trainingExamples.size());
		accuracies.add((double)k5/trainingExamples.size());
		accuracies.add((double)k9/trainingExamples.size());
	
		 bestKValue = getBestKValue(accuracies);
	
		//Print accuracy of each value of k on the tuning training set
		System.out.println("Accuracy of k = 1 on tuning set: " + accuracies.get(0));
		System.out.println("Accuracy of k = 3 on tuning set: " + accuracies.get(1));
		System.out.println("Accuracy of k = 5 on tuning set: " + accuracies.get(2));
		System.out.println("Accuracy of k = 9 on tuning set: " + accuracies.get(3));
	
		System.out.println("Best k: " + bestKValue);
		System.out.println("////////////////////////////////////////////////////////////");
		
		//	Tune the value ok k using leave-one-out testing 
		for(int i = 0; i < testExamples.size(); i++)
		{
		
			//calculate the distance between the
			//held out example and the tuning set
			for(Example current: trainingExamples)
			{
				current.setDistance(testExamples.get(i), features);
			
			}
		
			//Get the nearest neighbors for this query out example
			ArrayList<Example> nearestNeighbors = getNearestNeighbors(testExamples.get(i), 
																	trainingExamples, bestKValue);
			correctTestExamples += checkClassification(testExamples.get(i), 
									nearestNeighbors, outputValues, bestKValue);
		}
		
		double totalAccuracy = correctTestExamples/testExamples.size();
		
		System.out.println("Accuracy of k-NN: " + totalAccuracy);
		System.out.println("////////////////////////////////////////////////////////////");
	}
	
	
	
	
}

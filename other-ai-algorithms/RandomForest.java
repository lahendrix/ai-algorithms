
///////////////////////////////////////////////////////////////////////////////
//                   
// Title:            Random Forest
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

public class RandomForest
{

	private ArrayList<Feature> features;
	private ArrayList<Example>trainingExamples;
	private ArrayList<Example>testExamples;
	
	private int numbering = 0;
	private int outputIndex;
	private ArrayList<String> outputValues;
	
	public RandomForest()
	{
		
	}

	private ArrayList<Feature> getFeatureSubset()
	{
		ArrayList<Feature> copy = new ArrayList<Feature>();
		ArrayList<Feature> featureSubset = new ArrayList<Feature>();
		for(Feature f: features)
		{
			copy.add(f);
		}
		
		int numOfFeatures = (int)Math.floor((features.size() * 0.2));
		for(int k = 0; k < numOfFeatures; k++)
		{
			int random = (int)(Math.floor(Math.random()*copy.size()));
			featureSubset.add(features.get(random));
			copy.remove(random);
		}
		return featureSubset;
	}
	
	/**
	 * Determines the classification of a specific example
	 * @param tree tree to test
	 * @param e example to classify
	 * @return 1 if classified correctly, 0 if incorrectly
	 */
	private String getClassification(DecisionTree tree, Example e)
	{
		String classification = null;
		//If the node is a leaf node just compare the classifications
		if(tree.getRootNode().isLeaf())
		{
				classification =  tree.getRootNode().getClassification();
		}
		
		//If the node is a leaf node but doesn't have a split value declared 
		//use the majority classification and compare
		if(tree.getRootNode().getSplitValue() == null)
		{
				classification =  tree.getRootNode().getMajorityClassification();
		}
		
	
		else
		{
			//Determine the path for a continuous function
			if(tree.getRootNode().getFeature().isContinuous())
			{
				
				double exampleVal = Double.parseDouble(e.getValues().get(tree.getRootNode().getFeature().getIndex()));
				double t = Double.parseDouble(tree.getRootNode().getSplitValue());
				
					if(exampleVal > t) 
						classification = getClassification(tree.getChildren().get(0), e);
					else
						classification = getClassification(tree.getChildren().get(1), e);
			}
			
			//Determine the path for a discrete feature
			else
			{
				int featIndex = tree.getRootNode().getFeature().getIndex();
				String exampleVal = e.getValues().get(tree.getRootNode().getFeature().getIndex());
				//FInd which path to continue down by selecting the correct child node
				if(tree.getRootNode().getSplitValue().equalsIgnoreCase(exampleVal))
				{
					for(DecisionTree x: tree.getChildren())
					{
						if(x.getRootNode().getExamples().get(0).getValues().get(featIndex).equalsIgnoreCase(exampleVal))
						{
							classification = getClassification(x, e);
						}
					}
				}
				
				else
					classification = outputValues.get(0);
					
			}
			
		}
		
		return classification;
	}
	/**
	 * Method that runs reads the appropriate files and 
	 * runs the id3 learning algorithm with pruning
	 * @param namesFile file containing features
	 * @param trainsetFile file containing the training set
	 * @param testsetFile file containing the test set
	 * @param splittingFunction 
	 */
	public void run_random_forest(String namesFile, String trainsetFile, String testsetFile, String splittingFunction)
	{
		
		double scoreOfBestTree = 0.0;
		
		//Read files and store examples
		readFiles(namesFile, trainsetFile, testsetFile);
		
		//Create set as 20% of the training set and create a train' set
		int numOfExamples = (int)Math.floor((trainingExamples.size()));
		ArrayList<Example> tuningExamples1 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples2 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples3 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples4 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples5 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples6 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples7 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples8 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples9 = new ArrayList<Example>();
		ArrayList<Example> tuningExamples10 = new ArrayList<Example>();
		
		for(int k = 0; k < numOfExamples; k++)
		{
			int random1 = (int)(Math.floor(Math.random()*numOfExamples));
			int random2 = (int)(Math.floor(Math.random()*numOfExamples));
			int random3 = (int)(Math.floor(Math.random()*numOfExamples));
			int random4 = (int)(Math.floor(Math.random()*numOfExamples));
			int random5 = (int)(Math.floor(Math.random()*numOfExamples));
			int random6 = (int)(Math.floor(Math.random()*numOfExamples));
			int random7 = (int)(Math.floor(Math.random()*numOfExamples));
			int random8 = (int)(Math.floor(Math.random()*numOfExamples));
			int random9 = (int)(Math.floor(Math.random()*numOfExamples));
			int random10 = (int)(Math.floor(Math.random()*numOfExamples));
			tuningExamples1.add(trainingExamples.get(random1));
			tuningExamples2.add(trainingExamples.get(random2));
			tuningExamples3.add(trainingExamples.get(random3));
			tuningExamples4.add(trainingExamples.get(random4));
			tuningExamples5.add(trainingExamples.get(random5));
			tuningExamples6.add(trainingExamples.get(random6));
			tuningExamples7.add(trainingExamples.get(random7));
			tuningExamples8.add(trainingExamples.get(random8));
			tuningExamples9.add(trainingExamples.get(random9));
			tuningExamples10.add(trainingExamples.get(random10));
		}
		
		
		DecisionTree tree1 = id3( tuningExamples1,features,  splittingFunction);
		DecisionTree tree2 = id3( tuningExamples2, features,  splittingFunction);
		DecisionTree tree3 = id3( tuningExamples3, features,  splittingFunction);
		DecisionTree tree4 = id3( tuningExamples4, features,  splittingFunction);
		DecisionTree tree5 = id3( tuningExamples5, features,  splittingFunction);
		DecisionTree tree6 = id3( tuningExamples6,features,  splittingFunction);
		DecisionTree tree7 = id3( tuningExamples7, features,  splittingFunction);
		DecisionTree tree8 = id3( tuningExamples8, features,  splittingFunction);
		DecisionTree tree9 = id3( tuningExamples9, features,  splittingFunction);
		DecisionTree tree10 = id3( tuningExamples10, features,  splittingFunction);
		ArrayList<DecisionTree> treeList = new ArrayList<DecisionTree>();
		treeList.add(tree1);
		treeList.add(tree2);
		treeList.add(tree3);
		treeList.add(tree4);
		treeList.add(tree5);
		treeList.add(tree6);
		treeList.add(tree7);
		treeList.add(tree8);
		treeList.add(tree9);
		treeList.add(tree10);;
		
		double sum = 0.0;
		//Determine the majority vote classification for each example
		for(Example e: testExamples)
		{
			int out1 = 0;
			int out2 = 0;
			DecisionTree class1Tree = null;
			DecisionTree class2Tree = null;
			
			for(DecisionTree t: treeList)
			{
				if(getClassification(t, e).equalsIgnoreCase(outputValues.get(0)))
				{	
					out1++;
					class1Tree = t;
				}
				else
				{
					out2++;
					class2Tree = t;
				}
			}
			
			if(out1 > out2)
				sum += class1Tree.classify(class1Tree, e);
			else
				sum += class2Tree.classify(class2Tree, e);
			
		}
		
		System.out.println("////////////////////////////////////////////////////////////");
		System.out.println("Accuracy of Random Forest learner: " + sum/testExamples.size());
		System.out.println("////////////////////////////////////////////////////////////");
		
		
	}
	
	/**
	 * Method that reads the files and creates a list of features and examples
	 * @param namesFile
	 * @param trainingFile
	 */
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
		testExamples = reader.createExampleList(testFile, outputIndex);
	
	}
	
	/**
	 * Creates an arraylist of type ContFeatureList that hold the threshold
	 * values for each continuous feature
	 * @param exampleList list of examples 
	 */
	private ArrayList<ContFeatureList> getBoundaryList(ArrayList<Example> exampleList)
	{
		 ArrayList<ContFeatureList> thresholds = new ArrayList<ContFeatureList>();
		for(int i = 0; i < features.size(); i++)
		{
			Feature f = features.get(i);
			if(f.getFeatureType().equalsIgnoreCase("continuous"))
			{
				thresholds.add(new ContFeatureList(exampleList, f.getIndex(), f, outputValues));
			}
		}
		
		return thresholds;
	}
	
	/**
	 * Method that implements the id3 algorithm
	 * @param examples list of examples 
	 * @param attributes list of features 
	 * @param splitFunction 
	 * @return
	 */
	public DecisionTree id3(ArrayList<Example> examples, ArrayList<Feature> attributes, String splitFunction)
	{
		numbering++;
		ArrayList<Feature> newAttributeList = new ArrayList<Feature>();
		
			//create copy of feature list
			for(int i = 0; i < attributes.size(); i++)
			{
				Feature tmp = attributes.get(i);
				newAttributeList.add(tmp);
			}
		
		ArrayList<ContFeatureList> boundaryValues = getBoundaryList(examples);
		
		//Create a new root node
		Node root = new Node( examples, outputValues, numbering);
		DecisionTree tree = new DecisionTree(root);
		
		/*If the root node contains all the same classifications, return the
		 * single tree node with the corresponding classification 
		 */
		if(root.isLeaf())
			return tree;
		
		/*
		 * If there are no more attributes to test, simply 
		 * return the majority classification
		 */
		else if(newAttributeList.isEmpty())
		{
			tree.getRootNode().setClassification(root.getMajorityClassification());
			return tree;
		}
	
		/*
		 * Otherwise begin the algorithm to search for a DecisionTree
		 */
		else
		{
			
			double totalEntropy = getTotalEntropy(examples);
			
			//create a subset of features
			ArrayList<Feature> featureSubset = new ArrayList<Feature>();
			
			int numOfFeatures = (int)Math.floor((newAttributeList.size()  * 0.75));
			if(numOfFeatures == 0)
				numOfFeatures = newAttributeList.size();
			for(int k = 0; k < numOfFeatures; k++)
			{				
				int random = (int)(Math.floor(Math.random()*newAttributeList.size()));
				featureSubset.add(newAttributeList.get(random));

				if(!newAttributeList.get(random).isContinuous())
				{
					newAttributeList.remove(random);
				}
				
			}
			
			//determine the best feature of the subset
			Feature bestFeature = getBestFeature(totalEntropy, examples, featureSubset, 
					 splitFunction, boundaryValues);
			root.setFeature(bestFeature);
			
			ArrayList<Feature> newAttributeList2 = new ArrayList<Feature>();
			for(int i = 0; i < newAttributeList.size(); i++)
			{
				newAttributeList2.add(newAttributeList.get(i));
			}
			/*
			 * If the split feature is continuous simply check each example
			 * versus the split threshold and create a new list of examples
			 * that meet that threshold
			 */
			if(bestFeature.isContinuous())
			{
				ArrayList<Example> aboveThresholdList = new ArrayList<Example>();
				ArrayList<Example> belowThresholdList = new ArrayList<Example>();
				double thresh = 0;
				for(ContFeatureList tmp: boundaryValues)
				{
					if(tmp.getFeature().same(bestFeature))
					{
						thresh = tmp.getBestThreshold();
					}
				}
				
				tree.getRootNode().setFeatureValue(Double.toString(thresh));
				for(Example e: examples)
				{
					double val = Double.parseDouble(e.getValues().get(bestFeature.getIndex()));
					
					/*Simultaneously create both a set of examples that exceed 
					 * the threshold and a set of examples that fall below
					 * the threshold
					 */
					if(val > thresh)
					{
						aboveThresholdList.add(e);
					}
					else
						belowThresholdList.add(e);
				}
				
				
				 //Create sub trees for examples that exceed the threshold
				if(!aboveThresholdList.isEmpty())
				{
					newAttributeList.remove(bestFeature);
					tree.addTree(id3(aboveThresholdList, newAttributeList, splitFunction));
					
				}
				
				else
				{
					DecisionTree child = new DecisionTree(new Node(aboveThresholdList, outputValues, numbering));
					tree.addTree(child);
				}
				
				
				
				//Create sub trees for examples that fall below the threshold
				if(!belowThresholdList.isEmpty())
				{
					newAttributeList2.remove(bestFeature);
					tree.addTree(id3(belowThresholdList, newAttributeList2, splitFunction));
				}
				
				else
				{
					DecisionTree child = new DecisionTree(new Node(belowThresholdList, outputValues, numbering));
					tree.addTree(child);
				}
			}
			
			/*
			 * If the split feature is discrete create a new list of examples
			 * for each possible feature value
			 */
			else
			{
				int k = bestFeature.getIndex();
				
			
				for(String val: bestFeature.getPossibleValues())
				{
					//Create a new subtree for each of the possible values
					ArrayList<Example> newExampleList = new ArrayList<Example>();
					for(Example e: examples)
					{
						
						if(e.getValues().get(k).equalsIgnoreCase(val))
						{
							newExampleList.add(e);
						}
					
					}
					DecisionTree child = new DecisionTree(new Node(newExampleList, outputValues, numbering));
					child.getRootNode().setFeatureValue(val);
					
			
					if(!child.getRootNode().isLeaf())
					{
						newAttributeList.remove(bestFeature);
						child.addTree(id3(newExampleList, newAttributeList, splitFunction));
						
					}
					
					else
					{
						child.getRootNode().setClassification(child.getRootNode().getMajorityClassification());
						tree.addTree(child);
					}
				}
			}
			
			return tree;	
		}
		
	
		
	}
	
	/**
	 * Determines the best split feature by calculating information gain
	 * @param totalEntropy entropy of the entire training set
	 * @param examples list of examples 
	 * @param featureList list of features
	 * @param splittingFunction metric to use when picking best feature
	 * @param contFeatures list of continuous features and their thresholds
	 * @return
	 */
	public Feature getBestFeature(double totalEntropy, ArrayList<Example> examples, ArrayList<Feature> featureList, 
									String splittingFunction, ArrayList<ContFeatureList> contFeatures)
	{
		
		if(splittingFunction.equalsIgnoreCase("random"))
		{
			
			int index = (int)(Math.floor(Math.random() * featureList.size()));
			return featureList.get(index);
		}
		
		else if(splittingFunction.equalsIgnoreCase("info_gain"))
		{
			Feature bestFeature = featureList.get(0);
			double maxGain = getInformationGain(totalEntropy, featureList.get(0),
												examples, contFeatures);
			for(Feature f: featureList)
			{
				double gain = getInformationGain(totalEntropy, f, examples, contFeatures);
				if(gain > maxGain)
				{
					maxGain = gain;
					bestFeature = f;
				}
			}
			
			return bestFeature;
		}
		
		else
		{
			Feature bestFeature = featureList.get(0);
			double maxGain = getInformationGain(totalEntropy, featureList.get(0),
					examples, contFeatures)/getSplitInformation(bestFeature, examples,contFeatures);
			
			for(Feature f: featureList)
			{
				double gain = getInformationGain(totalEntropy, f, examples, contFeatures)/
								getSplitInformation(bestFeature, examples,contFeatures);
				if(gain > maxGain)
				{
					maxGain = gain;
					bestFeature = f;
				}
			}
			
			return bestFeature;
		}
		
	}
	
	/**
	 * Calculates the total entropy of a list of examples
	 * @param examples list of examples
	 * @return values of the total entropy
	 */
	private double getTotalEntropy(ArrayList<Example> examples)
	{
		double categoryA = 0;
		double categoryB = 0;
		
		//Count the number of "positive" and "negative" examples
		for(Example e: examples)
		{
			if(e.getClassification().equals(outputValues.get(0)))
				categoryA++;
			else
				categoryB++;
		}
		
		double posTerm = categoryA/examples.size();
		double negTerm = categoryB/examples.size();
		
		//Calculate total entropy
		double totalEntropy = -posTerm*(Math.log(posTerm)/Math.log(2)) - negTerm*(Math.log(negTerm)/Math.log(2));
		
		return totalEntropy;
	}

	/**
	 * Calculates the entropy for a continuous feature
	 * @param examples list of training examples
	 * @param f feature of which to calculate entropy 
	 * @param contFeatures list of the continuous features and their thresholds
	 * @return entropy of this current feature using the best threshold
	 */
	private double getContinuousEntropy( ArrayList<Example> examples, Feature f, 
											ArrayList<ContFeatureList> contFeatures)
	{
		double pos = 0;
		double neg = 0;
		double numOfExamples = 0;
		double threshold = contFeatures.get(0).getBestThreshold();
		int index = 0;
		
		for(int i = 0; i < contFeatures.size(); i++)
		{
			if(contFeatures.get(i).getFeature() == f)
			{
				
				threshold = contFeatures.get(i).getBestThreshold();
				index = contFeatures.get(i).getIndex();
			}
		}
		
		for(Example e: examples)
		{
		
			double tmp = Double.parseDouble(e.getValues().get(index));
			if(tmp >= threshold)
			{
				numOfExamples++;
				if(e.getClassification().equals(outputValues.get(0)))
					pos++;
				else
					neg++;
			}
		}
	
		double posFraction = pos/numOfExamples;
		double negFraction = neg/numOfExamples;
		
		//Calculate entropy
		double entropy = -posFraction*(Math.log(posFraction)/Math.log(2)) - negFraction*(Math.log(negFraction)/Math.log(2));
		
		return entropy;
		
	}
	
	/**
	 * Returns the information gain of a feature
	 * @param totalEntropy total entropy of the training set 
	 * @param f current feature to split on
	 * @param examples list of training examples
	 * @param contFeatures list of continuous features and their thresholds
	 * @return information gain for a certain feature
	 */
	public double getInformationGain(double totalEntropy, Feature f, 
										ArrayList<Example> examples, 
										ArrayList<ContFeatureList> contFeatures)
	{
		if(f.isContinuous())
		{
			return totalEntropy - getContinuousEntropy( examples, f, contFeatures);
		}
		
		else
		{	
			double entropy = 0.0;
			
			for(String val: f.getPossibleValues())
			{
				double pos = 0;
				double neg = 0;
				double numOfExamples = 0;
				for(Example e: examples)
				{
							
					/*
					* If the example has this attribute value, count the number 
					* of each classification
					*/
					if(e.getValues().get(f.getIndex()).equalsIgnoreCase(val))
					{
						numOfExamples++;
						if(e.getClassification().equalsIgnoreCase(outputValues.get(0)))
							pos++;
						else
							neg++;
					}
				}
				
				double posFraction = pos/numOfExamples;
				double negFraction = neg/numOfExamples;
				
				//Sum the entropies
				entropy += -posFraction*(Math.log(posFraction)/Math.log(2)) - negFraction*(Math.log(negFraction)/Math.log(2));
			}
			
				return totalEntropy - entropy;
		}
		
	}
	
	
	/**
	 * Calculates the Split(a,b) value
	 * @param f feature to calculate value on
	 * @param exampleslist of examples
	 * @param contFeatures list of continuous features and thresholds
	 * @return
	 */
	public double getSplitInformation(Feature f, ArrayList<Example> examples,ArrayList<ContFeatureList> contFeatures)
	{
		if(f.isContinuous())
		{
			//return  getContinuousEntropy( examples, f, contFeatures)
			double numOfExamples = 0;
			double threshold = 0;
			int index = 0;
			double split = 0.0;
			
			//Find the threshold value associated with this feature
			for(int i = 0; i < contFeatures.size(); i++)
			{
				if(contFeatures.get(i).getFeature().equals(f))
				{
					threshold = contFeatures.get(i).getBestThreshold();
					index = contFeatures.get(i).getIndex();
				}
			}
			
			for(Example e: examples)
			{
				double tmp = Double.parseDouble(e.getValues().get(index));
				if(tmp > threshold)
				{
					numOfExamples++;
				}
			}
		
			double fraction = numOfExamples/examples.size();
			
			//Sum the entropies
			split += fraction*(Math.log(fraction)/Math.log(2));
			
			return split;
			
		}
		
		else
		{	
			double split = 0.0;
			
			for(String val: f.getPossibleValues())
			{
				double numOfExamples = 0;
				for(Example e: examples)
				{	
					/*
					* If the example has this attribute value, count the number 
					* of each classification
					*/
					if(e.getValues().get(f.getIndex()).equalsIgnoreCase(val))
					{
						numOfExamples++;
					}
				}
				
				double fraction = numOfExamples/examples.size();
				
				//Sum the entropies
				split += fraction*(Math.log(fraction)/Math.log(2));
			}
			
				return  split;
		}
	}
}


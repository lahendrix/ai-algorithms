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

public class HW1 
{
	
	public static void main(String[] args) 
	{
		int numOfComLineArgs = args.length; //number of command line arguments
		if(numOfComLineArgs != 3)
		{
			System.out.println("Usage: java HW1 task.names " +
							"train_examples.data test_examples.data ");
			System.exit(0);
		}
		
		//Create a KNN object
		//KNN runNN = new KNN(args[0], args[1], args[2]);
		
		//Run the k-NN algorithm
		//runNN.runNearestNeighbor();
		
		//Create an ID3 object
		ID3 id3 = new ID3();
		
		//Run the id3 algorithm
		id3.run_id3(args[0], args[1], args[2], "info_gain");
		
		//RandomForest rf = new RandomForest();
		//rf.run_random_forest(args[0], args[1], args[2], "info_gain");
		
	}
	
}



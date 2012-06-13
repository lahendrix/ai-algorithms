import java.util.*;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;


public class gibbs 
{

	static ArrayList<Node> nodes = new ArrayList<Node>();
	static String[] evidence;
	static ArrayList<String[]> evidenceList = new ArrayList<String[]>();
	static ArrayList<Integer> query = new ArrayList<Integer>();
	
	/**
	 * Read nodes from input file
	 * @param inputFile
	 */
	public static void readNodes(String inputFile)
	{
		ArrayList<String> readLines = new ArrayList<String>();
		
		
		int numOfEvidence = 0;
		//Read lines from the input file
		try
		{
			BufferedReader in = new BufferedReader(new FileReader(inputFile));
			String line;
			
			while((line = in.readLine())  != null) 
			{ 
				if(line.startsWith("E"))
				{
					evidence = line.split(" ");
					evidenceList.add(line.split(" "));
				}
				
				else if(line.startsWith("Q"))
				{
					String[] tmp = line.split(" ");
					query.add(Integer.parseInt(tmp[1])); //store the query node number
					
				}
				
				else if(!line.startsWith("//"))
				{
					readLines.add(line);
				}
			}
			
			in.close();
		}
		
		catch(FileNotFoundException e)
		{
			System.out.println("File " + inputFile + " not found");
			System.exit(0);
		}
		catch(IOException e)
		{
			System.out.println("IO Exception!");
			System.exit(0);
		}	
		/////////////////////////////////////////
		nodes.add(new Node(0));
		/////////////////////////////////////////
		
		boolean creatingNewNode = false;
		int nodeID = 1;
		int index = 0;
		int numOfNodes = 0;
		String[][] info = new String[100][100];
		
		for(String s: readLines)
		{
			if(s.startsWith("{"))
			{
				creatingNewNode = true;
				index = 0;
				numOfNodes++;
			}
			
			else if(s.startsWith("}"))
			{
				creatingNewNode = false;
				nodeID++;
			}
			
			if(creatingNewNode && !s.startsWith("{"))
			{
				info[nodeID-1][index] = s;
				index++;
			}
		}
		
		for(int i = 0; i < numOfNodes; i++)
		{
			Node n = new Node(i+1);
			
			String parentInfo[] = info[i][0].split("//");
			
			String parents[] = parentInfo[0].split(" ");
			
			//Check to see if the node has parents
			if(parents.length > 0)
			{
				n.hasParents();
				for(String s: parents)
				{
					n.addParent(Integer.parseInt(s));
					
				}
			}
			String rowInfo[] = new String[100];
			for(int k = 0; k < info.length - 1; k++)
			{
				rowInfo[k] = info[i][k+1];
			}
			
			n.setCPT(rowInfo);
			nodes.add(n);
		}
		
		
		
	}
	
	public static void createMarkovBlanket()
	{
		for(Node n: nodes)
		{
			int id = n.getId();
			
			if(id != 0)
			{
				//Add the parents to the markov blanket
				for(Integer i: n.getParents())
				{
					n.addToMarkovBlanket(i);
				}
				
				//Add its children
				for(Node k: nodes)
				{
					if(k.getId() != id)
					{
						ArrayList<Integer> childrensParents = k.getParents();
						if(childrensParents.contains(id))
						{
							n.addToMarkovBlanket(k.getId());
							//Add it's children's parents
							for(Integer m: childrensParents)
							{
								n.addToMarkovBlanket(m);
							}
						}
					}
				}
			}
		}
	}
	/**
	 * Sets the evidence nodes to their appropriate values and
	 * returns a list of those evidence nodes
	 * @param e array of the parsed evidence read from the input file
	 * @return list of the evidence nodes' id numbers
	 */
	public static ArrayList<Integer> applyEvidence(String[] e)
	{
		ArrayList<Integer> evidNodes = new ArrayList<Integer>();
		
		for(int i = 1; i < e.length ; i++)
		{
			if(i % 2 > 0) //Must be a node number
			{
				int nodeNum = Integer.parseInt(e[i]);
				evidNodes.add(nodeNum);
				
				if(e[i+1].equalsIgnoreCase("t"))
					nodes.get(nodeNum ).setToTrue();
				else
					nodes.get(nodeNum ).setToFalse();
			}
		}
		
		return evidNodes;
	}
	
	/**
	 * Query the probability of a certain node being true
	 * @param nodeID
	 * @return
	 */
	public static double computeProbabilityTrue(int nodeID)
	{
		
		ArrayList<Integer> parents = nodes.get(nodeID).getParents();
	
		String[] parentsValues = new String[parents.size()];
	
		//Look up this node's potential of being TRUE for the current settings
		for(int i = 0; i < parents.size(); i++)
		{
			//Get the settings for it's parents to look up the potential in the CPT
			int parentNodeID = parents.get(i); //retrieve the parent node id
			parentsValues[i] = nodes.get(parentNodeID).getSetting(); //get the setting for that parent
		
		}
		double potential = nodes.get(nodeID).getPotential(parentsValues);
		
		//System.out.print(potential + " * ");
		
		//Look up all CPT's for its Markov blanket
		for(Integer j: nodes.get(nodeID).getMarkovBlanket())
		{
			Node n = nodes.get(j);
			
			if(n.getNumOfParents() == 0)
			{
				if(n.getSetting().equals("1"))
					potential *= n.getPrior();
				else
					potential *= 1 - n.getPrior();
			}
			
			else
			{
				ArrayList<Integer> parents2 = n.getParents();
				
				String[] currentParents = new String[parents2.size()];
				
					for(int i = 0; i < parents2.size(); i++)
					{
						int parentNodeID = parents2.get(i); //retrieve the parent node id
						if(parentNodeID == nodeID)
							currentParents[i] = "1"; //Set this node to TRUE
						else
							currentParents[i] = nodes.get(parentNodeID).getSetting(); //get the setting for that parent
					}
					double tmp;
					if(n.getSetting().equals("1"))
						tmp = n.getPotential(currentParents);
					else
						tmp = 1 - n.getPotential(currentParents);
					
					potential *= tmp;
				
			}
		}
		
		return potential;
	}
	
	/**
	 * Query the probability of a certain node being false
	 * @param nodeID
	 * @return
	 */
	public static double computeProbabilityFalse(int nodeID)
	{
		
		ArrayList<Integer> parents = nodes.get(nodeID).getParents();
		
		String[] parentsValues = new String[parents.size()];
		
		//Look up this node's potential of being TRUE for the current settings
		for(int i = 0; i < parents.size(); i++)
		{
			//Get the settings for it's parents to look up the potential in the CPT
			int parentNodeID = parents.get(i); //retrieve the parent node id
			parentsValues[i] = nodes.get(parentNodeID).getSetting(); //get the setting for that parent
		}
		double potential = 1 - nodes.get(nodeID).getPotential(parentsValues); //potential of node being FALSE, subtract from 1
		//System.out.print(potential + " * ");
		
		//Look up CPT's for it's markov blanket
		for(Integer j: nodes.get(nodeID).getMarkovBlanket())
		{
			Node n = nodes.get(j);
			
				ArrayList<Integer> parents2 = n.getParents();
				String[] currentParents = new String[parents2.size()];
				
			
					for(int i = 0; i < parents2.size(); i++)
					{
						int parentNodeID = parents2.get(i); //retrieve the parent node id
						if(parentNodeID == nodeID)
							currentParents[i] = "0"; //Set this node to False
						else
							currentParents[i] = nodes.get(parentNodeID).getSetting(); //get the setting for that parent
					}
					double tmp;
					if(n.getSetting().equals("1"))
						tmp = n.getPotential(currentParents);
					else
						tmp = 1 - n.getPotential(currentParents);
					
					potential *= tmp;
			
		}
		
		return potential;
	}
	
	/**
	 * Normalizes a given interval
	 * @param t
	 * @param f
	 * @return
	 */
	public static double normalizeInterval(double t, double f)
	{
		double trueBin;
		double normalizingFactor = t + f;
		
		trueBin = t/normalizingFactor;
	
		return trueBin;
	}
	
	public static void printSettings()
	{
		for(int i = 1; i < nodes.size(); i++)
		{
			System.out.print(nodes.get(i).getSetting());
		}
		System.out.println();
	}
	
	public static void main(String args[])
	{
		String fileName = args[0];
		
		
		//Read nodes, evidence, and queries from the input file
		readNodes(fileName);
		createMarkovBlanket(); //Define the Markov Blanket for each node
		final int BURN_IN = 200;
		final int rounds = 100000;
		
		for(String[] e: evidenceList)
		{
			for(String s: e)
			{
				System.out.print(s + " ");
			}
			System.out.println();
			//Apply the evidence to the nodes and get back a list of evidence nodes
			
			ArrayList<Integer> evidenceNodes = applyEvidence(e);
			ArrayList<Integer>changeableNodes = new ArrayList<Integer>();
			
			//Create a list of nodes that can be changed (exclude evidence nodes)
			for(Node n: nodes)
			{
				int id = n.getId();
				
				//If it's not an evidence node, add it to the list
				if(!evidenceNodes.contains(id) && n.getId() != 0)
				{
					changeableNodes.add(id);
				}
				
			}
			
			int numOfChangeableNodes = changeableNodes.size();
			Random r = new Random();
	
			for(int queryNum = 1; queryNum < nodes.size(); queryNum++)
			{
				if(!evidenceNodes.contains(queryNum))
				{
					for(Integer index: changeableNodes)
					{
						nodes.get(index).setToTrue();
					}
					double trueCount = 0.0;
					
					for(int i = 0; i < rounds; i++)
					{
						
						int nodeToChange = changeableNodes.get(r.nextInt(numOfChangeableNodes));
						
						double potentialTrue = computeProbabilityTrue(nodeToChange);
						
						double potentialFalse = computeProbabilityFalse(nodeToChange);
						
						double trueInterval = normalizeInterval(potentialTrue, potentialFalse);
						double d = r.nextDouble();
						
						if(d <= trueInterval)
						{
							nodes.get(nodeToChange).setToTrue();
						}
						else
						{
							nodes.get(nodeToChange).setToFalse();
						}
						
						if(i >= BURN_IN)
						{
							if(nodes.get(queryNum).getSetting().equals("1"))
								trueCount++; //keep tally of the query node
						}
						
					}
				
					System.out.println("P(" + queryNum + ") = " + trueCount/(rounds-BURN_IN ));
					
				}
			}
			System.out.println("************************************************");
		}
	}
}

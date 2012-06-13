import java.io.*;
import java.util.*;

public class Node 
{

	final String TRUE = "1";
	final String FALSE = "0";
	String setting; //current setting TRUE/FALSE for this node
	int id; //ID number for this node
	ArrayList<Integer> parents = new ArrayList<Integer>();
	ArrayList<Integer> markovBlanket = new ArrayList<Integer>();
	int numOfParents;
	boolean hasParents;
	
	double priorProb = 0.0;
	Hashtable<String, Double> CPT = new Hashtable<String, Double>(100);

	public Node()
	{
		//Initialize setting to true
		setting = TRUE;
	}
	
	public Node(int idNum)
	{
		id = idNum;
		setting = TRUE;
		numOfParents = 0;
	}
	
	
	/**
	 * Set the id of this node
	 * @param i
	 */
	public void setID(int i)
	{
		id = i;
	}
	
	/**
	 * Set the value of this node to true
	 */
	public void setToTrue()
	{
		setting = TRUE;
	}
	
	/**
	 * Set the value of this node to false
	 */
	public void setToFalse()
	{
		setting = FALSE;
	}
	
	/**
	 * Get the current setting of this node
	 * @return
	 */
	public String getSetting()
	{
		return setting;
	}
	
	/**
	 * Add parent node for this current node
	 * @param n
	 */
	public void addParent(int n)
	{
		parents.add(n);
		numOfParents++;
	}
	
	/**
	 * Add a node to this node's markov blanket
	 * @param i
	 */
	public void addToMarkovBlanket(int i)
	{
		if(!markovBlanket.contains(i) && i != id)
		{
			markovBlanket.add(i);
		}
	}
	
	/**
	 * Returns this node's Markov Blanket
	 * @return
	 */
	public ArrayList<Integer> getMarkovBlanket()
	{
		return markovBlanket;
	}
	
	/**
	 * Indicates if this node has parents
	 */
	public void hasParents()
	{
		hasParents = true;
	}
	
	public double getPrior()
	{
		return priorProb;
	}
	/**
	 * Creates the CPT for this node using a hash table. Each of the parents'
	 * values are concatenated to form the key (String) and the appropriate 
	 * probability is stored under that key
	 * @param rows array of Strings read from the file for each row of the CPT
	 * array must be correct size
	 */
	public void setCPT(String[] rows )
	{
		//System.out.println("**********NODE " + id + " **********");
		for(int i = 0; i < rows.length; i++)
		{
			if(rows[i] != null)
			{
				String[] tmp;
				
				//A filter in case the line contains comments
				if(rows[i].contains("//"))
				{
					String parentInfo[] = rows[i].split("//");
					 tmp = parentInfo[0].split(" ");
				}
				else
					 tmp = rows[i].split(" ");
				
				String key = "";
				
				if(!hasParents) //If no parents simply read in the prior probability
				{
					//System.out.println("NO PARENTS");
					key = "prior";
					CPT.put(key, Double.parseDouble(tmp[0]));
					priorProb = Double.parseDouble(tmp[0]);
				}
				
				else
				{
				
					for(int j = 0; j < tmp.length - 1; j++)
					{
						key += tmp[j]; //make the key for each potential be the concatenantion of its parents' values
					}
					CPT.put(key, Double.parseDouble(tmp[tmp.length - 1]));
				}
				
				//System.out.println("KEY: " + key + "     " + "VALUE: " + CPT.get(key));
			}
		}
		
	}
	
	
	/**
	 * Returns the id of this node
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Returns an arraylist of this node's parents
	 * @return
	 */
	public ArrayList<Integer> getParents()
	{
		return parents;
	}
	
	public int getNumOfParents()
	{
		return parents.size();
	}
	
	/**
	 * Returns the potential for a given setting of this node's parents
	 * @param parentsValues
	 * @return
	 */
	public double getPotential(String[] parentsValues)
	{
		if(!hasParents || parentsValues.length == 0)
		{
				return CPT.get("prior");
	
		}
		String key = "";
		
		for(String s: parentsValues)
		{
			key += s;
		}
		return CPT.get(key);
	}
	
	/**
	 * Prints the node's id and its CPT
	 */
	public void printNode()
	{
		System.out.println("***** NODE " + id + " *****");
		System.out.println(CPT.toString());
	}
	
	public static void main(String agrs[])
	{
		String row1 = "0 0 0 0.2";
		String row2 = "0 0 1 0.3";
		String row3 = "1 0 0 0.4";
		String row4 = "1 0 1 0.5";
		
		String values[] = new String[4];
		values[0] = row1;
		values[1] = row2;
		values[2] = row3;
		values[3] = row4;
		
		Node n = new Node();
		n.setCPT(values);
	}
	
}

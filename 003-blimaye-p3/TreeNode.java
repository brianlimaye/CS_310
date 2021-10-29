//-------------------------------------------------------------
// DO NOT EDIT ANYTHING FOR THIS CLASS EXCEPT TO ADD JAVADOCS
//-------------------------------------------------------------
import java.io.Serializable;

//Tree node used in a binary huffman tree

/**
 * Class represents the charcteristics and properties of TreeNode instances.
 * @author Brian Limaye
 */
public class TreeNode implements Serializable, Comparable<TreeNode> {

	//bad practice to have public inst. variables, 
	//but we want to test this more easily...
	
	//count for the character (leaf node) or 
	//total of counts from both children (internal node)
	/**
	 * Represents the number of occurrences of a character.
	 */
	public int count;
	
	//character represented by this node
	//internal node: keep character to be null
	/**
	 * Represents the stored character within the TreeNode.
	 */	
	public Character character = null;
	
	//children links
	/**
	 * Represents the left and right children of the current TreeNode instance.
	 */
	public TreeNode left, right;
	
	/**
	 * One-argument constructor used to initalize a new TreeNode instance with a count.
	 * @param count The number of occurrences of the stored character.
	 */
	public TreeNode(int count){
		this.count = count;
	}
	
	/**
	 * Two-argument constructor used to initialize a new TreeNode instance with a count and character.
	 * @param count The number of occurrences of the stored and set character.
	 * @param character The character being represented within the current TreeNode instance.
	 */
	public TreeNode(int count, Character character){
		this.count = count;
		this.character = character;
	}

	/**
	 * Sets the left child of the current TreeNode instance.
	 * @param left The left-most child to be set.
	 */
	public void setLeft(TreeNode left){ this.left = left;}

	/**
	 * Sets the right child of the current TreeNode instance.
	 * @param right The right-most child to be set.
	 */
	public void setRight(TreeNode right){ this.right = right;}
	
	/**
	 * Overriden method to compare two TreeNode instances, in terms of count and character.
	 * @param otherNode An instance of TreeNode to be compared with the current instance.
	 * @return Returns greater than 1 if the current instance is greater, less than 1 if otherNode is greater, and 0 if equal.
	 */
	public int compareTo(TreeNode otherNode){
		if (this.count - otherNode.count!=0){
			return (this.count - otherNode.count); //compare count
		}
		else{
			if (this.character!=null && otherNode.character!=null) {//use char to break the tie
				return (this.character - otherNode.character); 
				//same character + same count would be a tie
			}
			else{
				return (this.count - otherNode.count); 
				//null + same count would be a tie				
			}
		}		
	}
	
	/**
	 * Overriden method to check for direct equality among two potential TreeNode instances.
	 * @param o The object to be checked for equality with the current TreeNode instance.
	 * @return Returns true if equal, false otherwise.
	 */
	@Override
	public boolean equals(Object o){
		if (!(o instanceof TreeNode)){
			return false;
		}
		TreeNode otherNode = (TreeNode) o;
		return (this.compareTo(otherNode) == 0);
	}
	
	/**
	 * Overriden method to get the human interpreted version of the current TreeNode instance.
	 * @return Returns the human interpreted String resembling the current TreeNode instance.
	 */
	public String toString(){ 
		return "<"+this.character+","+this.count+">";			
	}
}
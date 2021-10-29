import java.io.Serializable;

/**
 * Class represents an implementation of a Binary Tree.
 * @author Brian Limaye
 */
public class BinaryTree implements Serializable {
	
	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	
	//bad practice to have public inst. variables, but we want to test this...
	//Root of tree
	/**
	 * The root of the current Binary Tree instance.
	 */
	public TreeNode root;
  	
	/**
	 * Sets the root for the current instance.
	 * @param node The node to be set as the root.
	 */
	public void setRoot(TreeNode node){
		this.root = node;
	}
	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	/**
	 * Buffer used to store the walk print info temporarily.
	 */
	private StringBuilder orderedWalk = new StringBuilder();

	/**
	 * Helper function to perform a level-order walk, while also counting the maximum height found.
	 * @return Returns the String resembling the level-order traverse along with the maximum depth following between parentheses.
	 */
	private String printLevelInfo() {

		/**
		 * Class represents a basic Node.
		 * @param <T> The generic type of Node.
		 * @author Brian Limaye
		 */
		class Node<T> {
			/**
			 * The node value.
			 */
			T value;
			/**
			 * The node's next reference.
			 */
			Node<T> next;

			/**
			 * One argument constructor to set a node's value.
			 * @param value The value to be set to the node.
			 */
			Node(T value) {
        		this.value = value;
        		this.next = null;
   			}

			/**
			 * Sets the next reference of the current node.
			 * @param next The node to be set as the next reference.
			 */
			void setNext(Node<T> next) {
        		this.next = next;
    		}
		}

		/**
		 * Class represents a basic Linked List.
		 * @param <T> The generic type of each Node.
		 * @author Brian Limaye
		 */
		class LinkedList<T> {
			/**
			 * The head of the Linked List.
			 */
			Node<T> head;
			/**
			 * The tail of the Linked List.
			 */
			Node<T> tail;

			/**
			 * Default constructor to initialize a new instance of a Linked List.
			 */
			LinkedList() {

		        this.head = null;
		        this.tail = null;
    		}

			/**
			 * Inserts a node into the Linked List to the tail.
			 * @param newNode The new node to be appended.
			 */
			void insertLast(Node<T> newNode) {

		        if (newNode == null) {
		            return;
		        }

		        if (tail == null) {

		            tail = newNode;
		            head = tail;
		            return;
		        }

		        tail.next = newNode;
		        newNode.next = null;
		        tail = newNode;
		    }

		    /**
		     * Removes the first node from the Linked List, if possible.
		     */
		    void removeFirst() {

		        if (head == null) {
		            return;
		        }

		        head = head.next;
		    }
		}

		if(root == null) {
			return "";
		}

		int prevNumChildren = 1;
		int numChildrenLevel = 0;
		int levelNo = 0;
		StringBuilder sb = new StringBuilder();
		LinkedList<TreeNode> queue = new LinkedList<>();
		queue.insertLast(new Node<TreeNode>(root));

		while(queue.head != null) {

			//Adds all children to the queue from the previous number of children.
			for(int i = 0; i < prevNumChildren; i++) {

				if(queue.head == null) {
					break;
				}

				TreeNode curr = queue.head.value;

				//Adds the node's left child to queue if non-null.
				if(curr.left != null) {
					
					queue.insertLast(new Node<TreeNode>(curr.left));
					++numChildrenLevel;
				}

				//Adds the node's right child to queue if non-null.
				if(curr.right != null) {

					queue.insertLast(new Node<TreeNode>(curr.right));
					++numChildrenLevel;
				}
	
				sb.append(curr.toString());
				queue.removeFirst();
			}

			//Increments the number of levels if children were found on that level.
			levelNo = (numChildrenLevel != 0) ? levelNo + 1 : levelNo;
			prevNumChildren = numChildrenLevel;
			numChildrenLevel = 0;
		}
		
		//Special custom String return consisting of the level-order traversal and maximum depth.
		return sb.toString() + "(" + levelNo + ")";
	}

	/**
	 * Recursive method to calculate the number of leaves from a starting node.
	 * @param node The node to start from.
	 * @return Returns the number of leaves from a given node.
	 */
	private int calculateNumLeaves(TreeNode node) {

		if(node == null) {
			return 0;
		}

		//When a leaf has been found.
		if((node.left == null) && (node.right == null)) {
			return 1;
		}

		return calculateNumLeaves(node.left) + calculateNumLeaves(node.right);
	}

	/**
	 * Recursive method for the pre-order traversal from a starting node.
	 * @param node The node to start from.
	 */
	private void preOrderWalk(TreeNode node) {

		if(node == null) {
			return;
		}

		orderedWalk.append(node.toString());
		preOrderWalk(node.left);
		preOrderWalk(node.right);
	}

	/**
	 * Recursive method for the in-order traversal from a starting node.
	 * @param node The node to start from.
	 */
	private void inOrderWalk(TreeNode node) {

		if(node == null) {
			return;
		}

		inOrderWalk(node.left);
		orderedWalk.append(node.toString());
		inOrderWalk(node.right);
	}

	/**
	 * Parses the maximum height from the orderedWalk buffer.
	 * @return Returns the maximum height from the root.
	 */
	public int height(){
	
		if(root == null) {
			return -1;
		}   

		String path = printLevelInfo();

		//Parses the path for opening and closing parentheses.
		int indexOfParen = path.indexOf("(");
		int indexOfClosing = path.indexOf(")");

		//Case where no parentheses had found.
		if((indexOfParen == -1) || (indexOfClosing == -1)) {
			return -1;
		}

		//Parses the integer placed within the set of parentheses.
		return Integer.parseInt(path.substring(indexOfParen + 1, indexOfClosing));
	}
	
	/**
	 * Calculates the number of leaves from the root node.
	 * @return Returns the number of leaves from the root node.
	 */
	public int numLeaves(){
	
		if(root == null) {
			return -1;
		}

		return calculateNumLeaves(root);
	}
	
	/**
	 * Performs a pre-order traversal starting at the root node.
	 * @return Returns the pre-order traversal starting at the root.
	 */
	public String toStringPreOrder(){

		if(root == null) {
			return "";
		} 

		preOrderWalk(root);
		String path = orderedWalk.toString();
		orderedWalk.setLength(0);

		return path;
	}
	
	/**
	 * Performs a in-order traversal starting at the root node.
	 * @return Returns the in-order traversal starting at the root.
	 */
	public String toStringInOrder(){

		if(root == null) {
			return "";
		}

		inOrderWalk(root);
		String path = orderedWalk.toString();

		orderedWalk.setLength(0);

		return path;
	}
	
	/**
	 * Performs a level-order traversal starting at the root node.
	 * @return Returns the level-order traversal starting at the root.
	 */
	public String toStringLevelOrder(){

		String path = printLevelInfo();

		int indexOfParen = path.indexOf("(");

		if(indexOfParen == -1) {
			return path;
		}

		//Parses the level-order traversal portion of the orderedWalk buffer.
		return path.substring(0, indexOfParen);
	}
	
	//-------------------------------------------------------------
	// Main Method For Your Testing -- Edit all you want
	//-------------------------------------------------------------
	
	/**
	 * Main method primarily used for testing the implementation of the Binary Tree.
	 * @param args The command-line arguments used for testing functionality at run-time.
	 */
	public static void main(String[] args){
	
		BinaryTree tree = new BinaryTree();
		
		//a single-node tree
		tree.setRoot(new TreeNode(1, 'r'));
		if (tree.height() == 0 && tree.numLeaves() == 1 
				&& tree.toStringPreOrder().equals("<r,1>")){
			System.out.println("Yay1");
		}

		//set up a tree
		//        r,1
		//       /   \
		//     a,2    e,10
		//   /     \
		// b,3     c,4
		//           \
		//           d,5
		// Note: this tree is a general binary tree but not a Huffman tree.
		TreeNode node1 = new TreeNode(2, 'a');
		TreeNode node2 = new TreeNode(3, 'b');		
		TreeNode node3 = new TreeNode(4, 'c');
		TreeNode node4 = new TreeNode(5, 'd');
		TreeNode node5 = new TreeNode(10, 'e');
		tree.root.setLeft(node1);
		tree.root.setRight(node5);
		node1.setLeft(node2);
		node1.setRight(node3);
		node3.setRight(node4);
		
		//tree basic features
		if (tree.root.left.right.count == 4 && tree.height() == 3 && tree.numLeaves() == 3){
			System.out.println("Yay2");
		}
		
		//tree traverals
		if (tree.toStringPreOrder().equals("<r,1><a,2><b,3><c,4><d,5><e,10>")){
			System.out.println("Yay3");
		}

		if (tree.toStringInOrder().equals("<b,3><a,2><c,4><d,5><r,1><e,10>")){
			System.out.println("Yay4");
		}
		
		//System.out.println(tree.toStringLevelOrder());

		if (tree.toStringLevelOrder().equals("<r,1><a,2><e,10><b,3><c,4><d,5>")){
			System.out.println("Yay5");
		}
	}	
}

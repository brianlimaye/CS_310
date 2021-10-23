import java.io.Serializable;

public class BinaryTree implements Serializable {
	
	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	
	//bad practice to have public inst. variables, but we want to test this...
  	//Root of tree
  	public TreeNode root;
  	
	public void setRoot(TreeNode node){
		this.root = node;
	}
	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	private StringBuilder orderedWalk = new StringBuilder();

	private String printLevelInfo(TreeNode node) {

		class Node<T> {

			T value;
			Node<T> next;

			Node(T value) {
        		this.value = value;
        		this.next = null;
   			}

			void setNext(Node<T> next) {
        		this.next = next;
    		}

    		Node<T> getNext() {
        		return this.next;
    		}

    		T getValue() {
        		return value;
    		}
		}

		class LinkedList<T> {

			Node<T> head;
			Node<T> tail;

			LinkedList() {

		        this.head = null;
		        this.tail = null;
    		}

			void insertLast(Node<T> newNode) {

		        if (newNode == null) {
		            return;
		        }

		        if (tail == null) {

		            tail = newNode;
		            head = tail;
		            return;
		        }

		        tail.setNext(newNode);
		        newNode.setNext(null);
		        tail = newNode;
		    }

		    void removeFirst() {

		        if (head == null) {
		            return;
		        }

		        head = head.getNext();
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

			for(int i = 0; i < prevNumChildren; i++) {

				if(queue.head == null) {
					break;
				}

				TreeNode curr = queue.head.getValue();

				if(curr.left != null) {
					
					queue.insertLast(new Node<TreeNode>(curr.left));
					++numChildrenLevel;
				}

				if(curr.right != null) {

					queue.insertLast(new Node<TreeNode>(curr.right));
					++numChildrenLevel;
				}
	
				sb.append(curr.toString());
				queue.removeFirst();
			}

			levelNo = (numChildrenLevel != 0) ? levelNo + 1 : levelNo;
			prevNumChildren = numChildrenLevel;
			numChildrenLevel = 0;
		}
		
		return sb.toString() + "(" + levelNo + ")"; //default return: change or remove as needed
	}

	private int calculateNumLeaves(TreeNode node) {

		if(node == null) {
			return 0;
		}

		if((node.left == null) && (node.right == null)) {
			return 1;
		}

		return calculateNumLeaves(node.left) + calculateNumLeaves(node.right);
	}

	private void preOrderWalk(TreeNode node) {

		if(node == null) {
			return;
		}

		orderedWalk.append(node.toString());
		preOrderWalk(node.left);
		preOrderWalk(node.right);
	}

	private void inOrderWalk(TreeNode node) {

		if(node == null) {
			return;
		}

		inOrderWalk(node.left);
		orderedWalk.append(node.toString());
		inOrderWalk(node.right);
	}

	public int height(){
		// Return the height of the tree.
		// Return -1 for a null tree
		// 
		// Hint: this is doable in _very_ few lines of code 
		//       if you choose to use recursion.
		//
		// O(H): H as the tree height

		if(root == null) {
			return -1;
		}   

		String path = printLevelInfo(root);

		int indexOfParen = path.indexOf("(");
		int indexOfClosing = path.indexOf(")");

		if((indexOfParen == -1) || (indexOfClosing == -1)) {
			return -1;
		}

		return Integer.parseInt(path.substring(indexOfParen + 1, indexOfClosing));
	}
	

	public int numLeaves(){
		// Return the number of leaf nodes in the tree.
		// Return zero for null trees.
		// 
		// Hint: this is doable in _very_ few lines of code 
		//       if you choose to use recursion.
		//
		// O(N): N is the tree size

		if(root == null) {
			return -1;
		}

		return calculateNumLeaves(root); //default return: change or remove as needed
	}
	
	
	public String toStringPreOrder(){
		// Return a string representation of the tree
		// follow PRE-ORDER traversal to include all nodes.

		// Return empty string "" for null trees.
		// Use the toString() method of TreeNode class.
		// Check main method below for examples.

		// Hint: this is doable in _very_ few lines of code 
		//       if you choose to use recursion.

		if(root == null) {
			return "";
		} 

		preOrderWalk(root);
		String path = orderedWalk.toString();
		orderedWalk.setLength(0);

		return path; //default return: change or remove as needed
	}
	

	public String toStringInOrder(){
		// Return a string representation of the tree
		// follow IN-ORDER traversal to include all nodes.

		// Return empty string "" for null trees.
		// Use the toString() method of TreeNode class.
		// Check main method below for examples.

		// Hint: this is doable in _very_ few lines of code 
		//       if you choose to use recursion.
		//

		if(root == null) {
			return "";
		}

		inOrderWalk(root);
		String path = orderedWalk.toString();

		orderedWalk.setLength(0);

		return path; //default return: change or remove as needed
	}
	
	
	public String toStringLevelOrder(){
		// Return a string representation of the tree
		// follow LEVEL-ORDER traversal to include all nodes.

		// Return empty string "" for null trees.
		// Use the toString() method of TreeNode class.
		// Check main method below for examples.
		
		// Hint: Remember that you can create a local class 
		// to help you with this!

		// [Hint]Possible approach 1:
		// It is easy to make a priority queue into a FIFO queue 
		// if you think a little bit about it. Reuse your priority 
		// queue here to do the level-order traversal. 
		
		// [Hint]Possible approach 2:
		// It is also easy to reuse the linked list class from 
		// Project 2 to implement a FIFO queue and help with the 
		// level-order traversal.

		String path = printLevelInfo(root);
		int indexOfParen = path.indexOf("(");

		if(indexOfParen == -1) {
			return path;
		}

		return path.substring(0, indexOfParen);
	}
	
	//-------------------------------------------------------------
	// Main Method For Your Testing -- Edit all you want
	//-------------------------------------------------------------
	
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

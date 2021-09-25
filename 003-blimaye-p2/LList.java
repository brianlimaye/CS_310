/**
 *A Class that represents a basic implementation of a Linked List of generic values.
 *@param <T> A generic type of Nodes.
 *@author Brian Limaye
 */
public class LList<T> { 

    /**
	 *Represents the head of the given Linked List.
	 */
   	private Node<T> head;
	/**
	 *Represents the tail of the given Linked List.
	 */
	private Node<T> tail;

	/**
	 *Default constructor to initialize a new Linked List.
	 */
	public LList(){

		this.head = null;
		this.tail = null;
	}
	
	/**
	 *Gets the head of the Linked List.
	 *@return Returns the head.
	 */
	public Node<T> getFirst() {

		return this.head;
	}

	/**
	 *Inserts a new value with value T at the front of the Linked List.
	 *@param value A generic value to be inserted at the front.
	 */
	public void insertFirst(T value) {

		Node<T> first;

		//If head is null, a simple reassignment is sufficient.
		if(head == null) {

			head = new Node<>(value);
			tail = head;
			return;
		}

		//Otherwise, a new node is created, while references are set from the previous head and the new head.
		first = new Node<>(value);
		first.setNext(head);
		head = first;
	}
	
	/**
	 *Inserts a new node with value T at the front of the Linked List.
	 *@param newNode A generic node to be inserted at the front.
	 */
	public void insertFirst(Node<T> newNode) {

		//Check for an invalid node.
		if(newNode == null) {
			return;
		}

		//If head is null, a simple reassignment is sufficient.
		if(head == null) {

			head = newNode;
			tail = head;
			return;
		}

		//Otherwise, references are set from the previous head and the new head.
		newNode.setNext(head);
		head = newNode;
	}

	/**
	 *Inserts a new node with value T at the end of the Linked List.
	 *@param newNode A generic node to be inserted at the end.
	 */
	public void insertLast(Node<T> newNode) {

		//Check for an invalid node.
		if(newNode == null) {
			return;
		}

		//If tail is null, a simple reassignment is sufficient.
		if(tail == null) {

			tail = newNode;
			head = tail;
			return;
		}

		//Otherwise, references are set from the previous head and the new head.
		tail.setNext(newNode);
		newNode.setNext(null);
		tail = newNode;		
	}

	/**
	 *Removes the first node of the Linked List, if possible.
	 *@return Returns the removed node, if possible.
	 */
	public Node<T> removeFirst() {

		//Case where the Linked List is already empty.
		if(head == null) {
			return null;
		}

		//Stores the head, prior to changing the head reference to the next node.
		Node<T> first = head;
		head = head.getNext();
		return first;
	}
	
	/**
	 *Returns the representation of the Linked List, with only its values being outputted.
	 *@return Returns the list of values for each Node in the Linked List.
	 */
	public String listToString() {  

		Node<T> curr = head;
		StringBuilder sb = new StringBuilder(); 	//Used for an optimized time complexity for appension.

		while(curr != null) {

			sb.append(curr.getValue());		//Appends the value to the outputstring.

			if(curr.getNext() != null) {
				sb.append(" ");		//Each value is delimited by a space, EXCEPT for the last node.
			}

			curr = curr.getNext();
		}

		return sb.toString();
	}

	/**
	 *Main method used for testing the functionality of the Linked List implementation.
	 *@param args Command-line arguments primarily used for testing the functionality at runtime.
	 */
	public static void main(String[] args) {
		
		/**
		 *Inner class representing an arbitrary type for testing.
		 */
		class SomeType {
			/**
			 *A value.
			 */
			private int value;

			/**
			 *One-arg constructor for creating a new SomeType instance.
			 *@param value The value to be set.
			 */
			public SomeType(int value) { this.value = value; }
			/**
			 *Gets the human interpreted representation for the SomeType instance.
			 *@return Returns the human interpreted representation for the SomeType.
			 */
			public String toString() { return "" + value; }
			/**
			 *Compares the current SomeType and the parameter Object, if both are SomeTypes.
			 *@param o The other object being compared.
			 *@return Returns true if both are equal, false otherwise.
			 */
			public boolean equals(Object o) {
				if (!(o instanceof SomeType)) return false;
				return ((SomeType)o).value == value;
			}	
		}
		
		SomeType item1 = new SomeType(100);
		SomeType item2 = new SomeType(200);
		SomeType item3 = new SomeType(300);
		SomeType item4 = new SomeType(400);

		Node<SomeType> n5 = new Node<>(new SomeType(500));
		
		LList<SomeType> list = new LList<>();
		list.insertFirst(item1);
		list.insertFirst(item2);
		list.insertFirst(item3);
		list.insertFirst(item4);

		System.out.println("List: " + list.listToString());		

		if (list.listToString().equals("400 300 200 100")) {
			System.out.println("Yay1");
		}

		list.insertLast(n5);	
		if (list.listToString().equals("400 300 200 100 500")) {
			System.out.println("Yay2");
		}

		list.removeFirst();	
		if (list.listToString().equals("300 200 100 500")) {
			System.out.println("Yay3");
		}

		if (list.getFirst().getValue().toString().equals("300")) {
			System.out.println("Yay4");
		}
		
		list.insertFirst(new SomeType(600));	
		if (list.listToString().equals("600 300 200 100 500")) {
			System.out.println("Yay5");
		}	
	}
}
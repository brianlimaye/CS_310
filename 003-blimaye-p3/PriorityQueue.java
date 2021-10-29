import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Class represents a simple implementation of a Priority Queue.
 * @param <T> The generic type of the Priority Queue.
 * @author Brian Limaye
 */
public class PriorityQueue<T extends Comparable<T>> implements Iterable<T> {

	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	
	/**
	 * The head of the Priority Queue.
	 */
	private Node<T> head = null;
	
	//provided linked list node class
	/**
	 * Class represents a Node to be stored in the Priority Queue.
	 * @param <T> The generic type of the Node.
	 * @author Yutao Zhong
	 */
	private static class Node<T> {
		/**
		 * The value stored within the Node.
		 */
		private T value;
		/**
		 * The next reference stored within the Node.
		 */
		private Node<T> next;
		/**
		 * One-argument constructor to initialize a new Node with a value.
		 * @param value The value to be set for the Node.
		 */
		public Node(T value) { this.value = value; }
	}
		
	//provided toString() method using the iterator
	/**
	 * Gets the human interpreted version of the current Priority Queue instance.
	 * @return Returns the human interpreted version of the current Priority Queue instance.
	 */
	public String toString(){
		StringBuilder builder = new StringBuilder("");
		for (T value : this){
			builder.append(value);
			builder.append(" ");
		}
		return builder.toString().trim();
	}
	
	//provided iterator, if your code is working, this should
	//work too...
	/**
	 * Gets an iterator associated with traversing the Priority Queue.
	 * @return Returns an iterator of generic type of the Priority Queue.
	 */
	public Iterator<T> iterator() {
		return new Iterator<T>() {
			Node<T> current = head;
			
			public T next() {
				if(!hasNext()) {
					throw new NoSuchElementException();
				}
				T val = current.value;
				current = current.next;
				return val;
			}
			
			public boolean hasNext() {
				return (current != null);
			}
		};
	}

	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------
	
	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!
	
	/**
	 * The current size of the Priority Queue.
	 */
	private int currentSize;
	
	/**
	 * Default constructor to initalize a new PriorityQueue instance.
	 */
	public PriorityQueue() {
		//Constructor
		//initializing members if needed
		this.currentSize = 0;
	}
	
	/**
	 * Gets the current size of the current PriorityQueue instance.
	 * @return Returns the current size of the current PriorityQueue instance.
	 */
	public int size(){
		//Return the number of elements in the priority queue
		//O(1)
		return currentSize; //default return: change or remove as needed
	}
	
	/**
	 * Adds a value into the PriorityQueue based on the intrinsic priority of its nature.
	 * @param value The value to be added to the PriorityQueue.
	 */
	public void add(T value) {
		
		//Check for an invalid value.
		if(value == null) {
			return;
		}

		//Special case where the head hasn't been set yet, where the priority queue is empty.
		if(head == null) {

			head = new Node<>(value);
			++currentSize;
			return;
		}

		Node<T> prev = null;
		Node<T> curr = head;
		Node<T> newNode = new Node<>(value);

		while(curr != null) {

			//Traverses the PriorityQueue until a bigger value is found.
			if(value.compareTo(curr.value) < 0) {

				if(prev == null) {

					newNode.next = head;
					head = newNode;
					break;
				}

				prev.next = newNode;
				newNode.next = curr;
				break;
			}

			//Special case where set value is greater than all values in the queue.
			if(curr.next == null) {

				curr.next = newNode;
				break;
			}

			prev = curr;
			curr = curr.next;
		}

		++currentSize;
	}

	/**
	 * Attempts to remove the element at the start of the queue.
	 * @return Returns the removed smallest value at the front of the queue.
	 */
	public T remove( ) {

		if(currentSize == 0) {
			throw new NoSuchElementException("Priority queue empty!");
		}

		T removed = head.value;
		head = head.next;
		--currentSize;
		
		return removed;
	}
	
	/**
	 * Gets the element at the start of the queue.
	 * @return Returns the smallest value at the front of the queue.
	 */
	public T element( ) {

		if(currentSize == 0) {
			throw new NoSuchElementException("Priority queue empty!");
		}
		
		return head.value;
	}

	/**
	 * Checks to see if the priority queue contains a given value.
	 * @param value The value to be checked within the queue.
	 * @return Returns true if the value was found. False otherwise.
	 */
	public boolean contains(T value){

		Iterator<T> iterator = this.iterator();

		while(iterator.hasNext()) {

			T currentElement = iterator.next();

			if(currentElement.equals(value)) {
				return true;
			}
		}
		
		return false;
	}
	
	//-------------------------------------------------------------
	// Main Method For Your Testing -- Edit all you want
	//-------------------------------------------------------------
	
	/**
	 * Main method primarily used for testing the PriorityQueue implementation.
	 * @param args Command-line arguments used for dynamically testing the functionality at runtime.
	 */
	public static void main(String[] args){
		PriorityQueue<Character> letters = new PriorityQueue<>();
				
		//add/size/element/contains
		String chars = "MASON";
		for (int i=0; i<5; i++){
			letters.add(chars.charAt(i));
		}
			
		if (letters.size() == 5 && letters.element() == 'A' 
			&& letters.contains('O') && !letters.contains('B')){
			System.out.println("Yay 1");
		}
				
		//remove
		if (letters.remove() == 'A' && letters.size() == 4 && letters.element() == 'M') {
			System.out.println("Yay 2");
		}
		
		//sequence of add/remove
		PriorityQueue<Integer> nums = new PriorityQueue<>();
		for (int i=0; i<10; i++){
			int val = (i*i) % 17;
			nums.add(val);
		}
		boolean ok = nums.toString().trim().equals("0 1 2 4 8 9 13 13 15 16");
		StringBuilder output = new StringBuilder();
		for (int i=0; i<10; i++){
			int val = nums.remove();
			output.append(val);
			output.append(" ");
		}
		if (ok && output.toString().trim().equals("0 1 2 4 8 9 13 13 15 16")){
			System.out.println("Yay 3");		
		}
		
		//values added with the same priority are kept in FIFO order
		PriorityQueue<String> msgs = new PriorityQueue<>();
		String msg1 = new String("Hello");
		String msg2 = new String("Hello");
		msgs.add(msg1);
		msgs.add(chars);
		msgs.add(msg2);
		if (msgs.toString().trim().equals("Hello Hello MASON") && 
			msgs.contains(msg1) && msgs.contains(msg2) &&
			msgs.element()==msg1 && msgs.remove() != msg2){  //use of "==" is intentional here
			System.out.println("Yay 4");	
		}	
	}
	
}
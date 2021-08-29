// TO DO: add your implementation and JavaDocs.

public class Column<T> {
	//default initial capacity / minimum capacity
	private static final int DEFAULT_CAPACITY = 2;
	
	//underlying array for storage -- you MUST use this for credit!
	//Do NOT change the name or type
	private T[] data;

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

	private int currentSize;
	private int capacity;


	@SuppressWarnings("unchecked")
	private void resizeArray(double mult) {

		T[] prev = data;
		T[] newArr = (T[]) new Object[(int) (capacity * mult)];
		int len = (int) Math.min(capacity * mult, capacity);

		for(int i = 0; i < len; i++) {

			newArr[i] = prev[i];
		}

		capacity *= mult;
		this.data = newArr;
	}
		
	@SuppressWarnings("unchecked")
	public Column() {
		// Constructor
		this.data = (T[]) new Object[DEFAULT_CAPACITY];
		this.currentSize = 0;
		this.capacity = DEFAULT_CAPACITY;
		// Initial capacity of the storage should be DEFAULT_CAPACITY
		// Hint: Can't remember how to make an array of generic Ts? It's in the textbook...
		
	}

	@SuppressWarnings("unchecked")
	public Column(int initialCapacity) {
		// Constructor

		// Initial capacity of the storage should be initialCapacity

		if(initialCapacity < 1) {
			throw new IllegalArgumentException("Capacity must be positive.");
		}
		this.data = (T[]) new Object[initialCapacity];
		this.currentSize = 0;
		this.capacity = initialCapacity;
		// Throw IllegalArgumentException if initialCapacity is smaller than 1
		// Use this _exact_ error message for the exception
		// (quotes are not part of the message):
		//    "Capacity must be positive."
		
	}
	

	public int size() {	
		// Report the current number of elements
		return currentSize;
		// O(1)
	}  
		
	public int capacity() { 
		// Report max number of elements before expansion
		return capacity;
		// O(1)
	}


	public T set(int index, T value) {
		// Change the item at the given index to be the given value.
		// Return the old item at that index.
		// Note: You cannot add new items with this method.

		T oldValue;

		if((index < 0) || (index > currentSize)) {
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}

		oldValue = data[index];
		data[index] = value;

		return oldValue;
		// O(1)
		
		// For an invalid index, throw an IndexOutOfBoundsException
		// Use this code to produce the correct error message for
		// the exception (do not use a different message):
		//	  "Index: " + index + " out of bounds!"		
	}

	public T get(int index) {
		// Return the item at the given index
		
		if((index < 0) || (index >= capacity)) {
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}

		return data[index];
		
		// O(1)
		
		// Use the exception (and error message) described in set()
		// for invalid indicies.				
	}

	@SuppressWarnings("unchecked")
	public void add(T value) {
		// Append an element to the end of the storage.		
		// Double the capacity if no space available.

		if(currentSize >= capacity) {
			resizeArray(2);
		}

		data[currentSize++] = value;
		// Amortized O(1)	
	} 

	@SuppressWarnings("unchecked")
	public void add(int index, T value) {
		// Insert the given value at the given index. Shift elements if needed,  
		// double capacity if no space available, throw an exception if you cannot
		// insert at the given index. You _can_ append items with this method.
		
		// For the exception, use the same exception and message as set() and
		// get()... however remember that the condition of the exception is
		// different (different indexes are invalid).
		boolean hasReached = false;
		T prev = null;
		T curr = null;

		if((index < 0) || (index > currentSize)) {
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}

		if(currentSize >= capacity) {
			resizeArray(2);
		}

		for(int i = 0; i < currentSize; i++) {

			if(i == index) {
				hasReached = true;
				++currentSize;
				prev = data[i];
				data[i] = value;
			}

			else if(hasReached) {
				curr = data[i];
				data[i] = prev;
				prev = curr;
			}
		}

		if(!hasReached) {

			add(value);
		}
		// O(N) where N is the number of elements in the storage
	} 
	
	
	@SuppressWarnings("unchecked")
	public T delete(int index) {
		// Remove and return the element at the given index. Shift elements
		// to remove the gap. Throw an exception when there is an invalid
		// index (see set(), get(), etc. above).
		
		// Halve capacity of the storage if the number of elements falls
		// below 1/3 of the capacity. Capacity should NOT go below DEFAULT_CAPACITY.
		boolean hasReached = false;
		T deleted = null;

		if((index < 0) || (index > currentSize)) {
			throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
		}

		for(int i = 0; i < currentSize; i++) {

			if(i == index) {
				hasReached = true;
				deleted = data[i];
			}

			else if(hasReached) {

				data[i - 1] = data[i];
			}
		}

		if((--currentSize <= capacity / 3) && (capacity / 2 > DEFAULT_CAPACITY)) {
			resizeArray(0.5);
		}

		return deleted;
		// O(N) where N is the number of elements currently in the storage
	}  
	//******************************************************
	//*******     BELOW THIS LINE IS TESTING CODE    *******
	//*******      Edit it as much as you'd like!    *******
	//*******		Remember to add JavaDoc			 *******
	//******************************************************
	
	public String toString() {
		//This method is provided for debugging purposes
		//(use/modify as much as you'd like), it just prints
		//out the column for easy viewing.
		StringBuilder s = new StringBuilder("Column with " + size()
			+ " items and a capacity of " + capacity() + ":");
		for (int i = 0; i < size(); i++) {
			s.append("\n  ["+i+"]: " + get(i));
		}
		return s.toString();
		
	}
	
	public static void main(String args[]){
		//These are _sample_ tests. If you're seeing all the "yays" that's
		//an excellent first step! But it might not mean your code is 100%
		//working... You may edit this as much as you want, so you can add
		//own tests here, modify these tests, or whatever you need!

		//create a column of integers
		Column<Integer> nums = new Column<>();
		if((nums.size() == 0) && (nums.capacity() == 2)){
			System.out.println("Yay 1");
		}

		//append some numbers 
		for(int i = 0; i < 3; i++) {
			nums.add(i*2);
		}
		
		if(nums.size() == 3 && nums.get(2) == 4 && nums.capacity() == 4){
			System.out.println("Yay 2");
		}
		
		//create a column of strings
		Column<String> msg = new Column<>();
		
		//insert some strings
		msg.add(0,"world");
		msg.add(0,"hello");
		msg.add(1,"new");
		msg.add(3,"!");
		
		//checking
		if (msg.get(0).equals("hello") && msg.set(1,"beautiful").equals("new") 
			&& msg.size() == 4 && msg.capacity() == 4){
			System.out.println("Yay 3");
		}
		
		//delete 
		if (msg.delete(1).equals("beautiful") && msg.get(1).equals("world")  
			&& msg.size() == 3 ){
			System.out.println("Yay 4");
		}

		//shrinking
		nums.add(100);

		nums.add(0, -10);

		System.out.println(nums.toString());
		if (nums.delete(0) == -10 && nums.delete(1) == 2 && nums.delete(2) == 100
			&& nums.size() == 2  && nums.capacity() == 4) {
			System.out.println("Yay 5");
		}		
	}
	

	

}
// TO DO: add your implementation and JavaDocs.

public class Stack<T> {  
	// use a linked list (not an array)       
	private LList<T> elements;
	
	// keeps track of the number of elements on the stack
	private int currentSize;
	
	// initialize the stack to being an empty stack (or list)
	public Stack() {

		this.elements = new LList<>();
		this.currentSize = 0;
	}

	// insert element at the begining of the list
	// O(1)
	public void push(T e) {

		elements.insertFirst(e);
		++currentSize;
	}

	// remove element at the begining of the list and return it
	// O(1)
	public T pop() {
		
		Node<T> removed = elements.removeFirst();

		if(removed != null) {
			--currentSize;
			return removed.getValue();
		}

		return null;
	}

	// return element at begining of list
	// O(1)
	public T peek() {

		Node<T> first = elements.getFirst();
		
		if(first != null) {
			return first.getValue();
		}

		return null;
	}

	// O(1)
	public boolean isEmpty() {
		return currentSize == 0;		
	}

	// O(1)
	public int getSize() {	
		return currentSize;
	}

	// return string representing the values in the stack from top to bottom
	// O(n)
	public String toString() { 
		
		return elements.listToString();
	}

	public static void main(String[] args) {
		class SomeType {
			private int value;

			public SomeType(int value) { this.value = value; }
			public String toString() { return "" + value; }
			public boolean equals(Object o) {
				if (!(o instanceof SomeType)) return false;
				return ((SomeType)o).value == value;
			}	
		}
		
		SomeType item1 = new SomeType(100);
		SomeType item2 = new SomeType(200);
		SomeType item3 = new SomeType(300);
		SomeType item4 = new SomeType(400);
		
		Stack<SomeType> s = new Stack<>();
		s.push(item1);
		s.push(item2);

		if (s.getSize() == 2) {
			System.out.println("Yay1");
		}

		if (s.peek().toString().equals("200")) {
			System.out.println("Yay2");
		}
		if (s.pop().toString().equals("200")) {
			System.out.println("Yay3");
		}
	
		s.push(item3);
		s.push(item4);

		if (s.toString().equals("400 300 100")) {
			System.out.println("Yay4");
		}
		
		s.pop();
		s.pop();
		if (s.toString().equals("100")) {
			System.out.println("Yay5");
		}

		s.pop();
		if (s.isEmpty()) {
			System.out.println("Yay6");
		}

	}
}
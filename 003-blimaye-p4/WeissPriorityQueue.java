import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.HashMap;

/**
 * PriorityQueue class implemented via the binary heap.
 * From your textbook (Weiss)
 * @param <T> The generic type of the queue.
 * @author Mark Allen Weiss
 * @author Brian Limaye
 */
public class WeissPriorityQueue<T> extends WeissAbstractCollection<T>
{
	/**
	 * Main method used for testing functionality of WeissPriorityQueue.java.
	 * @param args Command-line arguments used to test functionality at run-time.
	 */
	public static void main(String[] args) {
		/**
		 * Class represents a student at George Mason University.
		 * @author Katherine (Raven) Russell
		 */
		class Student {
			/**
			 * G-Number of the student.
			 */
			String gnum;
			/**
			 * Name of the student.
			 */
			String name;

			/**
			 * Two-argument constructor to initialize a new Student instance.
			 * @param gnum The G-Number to be set.
			 * @param name The name to be set.
			 */
			Student(String gnum, String name) { this.gnum = gnum; this.name = name; }

			/**
			 * Overriden method to check equality among two potential Student instances.
			 * @param o The object to be compared with the current Student instance.
			 * @return Returns true if both instances are equal, false otherwise.
			 */
			public boolean equals(Object o) {
				if(o instanceof Student) return this.gnum.equals(((Student)o).gnum);
				return false;
			}

			/**
			 * Gets the human interpreted representation of a Student instance.
			 * @return Returns the human interpreted representation of a Student instance.
			 */
			public String toString() { return name + "(" + gnum + ")"; }

			/**
			 * Computes the hashcode for the current Student instance.
			 * @return Returns the hashcode of the current Student instance.
			 */
			public int hashCode() { return gnum.hashCode(); }
		}
		
		Comparator<Student> comp = new Comparator<>() {
			public int compare(Student s1, Student s2) {
				return s1.name.compareTo(s2.name);
			}
		};
		
		
		//TESTS FOR INDEXING -- you'll need more testing...
		
		WeissPriorityQueue<Student> q = new WeissPriorityQueue<>(comp);
		q.add(new Student("G00000000", "Robert"));
		System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); //-1, no index
		System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); //1, at root
		System.out.println();
		
		q.add(new Student("G00000001", "Cindi"));
		System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); //1, at root
		System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); //2, lower down
		System.out.println();
		
		q.remove(); //remove Cindi
		System.out.print(q.getIndex(new Student("G00000001", "Cindi")) + " "); //-1, no index
		System.out.print(q.getIndex(new Student("G00000000", "Robert")) + " "); //1, at root
		System.out.println();
		System.out.println();
		
		
		//TESTS FOR UPDATING -- you'll need more testing...
		
		q = new WeissPriorityQueue<>(comp);
		q.add(new Student("G00000000", "Robert"));
		q.add(new Student("G00000001", "Cindi"));
		
		for(Student s : q) System.out.print(q.getIndex(s) + " "); //1 2
		System.out.println();
		for(Student s : q) System.out.print(s.name + " "); //Cindi Robert
		System.out.println();
		
		Student bobby = new Student("G00000000", "Bobby");
		System.out.println(q.update(bobby));

		//System.out.println(Arrays.toString(q.array));
		
		for(Student s : q) System.out.print(q.getIndex(s) + " "); //1 2
		System.out.println();
		for(Student s : q) System.out.print(s.name + " ");  //Bobby Cindi
		System.out.println();
		
		bobby.name = "Robert";
		q.update(bobby);

		//System.out.println(Arrays.toString(q.array));
		
		for(Student s : q) System.out.print(q.getIndex(s) + " "); //1 2
		System.out.println();
		for(Student s : q) System.out.print(s.name + " "); //Cindi Robert
		System.out.println();
		
		//you'll need more testing...
	}
	
	/**
	 * HashMap used to track elements on the heap with their corresponding indices.
	 */
	protected HashMap<T, Integer> indexMap;
	
	/**
	 * Gets the corresponding index of an element on the heap, if found.
	 * @param x The element to be searched.
	 * @return Returns the index corresponding to the element.
	 */
	public int getIndex(T x) {
		//average case O(1)

		if(x == null) {
			return -1;
		}
		
		Integer index = indexMap.get(x);

		//Returns the index of x, or -1 if not found.
		return (index != null) ? index : -1;	
	}
	
	/**
	 * Attempts to update an element on the heap, changing its corresponding index, if possible.
	 * @param x The element to be updated.
	 * @return Returns true if the element has been successfuly updated, false otherwise.
	 */
	public boolean update(T x) {
		//O(lg n) average case
		//or O(lg n) worst case if getIndex() is guarenteed O(1)

		if(x == null) {
			return false;
		}

		//Flag indicating whether the element must percolate down/up when changing indices.
		boolean doesPercolateUp = true;
		int index, cmpIndex;
		int numUpdates = 0;

		if((index = getIndex(x)) != -1) {

			//Stores the new/updated element in the heap/HashMap. 
			indexMap.put(x, index);
			array[index] = x;

			cmpIndex = index / 2;

			//Gets the child and parent elements, in a percolating up algorithm.
			T child = array[index];
			T parent = array[cmpIndex];

			//Determines whether the element must percolated up or down. 
			doesPercolateUp = (compare(child, parent) < 0) ? true : false;

			if(!doesPercolateUp) {

				percolateDown(index);
				return getIndex(x) != index;
			}

			while((cmpIndex > 0) && (compare(child, parent) < 0)) {

				//Swaps the child and parent, in the case where child has a lower value than parent.
				T temp = array[index];
				array[index] = array[cmpIndex];
				array[cmpIndex] = temp;

				//Updates the new child and parent, along with their corresponding indices.
				indexMap.put(child, cmpIndex);
				indexMap.put(parent, index);
				++numUpdates;

				index = cmpIndex;
				cmpIndex = index / 2;

				child = array[index];
				parent = array[cmpIndex];
			}
		}

		return (numUpdates > 0) ? true : false;
	}
	
	/**
	 * Construct an empty PriorityQueue.
	 */
	@SuppressWarnings("unchecked")
	public WeissPriorityQueue( )
	{
		currentSize = 0;
		cmp = null;
		array = (T[]) new Object[ DEFAULT_CAPACITY + 1 ];
		indexMap = new HashMap<>();
	}
	
	/**
	 * One-argument constructor to create an empty PriorityQueue with a specified comparator.
	 * @param c User-defined comparator used to check equality between two Objects.
	 */
	@SuppressWarnings("unchecked")
	public WeissPriorityQueue( Comparator<? super T> c )
	{
		currentSize = 0;
		cmp = c;
		array = (T[]) new Object[ DEFAULT_CAPACITY + 1 ];
		indexMap = new HashMap<>();
	}
	
	 
	/**
	 * One-argument constructor to create a PriorityQueue from another Collection.
	 * @param coll Another WeissCollection instance to be based upon, in regards to the current heap.
	 */
	@SuppressWarnings("unchecked")
	public WeissPriorityQueue( WeissCollection<? extends T> coll )
	{
		cmp = null;
		currentSize = coll.size( );
		array = (T[]) new Object[ ( currentSize + 2 ) * 11 / 10 ];
		
		int i = 1;
		for( T item : coll )
			array[ i++ ] = item;
		buildHeap( );
		indexMap = new HashMap<>();
	}
	
	/**
	 * Compares lhs and rhs using comparator.
	 * @param lhs The first generic object to be checked in a comparison.
	 * @param rhs The second generic object to be checked in a comparison.
	 * @return Returns greater than 0 if lhs is greater than rhs, less than 0 if lhs is less than rhs. 0 otherwise.
	 */
	@SuppressWarnings("unchecked")
	private int compare( T lhs, T rhs )
	{
		if( cmp == null )
			return ((Comparable)lhs).compareTo( rhs );
		else
			return cmp.compare( lhs, rhs );	
	}
	
	/**
	 * Adds an item to this PriorityQueue.
	 * @param x any object.
	 * @return true.
	 */
	   
	public boolean add( T x )				
	{
		if( currentSize + 1 == array.length )
			doubleArray( );

		// Percolate up
		int hole = ++currentSize;
		array[ 0 ] = x;
		
		for( ; compare( x, array[ hole / 2 ] ) < 0; hole /= 2 ) {

			array[ hole ] = array[ hole / 2 ];
			indexMap.put(array[hole], hole);
		}
		
		array[ hole ] = x;
		indexMap.put(x, hole);
		
		return true;
	}
	
	/**
	 * Returns the number of items in this PriorityQueue.
	 * @return the number of items in this PriorityQueue.
	 */
	public int size( )
	{
		return currentSize;
	}
	
	/**
	 * Make this PriorityQueue empty.
	 */
	public void clear( )
	{
		currentSize = 0;
		indexMap.clear();
	}
	
	/**
	 * Returns an iterator over the elements in this PriorityQueue.
	 * @return Returns an iterator over the elements in this PriorityQueue.
	 */
	public Iterator<T> iterator( )
	{
		return new Iterator<T>( )
		{
			int current = 0;
			
			public boolean hasNext( )
			{
				return current != size( );
			}
			
			@SuppressWarnings("unchecked")
			public T next( )
			{
				if( hasNext( ) )
					return array[ ++current ];
				else
					throw new NoSuchElementException( );
			}
			
			public void remove( )
			{
				throw new UnsupportedOperationException( );
			}
		};
	}
	 
	/**
	 * Returns the smallest item in the priority queue.
	 * @return the smallest item.
	 * @throws NoSuchElementException if empty.
	 */
	public T element( )
	{
		if( isEmpty( ) )
			throw new NoSuchElementException( );
		return array[ 1 ];
	}
	
	/**
	 * Removes the smallest item in the priority queue.
	 * @return the smallest item.
	 * @throws NoSuchElementException if empty.
	 */
	public T remove( )
	{
		T minItem = element( );
		indexMap.remove(minItem);
		array[ 1 ] = array[ currentSize-- ];

		percolateDown( 1 );

		return minItem;
	}


	/**
	 * Establish heap order property from an arbitrary
	 * arrangement of items. Runs in linear time.
	 */
	private void buildHeap( )
	{
		for( int i = currentSize / 2; i > 0; i-- )
			percolateDown( i );
	}

	/**
	 * Default capacity.
	 */
	private static final int DEFAULT_CAPACITY = 100;

	/**
	 * Current size of the heap.
	 */
	private int currentSize;   // Number of elements in heap
	/**
	 * The heap represented by an array.
	 */
	protected T [ ] array; // The heap array
	/**
	 * A comparator used to compare two generic objects.
	 */
	private Comparator<? super T> cmp;

	/**
	 * Internal method to percolate down in the heap.
	 * @param hole the index at which the percolate begins.
	 */
	private void percolateDown( int hole )
	{
		int child;
		T tmp = array[ hole ];

		for( ; hole * 2 <= currentSize; hole = child )
		{
			child = hole * 2;
			if( child != currentSize &&
					compare( array[ child + 1 ], array[ child ] ) < 0 )
				child++;
			if( compare( array[ child ], tmp ) < 0 ) {

				array[ hole ] = array[ child ];
				indexMap.put(array[hole], hole);
			}
			else
				break;
		}

		array[ hole ] = tmp;
		indexMap.put(array[hole], hole);
	}
	
	/**
	 * Internal method to extend array.
	 */
	@SuppressWarnings("unchecked")
	private void doubleArray( )
	{
		T [ ] newArray;

		newArray = (T []) new Object[ array.length * 2 ];
		for( int i = 0; i < array.length; i++ )
			newArray[ i ] = array[ i ];
		array = newArray;
	}
}

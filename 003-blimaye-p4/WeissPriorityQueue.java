//TODO:
//  (1) Update this code to meet the style and JavaDoc requirements.
//			Why? So that you get experience with the code for a heap!
//			Also, this happens a lot in industry (updating old code
//			to meet your new standards). We've done this for you in
//			WeissCollection and WeissAbstractCollection.
//  (2) Implement getIndex() method and the related map integration
//			 -- see project description
//  (3) Implement update() method -- see project description

import java.util.Iterator;
import java.util.Comparator;
import java.util.NoSuchElementException;
import java.util.Arrays;

//You may uncomment this to use it, or use your HashTable class
//from Project 3.
import java.util.HashMap;

/**
 * PriorityQueue class implemented via the binary heap.
 * From your textbook (Weiss)
 */
public class WeissPriorityQueue<AnyType> extends WeissAbstractCollection<AnyType>
{
	//--------------------------------------------------------
	// testing code goes here... edit this as much as you want!
	//--------------------------------------------------------
	
	public static void main(String[] args) {
		class Student {
			String gnum;
			String name;
			Student(String gnum, String name) { this.gnum = gnum; this.name = name; }
			public boolean equals(Object o) {
				if(o instanceof Student) return this.gnum.equals(((Student)o).gnum);
				return false;
			}
			public String toString() { return name + "(" + gnum + ")"; }
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
	
	//Uncomment one of these lines depending on which hash table implementation you are using
	protected HashMap<AnyType, Integer> indexMap; //if you're using the JCF hash table
	//private HashTable<AnyType, Integer> indexMap; //if you're using project 3's hash table
	
	//you implement this
	public int getIndex(AnyType x) {
		//average case O(1)

		if(x == null) {
			return -1;
		}
		
		Integer index = indexMap.get(x);

		return (index != null) ? index : -1;
		//returns the index of the item in the heap,
		//or -1 if it isn't in the heap		
	}
	
	//you implement this
	public boolean update(AnyType x) {
		//O(lg n) average case
		//or O(lg n) worst case if getIndex() is guarenteed O(1)

		if(x == null) {
			return false;
		}

		boolean doesPercolateUp = true;
		int index, cmpIndex;
		int numUpdates = 0;

		if((index = getIndex(x)) != -1) {

			indexMap.put(x, index);
			array[index] = x;

			cmpIndex = index / 2;
			AnyType child = array[index];
			AnyType parent = array[cmpIndex];

			doesPercolateUp = (cmp.compare(child, parent) < 0) ? true : false;

			if(!doesPercolateUp) {

				percolateDown(index);
				return getIndex(x) != index;
			}


			while((cmpIndex > 0) && (cmp.compare(child, parent) < 0)) {

				AnyType temp = array[index];
				array[index] = array[cmpIndex];
				array[cmpIndex] = temp;

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
		array = (AnyType[]) new Object[ DEFAULT_CAPACITY + 1 ];
		indexMap = new HashMap<>();
	}
	
	/**
	 * Construct an empty PriorityQueue with a specified comparator.
	 */
	@SuppressWarnings("unchecked")
	public WeissPriorityQueue( Comparator<? super AnyType> c )
	{
		currentSize = 0;
		cmp = c;
		array = (AnyType[]) new Object[ DEFAULT_CAPACITY + 1 ];
		indexMap = new HashMap<>();
	}
	
	 
	/**
	 * Construct a PriorityQueue from another Collection.
	 */
	@SuppressWarnings("unchecked")
	public WeissPriorityQueue( WeissCollection<? extends AnyType> coll )
	{
		cmp = null;
		currentSize = coll.size( );
		array = (AnyType[]) new Object[ ( currentSize + 2 ) * 11 / 10 ];
		
		int i = 1;
		for( AnyType item : coll )
			array[ i++ ] = item;
		buildHeap( );
		indexMap = new HashMap<>();
	}
	
	/**
	 * Compares lhs and rhs using comparator if
	 * provided by cmp, or the default comparator.
	 */
	@SuppressWarnings("unchecked")
	private int compare( AnyType lhs, AnyType rhs )
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
	   
	public boolean add( AnyType x )				
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
	 * The iterator does not view the elements in any particular order.
	 */
	public Iterator<AnyType> iterator( )
	{
		return new Iterator<AnyType>( )
		{
			int current = 0;
			
			public boolean hasNext( )
			{
				return current != size( );
			}
			
			@SuppressWarnings("unchecked")
			public AnyType next( )
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
	public AnyType element( )
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
	public AnyType remove( )
	{
		AnyType minItem = element( );
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

	private static final int DEFAULT_CAPACITY = 100;

	private int currentSize;   // Number of elements in heap
	protected AnyType [ ] array; // The heap array
	private Comparator<? super AnyType> cmp;

	/**
	 * Internal method to percolate down in the heap.
	 * @param hole the index at which the percolate begins.
	 */
	private void percolateDown( int hole )
	{
		int child;
		AnyType tmp = array[ hole ];

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
		AnyType [ ] newArray;

		newArray = (AnyType []) new Object[ array.length * 2 ];
		for( int i = 0; i < array.length; i++ )
			newArray[ i ] = array[ i ];
		array = newArray;
	}
}

import java.util.NoSuchElementException;
import java.util.Iterator;
import java.io.Serializable;

/**
 * Class represents a simple HashTable implementation.
 * @param <K> The key of the HashTable.
 * @param <V> The value of the HashTable.
 * @author Brian Limaye
 */
public class HashTable<K, V> implements Serializable{

	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------

	/**
	 *Class represents a TableEntry, to be placed as Objects into the HashTable.
	 * @param <K> The key of the TableEntry.
	 * @param <V> The value of the TableEntry.
	 * @author Yutao Zhong
	 */
	private class TableEntry<K,V> implements Serializable{
		/**
		 * The key.
		 */
		private K key;
		/**
		 * The value.
		 */
		private V value;
	
		/**
		 * Two argument constructor to initialize a new TableEntry instance with a key, value pair.
		 * @param key The key to be set.
		 * @param value The value to be set.
		 */
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
	
		/**
		 * Gets the key from the current TableEntry instance.
		 * @return Returns the key.
		 */
		public K getKey() {
			return key;
		}
	
		/**
		 * Gets the value from the current TableEntry instance.
		 * @return Returns the value.
		 */
		public V getValue() {
			return value;
		}
	
		/**
		 * Gets a human interpreted representation of the current TableEntry instance.
		 * @return Returns the human interpreted representation of the current TableEntry instance.
		 */
		public String toString() {
			return key.toString()+":"+value.toString();
		}
	}

	/**
	 * Represents the storage used to store the HashMap of TableEntries.
	 */
	private TableEntry<K,V>[] storage;

	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

	/**
	 * The current size of occupied entries.
	 */
	private int currentSize;

	/**
	 * The current maximum capacity of the HashTable.
	 */
	private int capacity;

	/**
	 * An array of states, resembling which indices hold a tombstone within the HashMap.
	 */
	private boolean[] tombStates;

	/**
	 * Helper method to resize the array of tombstone states when rehashing.
	 * @param size The new rehashed size for the tombstone states to be resized to.
	 */
	private void resizeTombStates(int size) {

		boolean[] newTombStates = new boolean[size];
		this.tombStates = newTombStates;
	}

	/**
	 * One argument constructor to initialize a new HashTable instance given an initial size.
	 * @param size The initial size of the HashTable.
	 */
	@SuppressWarnings("unchecked")
	public HashTable(int size) {
		//Create a hash table where the initial storage
		//is size and K keys can be mapped to V values.
		//You may assume size is >= 2
		storage = new TableEntry[size];
		this.capacity = size;
		this.currentSize = 0;
		this.tombStates = new boolean[capacity];
	}
	
	/**
	 * Gets the current capacity of the HashTable.
	 * @return Returns the current capacity of the HashTable.
	 */
	public int getCapacity() {
		return capacity;
	}
	
	/**
	 * Gets the current size of the HashTable.
	 * @return Returns the current size of the HashTable.
	 */
	public int size() {
		return currentSize; //default return: change or remove as needed
	}
	
	/**
	 * Puts/updates a TableEntry in the HashTable given a key and value.
	 * @param key The key of the TableEntry.
	 * @param val The value of the TableEntry.
	 * @return Returns true if successfully added. False otherwise.
	 */ 
	public boolean put(K key, V val) {

		//Check for an invalid key/val input.
		if((key == null) || (val == null)) {
			return false;
		}

		boolean result = false;
		TableEntry<K, V> entry = new TableEntry<>(key, val);
		int startProbe = Math.abs(key.hashCode()) % capacity;

		//Starts at the calculated probe index. Probes linearly in the case the TableEntry was shifted to the right.
		for(int i = startProbe; i < startProbe + capacity; i++) {

			//Calculates the current probe, or index in the HashTable for inspection.
			int currentProbe = i % capacity;

			//Once a matching key in the HashTable is found or an empty position.
			if((storage[currentProbe] == null) || (storage[currentProbe].getKey().equals(key))) {

				if(storage[currentProbe] == null) { 
					currentSize++; 
				}
				storage[currentProbe] = entry;
				result = true;

				if(tombStates[currentProbe]) {
					tombStates[currentProbe] = false;
				}
				break;
			}
		}

		//Rehashes/resizes the HashTable when 80% capacity is reached/exceeded.
		if(Double.compare(currentSize / ((double) capacity), 0.8) >= 0){
			rehash(capacity * 2);		
		}

		return result;
	}

	/**
	 * Gets the corresponding value from the HashTable given the key, if possible.
	 * @param key The key of the HashTable used for lookup.
	 * @return Returns the value corresponding with the key lookup.
	 */
	public V get(K key) {
		
		//Check for an invalid key.
		if(key == null) {
			return null;
		}

		int startProbe = Math.abs(key.hashCode()) % capacity;

		//Starts at the calculated start probe. Probes linearly in the case the TableEntry was shifted to the right.
		for(int i = startProbe; i < startProbe + capacity; i++) {

			//Calculates the current probe, or index in the HashTable for inspection.
			int currentProbe = i % capacity;

			TableEntry<K, V> currEntry = storage[currentProbe];

			//Occurs when the HashTable doesn't contain the given key.
			if(currEntry == null) {
				break;
			}

			//Once a matching key in the HashTable is found.
			if(currEntry.getKey().equals(key)) {
				return currEntry.getValue();
			}
		}

		return null;

	}
	
	/**
	 * Determines whether a given index holds a tombstone state.
	 * @param loc The index to be inspected.
	 * @return Returns true if index has a tombstone. False otherwise.
	 */
	public boolean isTombstone(int loc) {

		//Check for an invalid index.
		if((loc < 0) || (loc >= capacity)) {
			return false;
		}

		return tombStates[loc];
	}

	/**
	 * Attempts to rehash the HashTable when prompted to do so.
	 * @param size The new size to be rehashed to.
	 * @return Returns true if the rehashing was successful. False otherwise.
	 */
	@SuppressWarnings("unchecked")
	public boolean rehash(int size) {
	
		//Rehash fails if the new size cannot fit all items.
		if(size < currentSize) {
			return false;
		}

		int oldCapacity = capacity;

		TableEntry<K, V>[] oldTable = storage;
		TableEntry<K, V>[] newTable = new TableEntry[size];
		this.storage = newTable;
		this.capacity = size;
		this.currentSize = 0;
		resizeTombStates(size);

		//Copies all old items into the newly created HashTable.
		for(int i = 0; i < oldCapacity; i++) {

			if(oldTable[i] != null) {
				put(oldTable[i].getKey(), oldTable[i].getValue());
			}
		}
	
		return true;
	}
	
	/**
	 * Attempts to remove a TableEntry from the HashTable given a key.
	 * @param key The key used for lookup and removal.
	 * @return Returns the value associated with the key that had just been removed.
	 */
	public V remove(K key) {

		//Check for an invalid key.
		if(key == null) {
			return null;
		}

		V removedVal = null;
		int startProbe = Math.abs(key.hashCode()) % capacity;

		//Starts at the calculated start probe. Probes linearly in the case the TableEntry was shifted to the right.
		for(int i = startProbe; i < startProbe + capacity; i++) {

			//Calculates the current probe, or index in the HashTable for inspection.
			int currentProbe = i % capacity;

			TableEntry<K, V> currEntry = storage[currentProbe];

			//If a tombstone is reached, it's skipped.
			if(tombStates[currentProbe]) {
				continue;
			}

			//Indicator that the key is not found in the HashTable.
			if(currEntry == null) {
				break;
			}

			//Once a matching key is found in the HashTable.
			if(currEntry.getKey().equals(key)) {
				
				removedVal = currEntry.getValue();
				storage[currentProbe] = null;
				tombStates[currentProbe] = true;
				--currentSize;
				break;
			}
		}

		return removedVal;
	}

	//-------------------------------------------------------------
	// PROVIDED METHODS BELOW 
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	
	/**
	 * Gets the human interpreted representation of the current HashTable instance.
	 * @return Returns the human interpretted representation of the current HashTable instance.
	 */
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null && !isTombstone(i)) {
				s.append(storage[i] + "\n");
			}
		}
		return s.toString().trim();
	}
	
	/**
	 * Gets the human interpreted representation of the entire HashTable instance, including tombstones and nulls.
	 * @return Returns the human interpreted representation of the entire HashTable instance, including tombstones and nulls.
	 */
	public String toStringDebug() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			if(!isTombstone(i)) {
				s.append("[" + i + "]: " + storage[i] + "\n");
			}
			else {
				s.append("[" + i + "]: tombstone\n");
			}
			
		}
		return s.toString().trim();
	}
	
	//-------------------------------------------------------------
	// END OF PROVIDED METHODS SECTION 
	//-------------------------------------------------------------

	//-------------------------------------------------------------
	// TESTING CODE   
	//-------------------------------------------------------------
	
	/**
	 * Main method used primarily to test the functionality of the HashTable implementation.
	 * @param args Command-line arguments, primarily used to dynamically test functionality at run-time.
	 */
	public static void main(String[] args) {
		//main method for testing, edit as much as you want
		HashTable<String,Integer> ht1 = new HashTable<>(10);
		HashTable<Integer,Character> ht2 = new HashTable<>(5);
		
		//initialize
		if(ht1.getCapacity() == 10 && ht2.getCapacity() == 5 && ht1.size() == 0 && ht2.size() == 0) {
			System.out.println("Yay 1");
		}
		
		//put
		ht1.put("a",1); //"a".hashCode = 97
		ht1.put("b",1); //"b".hashCode = 98
		ht1.put("b",2); //update
		ht1.put("b",3);
		
		//System.out.println(ht1);
		//System.out.println(ht1.toStringDebug());
		
		if(ht1.toString().equals("a:1\nb:3") 
			&& ht1.toStringDebug().equals("[0]: null\n[1]: null\n[2]: null\n[3]: null\n[4]: null\n[5]: null\n[6]: null\n[7]: a:1\n[8]: b:3\n[9]: null")) {
			System.out.println("Yay 2");
		}		
		
		if(!ht1.put(null,0) && ht1.getCapacity() == 10 && ht1.size() == 2 && ht1.get("a")==1 && ht1.get("b")==3) {
			System.out.println("Yay 3");
		}
		
		//put with collision
		ht2.put(12,'A');
		ht2.put(22,'B');
		ht2.put(37,'C');
		//System.out.println(ht2.toStringDebug());
		ht2.put(47,'D');

		//System.out.println(ht2.toStringDebug());
		
		if(ht2.getCapacity() == 10 && ht2.size() == 4 && ht2.get(1)==null
			&& ht2.get(12)=='A' && ht2.get(22)=='B' && ht2.get(37)=='C'
			&& ht2.get(47)=='D') {
			System.out.println("Yay 4");
		}

		if(ht2.toString().equals("12:A\n22:B\n47:D\n37:C") && 
			ht2.toStringDebug().equals("[0]: null\n[1]: null\n[2]: 12:A\n[3]: 22:B\n[4]: null\n[5]: null\n[6]: null\n[7]: 47:D\n[8]: 37:C\n[9]: null")) {
			System.out.println("Yay 5");
		}
		
		//remove
		HashTable<String,Integer> ht3 = new HashTable<>(2);
		ht3.put("apple",1);  //hashCode: 93029210

		if(ht3.remove("apple") == 1 && ht3.remove("banana") == null && ht3.toString().equals("")
			&& ht3.toStringDebug().equals("[0]: tombstone\n[1]: null")) {
			ht3.put("B",1);
			System.out.println(ht3.size());
			if(ht3.toString().equals("B:1") && ht3.toStringDebug().equals("[0]: B:1\n[1]: null")) {
				System.out.println("Yay 6");
			}
		}
		
		//rehash
		if(!ht2.rehash(2) && ht2.size() == 4 && ht2.getCapacity() == 10) {
			System.out.println("Yay 7");
		}
		
		if(ht2.rehash(7) && ht2.size() == 4 && ht2.getCapacity() == 7) {
			System.out.println("Yay 8");
		}
		
		if(ht2.toString().equals("22:B\n37:C\n12:A\n47:D") 
			&& ht2.toStringDebug().equals("[0]: null\n[1]: 22:B\n[2]: 37:C\n[3]: null\n[4]: null\n[5]: 12:A\n[6]: 47:D")) {
			System.out.println("Yay 9");
		}

		HashTable<Integer, String> ht4 = new HashTable<>(2);

        ht4.put(45, "Pittman");
        ht4.put(34, "Pierce");
        ht4.put(37, "Artest");

        //assertEquals(3, ht4.size());

        //assertEquals("Artest", ht4.get(37));

        //assertEquals("[0]: null\n[1]: 45:Pittman\n[2]: 34:Pierce\n[3]: 37:Artest", ht4.toStringDebug());

        //assertEquals(null, ht4.remove(38));
        //System.out.println(ht4.remove(34));
        System.out.println(ht4.remove(37));
	}
}
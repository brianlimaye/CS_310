import java.util.NoSuchElementException;
import java.util.Iterator;
import java.io.Serializable;

public class HashTable<K, V> implements Serializable{

	//-------------------------------------------------------------
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------

	private class TableEntry<K,V> implements Serializable{
		private K key;
		private V value;
	
		public TableEntry(K key, V value) {
			this.key = key;
			this.value = value;
		}
	
		public K getKey() {
			return key;
		}
	
		public V getValue() {
			return value;
		}
	
		public String toString() {
			return key.toString()+":"+value.toString();
		}
	}

	private TableEntry<K,V>[] storage;

	//-------------------------------------------------------------
	// END OF PROVIDED "DO NOT EDIT" SECTION 
	//-------------------------------------------------------------

	// ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

	private int currentSize;
	private int capacity;
	private boolean[] tombStates;

	private void resizeTombStates(int size) {

		boolean[] newTombStates = new boolean[size];
		this.tombStates = newTombStates;
	}

	
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
	
	public int getCapacity() {
		//return how big the storage is
		//O(1)

		return capacity;
	}
	
	public int size() {
		//return the number of elements in the table
		//O(1)

		return currentSize; //default return: change or remove as needed
	}
	
	public boolean put(K key, V val) {

		//Place value val at the location of key.
		//Use linear probing if that location is in use.
		
		//Return false w/o updating the table if 
		//either key or val is null; otherwise return true. 
		
		//Hint: Make a TableEntry to store in storage
		//and use the absolute value of key.hashCode() for
		//the probe start. You may also need to ensure 
		//a valid index that is within bound.
		
		//If the key already exists in the table
		//replace the current value with val.
		
		//If key isn't in the table, add in the key,val. 
		//If the table is >= 80% full, rehash to ensure the 
		//table is expanded to twice the size.
		
		//Worst case: O(n), Average case: O(1)

		if((key == null) || (val == null)) {
			return false;
		}

		boolean result = false;
		TableEntry<K, V> entry = new TableEntry<>(key, val);
		int startProbe = Math.abs(key.hashCode()) % capacity;

		for(int i = startProbe; i < startProbe + capacity; i++) {

			int currentProbe = i % capacity;

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

		if(Double.compare(currentSize / ((double) capacity), 0.8) >= 0){
			rehash(capacity * 2);		
		}

		return result;
	}

	public V get(K key) {
		//Given a key, return the value from the table.
		
		//If the key is not in the table, return null.
		
		//Worst case: O(n), Average case: O(1)

		if(key == null) {
			return null;
		}

		int startProbe = Math.abs(key.hashCode()) % capacity;

		for(int i = startProbe; i < startProbe + capacity; i++) {

			int currentProbe = i % capacity;

			TableEntry<K, V> currEntry = storage[currentProbe];

			if(currEntry == null) {
				break;
			}

			if(currEntry.getKey().equals(key)) {
				return currEntry.getValue();
			}
		}

		return null; //default return: change or remove as needed

	}
	
	public boolean isTombstone(int loc) {
		//this is a helper method needed for printing
		
		//return whether or not there is a tombstone at the
		//given index
		
		//O(1)

		if((loc < 0) || (loc >= capacity)) {
			return false;
		}

		return tombStates[loc]; //default return: change or remove as needed
	}


	@SuppressWarnings("unchecked")
	public boolean rehash(int size) {
		//Increase or decrease the size of the storage,
		//rehashing all values.
		
		//If the new size won't fit all the elements,
		//return false and do not rehash. Return true
		//if you were able to rehash.

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

		for(int i = 0; i < oldCapacity; i++) {

			if(oldTable[i] != null) {
				put(oldTable[i].getKey(), oldTable[i].getValue());
			}
		}
	
		return true; //default return: change or remove as needed
	}
	

		
	public V remove(K key) {
		//Remove the given key (and associated value)
		//from the table. Return the value removed.		
		//If the key is not in the table, return null.
		
		//Hint 1: Remember to leave a tombstone!
		//Hint 2: Does it matter what a tombstone is?
		//   Yes and no... You need to be able to tell
		//   the difference between an empty spot and
		//   a tombstone and you also need to be able
		//   to tell the difference between a "real"
		//   element and a tombstone.
		
		//Worst case: O(n), Average case: O(1)

		if(key == null) {
			return null;
		}

		V removedVal = null;
		int startProbe = Math.abs(key.hashCode()) % capacity;

		for(int i = startProbe; i < startProbe + capacity; i++) {

			int currentProbe = i % capacity;

			TableEntry<K, V> currEntry = storage[currentProbe];

			if(tombStates[currentProbe]) {
				continue;
			}

			if(currEntry == null) {
				break;
			}

			if(currEntry.getKey().equals(key)) {
				
				removedVal = currEntry.getValue();
				storage[currentProbe] = null;
				tombStates[currentProbe] = true;
				--currentSize;
				break;
			}
		}

		return removedVal; //default return: change or remove as needed

	}

	//-------------------------------------------------------------
	// PROVIDED METHODS BELOW 
	// DO NOT EDIT ANYTHING FOR THIS SECTION EXCEPT TO ADD JAVADOCS
	//-------------------------------------------------------------
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(int i = 0; i < storage.length; i++) {
			if(storage[i] != null && !isTombstone(i)) {
				s.append(storage[i] + "\n");
			}
		}
		return s.toString().trim();
	}
	
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
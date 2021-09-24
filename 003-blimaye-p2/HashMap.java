// TO DO: add your implementation and JavaDocs.

// ONLY IMPLEMENT THE TWO METHODS: put() and get()
// ALL ELSE KEEP AS IS


public class HashMap<K, V> 
{
	
	// This HashMap implementation uses a LList<T> composed of Node<T>.
	// Since two generic parameters <K, V> are needed instead of one, 
	// the Pair class below is provided to be used as follows: Node<Pair> and LList<Pair>
	class Pair<K,V> {
		private K key;
		private V value;
		public Pair(K key, V value){
			this.key = key;
			this.value = value;
		}
		public K getKey(){ return key; }
		public V getValue(){ return value; }
		public void setKey(K key){ this.key = key; }
		public void setValue(V value){ this.value = value; }
		@Override public int hashCode() {  
			return key.hashCode(); 
		}
		@Override public boolean equals(Object obj) {  
			if (obj == null) return false;
			if (!(obj instanceof Pair)) return false;
			Pair pair = (Pair)obj;
			return pair.key.equals(key); 
		}
	}


    // array of LLists where each list will be composed of Node<Pair>
    private LList<Pair>[] buckets;

    // will fix the capacity to 20
    final static private int DEFAULT_CAPACITY = 20;

    // track how many elements in HashMap
    private int size = 0;
	
    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
    	buckets = (LList<Pair>[])new LList[capacity];

    	for(int i = 0; i < capacity; i++) {
    		buckets[i] = new LList<Pair>();
    	}
    }

    public int size() {
        return size;
    }

    private int getCapacity() {
        return buckets.length;
    }

    private int getHash(K key) {
        return key == null ? 0 : Math.abs(key.hashCode());
    }

    @Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (LList<Pair> list : buckets) {	
			sb.append("[");	
			if (list != null) {
				sb.append(list.listToString());
			}
			sb.append(", ");	  
			sb.append("]");
		}
		return "{" + sb.toString() + "}";
	}

	
	//To-Do: Possibly fix Big O...
	// Hint: This function involves LList<Pair> and Node<Pair>
	// Cost: O(1) on average, and O(n) worst case
	// 
	// if element already exists (the keys matched), override the old value with the new value
    @SuppressWarnings("unchecked")
    public void put(K key, V value) {

    	if((key == null) || (value == null)) {
    		return;
    	}

    	int hash = getHash(key);
    	int index = hash % buckets.length;
    	Node<Pair> curr = buckets[index].getFirst();

    	while(curr != null) {

    		if(getHash((K) curr.getValue().getKey()) == hash) {

    			curr.getValue().setValue(value);
    			return;
    		}

    		curr = curr.getNext();
    	}

    	++size;
    	buckets[index].insertLast(new Node<Pair>(new Pair<>(key, value)));	
    }

	//To-Do: Possibly fix Big O...
	// Hint: This function involves LList<Pair> and Node<Pair>
	// Cost: O(1) on average, and O(n) worst case
	//
	// if element was not found return null
	@SuppressWarnings("unchecked")
	public V get(K key) {

		if(key == null) {
			return null;
		}

		int hash = getHash(key);
		int index = hash % buckets.length;
		Node<Pair> curr = buckets[index].getFirst();

		while(curr != null) {
			
			if(getHash((K) curr.getValue().getKey()) == hash) {
				break;
			}

			curr = curr.getNext();
		}

		if(curr != null) {
			return (V) curr.getValue().getValue();
		}

		return null;
	}

	public static void main(String args[]) {
		HashMap<Integer, String> map = new HashMap<>();
		for (int i = 0; i < 10000; i++) {
			map.put(i, "Val" + i);
		}

		if (map.size() == 10000) {
			System.out.println("Yay1");
		}
		
		
		if (map.get(5).equals("Val5") && map.get(500).equals("Val500") &&
			map.get(5000).equals("Val5000") && map.get(9999).equals("Val9999")) {
			System.out.println("Yay2");
		} 
		
		map.put(0, "Val" + 0);
		if (map.size() == 10000) {
			System.out.println("Yay3");
		}
	}

}
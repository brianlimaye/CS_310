/**
 * Class representing a basic HashMap functionality.
 *
 * @param <K> The key of the pair.
 * @param <V> The value of the pair.
 * @author Brian Limaye
 */
public class HashMap<K, V> {

    // This HashMap implementation uses a LList<T> composed of Node<T>.
    // Since two generic parameters <K, V> are needed instead of one,
    // the Pair class below is provided to be used as follows: Node<Pair> and LList<Pair>

    /**
     * Class representing a Pair of a key and value.
     *
     * @param <K> The key.
     * @param <V> The value.
     */
    class Pair<K, V> {
        /**
         * The key.
         */
        private K key;
        /**
         * The value.
         */
        private V value;

        /**
         * One-arg constructor to initialize a Pair instance.
         *
         * @param key   The key to be set.
         * @param value The value to be set.
         */
        public Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * Gets the key.
         *
         * @return Returns the key.
         */
        public K getKey() {
            return key;
        }

        /**
         * Gets the value.
         *
         * @return Returns the value.
         */
        public V getValue() {
            return value;
        }

        /**
         * Sets the key to a new key.
         *
         * @param key The new key to be set.
         */
        public void setKey(K key) {
            this.key = key;
        }

        /**
         * Sets the value to a new value.
         *
         * @param value The new value to be set.
         */
        public void setValue(V value) {
            this.value = value;
        }

        /**
         * Computes the hashcode based on the key of the Pair.
         *
         * @return Returns the computed hashcode.
         */
        @Override
        public int hashCode() {
            return key.hashCode();
        }

        /**
         * Determines whether the current Pair and the other object are equal, if both are Pairs.
         *
         * @param obj The object being compared with the current Pair.
         * @return Returns true if they are equal, false otherwise.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj == null) return false;
            if (!(obj instanceof Pair)) return false;
            Pair pair = (Pair) obj;
            return pair.key.equals(key);
        }
    }


    /**
     * Array of LLists.
     */
    private LList<Pair>[] buckets;

    /**
     * The default capacity for the array of Linked Lists (buckets).
     */
    final static private int DEFAULT_CAPACITY = 20;

    /**
     * Tracks how many elements in HashMap.
     */
    private int size = 0;

    /**
     * Default constructor for creating a HashMap instance.
     */
    public HashMap() {
        this(DEFAULT_CAPACITY);
    }

    /**
     * One-argument constructor for creating a HashMap instance.
     *
     * @param capacity The maximum capacity for the array of Linked Lists (buckets).
     */
    @SuppressWarnings("unchecked")
    public HashMap(int capacity) {
        buckets = (LList<Pair>[]) new LList[capacity];

        for (int i = 0; i < capacity; i++) {
            buckets[i] = new LList<Pair>();
        }
    }

    /**
     * Gets the number of "registered" or stored pairs in the HashMap.
     *
     * @return Returns the number of stored pairs in the HashMap.
     */
    public int size() {
        return size;
    }

    /**
     * Gets the maximum capacity for the array of Linked Lists (buckets).
     *
     * @return Returns the capacity for the array of buckets.
     */
    private int getCapacity() {
        return buckets.length;
    }

    /**
     * Computes the hashcode of a particular key.
     *
     * @param key A generic key used for hashing.
     * @return Returns the computed hashcode for the key.
     */
    private int getHash(K key) {
        if (key == null)
        {
            return 0;
        }
        long l = key.hashCode();
        l = Math.abs(l);
        return (int) l;
    }

    /**
     * Returns the human interpreted representation of the current HashMap instance.
     *
     * @return Returns the human interpreted representation of the HashMap.
     */
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


    /**
     * Puts the new (key, value) pair into the HashMap of keys and values.
     *
     * @param key   The generic key associated with the pair.
     * @param value The generic value associated with the pair.
     */
    @SuppressWarnings("unchecked")
    public void put(K key, V value) {

        //Validation for a null key/value.
        if ((key == null) || (value == null)) {
            return;
        }


        int hash = getHash(key);                //Computes the hashcode based on the key.
        int index = hash % buckets.length;        //Computes the correct bucket, in which the pair should be stored.
        Node<Pair> curr = buckets[index].getFirst();

        while (curr != null) {

            //Compares each stored element and their respective keys for a match.
            if (getHash((K) curr.getValue().getKey()) == hash) {

                curr.getValue().setValue(value);
                return;
            }

            curr = curr.getNext();
        }

        //Case where appending is necessary to the bucket.
        ++size;
        buckets[index].insertLast(new Node<Pair>(new Pair<>(key, value)));
    }

    /**
     * Gets an existing (key, value) pair from the HashMap of keys and values, if possible.
     *
     * @param key The generic key used for lookup.
     * @return Returns the generic value associated with the key, if found.
     */
    @SuppressWarnings("unchecked")
    public V get(K key) {

        //Initial check for a null key.
        if (key == null) {
            return null;
        }

        int hash = getHash(key);            //Computes the hashcode based on the key.
        int index = hash % buckets.length;    //Computes the correct bucket, in which the pair should be found.
        Node<Pair> curr = buckets[index].getFirst();

        while (curr != null) {

            //Compares each stored element and their respective keys for a match.
            if (getHash((K) curr.getValue().getKey()) == hash) {
                break;
            }

            curr = curr.getNext();
        }

        //If the current Pair node contains the key, the corresponding value is returned. Otherwise, null is returned.
        if (curr != null) {
            return (V) curr.getValue().getValue();
        }

        return null;
    }

    /**
     * Main method primarily used for testing the functionality of the HashMap class.
     *
     * @param args Command-line arguments used solely for testing functionality at runtime.
     */
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
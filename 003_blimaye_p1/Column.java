/**
 * A class that represents a column of the PowerConnectFour game, where each column holds a certain Token of color.
 *
 * @param <T> A generic type representing the varying type of each Token.
 * @author Brian Limaye
 */
public class Column<T> {
    //default initial capacity / minimum capacity

    /**
     * The default capacity for a column.
     */
    private static final int DEFAULT_CAPACITY = 2;

    //underlying array for storage -- you MUST use this for credit!
    //Do NOT change the name or type

    /**
     * A collection of generic objects, used to resemble a column.
     */
    private T[] data;

    // ADD MORE PRIVATE MEMBERS HERE IF NEEDED!

    /**
     * The number of occupied cells of a given column.
     */
    private int currentSize;

    /**
     * The supposed maximum amount of cells that can be occupied by a given column.
     */
    private int capacity;

    /**
     * Resizes a column, if its capacity is reached.
     *
     * @param mult The multiplier of resizing, with any value greater than 1 growing the column. Any value less than 1 will shrink the column.
     */
    @SuppressWarnings("unchecked")
    private void resizeArray(double mult) {

        T[] prev = data;
        //New array, being either shrunk or augmented from the original.
        T[] newArr = (T[]) new Object[(int) (capacity * mult)];
        int len = (int) Math.min(capacity * mult, capacity);     //The new and old capacities and compared, in order to obtain the smallest for copying.

        //Elements from the old column are copied to the new.
        for (int i = 0; i < len; i++) {

            newArr[i] = prev[i];
        }

        capacity *= mult;
        this.data = newArr; //New column is set, following the resizing.
    }

    /**
     * Creates a new instance of a column, with an initial capacity of DEFAULT_CAPACITY.
     */
    @SuppressWarnings("unchecked")
    public Column() {

        //Creates a new empty storage array for the given column, with a default capacity of DEFAULT_CAPACITY.
        this.data = (T[]) new Object[DEFAULT_CAPACITY];
        this.currentSize = 0;
        this.capacity = DEFAULT_CAPACITY;
    }

    /**
     * Creates a new instance of a column, with an initial capacity of initialCapacity.
     *
     * @param initialCapacity The initial capacity of the current column.
     */
    @SuppressWarnings("unchecked")
    public Column(int initialCapacity) {

        //Check for an invalid initialCapcity.
        if (initialCapacity < 1) {
            throw new IllegalArgumentException("Capacity must be positive.");
        }

        //Creates a new empty storage array for the given column, with a default capacity of initialCapacity.
        this.data = (T[]) new Object[initialCapacity];
        this.currentSize = 0;
        this.capacity = initialCapacity;
    }

    /**
     * Gets the current size of a given column.
     *
     * @return Returns the number of occupied cells in a given column.
     */
    public int size() {
        return currentSize;
        // O(1)
    }

    /**
     * Gets the current capacity of a given column.
     *
     * @return Returns the maximum capacity, in which a given column can hold.
     */
    public int capacity() {
        return capacity;
        // O(1)
    }


    /**
     * Attempts to set a cell of a column at a given index to a new value.
     *
     * @param index The position of a column to be set, if possible.
     * @param value A generic value to replace the previous value, if possible.
     * @return Returns the previous value, following the modification, if possible.
     */
    public T set(int index, T value) {

        T oldValue;

        //Check for a null input value.
        if (value == null) {
            return null;
        }

        //Checks for an invalid index.
        if ((index < 0) || (index >= currentSize)) {
            throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
        }

        //Obtains the old value, prior to setting the new value.
        oldValue = data[index];
        data[index] = value;

        return oldValue;
        // O(1)
    }

    /**
     * Attempts to get an element at a specified index, if possible.
     *
     * @param index The index to be obtained, if in bounds.
     * @return The generic value at the specified index.
     */
    public T get(int index) {
        // Return the item at the given index

        if ((index < 0) || (index > capacity)) {
            throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
        }

        return data[index];
        // O(1)

        // Use the exception (and error message) described in set()
        // for invalid indicies.
    }

    /**
     * Attempts to append a value at the end of a given column.
     *
     * @param value A generic value to be appended at the end of a given column, if possible..
     */
    @SuppressWarnings("unchecked")
    public void add(T value) {
        //Check for a null value
        if (value == null) {
            return;
        }

        //Doubles the current storage array, if its capacity is reached.
        if (currentSize + 1 > capacity) {
            resizeArray(2);
        }

        //Appends the value to the column, while incrementing the currentSize.
        data[currentSize++] = value;
        // Amortized O(1)
    }

    /**
     * Attempts to insert a value into a given column, at a particular index.
     *
     * @param index The index of insertion in a given column.
     * @param value The generic value to be inserted, if possible.
     */
    @SuppressWarnings("unchecked")
    public void add(int index, T value) {
        boolean hasReached = false; //A flag to indicate once the index is reached.
        T prev = null;
        T curr = null;

        //A check for a null value.
        if (value == null) {
            return;
        }

        //A check for an invalid index input.
        if ((index < 0) || (index > currentSize)) {
            throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
        }

        //A check for a possible augmentation of the storage array, this occurs once the capacity is met.
        if (currentSize + 1 > capacity) {
            resizeArray(2);
        }

        for (int i = 0; i < currentSize; i++) {

            //Once the index is found.
            if (i == index) {
                hasReached = true;
                ++currentSize;
                prev = data[i];     //The previous value is saved, to be set at the next index.
                data[i] = value;     //The new value is set.
            }

            //Every other successive index.
            else if (hasReached) {
                curr = data[i];     //Saves the previous value, prior to shifting.
                data[i] = prev;         //Shifts the value from the previous index.
                prev = curr;
            }
        }

        //If the value needs to be appended, rather than inserted.
        if (!hasReached) {

            add(value);
        }
        // O(N) where N is the number of elements in the storage
    }


    /**
     * Attempts to delete a value from a given column, at a particular index.
     *
     * @param index The index of deletion from a given column.
     * @return Returns the deleted element, if successful.
     */
    @SuppressWarnings("unchecked")
    public T delete(int index) {
        boolean hasReached = false;    //Flag to indicate once the index is reached.
        T deleted = null;

        //A check for an invalid index.
        if ((index < 0) || (index >= currentSize)) {
            throw new IndexOutOfBoundsException("Index: " + index + " out of bounds!");
        }

        for (int i = 0; i < currentSize; i++) {

            //Once the index is met, the previous value is stored.
            if (i == index) {
                hasReached = true;
                deleted = data[i];
                data[i] = null;
            } else if (hasReached) {

                //Simple swap, in order to shift the elements, following the deleted element.
                T temp = data[i - 1];
                data[i - 1] = data[i];
                data[i] = temp;
            }
        }

        //Shrinks the capacity, if the currentSize falls below 1/3. However, any capacity greater than DEFAULT_CAPACITY is preserved.
        if ((Double.compare(--currentSize, capacity / 3.0) < 0) && (capacity / 2 >= DEFAULT_CAPACITY)) {
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

    /**
     * Gets a human interpretted representation of the column.
     *
     * @return Returns the human interpretted data for a particular column. This includes the current size and capacity.
     */
    public String toString() {
        //This method is provided for debugging purposes
        //(use/modify as much as you'd like), it just prints
        //out the column for easy viewing.
        StringBuilder s = new StringBuilder("Column with " + size()
                + " items and a capacity of " + capacity() + ":");
        for (int i = 0; i < size(); i++) {
            s.append("\n  [" + i + "]: " + get(i));
        }
        return s.toString();

    }

    /**
     * Main method to briefly test the functionality of the column implementation.
     *
     * @param args Command-line arguments, primarily used for testing purposes at run-time.
     */
    public static void main(String args[]) {
        //create a column of integers
        Column<Integer> nums = new Column<>();
        if ((nums.size() == 0) && (nums.capacity() == 2)) {
            System.out.println("Yay 1");
        }

        //append some numbers
        for (int i = 0; i < 3; i++) {
            nums.add(i * 2);
        }

        if (nums.size() == 3 && nums.get(2) == 4 && nums.capacity() == 4) {
            System.out.println("Yay 2");
        }

        //create a column of strings
        Column<String> msg = new Column<>();

        //insert some strings
        msg.add(0, "world");
        msg.add(0, "hello");
        System.out.println(msg.toString());
        msg.add(1, "new");
        msg.add(3, "!");

        //checking
        if (msg.get(0).equals("hello") && msg.set(1, "beautiful").equals("new")
                && msg.size() == 4 && msg.capacity() == 4) {
            System.out.println("Yay 3");
        }

        //delete
        if (msg.delete(1).equals("beautiful") && msg.get(1).equals("world")
                && msg.size() == 3) {
            System.out.println("Yay 4");
        }

        //shrinking
        nums.add(100);

        nums.add(0, -10);

        System.out.println(nums.toString());
        if (nums.delete(0) == -10 && nums.delete(1) == 2 && nums.delete(2) == 100
                && nums.size() == 2 && nums.capacity() == 4) {
            System.out.println("Yay 5");
        }

        //Misc Tests
        Column<Integer> nums2 = new Column<>(6);

        nums2.add(1);
        nums2.add(2);
        nums2.add(3);
        nums2.add(4);
        nums2.add(5);
        nums2.add(6);
        nums2.add(7);

        if (nums2.capacity() == 12) {

            System.out.println("Yay 7");
        }

        nums2.add(0, 11);
        nums2.add(3, 129);

        assert (nums2.get(0) == 11);
        assert (nums2.get(3) == 129);

        nums2.delete(0);
        nums2.delete(4);
        nums2.delete(5);
        nums2.delete(2);
        nums2.delete(1);
        System.out.println(nums2.toString());

        try {

            nums2.add(100, 4);
        } catch (IndexOutOfBoundsException iobe) {

            System.out.println("Yay 8");
        }
    }
}
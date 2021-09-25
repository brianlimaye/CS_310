/**
 * A class representing a simple Stack.
 *
 * @param <T> A generic type for the Stack.
 * @author Brian Limaye
 */
public class Stack<T> {

    /**
     * A Linked List of elements used to internally store the contents of the Stack.
     */
    protected final LList<T> elements;

    /**
     * Stores the current size of the Stack.
     */
    protected int currentSize;

    /**
     * Default constructor used to initialize a Stack instance.
     */
    public Stack() {
        this.elements = new LList<>();
        this.currentSize = 0;
    }

    /**
     * Adds a generic value to the Stack.
     *
     * @param e The generic value to be added to the Stack.
     */
    public void push(final T e) {
        if (e == null) {
            return;
        }
        elements.insertFirst(e);
        ++currentSize;
    }

    /**
     * Removes the top-most value from the Stack, if possible.
     *
     * @return Returns the top element from the Stack, null if empty.
     */
    public T pop() {

        final Node<T> removed = elements.removeFirst();

        //If there had existed an item prior to deletion, the size is decremented and the value is returned.
        if (removed != null) {
            --currentSize;
            return removed.getValue();
        }

        return null;
    }

    /**
     * Obtains the top-most value from the Stack, if possible.
     *
     * @return Returns the top element from the Stack, null if empty.
     */
    public T peek() {

        final Node<T> first = elements.getFirst();

        //If head had not been null, there exists an item at the front.
        if (first != null) {
            return first.getValue();
        }

        return null;
    }

    /**
     * Determines whether or not the Stack is empty.
     *
     * @return Returns true if the stack is empty, false otherwise.
     */
    public boolean isEmpty() {
        return currentSize == 0;
    }

    /**
     * Gets the current size of the Stack.
     *
     * @return Returns the current size of the Stack.
     */
    public int getSize() {
        return currentSize;
    }

    /**
     * Gets the human interpreted representation of the current Stack instance.
     *
     * @return Returns the human interpreted representation of the Stack.
     */
    public String toString() {

        return elements.listToString();
    }

    /**
     * Main method primarily used for testing the functionality of the Stack class implementation.
     *
     * @param args Command-line arguments primarily used for testing functionality at runtime.
     */
    public static void main(final String[] args) throws Throwable {
        /**
         *Inner class representing an arbitrary type for testing.
         */
        class SomeType {
            /**
             *A value.
             */
            private int value;

            /**
             *One-arg constructor for creating a new SomeType instance.
             *@param value The value to be set.
             */
            public SomeType(int value) {
                this.value = value;
            }

            /**
             *Gets the human interpreted representation for the SomeType instance.
             *@return Returns the human interpreted representation for the SomeType.
             */
            public String toString() {
                return "" + value;
            }

            /**
             *Compares the current SomeType and the parameter Object, if both are SomeTypes.
             *@param o The other object being compared.
             *@return Returns true if both are equal, false otherwise.
             */
            public boolean equals(Object o) {
                if (!(o instanceof SomeType)) return false;
                return ((SomeType) o).value == value;
            }

            /**
             * @return Returns the hashcode representation of the toString() method.
             */
            public int hashCode()
            {
                return value;
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

        Object o = s.peek();
        if (o != null)
        {
            if (o.equals("200")) {
                System.out.println("Yay2");
            }
        }  
        
        o = s.pop();
        if (o != null)
        {
            if (o.equals("200")) 
            {
                System.out.println("Yay3");
            }
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

        s.pop();
        s.pop();

        s.push(item1);
        s.pop();
                s.pop();


        System.out.println(s.getSize());
    }
}
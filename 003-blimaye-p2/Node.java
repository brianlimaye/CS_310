/**
 * A generic class representing a node in the linked list implementation LList.
 *
 * @param <T> The generic type for the Node.
 * @author W. Masri
 */
public class Node<T> {
    /**
     * The node's value.
     */
    private T value;

    /**
     * The node's link to the next element in the linked list.
     */
    private Node<T> next;

    /**
     * Two-argument constructor used to initialize a Node instance.
     *
     * @param value The value stored within that given Node.
     * @param next  The next Node, in which the current has a reference to.
     */
    public Node(T value, Node<T> next) {
        this.value = value;
        this.next = next;
    }

    /**
     * One-argument constructor used to initialize a Node instance.
     *
     * @param value The value stored within that given Node.
     */
    public Node(T value) {
        this.value = value;
        this.next = null;
    }

    /**
     * Gets the value stored in the Node.
     *
     * @return Returns the value stored within the Node, null if there exists no value.
     */
    public T getValue() {
        return value;
    }

    /**
     * Sets the value stored in the Node to a new value.
     *
     * @param value The generic value to be set.
     */
    public void setValue(T value) {
        this.value = value;
    }

    /**
     * Gets the next reference Node.
     *
     * @return Returns the next reference Node, null otherwise.
     */
    public Node<T> getNext() {
        return this.next;
    }

    /**
     * Sets the next reference Node.
     *
     * @param next The reference Node to be set as the "next" Node.
     */
    public void setNext(Node<T> next) {
        this.next = next;
    }

    /**
     * Gets the human interpreted representation of the current Node, denoted by its value.
     *
     * @return Returns the human interpreted version of the Node, from its respective value.
     */
    @Override
    public String toString() {
        return value.toString();
    }
}


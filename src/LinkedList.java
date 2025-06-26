/**
 * LinkedList.java
 * @author Stephen Lin
 * CIS 22C, Final Project
 */
import java.util.NoSuchElementException;

/**
 * Generic LinkedList class implementation
 * @param <T> the type of elements stored in the LinkedList
 */
public class LinkedList<T> {
	/**
	 * Node class implementation
	 */
	private class Node {
		/**
		 * The node's data
		 */
		private T data;
		/**
		 * The left connecting node
		 */
		private Node next;
		/**
		 * The right connecting node
		 */
		private Node prev;
		/**
		 * Constructs a new node
		 * @param data the generic data to place in the node
		 */
		public Node(T data) {
			this.data = data;
			this.next = null;
			this.prev = null;
		}
	}

	/**
	 * The length of the list
	 */
	private int length;
	/**
	 * The first node of the list
	 */
	private Node first;
	/**
	 * The last node of the list
	 */
	private Node last;
	/**
	 * The node used to iterate the list
	 */
	private Node iterator;

	/* CONSTRUCTORS */
	/**
	 * Instantiates a new LinkedList with default values
	 * 
	 * @postcondition a linked list with no objects is created
	 */
	public LinkedList() {
		first = null;
		last = null;
		length = 0;
		iterator = null;
	}

	/**
	 * Converts the given array into a LinkedList
	 * 
	 * @param array the array of values to insert into this LinkedList
	 * @postcondition a linkedlist with the same data is created
	 */
	public LinkedList(T[] array) {
		this();
		if (array == null) {
			return;
		}
		for (int i = 0; i < array.length; i++) {
			addLast(array[i]);
		}
	}

	/**
	 * Instantiates a new LinkedList by copying another List
	 * 
	 * @param original the LinkedList to copy
	 * @postcondition a new List object, which is an identical, but separate, copy
	 *                of the LinkedList original
	 */
	public LinkedList(LinkedList<T> original) {
		this();
		if (original == null) {
			return;
		}
		Node temp = original.first;
		while (temp != null) {
			addLast(temp.data);
			temp = temp.next;
		}
	}

	/* ACCESSORS */
	/**
	 * Returns the value stored in the first node
	 * 
	 * @precondition there is a first node
	 * @return the value stored at node first
	 * @throws NoSuchElementException if there is no first node
	 */
	public T getFirst() throws NoSuchElementException {
		if (first == null) {
			throw new NoSuchElementException("There is no first element!");
		}
		return first.data;
	}

	/**
	 * Returns the value stored in the last node
	 * 
	 * @precondition there is a last node
	 * @return the value stored in the node last
	 * @throws NoSuchElementException if there is no last node
	 */
	public T getLast() throws NoSuchElementException {
		if (last == null) {
			throw new NoSuchElementException("There is no last element!");
		}
		return last.data;
	}

	/**
	 * Returns the data stored in the iterator node
	 * 
	 * @precondition iterator is not null
	 * @return the data stored in the iterator node
	 * @throws NullPointerException if iterator is null (off end)
	 */
	public T getIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("getIterator: iterator is off end");
		}
		return iterator.data;
	}

	/**
	 * Returns the current length of the LinkedList
	 * 
	 * @return the length of the LinkedList from 0 to n
	 */
	public int getLength() {
		return length;
	}

	/**
	 * Returns whether the LinkedList is currently empty
	 * 
	 * @return whether the LinkedList is empty
	 */
	public boolean isEmpty() {
		return length == 0;
	}

	/**
	 * Returns whether the iterator is offEnd, i.e. null
	 * 
	 * @return whether the iterator is null
	 */
	public boolean offEnd() {
		return iterator == null;
	}

	/* MUTATORS */
	/**
	 * Creates a new first element
	 * 
	 * @param data the data to insert at the front of the LinkedList
	 * @postcondition the given data is at the head of the linked list
	 */
	public void addFirst(T data) {
		if (length == 0) {
			first = last = new Node(data);
		} else {
			Node node = new Node(data);
			node.next = first;
			first.prev = node;
			first = node;
		}
		length++;
	}

	/**
	 * Creates a new last element
	 * 
	 * @param data the data to insert at the end of the LinkedList
	 * @postcondition data is inserted into the last position of the linked list
	 */
	public void addLast(T data) {
		if (length == 0) {
			first = last = new Node(data);
		} else {
			Node node = new Node(data);
			node.prev = last;
			last.next = node;
			last = node;
		}
		length++;
	}

	/**
	 * Inserts a new element after the iterator
	 * 
	 * @param data the data to insert
	 * @precondition iterator isn't null
	 * @throws NullPointerException if iterator is null
	 */
	public void addIterator(T data) throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("addIterator: iterator is null!");
		}

		Node node = new Node(data);
		if (iterator == last) {
			addLast(data);
		} else {
			node.next = iterator.next;
			node.prev = iterator;
			iterator.next.prev = node;
			iterator.next = node;
			length++;
		}
	}

	/**
	 * removes the element at the front of the LinkedList
	 * 
	 * @precondition list has a first element
	 * @postcondition first element is removed
	 * @throws NoSuchElementException if list is empty
	 */
	public void removeFirst() throws NoSuchElementException {
		if (length == 0) { // precondition
			throw new NoSuchElementException("removeFirst(): Cannot remove from an empty List!");
		} else {

			// general case:
			first = first.next;
			if (first != null) {
				first.prev = null;
			}
		}
		length--;
	}

	/**
	 * removes the element at the end of the LinkedList
	 * 
	 * @precondition list has a last element
	 * @postcondition last element is removed
	 * @throws NoSuchElementException if list is empty
	 */
	public void removeLast() throws NoSuchElementException {
		if (length == 0) { // precondition
			throw new NoSuchElementException("removeLast(): Cannot remove from an empty List!");
		} else {
			last = last.prev;
			if (last != null) {
				last.next = null;
			} else {
				first = null;
			}
		}
		length--;
	}

	/**
	 * removes the element referenced by the iterator
	 * 
	 * @precondition iterator is not null
	 * @postcondition iterator is null, the element it used to point at is removed
	 * @throws NullPointerException if iterator is null
	 */
	public void removeIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("removeIterator: iterator is null!");
		}
		if (iterator == first) {
			removeFirst();
		} else if (iterator == last) {
			removeLast();
		} else {
			iterator.prev.next = iterator.next;
			iterator.next.prev = iterator.prev;
			length--;
		}
		iterator = null;
	}

	/**
	 * places the iterator at the first node
	 * 
	 * @postcondition iterator is at first
	 */
	public void positionIterator() {
		iterator = first;
	}

	/**
	 * Moves the iterator one node towards the last
	 * 
	 * @precondition iterator is not null
	 * @postcondition iterator advances one node, towards last
	 * @throws NullPointerException if iterator is null
	 */
	public void advanceIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("advanceIterator: iterator is null!");
		}
		iterator = iterator.next;
	}

	/**
	 * Moves the iterator one node towards the first
	 * 
	 * @precondition iterator is not null
	 * @postcondition iterator moves back one node, towards first
	 * @throws NullPointerException if iterator is null
	 */
	public void reverseIterator() throws NullPointerException {
		if (offEnd()) {
			throw new NullPointerException("reverseIterator: iterator is null!");
		}
		iterator = iterator.prev;
	}

	/**** ADDITIONAL OPERATIONS ****/

	/**
	 * Re-sets LinkedList to empty as if the default constructor had just been
	 * called
	 */
	public void clear() {
		first = last = iterator = null;
		length = 0;
	}

	/**
	 * Converts the LinkedList to a String, with each value separated by a blank
	 * line At the end of the String, place a new line character
	 * 
	 * @return the LinkedList as a String
	 */
	@Override
	public String toString() {
		StringBuilder result = new StringBuilder();
		Node temp = first;
		while (temp != null) {
			result.append(temp.data + " ");
			temp = temp.next;
		}
		return result.toString() + "\n";
	}

	/**
	 * Determines whether the given Object is another LinkedList, containing the
	 * same data in the same order
	 * 
	 * @param obj another Object
	 * @return whether there is equality
	 */
	@SuppressWarnings("unchecked") // good practice to remove warning here
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LinkedList)) {
			return false;
		}
		if (((LinkedList<T>) obj).getLength() != getLength()) {
			return false;
		}
		Node temp1 = ((LinkedList<T>) obj).first;
		Node temp2 = first;
		while (temp1 != null) {
			if (temp1.data == null || temp2.data == null) {
				if (temp1.data != temp2.data) {
					return false;
				}
			} else if (!temp1.data.equals(temp2.data)) {
				return false;
			}
			temp1 = temp1.next;
			temp2 = temp2.next;
		}
		return true;
	}

	/**
	 * Moves all nodes in the list towards the end of the list the number of times
	 * specified. Any node that falls off the end of the list as it moves forward
	 * will be placed at the front of the list.
	 * 
	 * For example: [1, 2, 3, 4, 5], numMoves = 2 - [4, 5, 1, 2, 3] For example: [1,
	 * 2, 3, 4, 5], numMoves = 4 - [2, 3, 4, 5, 1] For example: [1, 2, 3, 4, 5],
	 * numMoves = 7 - [4, 5, 1, 2, 3]
	 * 
	 * @param numMoves the number of times to move each node.
	 * @precondition numMoves &gt;= 0
	 * @postcondition iterator position unchanged (i.e. still referencing the same
	 *                node in the list, regardless of new location of Node)
	 * @throws IllegalArgumentException when numMoves &lt; 0
	 */
	public void spinList(int numMoves) throws IllegalArgumentException {
		if (numMoves < 0) {
			throw new IllegalArgumentException("spinList: numMoves < 0");
		}
		if (length == 1 || length == 0) {
			return;
		}
		for (int i = 0; i < numMoves; i++) {

			last.prev.next = null;
			first.prev = last;
			last.next = first;
			first = last;
			last = last.prev;
			first.prev = null;
		}
	}

	/**
	 * Splices together two LinkedLists to create a third List which contains
	 * alternating values from this list and the given parameter.
	 * 
	 * For example: [1, 2, 3] and [4, 5, 6] → [1, 4, 2, 5, 3, 6] For example: [1, 2,
	 * 3, 4] and [5, 6] → [1, 5, 2, 6, 3, 4] For example: [1, 2] and [3, 4, 5, 6] →
	 * [1, 3, 2, 4, 5, 6]
	 * 
	 * @param list the second LinkedList
	 * @return a new LinkedList, which is the result of interlocking this and list
	 * @postcondition this and list are unchanged
	 */
	public LinkedList<T> altLists(LinkedList<T> list) {
		LinkedList<T> newList = new LinkedList<T>();
		Node temp1 = first;
		Node temp2;
		if (list == null) {
			temp2 = null;
		} else {
			temp2 = list.first;
		}
		int counter = 0;
		while (temp1 != null || temp2 != null) {
			if (counter % 2 == 0) {
				if (temp1 != null) {
					newList.addLast(temp1.data);
					temp1 = temp1.next;
				}
			} else {
				if (temp2 != null) {
					newList.addLast(temp2.data);
					temp2 = temp2.next;
				}
			}
			counter++;
		}
		return newList;
	}

	/**
	 * returns each element from the linkedlist in a string with its position (1-n),
	 * followed by new line
	 * 
	 * @return the numbered elements, as a string
	 */
	public String numberedListString() {
		Node node = first;
		String returnString = "";
		int count = 1;
		while (node != null) {
			returnString += count + ". " + node.data + "\n";
			count++;
			node = node.next;
		}
		return returnString + "\n";
	}

	/**
	 * finds the index of the data
	 * 
	 * @param data the data to find index of
	 * @return index of data or -1 if it doesn't exist
	 */
	public int findIndex(T data) {
		Node node = first;
		int count = 0;
		while (node != null) {
			if (node.data.equals(data)) {
				return count;
			}
			count++;
			node = node.next;
		}
		return -1;
	}

	/**
	 * advances the iterator to the index
	 * 
	 * @precondition index &gt;= 0, index &lt; length
	 * @param index the index to go to
	 * @throws IndexOutOfBoundsException when iterator is out of bounds
	 */
	public void advanceIteratorToIndex(int index) throws IndexOutOfBoundsException {
		if (index < 0 || index >= length) {
			throw new IndexOutOfBoundsException("advanceIteratorToIndex: invalid index!");
		}
		positionIterator();
		for (int i = 0; i < index; i++) {
			advanceIterator();
		}
	}

}

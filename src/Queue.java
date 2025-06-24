
/**
 * The Queue class definition
 * @author Stephen Lin
 * CIS 22C, Lab 5
 * @param <T> the generic data stored in the Queue
 */

import java.util.NoSuchElementException;

public class Queue<T> implements Q<T> {

	private class Node {
		private T data;
		private Node next;

		public Node(T data) {
			this.data = data;
			this.next = null;
		}
	}

	private int size;
	private Node front;
	private Node end;

	/**
	 * default constructor: sets size to 0, and front and end to null
	 */
	public Queue() {
		size = 0;
		front = end = null;
	}

	/**
	 * turns a given array into a queue
	 * 
	 * @param data an array holding the data to be converted into a queue
	 */
	public Queue(T[] data) {
		this();
		if (data == null) {
			return;
		}
		for (int i = 0; i < data.length; i++) {
			enqueue(data[i]);
		}
	}

	/**
	 * copy constructor: makes a deep copy of a given queue
	 * 
	 * @param data the queue to make a copy of
	 */
	public Queue(Queue<T> data) {
		this();
		if (data == null) {
			return;
		}
		Queue<T> temp = new Queue<T>();
		while (!data.isEmpty()) {
			temp.enqueue(data.getFront());
			data.dequeue();
		}
		while (!temp.isEmpty()) {
			enqueue(temp.getFront());
			data.enqueue(temp.getFront());
			temp.dequeue();
		}
	}

	/**
	 * returns the value stored in the front node of the queue
	 * 
	 * @return the value at the front of the queue
	 * @precondition the queue is not empty
	 * @throws NoSuchElementException if queue is empty
	 */
	@Override
	public T getFront() throws NoSuchElementException {
		if (size == 0) {
			throw new NoSuchElementException("getFront: the queue is empty!");
		}
		return front.data;
	}

	/**
	 * returns size of queue
	 * 
	 * @return size of queue
	 */
	@Override
	public int getSize() {
		return size;
	}

	/**
	 * returns if the queue is empty
	 * 
	 * @return true if the queue is empty, false otherwise
	 */
	@Override
	public boolean isEmpty() {
		return size == 0;
	}

	/**
	 * adds the given data into a new node at the end of the queue
	 * 
	 * @param data the data to be added to the queue
	 */
	@Override
	public void enqueue(T data) {
		Node newBack = new Node(data);
		if (size == 0) {
			front = end = newBack;
		} else {
			end.next = newBack;
			end = newBack;
		}
		size++;
	}

	/**
	 * removes the element at front of the queue
	 * 
	 * @precondition queue is not empty
	 * @throws NoSuchElementException if queue is empty
	 */
	@Override
	public void dequeue() throws NoSuchElementException {
		if (size == 0) {
			throw new NoSuchElementException("dequeue: the queue is empty!");
		}
		front = front.next;
		size--;
	}

	/**
	 * returns the string representation of the queue
	 * 
	 * @return the string representing the queue
	 */
	@Override
	public String toString() {
		Queue<T> temp = new Queue<T>();
		String returnString = "";

		while (!isEmpty()) {
			returnString += getFront().toString() + " ";
			temp.enqueue(getFront());
			dequeue();
		}

		while (!temp.isEmpty()) {
			enqueue(temp.getFront());
			temp.dequeue();
		}

		return returnString + "\n";
	}

	/**
	 * returns if two queues are equal
	 * 
	 * @param other the queue to be compared to
	 * @return true if all elements of queue are equal, false otherwise
	 */
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Queue)) {
			return false;
		}

		Queue<T> oth = (Queue) other;
		if (oth.size != size) {
			return false;
		}
		if (oth == this) {
			return true;
		}

		Queue<T> temp1 = new Queue<T>();
		Queue<T> temp2 = new Queue<T>();

		boolean returnVal = true;

		while (!isEmpty()) {
			if (!getFront().equals(oth.getFront())) {
				returnVal = false;
			}
			temp1.enqueue(getFront());
			temp2.enqueue(oth.getFront());
			dequeue();
			oth.dequeue();
		}

		while (!temp1.isEmpty()) {
			enqueue(temp1.getFront());
			oth.enqueue(temp2.getFront());
			temp1.dequeue();
			temp2.dequeue();
		}

		return returnVal;

	}

	/**
	 * returns if a queue is sorted from small to large by calling the corresponding
	 * helper method
	 * 
	 * @return if the queue is sorted from small to large
	 */
	public boolean isSorted() {
		if (isEmpty()) {
			return true;
		}
		return isSorted(front);
	}

	/**
	 * returns if an element is present in a queue by using linear search
	 * 
	 * @param element the element to search for
	 * @return if the element is present
	 */
	public boolean linearSearch(T element) {
		if (isEmpty()) {
			return false;
		}
		return linearSearch(front, element);
	}

	/**
	 * returns if an element is present in a queue by using binary search
	 * 
	 * @precondition queue is sorted in ascending order
	 * @param element the element to search for
	 * @return if the element is present
	 * @throws IllegalStateException if queue is not sorted in ascending order
	 */
	public boolean binarySearch(T value) throws IllegalStateException {
		if (isEmpty()) {
			return false;
		}
		if (!isSorted()) {
			throw new IllegalStateException("binarySearch: stack is not sorted in ascending order!");
		}
		return binarySearch(0, size - 1, value);
	}

	/**
	 * returns a string of the queue's elements in reverse order
	 * 
	 * @return a string of the queues elements in reverse order
	 */
	public String reverseQueue() {
		if (isEmpty()) {
			return "\n";
		}
		return reverseQueue(front) + " \n";
	}

	/** RECURSIVE HELPER METHODS */
	/**
	 * returns a string of the queue's elements in reverse order
	 * 
	 * @param node the current node being added to the string
	 * @return a string of the queues elements in reverse order
	 */
	private String reverseQueue(Node node) {
		if (node.next == null) {
			return node.data + "";
		}
		return reverseQueue(node.next) + " " + node.data;

	}

	/**
	 * checks if queue is sorted in ascending order
	 * 
	 * @param node currentNode being checked if in ascending order
	 * @return if the queue is in ascending order
	 */
	private boolean isSorted(Node node) {
		if (node.next == null) {
			return true;
		}
		Comparable<? super T> current = (Comparable<? super T>) node.data;
		if (current.compareTo(node.next.data) > 0) {
			return false;
		}
		return isSorted(node.next);
	}

	/**
	 * checks if a value appears in queue using linear search
	 * 
	 * @param node  current node being checked
	 * @param value the value being checked for
	 * @return if the value appears in the queue
	 */
	private boolean linearSearch(Node node, T value) {
		if (node.data.equals(value)) {
			return true;
		}
		if (node.next == null) {
			return false;
		}
		return linearSearch(node.next, value);
	}

	/**
	 * returns the data in the middle of the queue
	 * 
	 * @param node current node being checked
	 * @param mid  the middle of the queue
	 * @return the data in middle of queue
	 */
	private T getMid(Node node, int mid) {
		if (mid == 0) {
			return node.data;
		}
		return getMid(node.next, mid - 1);
	}

	/**
	 * returns if an element is in queue using binary search
	 * 
	 * @param low   the lower bound to check
	 * @param high  the upper bound to check
	 * @param value the value to check if present
	 * @return if an element is present in queue
	 */
	private boolean binarySearch(int low, int high, T value) {
		if (low > high) {
			return false;
		}
		int mid = low + (high - low) / 2;
		T nodeData = getMid(front, mid);
		if (nodeData.equals(value)) {
			return true;
		}
		Comparable<? super T> current = (Comparable<? super T>) nodeData;
		if (current.compareTo(value) > 0) {
			return binarySearch(low, mid - 1, value);
		} else {
			return binarySearch(mid + 1, high, value);
		}
	}

}

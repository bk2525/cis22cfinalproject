/**
 * HashTable.java
 * @author Stephen Lin
 * CIS 22C, Final Project
 */
import java.util.ArrayList;

/**
 * Generic HashTable class implementation
 * @param <T> the type of elements stored in the HashTable
 */
public class HashTable<T> {
	/**
	 * The number of elements stored in the HashTable
	 */
	private int numElements;
	/**
	 * The data table
	 */
	private ArrayList<LinkedList<T>> table;

	/* Constructors */
	/**
	 * Constructor that takes in a size to make the table
	 * 
	 * @param size size of table
	 * @throws IllegalArgumentException if size &lt;= 0
	 */
	public HashTable(int size) throws IllegalArgumentException {
		if (size <= 0) {
			throw new IllegalArgumentException("HashTable(size): size is <= 0");
		}
		table = new ArrayList<>(size);
		for (int i = 0; i < size; i++) {
			table.add(new LinkedList<T>());
		}
		numElements = 0;
	}

	/**
	 * Constructor that takes in a size and array to make the table
	 * 
	 * @param array the array to use
	 * @param size  size of table
	 * @throws IllegalArgumentException if size &lt;= 0
	 */
	public HashTable(T[] array, int size) throws IllegalArgumentException {
		this(size);
		if (array == null) {
			return;
		}
		for (int i = 0; i < array.length; i++) {
			add(array[i]);
		}
	}

	/**
	 * Gets the hash code of the object by taking the hashcode and modding it by the
	 * table size
	 * 
	 * @param obj the object to hash
	 * @return the hash
	 */
	private int hash(T obj) {
		return Math.abs(obj.hashCode() % table.size());
	}

	/* Accessors */
	/**
	 * Returns num of elements
	 * 
	 * @return num of elements
	 */
	public int getNumElements() {
		return numElements;
	}

	/**
	 * Returns number of elements at this bucket
	 * 
	 * @param bucket the bucket to check
	 * @return size of bucket
	 * @throws IndexOutOfBoundsException if bucket &lt; 0 or bucket &gt;= table.size
	 */
	public int countBucket(int bucket) throws IndexOutOfBoundsException {
		if (bucket < 0 || bucket >= table.size()) {
			throw new IndexOutOfBoundsException("countBucket: bucket is out of bounds!");
		}
		return table.get(bucket).getLength();
	}

	/**
	 * Checks if the hashtable contains an element
	 * 
	 * @param element the element to check
	 * @return if it contains the element
	 */
	public boolean contains(T element) {
		return find(element) != -1;
	}

	/**
	 * Returns load factor: numElements / numBuckets
	 * 
	 * @return load factor
	 */
	public double getLoadFactor() {
		return numElements * 1.0 / table.size();
	}

	/**
	 * Checks if an element exists in table or not
	 * 
	 * @param element the element to check for
	 * @return the bucket of the element or -1 if it doesn't exist
	 * @throws NullPointerException element is null
	 */
	public int find(T element) throws NullPointerException {
		if (element == null) {
			throw new NullPointerException("find: element is null!");
		}
		int bucket = hash(element);
		LinkedList<T> linkedList = table.get(bucket);
		linkedList.positionIterator();
		while (!linkedList.offEnd()) {
			if (linkedList.getIterator().equals(element)) {
				return bucket;
			}
			linkedList.advanceIterator();
		}
		// if (!table.get(bucket).isEmpty() &&
		// table.get(bucket).getFirst().equals(element)){
		// return bucket;
		// }
		return -1;
	}

	/**
	 * Gets the element if it exists
	 * 
	 * @param element the element to check for
	 * @return the element if it exists, null if not
	 * @throws NullPointerException if element is null
	 */
	public T get(T element) throws NullPointerException {
		if (element == null) {
			throw new NullPointerException("get: element is null!");
		}
		if (find(element) == -1) {
			return null;
		}
		int bucket = hash(element);
		LinkedList<T> linkedList = table.get(bucket);
		linkedList.positionIterator();
		while (!linkedList.offEnd()) {
			if (linkedList.getIterator().equals(element)) {
				return linkedList.getIterator();
			}
			linkedList.advanceIterator();
		}
		return null;
	}

	/**
	 * Gets all the elements and add them into an ArrayList The hashTable contains
	 * all the songs
	 * 
	 * @return an arrayList of elements, used for statistic
	 */
	public ArrayList<T> getAllElements() {
		ArrayList<T> elements = new ArrayList<>();
		for (LinkedList<T> bucket : table) {
			bucket.positionIterator();
			while (!bucket.offEnd()) {
				elements.add(bucket.getIterator());
				bucket.advanceIterator();
			}
		}
		return elements;
	}

	/* Mutators */
	/**
	 * Adds an element to the list
	 * 
	 * @param element the element to add
	 * @throws NullPointerException element is null
	 */
	public void add(T element) throws NullPointerException {
		if (element == null) {
			throw new NullPointerException("add: element is null!");
		}
		int bucket = hash(element);
		table.get(bucket).addLast(element);
		numElements++;
	}

	/**
	 * Deletes an element from the hashtable
	 * 
	 * @param element the element to delete
	 * @throws NullPointerException if element is null
	 * @return if element deleted or not
	 */
	public boolean delete(T element) throws NullPointerException {
		if (element == null) {
			throw new NullPointerException("delete: element is null!");
		}
		int hash = hash(element);
		LinkedList<T> list = table.get(hash);
		list.positionIterator();
		while (!list.offEnd()) {
			if (list.getIterator().equals(element)) {
				list.removeIterator();
				numElements--;
				return true;
			}
			list.advanceIterator();
		}
		return false;
	}

	/**
	 * Clears the hashtable
	 */
	public void clear() {
		numElements = 0;
		for (int i = 0; i < table.size(); i++) {
			table.set(i, new LinkedList<T>());
		}
	}

	/* Additional Methods */
	/**
	 * Returns the buckets contents as a string
	 * 
	 * @param bucket the bucket to return as a string
	 * @return a string of the buckets contents
	 * @throws IndexOutOfBoundsException bucket &lt; 0 or bucket &gt;= table.size
	 */
	public String bucketToString(int bucket) throws IndexOutOfBoundsException {
		if (bucket < 0 || bucket >= table.size()) {
			throw new IndexOutOfBoundsException("bucketToString: bucket is out of bounds!");
		}
		return table.get(bucket).toString();
	}

	/**
	 * String of bucket number, then colon, then first element in row
	 * 
	 * @return the string of the bucket number: first element in row
	 */
	public String rowToString() {
		String returnString = "";
		for (int i = 0; i < table.size(); i++) {
			returnString += "Bucket " + i + ": ";
			if (!table.get(i).isEmpty()) {
				returnString += table.get(i).getFirst();
			} else {
				returnString += "empty";
			}
			returnString += "\n";
		}
		return returnString;
	}

	/**
	 * A toString override for the hashtable
	 * 
	 * @return all elements in a string with newlines
	 */
	public String toString() {
		String returnString = "";
		for (int i = 0; i < table.size(); i++) {
			if (!table.get(i).isEmpty()) {
				returnString += table.get(i);
			}
		}
		return returnString + "\n";
	}

}

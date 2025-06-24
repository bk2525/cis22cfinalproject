
/**
 * BST.java
 * @author Stephen Lin
 * CIS 22C Lab 9
 */
import java.util.Comparator;
import java.util.NoSuchElementException;

public class BST<T> {
	private class Node {
		private T data;
		private Node left;
		private Node right;

		public Node(T data) {
			this.data = data;
			left = null;
			right = null;
		}
	}

	private Node root;

	/*** CONSTRUCTORS ***/
	/**
	 * default constructor. sets root to null
	 */

	public BST() {
		root = null;
	}

	/**
	 * creates a bst of minimal height from the given array
	 * 
	 * @param array the array to use
	 * @param cmp   the way the tree is organized
	 * @precondition array sorted in ascending order
	 * @throws IllegalArgumentException if not sorted
	 */
	public BST(T[] array, Comparator<T> cmp) throws IllegalArgumentException {
		if (array == null) {
			return;
		}
		if (!isSorted(array, cmp)) {
			throw new IllegalArgumentException("BST(T[], Comparator<T>): array isn't sorted!");
		}
		root = arrayHelper(0, array.length - 1, array);
	}

	/**
	 * checks if an array is sorted
	 * 
	 * @param array the array to check
	 * @param cmp   the way the array is ordered
	 * @return if the array is sorted
	 */
	private boolean isSorted(T[] array, Comparator<T> cmp) {
		for (int i = 0; i < array.length - 1; i++) {
			if (cmp.compare(array[i], array[i + 1]) > 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * recursive helper for constructor
	 * 
	 * @param begin where to start
	 * @param end   where to end
	 * @param array the array to search
	 * @return the newly created node
	 */
	private Node arrayHelper(int begin, int end, T[] array) {
		if (begin > end) {
			return null;
		}

		int mid = begin + (end - begin) / 2;
		Node node = new Node(array[mid]);
		node.left = arrayHelper(begin, mid - 1, array);
		node.right = arrayHelper(mid + 1, end, array);

		return node;
	}

	/**
	 * copy constructor for BST
	 * 
	 * @param bst the BST to copy
	 * @param cmp the comparator indicating how to sort data
	 */
	public BST(BST<T> bst, Comparator<T> cmp) {
		if (bst == null) {
			return;
		}
		copyHelper(bst.root, cmp);
	}

	/**
	 * helper method for copy constructor
	 * 
	 * @param node the current node to copy
	 * @param cmp  comparator indicating how to sort data
	 */
	private void copyHelper(Node node, Comparator<T> cmp) {
		if (node != null) {
			insert(node.data, cmp);
			copyHelper(node.left, cmp);
			copyHelper(node.right, cmp);
		}

	}

	/*** ACCESSORS ***/

	/**
	 * finds the maximum of the BST
	 * 
	 * @precondition !isEmpty
	 * @throws NoSuchElementException if the tree is empty
	 * @return the maximum element of the BST
	 */
	public T findMax() throws NoSuchElementException {
		if (root == null) {
			throw new NoSuchElementException("findMax(): is Empty!");
		}
		return findMax(root);
	}

	/**
	 * helper method for findMaximum; finds the maximum of the BST
	 * 
	 * @param node the current node to check
	 * @return the maximum of the BST
	 */
	public T findMax(Node node) {
		if (node.right == null) {
			return node.data;
		}
		return findMax(node.right);
	}

	/**
	 * finds the min of the BST
	 * 
	 * @precondition !isEmpty
	 * @throws NoSuchElementException if the tree is empty
	 * @return the min element of the BST
	 */
	public T findMin() {
		if (root == null) {
			throw new NoSuchElementException("findMin: isEmpty!");
		}
		return findMin(root);
	}

	/**
	 * helper method for findMinimum; finds the min of the BST
	 * 
	 * @param node the current node to check
	 * @return the min of the BST
	 */
	public T findMin(Node node) {
		if (node.left == null) {
			return node.data;
		}
		return findMin(node.left);
	}

	/**
	 * returns data stored in root
	 * 
	 * @precondition !isEmpty()
	 * @return data stored in root
	 * @throws NoSuchElementException if precondition is violated
	 */
	public T getRoot() {
		if (isEmpty()) {
			throw new NoSuchElementException("getRoot: root is null!");
		}
		return root.data;
	}

	/**
	 * returns if the BST is empty
	 * 
	 * @returns if the BST is empty
	 */
	public boolean isEmpty() {
		return root == null;
	}

	/**
	 * returns height of BST
	 * 
	 * @return the height
	 */
	public int getHeight() {
		return getHeight(root);
	}

	/**
	 * returns height of the bst
	 * 
	 * @param node the current node whose height to count
	 * @return height of tree
	 */
	public int getHeight(Node node) {
		if (node == null) {
			return -1;
		}

		int leftHeight = getHeight(node.left);
		int rightHeight = getHeight(node.right);
		return 1 + Math.max(leftHeight, rightHeight);
	}

	/**
	 * searches if a data exists in the tree
	 * 
	 * @param data the data to search for
	 * @param cmp  ordering if the tree
	 * @return the data stored in that tree otherwise null
	 */
	public T search(T data, Comparator<T> cmp) {
		return search(data, root, cmp);
	}

	/**
	 * searches if a data exists in the tree
	 * 
	 * @param data the data to search for
	 * @param node the current node to check
	 * @param cmp  ordering if the tree
	 * @return the data stored in that tree otherwise null
	 */
	public T search(T data, Node node, Comparator<T> cmp) {
		if (node == null) {
			return null;
		}
		if (cmp.compare(node.data, data) == 0) {
			return node.data;
		} else if (cmp.compare(node.data, data) > 0) {
			return search(data, node.left, cmp);
		} else {
			return search(data, node.right, cmp);
		}
	}

	/**
	 * returns size of tree (number of nodes)
	 * 
	 * @return size of tree
	 */
	public int getSize() {
		return getSize(root);
	}

	/**
	 * returns size of tree (number of nodes)
	 * 
	 * @param node the current node
	 * @return size of tree
	 */
	public int getSize(Node node) {
		if (node == null) {
			return 0;
		}
		int leftSize = getSize(node.left);
		int rightSize = getSize(node.right);
		return 1 + leftSize + rightSize;
	}

	/*** MUTATORS ***/

	/**
	 * removes data from the BST
	 * 
	 * @precondition !isEmpty
	 * @param data the data to remove
	 * @param cmp  comparator indicating how to sort data
	 */
	public void remove(T data, Comparator<T> cmp) {
		root = remove(data, root, cmp);
	}

	/**
	 * helper method for remove
	 * 
	 * @param data the data to remove
	 * @param node the current node to check
	 * @param cmp  comparator indicating how to sort data
	 * @return the node
	 */
	public Node remove(T data, Node node, Comparator<T> cmp) {
		if (node == null) {
			return null;
		} else if (cmp.compare(data, node.data) > 0) {
			node.right = remove(data, node.right, cmp);
		} else if (cmp.compare(data, node.data) < 0) {
			node.left = remove(data, node.left, cmp);
		} else {
			if (node.left == null && node.right == null) {
				node = null;
			} else if (node.left == null) {
				node = node.right;
			} else if (node.right == null) {
				node = node.left;
			} else {
				node.data = findMin(node.right);
				node.right = remove(findMin(node.right), node.right, cmp);
			}
		}
		return node;
	}

	/**
	 * adds data into the binary search tree
	 * 
	 * @param data the data to add
	 * @param cmp  comparator indicating how to sort data
	 */
	public void insert(T data, Comparator<T> cmp) {
		if (root == null) {
			root = new Node(data);
			return;
		}
		insert(data, root, cmp);
	}

	/**
	 * adds data into the binary search tree
	 * 
	 * @param data the data to add
	 * @param node the current node
	 * @param cmp  comparator indicating how to sort data
	 */
	public void insert(T data, Node node, Comparator<T> cmp) {
		if (cmp.compare(data, node.data) == 0) {
			return;
		}
		if (cmp.compare(data, node.data) > 0) {
			if (node.right == null) {
				node.right = new Node(data);
			} else {
				insert(data, node.right, cmp);
			}
		} else {
			if (node.left == null) {
				node.left = new Node(data);
			} else {
				insert(data, node.left, cmp);
			}
		}
	}

	/*** ADDITONAL OPERATIONS ***/

	/**
	 * Creates a String that is a height order traversal of the data in the tree
	 * starting at the Node with the largest height (the root) down to Nodes of
	 * smallest height - with Nodes of equal height added from left to right.
	 * 
	 * @return the level order traversal as a String
	 */
	public String levelOrderString() {
		Queue<Node> que = new Queue<>();
		StringBuilder sb = new StringBuilder();
		que.enqueue(root);
		levelOrderString(que, sb);
		return sb.toString() + "\n";
	}

	/**
	 * Helper method to levelOrderString. Inserts the data in level order into a
	 * String.
	 * 
	 * @param que            the Queue in which to store the data.
	 * @param heightTraverse a StringBuilder containing the data.
	 */
	private void levelOrderString(Queue<Node> que, StringBuilder heightTraverse) {
		if (!que.isEmpty()) {
			Node nd = que.getFront();
			que.dequeue();
			if (nd != null) {
				que.enqueue(nd.left);
				que.enqueue(nd.right);
				heightTraverse.append(nd.data + " ");
			}
			levelOrderString(que, heightTraverse);
		}
	}

	/**
	 * returns a string of data in preOrder
	 * 
	 * @return a string of data in preOrder
	 */
	public String preOrderString() {
		StringBuilder preOrder = new StringBuilder();
		preOrderString(root, preOrder);
		return preOrder.toString() + "\n";
	}

	/**
	 * helper method for preorderstring
	 * 
	 * @param node     the current node to check
	 * @param preOrder a stringbuilder to store data in
	 */
	private void preOrderString(Node node, StringBuilder preOrder) {
		if (node != null) {
			preOrder.append(node.data + " ");
			preOrderString(node.left, preOrder);
			preOrderString(node.right, preOrder);
		}
	}

	/**
	 * returns a string of data in inOrder
	 * 
	 * @return a string of data in inOrder
	 */
	public String inOrderString() {
		StringBuilder inOrder = new StringBuilder();
		inOrderString(root, inOrder);
		return inOrder.toString();
	}

	/**
	 * helper method for inOrderString
	 * 
	 * @param node    the current node to check
	 * @param inOrder a stringbuilder to store data in
	 */
	private void inOrderString(Node node, StringBuilder inOrder) {
		if (node != null) {
			inOrderString(node.left, inOrder);
			inOrder.append(node.data + "\n");
			inOrderString(node.right, inOrder);
		}
	}

	/**
	 * returns a string of data in postOrder
	 * 
	 * @return a string of data in postOrder
	 */
	public String postOrderString() {
		StringBuilder postOrder = new StringBuilder();
		postOrderString(root, postOrder);
		return postOrder.toString() + "\n";
	}

	/**
	 * helper method for postOrderString
	 * 
	 * @param node      the current node to check
	 * @param postOrder a stringbuilder to store data in
	 */
	private void postOrderString(Node node, StringBuilder postOrder) {
		if (node != null) {
			postOrderString(node.left, postOrder);
			postOrderString(node.right, postOrder);
			postOrder.append(node.data + " ");
		}
	}

	/*** CHALLENGE METHODS ***/
	/**
	 * returns the shared precursor of the nodes containing the two data points
	 * 
	 * @precondition data1 and data2 exist
	 * @param data1 the first data
	 * @param data2 the second data
	 * @param cmp   the comparator indicating how to sort data
	 * @return the shared precursor
	 */
	public T sharedPrecursor(T data1, T data2, Comparator<T> cmp) throws IllegalArgumentException {
		if (search(data1, cmp) == null || search(data2, cmp) == null) {
			throw new IllegalArgumentException("sharedPrecursor: one or more data does not exist!");
		}
		return sharedPrecursor(data1, data2, root, cmp);
	}

	/**
	 * returns the shared precursor of the nodes containing the two data points
	 * 
	 * @param data1 the first data
	 * @param data2 the second data
	 * @param node  the node to check
	 * @param cmp   the comparator indicating how to sort data
	 * @return the eshared precursor
	 */
	private T sharedPrecursor(T data1, T data2, Node node, Comparator<T> cmp) {
		if (cmp.compare(data1, node.data) > 0 && cmp.compare(data2, node.data) > 0) {
			return sharedPrecursor(data1, data2, node.right, cmp);
		} else if (cmp.compare(data1, node.data) < 0 && cmp.compare(data2, node.data) < 0) {
			return sharedPrecursor(data1, data2, node.left, cmp);
		} else {
			return node.data;
		}
	}

}

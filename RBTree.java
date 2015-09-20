/**
 * The red black tree class
 * @author HuyNguyen
 *
 */
public class RBTree {
	static final boolean INSERTION = true, DELETION = false;
	Node root;	
	
	/*****************************************************
	 * 	============== INSERTION TIPS ====================
	 * 
	 * If current node X is black and has 2 red children: 
	 * 		Make X red and its children black.
	 * 		If X's parent is red, perform single or double rotation.
	 * 
	 * If current node X is a red leaf node:
	 * 		Add the new red node as X's child
	 * 		Perform rotation on the newly added node and X.
	 * 
	 * Otherwise, keep advancing.
	 * 
	 *****************************************************/
	
	/**
	 * Insert a new node containing the specified value to the tree.
	 * @param value the specified value
	 */
	public void insert(int value){
		// the current node
		Node current = root;
		Node parent = null;
		
		// get down to the appropriate position
		while (current != null && value != current.value){	
			// if current node has 2 red children
			if (getColor(current.left) == Node.RED && getColor(current.right) == Node.RED)
				colorFlipAndRotate(current);
			
			// advance to the next node
			if (value < current.value){
				if (current.left == null) parent = current;
				current = current.left;
			}
			else {
				if (current.right == null) parent = current;
				current = current.right;
			}			
		}
		
		// if value is already present, print out error message
		if (current != null) {
			System.out.println("Duplicate value! Program will terminate");
		}
		
		// create a new leaf node at the current position
		current = new Node(value);
		if (root == null) root = current;
		
		// update the parent and child pointer
		current.p = parent;
		if (parent != null) {
			if (value < parent.value) parent.left = current;
			else parent.right = current;
		}
		
		// if current.p is red, perform rotation
		if (getColor(current.p) == Node.RED) rotation(current, INSERTION);
		
		// color the root black
		root.color = Node.BLACK;
	}
	
	/**
	 * Change the current node's color to <tt>RED</tt> and its two children's to <tt>BLACK</tt>.
	 * Then perform rotation if a violation occurs.
	 * @param current the current node
	 */
	private void colorFlipAndRotate(Node current){
		current.color = Node.RED;
		current.left.color = Node.BLACK;
		current.right.color = Node.BLACK;
		
		// if current node's parent is Node.RED, do rotation
		if (getColor(current.p) == Node.RED) rotation(current, INSERTION);
	}
	
	/**
	 * Perform rotation on the current node and its parent.
	 * @param current the current node.
	 * @param insertion a boolean to indicate whether insertion or deletion is being performed.
	 */
	private void rotation(Node current, boolean insertion){
		Node grandparent = current.p.p, parent = current.p;
		//outside rotation
		if ((current.value < parent.value) == (parent.value < grandparent.value)){
			if (current == parent.left) {
				rotateRight(grandparent);
				changeColor(grandparent, parent, insertion);
			}
			else {
				rotateLeft(grandparent);
				changeColor(grandparent, parent, insertion);
			}
		}
		//inside rotation
		else {
			//double rotation: rotate left first, then rotate right
			if (current == parent.right && parent == grandparent.left) {
				rotateLeft(parent);
				changeColor(parent, current, insertion);
				rotateRight(parent);
				changeColor(parent, current, insertion);
			}
			//double rotation: rotate right first, then rotate left
			else {
				rotateRight(parent);
				changeColor(parent, current, insertion);
				rotateLeft(parent);
				changeColor(parent, current, insertion);
			}
		}
	}
	
	/**
	 * Perform right rotation on the parent and its left child.
	 * @param parent the parent node
	 */
	private void rotateRight(Node parent) {
        Node child = parent.left;
        parent.left = child.right;
        if (child.right != null) child.right.p = parent;
        child.p = parent.p;
        if (parent.p == null) root = child;
        else if (parent == parent.p.left) parent.p.left = child;
        else parent.p.right = child;
        child.right = parent;
        parent.p = child;
        if (parent == root) root = child;
    }
	
	/**
	 * Perform left rotation on the parent and its right child.
	 * @param parent the parent node
	 */
	private void rotateLeft(Node parent) {
		Node child = parent.right;
        parent.right = child.left;
        if (child.left != null) child.left.p = parent;
        child.p = parent.p;
        if (parent.p == null) root = child;
        else if (parent == parent.p.right) parent.p.right = child;
        else parent.p.left = child;
        child.left = parent;
        parent.p = child;
        if (parent == root) root = child;
    }
	
	/**
	 * Change the appropriate nodes' color after rotation.
	 * @param parent the parent node
	 * @param child the child node
	 * @param insertion a boolean to indicate whether insertion is being performed or deletion is
	 */
	private void changeColor(Node parent, Node child, boolean insertion){
		// change color for insertion
		if (insertion) {
			parent.color = Node.RED;
			child.color = Node.BLACK;
		}
		// change color for deletion
		else {
			Node sibling = (child == parent.left) ? parent.right : parent.left;
			parent.color = Node.BLACK;
			child.color = Node.RED;
			if (sibling != null) {
				sibling.color = Node.RED;
				Node outerChild = (sibling == parent.left) ? sibling.left : sibling.right;
				if (outerChild != null) outerChild.color = Node.BLACK;
			}
			
		}
	}
	
	
	/*****************************************************
	 * 	============== DELETION TIPS ====================
	 * 
	 * Let X be current node, T be X's sibling, P be X's parent.
	 * As we traverse down the tree, we attempt to ensure that X is red.
	 * 
	 * Color the sentinel root red. This is to ensure that as X moves down, P will always be red.
	 * 
	 * If X is red:
	 * 		If X is leaf, see if it is the one to be deleted.
	 * 		If X is not leaf, continue further down to X's appropriate child.
	 * 
	 * If X is black and has 2 black children:
	 * 		If T has 2 black children, color flip.
	 * 		If T has an outer red child, single rotation.
	 * 		If T has an inner red child, double rotation.
	 * 
	 * If X is black and one of X's children is red:
	 * 		Go down one level (so new X is old X's child, new P is old X, new T is old X's other child)
	 * 			
	 *****************************************************/
	
	public void delete(int value){
		// the sentinel root
		final Node sentinel = new Node(Integer.MIN_VALUE);
		if (root == null) {
			System.out.println("Tree is empty");
			return;
		}
		Node current = sentinel;
		current.right = root;
		root.p = sentinel;
		Node nodeToDelete = null;
		
		while (!current.isLeaf()){
			current = (value < current.value) ? current.left : current.right;
			
			// Modify the tree based on the current node
			modifyTree(current);
			
			// If value is found
			if (value == current.value){
				nodeToDelete = current;
				break;
			}
			
		}
		
		// look for nodeToDelete's successor
		if (nodeToDelete != null){
			
			// if nodeToDelete is a leaf
			if (nodeToDelete.isLeaf()){
				// remove all pointers to the leaf node
				if (root == nodeToDelete) root = null;
				else {
					if (nodeToDelete == nodeToDelete.p.left) nodeToDelete.p.left = null;
					else nodeToDelete.p.right = null;
				}
			}
			
			// if nodeToDelete is non-leaf, look for its successor
			else {
				if (nodeToDelete.right == null){
					// successor is nodeToDelete.left
					nodeToDelete.value = nodeToDelete.left.value;
					nodeToDelete.left = nodeToDelete.left.left;
					if (nodeToDelete.left != null && nodeToDelete.left.left != null)
						nodeToDelete.left.left.p = nodeToDelete;
				}
				else {
					// find the leftmost node of the right subtree
					current = nodeToDelete.right;
					modifyTree(current);
					while (current.left != null){
						current = current.left;
						modifyTree(current);
					}
					
					// current.left is now null
					nodeToDelete.value = current.value;
					if (current == current.p.left)
						current.p.left = current.right;
					else 
						current.p.right = current.right;
					if (current.right != null){
						current.right.p = current.p;
						current.right.color = current.color;
					}

				}			
			}
		}
		
		// if nodeToDelete is null, print error message
		else System.out.println("Cannot find node to delete");
		
		// color the root black and delete sentinel root
		if (root != null) {
			root.color = Node.BLACK;
			root.p = null;
		}
	}
	
	/**
	 * Modify the current tree based on the current node (used in deletion).
	 * @param current the current node.
	 */
	private void modifyTree(Node current) {
		// if current.p is black and current is black, perform rotation
		if (getColor(current.p) == Node.BLACK && getColor(current) == Node.BLACK) {
			rotateWithBlackParent(current);
		}
		// If current is black and has 2 black children
		if (getColor(current) == Node.BLACK &&  
				getColor(current.left) == Node.BLACK && 
				getColor(current.right) == Node.BLACK)
		{
			Node sibling = (current == root) ? null : ((current == current.p.left) ? current.p.right : current.p.left);
			Node innerChild = null, outerChild = null;
			
			if (sibling != null){
				outerChild = (sibling == sibling.p.left) ? sibling.left : sibling.right;
				innerChild = (sibling == sibling.p.left) ? sibling.right : sibling.left;
			}

			// If sibling has 2 black children, color flip
			boolean siblingHasTwoBlackChildren = (sibling != null) &&
					getColor(sibling.left) == Node.BLACK && getColor(sibling.right) == Node.BLACK;
			if (sibling == null || siblingHasTwoBlackChildren) {
				// color flip
				current.p.color = Node.BLACK;
				current.color = Node.RED;
				if (sibling != null)
					sibling.color = Node.RED;
			}

			// If sibling has an outer red child
			else if (getColor(outerChild) == Node.RED) {
				changeColor(current.p, current, DELETION);
				if (outerChild == sibling.left)
					rotateRight(sibling.p);
				else
					rotateLeft(sibling.p);
			}

			// If sibling has an inner red child
			else if (getColor(innerChild) == Node.RED) {
				if (innerChild == sibling.left) {
					changeColor(sibling, innerChild, DELETION);
					rotateRight(sibling);
					changeColor(current.p, innerChild, false);
					rotateLeft(current.p);
				} else if (innerChild == sibling.right) {
					changeColor(sibling, innerChild, false);
					rotateLeft(sibling);
					changeColor(current.p, innerChild, false);
					rotateRight(current.p);
				}
			}
		}
	}
	
	private void rotateWithBlackParent(Node current){
		// rotate sibling and parent
		if (current == current.p.left) {
			current.p.color = Node.RED;
			current.p.right.color = Node.BLACK;
			rotateLeft(current.p);
		} else {
			current.p.color = Node.RED;
			current.p.left.color = Node.BLACK;
			rotateRight(current.p);
		}
	}
	
	/**
	 * Delete all nodes in the tree.
	 */
	public void deleteAll(){
		root = null;
	}
	
	/**
	 * Get the color of the specified node. 
	 * @param node the specified node.
	 * @return the node's color or <tt>BLACK</tt> if the node is <tt>NULL</tt>.
	 */
	private boolean getColor(Node node){
		// null leaf node is black
		return (node == null) ? Node.BLACK : node.color();
	}
	
	/**
	 * Get the root of the tree.
	 * @return the tree's root.
	 */
	public Node root() {
		return this.root;
	}
	
	/**
	 * Insert all elements in an array to the tree.
	 * @param a the array that contains all the elements to be inserted.
	 */
	private void insertElements(int[] a){
		for (int i : a) insert(i);
	}
	
	public static void main(String[] args){
		RBTree tree = new RBTree();
		tree.insertElements(new int[]{1,2,3,4,5});
		tree.delete(2);
		tree.root.traversal();
	}
	
	
}

/**
 * The red black tree class
 * @author HuyNguyen
 *
 */
public class RBTree {

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
			System.out.println("Duplicate value!");
			return;
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
		if (getColor(current.p) == Node.RED) rotation(current);
		
		// color the root Node.BLACK
		root.color = Node.BLACK;
	}
	
	/**
	 * Change the current node's color to <tt>RED</tt> and its two children's to <tt>BLACK</tt>.
	 * Then perform rotation if a violation occurs.
	 * @param current the current node
	 */
	public void colorFlipAndRotate(Node current){
		current.color = Node.RED;
		current.left.color = Node.BLACK;
		current.right.color = Node.BLACK;
		
		// if current node's parent is Node.RED, do rotation
		if (getColor(current.p) == Node.RED) rotation(current);
	}
	
	/**
	 * Perform rotation on the current node and its parent.
	 * @param current the current node
	 */
	public void rotation(Node current){
		//outside rotation
		if ((current.value < current.p.value) == (current.p.value < current.p.p.value)){
			if (current == current.p.left) rotateRight(current.p.p);
			else rotateLeft(current.p.p);
			//changeColorAfterRotationForInsertion(current.p.p, current.p);
		}
		//inside rotation
		else {
			//double rotation: rotate left first, then rotate right
			if (current == current.p.right && current.p == current.p.p.left) {
				rotateLeft(current.p);
				//changeColorAfterRotationForInsertion(current.p, current);
				rotateRight(current.p);
				//changeColorAfterRotationForInsertion(current.p, current);
			}
			//double rotation: rotate right first, then rotate left
			else {
				rotateRight(current.p);
				//changeColorAfterRotationForInsertion(current.p, current);
				rotateLeft(current.p);
				//changeColorAfterRotationForInsertion(current.p, current);
			}
		}
	}
	
	/**
	 * Perform right rotation on the parent and its left child, then change the color of the two.
	 * @param parent the parent node
	 */
	public void rotateRight(Node parent) {
        Node child = parent.left;
        parent.left = child.right;
        if (child.right != null) child.right.p = parent;
        child.p = parent.p;
        if (parent.p == null) root = child;
        else if (parent == parent.p.left) parent.p.left = child;
        else parent.p.right = child;
        child.right = parent;
        parent.p = child;
        changeColorAfterRotationForInsertion(parent, child);
        //child.color = child.right.color;

    }
	
	/**
	 * Perform right rotation on the parent and its left child, then change the color of the two.
	 * @param parent the parent node
	 */
	public void rotateLeft(Node parent) {
		Node child = parent.right;
        parent.right = child.left;
        if (child.left != null) child.left.p = parent;
        child.p = parent.p;
        if (parent.p == null) root = child;
        else if (parent == parent.p.right) parent.p.right = child;
        else parent.p.left = child;
        child.left = parent;
        parent.p = child;
        changeColorAfterRotationForInsertion(parent, child);
        //child.color = child.left.color;
    }
	
	public void changeColorAfterRotationForInsertion(Node parent, Node child){
		parent.color = Node.RED;
		child.color = Node.BLACK;
	}
	
	/*****************************************************
	 * 	============== DELETION TIPS ====================
	 * 
	 * Let X be current node, T be X's sibling, P be X's parent.
	 * As we traverse down the tree, we attempt to ensure that X is red.
	 * 
	 * Color the sentinel root red. This is to ensure that as X moves down, P will always be red.
	 * 
	 * If X is black and has 2 black children:
	 * 		If T has 2 black children, color flip.
	 * 		If T has an outer red child, single rotation.
	 * 		If T has an inner red child, double rotation.
	 * 		If T has 2 red children, single rotation.
	 * 
	 * If one of X's children is red:
	 * 		Go down one level (so new X is old X's child, new P is old X, new T is old X's other child)
	 * 		If new X is red:
	 * 			If X is leaf, see if it is the one to be deleted
	 * 			If X is not leaf, continue further down to X's appropriate child.
	 * 			
	 * 
	 * 
	 *****************************************************/
	
	public void delete(int value){
		Node current = root;
		Node parent;
		
		while (current != null && value != current.value){
			
		}
	}
	
	
	/**
	 * Get the color of the specified node. 
	 * @param node the specified node
	 * @return the node's color or <tt>BLACK</tt> if the node is <tt>NULL</tt>.
	 */
	public boolean getColor(Node node){
		return (node == null) ? Node.BLACK : node.color();
	}
	
	/**
	 * Get the root of the tree.
	 * @return the tree's root
	 */
	public Node root() {
		return this.root;
	}
	
	
}

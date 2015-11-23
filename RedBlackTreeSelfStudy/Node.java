
/**
 * The node of a red black tree.
 * @author HuyNguyen
 *
 */
public class Node {
	Node left, right;
	int value;
	boolean color;
	Node p;
	public static final boolean RED = true, BLACK = false;

	public Node(int value) {
		this.value = value;
		this.color = RED;
	}

	// getter methods
	public void setColor(boolean color) {
		this.color = color;
	}

	public void setLeftNode(Node left) {
		this.left = left;
	}

	public void setRightNode(Node right) {
		this.right = right;
	}

	public boolean color() {
		return this.color;
	}

	public Node parent() {
		return this.p;
	}

	public int value() {
		return this.value;
	}
	
	public boolean isLeaf() {
		return this.left == null && this.right == null;
	}
	
	public String toString(){
		return "" + this.value + " " + (this.color ? "RED" : "BLACK") + 
				" parent: " + ((this.p == null) ? "NULL" : this.p.value) ;
	}
	public void traversal(){
		System.out.println(this);
		if (this.left != null) this.left.traversal();
		if (this.right != null) this.right.traversal();
	}

	// Visitor interface (for visual presentation only)
	public interface Visitor {
		void visit(Node node);
	}

	public void traversePreorder(Node.Visitor visitor) {
		visitor.visit(this);
		if (left != null)
			left.traversePreorder(visitor);
		if (right != null)
			right.traversePreorder(visitor);
	}

	public void traversePostorder(Visitor visitor) {
		if (left != null)
			left.traversePostorder(visitor);
		if (right != null)
			right.traversePostorder(visitor);
		visitor.visit(this);
	}

	public void traverseInorder(Visitor visitor) {
		if (left != null)
			left.traverseInorder(visitor);
		visitor.visit(this);
		if (right != null)
			right.traverseInorder(visitor);
	}
}
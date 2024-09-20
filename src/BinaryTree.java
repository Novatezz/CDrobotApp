//Source:  http://www.newthinktank.com/2013/03/binary-tree-in-java/
// New Think Tank


public class BinaryTree {

	BNode root;
	public String list = "";

	public void addNode(int key, String name) {

		// Create a new Node and initialize it

		BNode newNode = new BNode(key, name);

		// If there is no root this becomes root

		if (root == null) {

			root = newNode;

		} else {

			// Set root as the Node we will start
			// with as we traverse the tree

			BNode focusNode = root;

			// Future parent for our new Node

			BNode parent;

			while (true) {

				// root is the top parent so we start
				// there

				parent = focusNode;

				// Check if the new node should go on
				// the left side of the parent node

				if (key < focusNode.key) {

					// Switch focus to the left child

					focusNode = focusNode.leftChild;

					// If the left child has no children

					if (focusNode == null) {

						// then place the new node on the left of it

						parent.leftChild = newNode;
						return; // All Done

					}

				} else { // If we get here put the node on the right

					focusNode = focusNode.rightChild;

					// If the right child has no children

					if (focusNode == null) {

						// then place the new node on the right of it

						parent.rightChild = newNode;
						return; // All Done

					}

				}

			}
		}

	}

	// All nodes are visited in ascending order
	// Recursion is used to go to one node and
	// then go to its child nodes and so forth

	public void inOrderTraverseTree(BNode focusNode) {

		if (focusNode != null) {

			// Traverse the left node

			inOrderTraverseTree(focusNode.leftChild);

			// Visit the currently focused on node

			//System.out.println(focusNode);
			list = list+ focusNode + "\n";

			// Traverse the right node

			inOrderTraverseTree(focusNode.rightChild);

		}

	}

	public void preorderTraverseTree(BNode focusNode) {

		if (focusNode != null) {

			//System.out.println(focusNode);
			list = list+ focusNode + "\n";

			preorderTraverseTree(focusNode.leftChild);
			preorderTraverseTree(focusNode.rightChild);

		}

	}

	public void postOrderTraverseTree(BNode focusNode) {

		if (focusNode != null) {

			postOrderTraverseTree(focusNode.leftChild);
			postOrderTraverseTree(focusNode.rightChild);

			//System.out.println(focusNode);
			list = list+ focusNode + "\n";

		}

	}

	public BNode findNode(int key) {

		// Start at the top of the tree

		BNode focusNode = root;

		// While we haven't found the Node
		// keep looking

		while (focusNode.key != key) {

			// If we should search to the left

			if (key < focusNode.key) {

				// Shift the focus Node to the left child

				focusNode = focusNode.leftChild;

			} else {

				// Shift the focus Node to the right child

				focusNode = focusNode.rightChild;

			}

			// The node wasn't found

			if (focusNode == null)
				return null;

		}

		return focusNode;

	}
}

class BNode {

	int key;
	String name;

	BNode leftChild;
	BNode rightChild;

	BNode(int key, String name) {

		this.key = key;
		this.name = name;

	}

	public String toString() {

		return "Title: "+ name + ", Barcode: " + key;

		/*
		 * return name + " has the key " + key + "\nLeft Child: " + leftChild +
		 * "\nRight Child: " + rightChild + "\n";
		 */

	}

}


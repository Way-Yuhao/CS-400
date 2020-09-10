/**
 * Filename:   AVLTree.java
 * Project:    p2
 * Authors:    Debra Deppeler, Yuhao Liu
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    002
 * 
 * Due Date:   specified in Canvas
 * Version:    1.0
 * 
 * Credits:    
 * 
 * Bugs:       no known bugs, but not complete either
 */

import java.lang.IllegalArgumentException;

/** 
 * This class contains an AVL balanced version of Binary Search Tree. The tree balances
 * itself immediately after each insert and delete attempts. A series of left/right rotations
 * are performed to complete each balancing attempt. As the result, the AVL should have a balance
 * factor between -1 and 1 after balancing is completed. To check if the the tree fits the standard
 * of an AVL or a BST, use checkForBinarySearchTree() and checkForBalancedTree() to get results.
 *  
 * @param <T>
 * 
 */
public class AVLTree<T extends Comparable<T>> implements AVLTreeADT<T> {

	/**
	 * This class contains individual nodes placed in the binary search tree.
	 * @param <K>
	 */
	class BSTNode<K> {
		private K key;	// the content stored in each node
		private int height;	// the number of edges on the longest path to the leaf
		private BSTNode<K> left, right;	//stores the reference to its children
		private int BF = 0; //balance factor, used to determine the balance of the node
		
		/**
		 * Constructor for a BST node.
		 * @param key
		 */
		BSTNode(K key) {
			this.key = key;
			height = 1;
			left = null;
			right = null;
		}
		
		/**
		 * Getter method that returns the key of the node.
		 * @return key
		 */
		public K getKey() {
			return this.key;
		}
		
		/**
		 * Getter method that returns the height of the node.
		 * @return height
		 */
		public int getHeight() {
			return this.height;
		}
		
		/**
		 * Getter method that returns the balance factor of the node.
		 * @return BF
		 */
		public int getBF() {
			return this.BF;
		}
		
		/**
		 * Getter method that returns the reference to its left child.
		 * @return left
		 */
		public BSTNode<K> getLeft() {
			return this.left;
		}
		
		/**
		 * Getter method that returns the reference to its right child.
		 * @return right
		 */
		public BSTNode<K> getRight() {
			return this.right;
		}
		
		/**
		 * Mutator that sets the value of the key
		 * @param Key
		 */
		public void setKey(K Key) {
			// this.key = key;
		}
		
		/**
		 * Mutator that sets the height of the node.
		 * @param height
		 */
		public void setHeight(int height) {
			this.height = height;
		}
		
		/**
		 * Mutator that sets the reference of its left child
		 * @param left
		 */
		public void setLeft(BSTNode<K> left) {
			this.left = left;
		}
		
		/**
		 * Mutator that sets the reference to its right child
		 * @param right
		 */
		public void setRight(BSTNode<K> right) {
			this.right = right;
		}
		
		/**
		 * Mutator that sets the balance factor
		 * @param BF
		 */
		public void setBF(int BF) {
			this.BF = BF;
		}
	}
	
	/* Fields */
	private BSTNode<T> root; //stores the reference to the root
	
	/**
	 * No-arg constructor that constructs a tree with no root reference.
	 */
	public AVLTree() {
		root = null;
		return;
	}
	
	/**
	 * Standard constructor that constructs an AVL tree with a root reference.
	 * @param root
	 */
	public AVLTree(BSTNode<T> root) {
		this.root = root;
		return;
	}
	
	/**
	 * Getter method that returns the height of a particular node.
	 * @param curNode
	 * @return
	 */
	private int height(BSTNode<T> curNode) {
		if (curNode == null) {
			return 0; //return 0 for a null reference
		}
		return curNode.height; //return height
	}
	
	/**
	 *  Checks if the AVL tree contains any node.
	 *  @return true if there are no nodes
	 */
	@Override
	public boolean isEmpty() {
		if (root == null) 
			return true; //return true if root is null
		else 
			return false;
	}
	
	/**
	 * Checks the balance factor at a particular node.
	 * @param curNode
	 * @return BF
	 */
	private int getBalance(BSTNode<T> curNode) {
		if (curNode == null) {
			return 0;
		} else {
			return (height(curNode.getLeft()) - height(curNode.getRight()));
		}
	}

	/**
	 * Insert a node with a particular key. Throws DuplicateKeyException if the value is a duplicate.
	 * @return
	 */
	@Override
	public void insert(T key) throws DuplicateKeyException, IllegalArgumentException {
		try {
			if (key == null)
				throw new IllegalArgumentException();
			else {
				this.root = recInsert(root, key);
			}
		} catch (ClassCastException e) {
			throw new IllegalArgumentException();
		} catch (DuplicateKeyException e) {
			throw e;
		}
	}
	
	/**
	 * Private helper method that recursively inserts node with value key under curNode.
	 * @param curNode
	 * @param key
	 * @return the root reference to the modified subtree following an insert
	 * @throws DuplicateKeyException
	 * @throws ClassCastException
	 */
	private BSTNode<T> recInsert(BSTNode<T> curNode, T key) throws DuplicateKeyException, ClassCastException{
		//1. Perform standard BST insertion
		if (curNode == null) {
			return (new BSTNode<T>(key));
		}
		if (key.compareTo(curNode.key) < 0) {
			curNode.setLeft(recInsert(curNode.getLeft(), key));
		} else if (key.compareTo(curNode.key) > 0) {
			curNode.setRight(recInsert(curNode.getRight(), key));
		} else {
			throw new DuplicateKeyException();
		}
		//2. Update height of ancestor node
		curNode.height = 1 + Math.max(height(curNode.getLeft()), height(curNode.getRight()));
		
		//3. Get the balance factor of this ancestor node
		int balance = getBalance(curNode);
		
		//4. Dealing with unbalanced cases
		//4.a Left Left
		if (balance > 1 && key.compareTo(curNode.getLeft().getKey()) < 0) 
			return rightRotate(curNode);
		//4.b Right Right
		if (balance < -1 && key.compareTo(curNode.getRight().getKey()) > 0)
			return leftRotate(curNode);
		//4.c Left Right
		if (balance > 1 && key.compareTo(curNode.getLeft().getKey()) > 0) {
			curNode.setLeft(leftRotate(curNode.getLeft()));
			return rightRotate(curNode);
		}
		//4.d Right Left
		if (balance < -1 && key.compareTo(curNode.getRight().getKey()) < 0) {
			curNode.setRight(rightRotate(curNode.getRight()));
			return leftRotate(curNode);
		}
		//if no rotations have taken place, return the original node at this level
		return curNode;
	}
	
	/**
	 * Private helper method that performs a left rotation
	 * @param x
	 * @return the root of this subtree
	 */
	private BSTNode<T> leftRotate(BSTNode<T> x) {
		BSTNode<T> y = x.getRight(); //y as parent
		BSTNode<T> T2 = y.getLeft(); //T2 as the left child of parent
		//perform rotation
		y.setLeft(x);
		x.setRight(T2);
		//update heights
		x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
		y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
		//return new root
		return y;
	}
	
	/**
	 * Private helper method that performs a right rotation
	 * @param y
	 * @return the root of this subtree
	 */
	private BSTNode<T> rightRotate(BSTNode<T> y) {
		BSTNode<T> x = y.getLeft(); // x as parent
		BSTNode<T> T2 = x.getRight(); //T2 as the right child of parent
		//perform rotation
		x.setRight(y);
		y.setLeft(T2);
		//update heights
		y.setHeight(Math.max(height(y.getLeft()), height(y.getRight())) + 1);
		x.setHeight(Math.max(height(x.getLeft()), height(x.getRight())) + 1);
		//return new root
		return x;
	}

	/**
	 * Deletes the node that matches a particular key. Throws IllegalArgumentException 
	 * if the key is null.
	 */
	@Override
	public void delete(T key) throws IllegalArgumentException {
		if (key == null)
			throw new IllegalArgumentException();
		else {
			updateGlobalData();
			this.root = recDelete(key, this.root);
		}
	}
	
	/**
	 * Private helper method that recursively attempts to insert at curNode
	 * @param parent
	 * @param curNode
	 * @param leftBranch
	 * @param key
	 */
	private BSTNode<T> recDelete(T key,BSTNode<T> curNode) {
	    if (curNode == null) {
	        return curNode;
	    }
	    // A. Delete the key if found
	    if (key.compareTo(curNode.key) < 0) { 
	        curNode.left = recDelete(key, curNode.left);
	    }
	    else if (key.compareTo(curNode.key) > 0) {
	        curNode.right = recDelete(key, curNode.right);
	    } 
	    else {  //if key matches with curNode
	        // A.1 if curNode has less than two children
	        if (curNode.left == null || curNode.right == null) {
	            BSTNode<T> temp = null;
	            if (temp == curNode.left) {
	                temp = curNode.right;
	            }
	            else {
	                temp = curNode.left;
	            }
	            // No children case
	            if (temp == null) {
	                temp = curNode;
	                curNode = null;
	            }
	            // One children case: replace node with children
	            else {
	                curNode = temp;
	            }
	        } else {
	            //A.2 if curNode has two children, replace it with in-order successor
	        	BSTNode<T> replace = curNode.getRight();
	        	while (replace.left != null){
	        		replace = replace.getLeft();
	    	    }
	            replace = new BSTNode<T>(replace.getKey());
	            curNode.key = replace.key;
	            curNode.right = recDelete(replace.key, curNode.right);
	            // After replacing node's information with in-order successor,
	            //delete it from its original spot
	        }
	        if (curNode == null) {
	            return curNode;
	        }
	    }
        //B. update height
        curNode.height = 1 + Math.max(height(curNode.left), height(curNode.right));
        //C. perform rotation if needed
        int balanceFactor = getBalance(curNode);
        // Again, if balanceFactor bigger than 1, left subtree is heavier
        // LeftRotate once
        if (balanceFactor < -1 && getBalance(curNode.right) <= 0) {
            return leftRotate(curNode);
        }
        // RightRotate once
        if (balanceFactor > 1 && getBalance(curNode.left) >= 0) {
            return rightRotate(curNode);
        }
        // Right-Left case
        if (balanceFactor < -1 && getBalance(curNode.right) > 0) {
            curNode.right = rightRotate(curNode.right);
            return leftRotate(curNode);
        }
        // Left-Right case
        if (balanceFactor > 1 && getBalance(curNode.left) < 0) {
            curNode.left = leftRotate(curNode.left);
            return rightRotate(curNode);
        } 

	    return curNode;
	}
	
	/**
	 * Searches the entire tree for a particular key. Throws IllegalArgumentException 
	 * if such key is null.
	 * @return true if the key is found.
	 */
	@Override
	public boolean search(T key) throws IllegalArgumentException {
		
		if (key == null)
			throw new IllegalArgumentException();
		else if (this.root == null) 
			return false;
		else {
			return recSearch(this.root, key);
		}
	}
	
	/**
	 * Private helper method that recursively searches for a key at curNode.
	 * @param curNode
	 * @param key
	 * @return true if the key exists in the AVL tree
	 */
	private boolean recSearch(BSTNode<T> curNode, T key) {
		if (key.compareTo(curNode.getKey()) == 0) {
			return true; //return true when found
		} else if (key.compareTo(curNode.getKey()) > 0) { 
			//key larger than current node
			if (curNode.getRight() == null)
				return false;
			else 
				return recSearch(curNode.getRight(), key);
		} else { //key smaller than current node
			if (curNode.getLeft() == null)
				return false;
			else 
				return recSearch(curNode.getLeft(), key);
		}
	}

	/**
	 * Perform an in-order traversal and prints the keys contained in every node
	 * found in this AVL tree.
	 * @return the string containing all keys
	 */
	@Override
	public String print() {
		return recPrint(this.root);
	}
	
	/**
	 * Private helper method that recursively appends the key at curNode to the final
	 * String to be returned.
	 * @param curNode
	 * @return a string representing all keys that have been traversed
	 */
	private String recPrint(BSTNode<T> curNode) {
		String str = ""; //message to be returned
		if (curNode == null) //will be true iff root is null
			return str;
		if (curNode.getLeft() != null)
			//in order traversal
			str += recPrint(curNode.getLeft());
			str += curNode.getKey() + " "; //append current key in place
		if (curNode.getRight() != null)
			str += recPrint(curNode.getRight());
		return str;
	}

	/**
	 * Checks if the entire tree is balanced.
	 * @return true if every node in this tree has an acceptable balance factor.
	 */
	@Override
	public boolean checkForBalancedTree() {
		updateGlobalData();
		if (this.root == null) {
			return true;
		}
		return recCheckBF(this.root);
	}
	
	/**
	 * Private helper method that recursively checks height at curNode.
	 * @param curNode
	 * @return the height of curNode
	 */
	private int recCalcHeight(BSTNode<T> curNode) {
		int leftChildHeight = 0, rightChildHeight = 0;
		//post order traversal
		if (curNode == null)
			return 0;
		if (curNode.getLeft() != null)
			leftChildHeight = recCalcHeight(curNode.getLeft());
		if (curNode.getRight() != null)
			rightChildHeight = recCalcHeight(curNode.getRight());
		curNode.setHeight(Math.max(leftChildHeight, rightChildHeight) + 1);
		curNode.setBF(leftChildHeight - rightChildHeight);
		return curNode.getHeight();	
	}
	
	/**
	 * Private helper method that recursively checks the balance factor at curNode.
	 * @param curNode
	 */
	private void recCalcBF(BSTNode<T> curNode) {
		if (curNode == null)
			return;
		curNode.setBF(height(curNode.getLeft()) - height(curNode.getRight()));
		recCalcBF(curNode.getLeft());
		recCalcBF(curNode.getRight());
	}
	
	/**
	 * Private helper method that recursively checks if curNode has an acceptable 
	 * balance factor
	 * @param curNode
	 * @return true if the balance factor is between -1 and 1, inclusive
	 */
	private boolean recCheckBF(BSTNode<T> curNode){
		if (Math.abs(curNode.getBF()) >= 2)
			return false;
		if (curNode.getLeft() != null)
			recCheckBF(curNode.getLeft());
		if (curNode.getRight() != null)
			recCheckBF(curNode.getRight());
		return true;
	}
	
	/**
	 * Private helper method that globally update height and BF.
	 */
	private void updateGlobalData() {
		recCalcHeight(this.root);
		recCalcBF(this.root);
	}

	/**
	 * Checks if the overall structure of the tree fits the constraints
	 * of a binary search tree.
	 * @return true if the structure holds
	 */
	@Override
	public boolean checkForBinarySearchTree() {
		return recCheckBST(root);
	}
	
	/**
	 * Private helper method that recursively checks if curNode is placed in 
	 * a position where its value is between its left and right child.
	 * @param curNode
	 * @return true if its key is between its left and right child
	 */
	private boolean recCheckBST(BSTNode<T> curNode) {
		if (curNode == null)
			return true; //base case
		//Post order traversal
		//checking on current node
		T parentData, leftChildData, rightChildData;
		parentData = curNode.getKey();
		leftChildData = curNode.getLeft() != null? curNode.getLeft().getKey() : null;
		rightChildData = curNode.getRight() !=  null? curNode.getRight().getKey() : null;
		if (leftChildData != null && leftChildData.compareTo(parentData) > 0)
			return false;
		if (rightChildData != null && rightChildData.compareTo(parentData) < 0)
			return false;
		//checking left child and then right child
		if (!recCheckBST(curNode.getLeft()) || !recCheckBST(curNode.getRight()))
			return false;
		else 
			return true;
	}
}
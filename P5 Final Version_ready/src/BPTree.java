package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implementation of a B+ tree to allow efficient access to many different
 * indexes of a large data set. BPTree objects are created for each type of
 * index needed by the program. BPTrees provide an efficient range search as
 * compared to other types of data structures due to the ability to perform
 * log_m N lookups and linear in-order traversals of the data items.
 *
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K>
 *            key - expect a string that is the type of id for each item
 * @param <V>
 *            value - expect a user-defined type that stores all data for a food
 *            item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

    // Root of the tree
    private Node root;

    // Branching factor is the number of children nodes
    // for internal nodes of the tree
    private int branchingFactor;

    /**
     * Public constructor
     *
     * @param branchingFactor
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
        }
        this.branchingFactor = branchingFactor; // assign branchingFactor to the class
        this.root = new LeafNode(); // set the root node to be an empty LeafNode
    }

    /**
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
     */
    @Override
    public void insert(K key, V value) {
        if (key != null) {
            Node newNode = root.insert(key, value);

            // Check if we need to create a new root
            if (newNode != null) {
                InternalNode newRoot = new InternalNode();// New root can only be InternalNodes
                newRoot.keys.add(newNode.keys.get(0));// Grab the first key in the node passed back

                // Assigning children to the new root
                newRoot.children.add(root);
                newRoot.children.add(newNode);

                // Check if the children is an InternalNode, if yes, then the appearance of the new root key should be
                // removed from the right child by just erasing the first element in the right child
                if (root instanceof BPTree.InternalNode) {
                    newRoot.children.get(1).keys.remove(0);
                }
                root = newRoot;
            }
        }
    }

    /**
     * This private helper method checks for the position that the key passed in should be in the node passed in.
     * @param node
     * @param key
     * @return index for diving downwards
     */
    private int findInsertIndex(InternalNode node, K key) {
        // Traverse through the key list to find its place
        for (int i = 0; i < node.keys.size(); i++) {
            if (key.compareTo(node.keys.get(i)) < 0) {
                return i;
            }
        }
        return node.keys.size(); // When the key is bigger than any of these keys currently
    }

    /**
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && !comparator.contentEquals("==") && !comparator.contentEquals("<=")) {
            return new ArrayList<V>(); // Case when the comparator isn't supported
        } else {
            return root.rangeSearch(key, comparator);
        }
    }

    /**
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        Queue<List<Node>> queue = new LinkedList<List<Node>>();
        queue.add(Arrays.asList(root));
        StringBuilder sb = new StringBuilder();
        while (!queue.isEmpty()) {
            Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
            while (!queue.isEmpty()) {
                List<Node> nodes = queue.remove();
                sb.append('{');
                Iterator<Node> it = nodes.iterator();
                while (it.hasNext()) {
                    Node node = it.next();
                    sb.append(node.toString());
                    if (it.hasNext())
                        sb.append(", ");
                    if (node instanceof BPTree.InternalNode)
                        nextQueue.add(((InternalNode) node).children);
                }
                sb.append('}');
                if (!queue.isEmpty())
                    sb.append(", ");
                else {
                    sb.append('\n');
                }
            }
            queue = nextQueue;
        }
        return sb.toString();
    }

    /**
     * This abstract class represents any type of node in the tree This class is a
     * super class of the LeafNode and InternalNode types.
     *
     * @author sapan
     */
    private abstract class Node {

        // List of keys
        List<K> keys;

        /**
         * Package constructor
         */
        Node() {
            keys = new ArrayList<K>(); // initializing the keys field
        }

        /**
         * Another constructor for creating an non-empty node
         * @param keys
         */
        Node(List<K> keys) {
            this.keys = keys;
        }

        /**
         * Inserts key and value in the appropriate leaf node and balances the tree if new node was popped up from
         * deeper level recursion, meaning we have split and new parent should be made.
         *
         * @param key
         * @param value
         * @return Node
         */
        abstract Node insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         *
         * @return key
         */
        abstract K getFirstLeafKey();

        /**
         * Gets the new sibling created after splitting the node
         *
         * @return Node
         */
        abstract Node split();

        /**
         * (non-Javadoc)
         *
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         *
         * @return boolean
         */
        abstract boolean isOverflow();

        public String toString() {
            return keys.toString();
        }

    } // End of abstract class Node

    /**
     * This class represents an internal node of the tree. This class is a concrete
     * sub class of the abstract Node class and provides implementation of the
     * operations required for internal (non-leaf) nodes.
     *
     * @author sapan
     */
    private class InternalNode extends Node {

        // List of children nodes
        List<Node> children;

        InternalNode previous;
        InternalNode next;

        /**
         * Package constructor
         */
        InternalNode() {
            super();
            this.children = new ArrayList<Node>();
            this.previous = null;
            this.next = null;
        }

        /**
         * Overloaded constructor for use.
         *
         * @param keys
         * @param children
         */
        InternalNode(List<K> keys, List<Node> children) {
            super(keys);
            this.children = children;
            this.previous = null;
            this.next = null;
        }

        /**
         * (non-Javadoc)
         *
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return children.get(0).getFirstLeafKey(); // Keep traversing till we reach leaf level
        }

        /**
         * (non-Javadoc)
         *
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
            return keys.size() == branchingFactor;
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        Node insert(K key, V value) {
            InternalNode indicator = null; // This is the node we return for this level of recursion.
            int guideIndex = findInsertIndex(key); // The index indicator for us to dive downwards

            // Insert downwards
            Node newNode = children.get(guideIndex).insert(key, value);


            if (newNode != null) { // Check if we have split, if yes, we need to handle the split

                // Step 1: add the information of the split node into the current node
                keys.add(guideIndex, newNode.keys.get(0));
                children.add(guideIndex + 1, newNode);


                // Step 2: We make sure the node passed back to this level of recursion is an InternalNode, since if it
                // is a LeafNode, we should't remove the key's appearance
                if (newNode instanceof BPTree.InternalNode) {
                    newNode.keys.remove(0); // That is because the key at index 0 will be in the upper level
                }


                // Step 3: Check if the current level of node is overflow. If overflow, split.
                if (isOverflow()) {
                    // Step 3.a: make a copy of the right part of current node
                    indicator = (BPTree<K, V>.InternalNode) split();

                    // Step 3.b: renew the current node's keys and children to the left part
                    ArrayList<K> newKeys = new ArrayList<K>();
                    ArrayList<Node> newChildren = new ArrayList<Node>();
                    newKeys.addAll(keys.subList(0, keys.size() / 2));
                    newChildren.addAll(children.subList(0, children.size() / 2));
                    keys = newKeys;
                    children = newChildren;

                    // Step 3.c: renew next and previous references
                    indicator.next = next;
                    indicator.previous = this;
                    if (next != null) { // Case when the current node wasn't the last node in the current level
                        next.previous = indicator;
                    }
                    next = indicator;
                }
            }
            return indicator;
        }

        /**
         * This is the InternalNode's findInsertIndex method. It calls the BPTree's findInsertIndex by passing the
         * current node in for flexible use.
         * @param key
         * @return int
         */
        private int findInsertIndex(K key) {
            return BPTree.this.findInsertIndex(this, key);
        }

        /**
         * (non-Javadoc)
         *
         * @see BPTree.Node#split()
         */
        Node split() {
            // Returns the right part of the current Node as split node
            InternalNode splitNode = new InternalNode();
            splitNode.keys.addAll(keys.subList(keys.size() / 2, keys.size()));
            splitNode.children.addAll(children.subList(children.size() / 2, children.size()));
            return splitNode;
        }

        /**
         * (non-Javadoc)
         *
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
            return children.get(0).rangeSearch(key, comparator);
            // Dive down to the left-most node
        }

    } // End of class InternalNode

    /**
     * This class represents a leaf node of the tree. This class is a concrete sub
     * class of the abstract Node class and provides implementation of the
     * operations that required for leaf nodes.
     *
     * @author sapan
     */
    private class LeafNode extends Node {

        // List of values
        List<V> values;

        // Reference to the next leaf node
        LeafNode next;

        // Reference to the previous leaf node
        LeafNode previous;

        /**
         * Package constructor
         */
        LeafNode() {
            super();
            this.values = new ArrayList<V>();
            this.next = null;
            this.previous = null;
        }

        /**
         * Overloaded constructor for use.
         *
         * @param keys
         * @param values
         * @param next
         * @param previous
         */
        LeafNode(List<K> keys, List<V> values, LeafNode next, LeafNode previous) {
            super(keys);
            this.values = values;
            this.next = next;
            this.previous = previous;
        }

        /**
         * This method returns the first key in the first LeafNode. It keeps going to
         * the previous node until it reaches the first node in the bottom level, then
         * returns the first key.
         *
         * @return the first key in the last level
         */
        K getFirstLeafKey() {
            if (previous != null) {
                return previous.getFirstLeafKey(); // Keep going forward until we reach the first leafnode
            }
            return keys.get(0); // Case when we reach the first LeafNode
        }

        /**
         * This method checks if the current LeafNode exceeds the maximum number of keys
         * assigned
         *
         * @return true if the LeafNode exceeds maximum capacity
         */
        boolean isOverflow() {
            return values.size() == branchingFactor; // Check if the LeafNode's content exceeds limit
        }

        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        Node insert(K key, V value) {

            // Temporary node for checking
            LeafNode newLeaf = null;

            // Step 1: find where to insert the key-value pair
            int insertIndex = 0;
            while (insertIndex < keys.size()) {
                if (key.compareTo(keys.get(insertIndex)) <= 0) {
                    break;
                }
                insertIndex++;
            }

            // Step 2: Insert the new key and value at the found index.
            keys.add(insertIndex, key);
            values.add(insertIndex, value);

            // Step 3: Check if we need to split
            if (isOverflow()) {
                // Step 3.a: Using split to create a copy of the current node's right part
                newLeaf = (BPTree<K, V>.LeafNode) split();

                // Step 3.b: Creating copy of left part for current node's field to renew
                ArrayList<K> newKeys = new ArrayList<K>();
                ArrayList<V> newValues = new ArrayList<V>();
                newKeys.addAll(keys.subList(0, keys.size() / 2));
                newValues.addAll(values.subList(0, values.size() / 2));

                // Step 3.c: Renewing current node's field
                keys = newKeys;
                values = newValues;
                newLeaf.next = next;
                newLeaf.previous = this;

                if (next != null) { // case when the current node has next
                    next.previous = newLeaf;
                }
                next = newLeaf;
            }
            return newLeaf;

        }

        /**
         * This method returns the right part of current LeafNode as a new LeafNode for
         * splitting the last level of the BPTree
         *
         * @return new LeafNode
         */
        Node split() {
            LeafNode newLeaf = new LeafNode();
            newLeaf.keys.addAll(keys.subList(keys.size() / 2, keys.size()));
            newLeaf.values.addAll(values.subList(values.size() / 2, values.size()));
            return newLeaf;
            // Sublist is startIndex (inclusive) to endIndex (exclusive), so we use keys.size();
        }

        /**
         * (non-Javadoc)
         *
         * @see BPTree.Node#rangeSearch(Comparable, String)
         */
        List<V> rangeSearch(K key, String comparator) {
            ArrayList<V> inRange = new ArrayList<V>();

            // Case 1: ==
            if (comparator.contentEquals("==")) {
                if (keys.contains(key)) {
                    // Add all keys in the current node that matches
                    for (int i = 0; i < keys.size(); i++) {
                        if (key.compareTo(keys.get(i)) == 0) {
                            inRange.add(values.get(i));
                        }
                    }
                }
                // If there are any key in the next, recursively add them
                if (next != null) {
                    inRange.addAll(next.rangeSearch(key, comparator));
                } else { // That is when we reach the end, return an empty list for concatenation
                    return inRange;
                }

            }
            // Case 2: >=
            if (comparator.contentEquals(">=")) {
                if (keys.get(keys.size() - 1).compareTo(key) >= 0) {
                    // See if the largest value in the current node is bigger than the key passed in
                    // If yes, that means there must be at least one key in the current node that meets out criteria
                    // If no, jump this block and check next node recursively
                    for (int i = 0; i < keys.size(); i++) {
                        if (keys.get(i).compareTo(key) >= 0) {
                            inRange.add(values.get(i));
                        }
                    }
                }
                if (next != null) {
                    inRange.addAll(next.rangeSearch(key, comparator));
                } else {
                    return inRange;
                }
            }
            // Case 3: <=
            if (comparator.contentEquals("<=")){
                if (keys.get(0).compareTo(key) <= 0) {
                    // See if the smallest value in the current node is smaller than the key passed in
                    // If yes, that means there must be at least one key in the current node that meets our criteria
                    // If no, jump this block and check previous node recursively
                    for (int i = 0; i < keys.size(); i++) {
                        if (keys.get(i).compareTo(key) <= 0) {
                            inRange.add(values.get(i));
                        }
                    }

                }
                if (next != null) {
                    inRange.addAll(next.rangeSearch(key, comparator));
                } else {
                    return inRange;
                }
            }
            return inRange;

        }





    } // End of class LeafNode

    /**
     * Contains a basic test scenario for a BPTree instance. It shows a simple
     * example of the use of this class and its related types.
     *
     * @param args
     */
//	public static void main(String[] args) {
//		// create empty BPTree with branching factor of 3
//		BPTree<Double, Double> bpTree = new BPTree<>(3);
//
//		// create a pseudo random number generator
//		Random rnd1 = new Random();
//
//		// some value to add to the BPTree
//		Double[] dd = { 0.0d, 0.5d, 0.2d, 0.8d };
//
//		// build an ArrayList of those value and add to BPTree also
//		// allows for comparing the contents of the ArrayList
//		// against the contents and functionality of the BPTree
//		// does not ensure BPTree is implemented correctly
//		// just that it functions as a data structure with
//		// insert, rangeSearch, and toString() working.
//		List<Double> list = new ArrayList<>();
//		for (int i = 0; i < 100; i++) {
//			Double j = dd[rnd1.nextInt(4)];
//			list.add(j);
//			bpTree.insert(j, j);
//			System.out.println("\n\nTree structure:\n" + bpTree.toString());
//		}
//		List<Double> filteredValues = bpTree.rangeSearch(0.2d, "<=");
//		System.out.println("Filtered values: " + filteredValues.toString());
//	}
    public static void main(String[] args) {
        BPTree<Integer, Integer> bpTree = new BPTree<>(3);

        // Random rnd1 = new Random();



        bpTree.insert(0, 100);
        bpTree.insert(2, 102);
        bpTree.insert(5, 106);
        bpTree.insert(7, 107);
        bpTree.insert(3, 103);
        bpTree.insert(4, 104);
        bpTree.insert(5, 105);
        bpTree.insert(7, 108);
        bpTree.insert(7, 109);
        bpTree.insert(10, 110);
        bpTree.insert(1, 101);

        System.out.println("\n\nTree structure:\n" + bpTree.toString());
        System.out.println(bpTree.root);
        System.out.println(bpTree.root.getFirstLeafKey());
        List<Integer> filtered = bpTree.rangeSearch(11, ">=");
        System.out.println("Filtered values: " + filtered.toString());
        // System.out.println(bpTree.root);
    }
} // End of class BPTree
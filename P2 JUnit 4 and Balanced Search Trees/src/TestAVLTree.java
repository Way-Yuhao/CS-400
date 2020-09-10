/**
 * Filename:   TestAVLTree.java
 * Project:    p2
 * Authors:    Debra Deppeler, Yuhao Liu
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:   002
 * 
 * Due Date:   as specified in Canvas
 * Version:    1.0
 * 
 * Credits:    
 * 
 * Bugs:       no known bugs, but not complete either
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import java.lang.IllegalArgumentException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


/**
 * This class runs numerous tests on AVLTrees. Some notable tests including testing 
 * correct operations on empty trees, inserting and deleting one item, inserting and
 * deleting multiple items, testing if the tree as an arbitrary capacity limit, etc.
 * @author LiuYuhao
 *
 */
public class TestAVLTree {
	private AVLTree<Integer> tree;
	
	/**
	 * This method creates a new instance of AVL tree prior to every test case.
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		tree = new AVLTree<Integer>();
	}
	
	/**
	 * This method points the tree reference to null following every test case.
	 * @throws Exception
	 */
	@After
	public void tearDown() throws Exception {
		tree = null;
	}
	
	
	/**
	 * Tests that an AVLTree is empty upon initialization.
	 */
	@Test
	public void test01isEmpty() {
		assertTrue("test01isEmpty Failed: isEmpty() returned true on an empty tree.", tree.isEmpty());
	}

	/**
	 * Tests that an AVLTree is not empty after adding a node.
	 */
	@Test
	public void test02isNotEmpty() {
		try {
			tree.insert(1);
			assertFalse("test02isNotEmpty Failed: isEmpty() returned false on a loaded tree.", tree.isEmpty());
		} catch (DuplicateKeyException e) {
			System.out.println(e.getMessage());
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}
	}
	
	/**
	 * Tests functionality of a single delete following several inserts.
	 */
	@Test
	public void test03insertManyDeleteOne() {
		try {
			tree.insert(2);
			tree.insert(1);
			tree.insert(3);
			tree.insert(4);
			tree.delete(3); //delete node with key 3
			//test if element is successfully deleted
			assertFalse("test03insertManyDeleteOne Failed: unable to delelte treeNode with key '3', "
					+ "expected successful deletion.", tree.search(3));
			//test if the subtree under the node delete remains 
			assertTrue("test03insertManyDeleteOne Failed: unable to delelte treeNode with key '4', "
					+ "expected successful deletion.", tree.search(4));
		} catch (Exception e ){
			fail("test03insertManyDeleteOne Failed: An unexpected exception is caught when running test03insertManyDeleteOne.");
		}
	}
	
	/**
	 * Tests functionality of many deletes following several inserts.
	 */
	@Test
	public void test04insertManyDeleteMany() {
		try {
			//insert integers from 1 to 10
			for (int key = 1; key <= 10; key++) {
				tree.insert(key);
			}
			//delete each key 
			for (int key = 10; key > 0; key--) {
				tree.delete(key);
				assertFalse("test04insertManyDeleteMany Failed: unable to delete " + key, tree.search(key));
				//check whether the tree maintains its balance and BST structure after each delete attempt
				assertTrue("test04insertManyDeleteMnay Failed: checkforBalancedTree() returned false after deleting keys, expected true.", tree.checkForBalancedTree());
				assertTrue("test04insertManyDeleteMnay Failed: checkForBinarySearchTree() returned false after deleting keys, expected true", tree.checkForBinarySearchTree());
			}
		} catch(Exception e) {
			fail("test04insertManyDeleteMany Failed: unexpectedly threw " + e.getClass().getName() + "when attempting to delte keys.");
		}
	}
	
	/**
	 * Tests the functionality of searching on an empty tree
	 */
	@Test
	public void test05searchEmptyTree() {
		try {
			//should return false when searching on an empty tree
			assertFalse("test05searchEmptyTree Failed: search() returned true on an empty tree; expected false", tree.search(1));
		} catch (Exception e) {
			//fail the test if any exceptions are thrown
			fail("test05searchEmptyTree Failed: search() unexpectedly threw " + e.getClass().getName() + " when running search() on an empty tree.");
		}
	}
	
	/**
	 * Test the functionality of preventing duplicated keys from being inserted.
	 */
	@Test 
	public void test06insertDuplicates() {
		try {
			//multiple attempts of inserting duplicate keys
			tree.insert(1);
			tree.insert(1);
			tree.insert(1);
			tree.insert(1);
			tree.delete(1); //should be empty after deleting this key
			assertFalse("test06insertDuplicate Failed: duplicate keys are incorrectly inserted; only one key with the same value"
					+ " is expected to be inserted", tree.isEmpty());
		} catch (DuplicateKeyException e ) {
			//passes the test; do nothing
		} catch (Exception e) {
			//fail the test if any other forms of exceptions are thrown
			fail("test06insertDuplicate Failed: unexpectedly thre " + e.getClass().getName() + " when inserting multiple duplicated "
					+ "keys and deleting one.");
		}
	}
	
	/**
	 * Tests if the AVLTree has an arbitrary capacity limit
	 */
	@Test
	public void test07dataLimit() {
		final int MIDPOINT = 5000; //half of the maximum capacity tested
		try {
			tree.insert(MIDPOINT);
			//insert (MIDPOINT * 2) treeNodes
			for (int offset = 1; offset <= 5000; offset++) {
				tree.insert(MIDPOINT + offset);
				tree.insert(MIDPOINT - offset);
				//assert if new nodes are successfully inserted
				assertTrue("test07dataLimit FAILED: unable to insert key after its capacity has reached " + (offset * 2), tree.search(MIDPOINT + offset));
				assertTrue("test07dataLimit FAILED: unable to insert key after its capacity has reached " + (offset * 2 + 1), tree.search(MIDPOINT - offset));
				//assert if the balance and BST structure is maintained
				assertTrue("test07dataLimit FAILED: ALV tree failed to maintain balance after its capacity has reached " + (offset * 2 + 1), tree.checkForBalancedTree());
				assertTrue("test07dataLimit FAILED: ALV tree failed to maintain BST structure after its capacity has reached " + (offset * 2 + 1),tree.checkForBinarySearchTree());
			}
		} catch (Exception e) {
			//fail the test if exceptions are thrown
			fail("test07dataLimit Failed: unexpectedly threw " + e.getClass().getName() + " when inserting " + (MIDPOINT * 2)
					+ "keys .");
		}
	}
	
	/**
	 * Tests if the string of keys returned by print() follows an ascending order
	 */
	@Test
	public void test08ascendingOrder() {
		//insert 50 keys
		try {
			for (int key = 1; key <= 50; key ++) {
				tree.insert(key);
			}
			String output = tree.print().trim();
			String[] keyArr = output.split(" ");
			int curMax = 0; //stores that max key traversed
			for (String curKey: keyArr) {
				if (Integer.parseInt(curKey) < curMax) {
					//if the current key is smaller than any key that has been traversed, fail the test
					fail("test08ascendingOrder Failed: print() returned \"" + output + "\", which is not "
							+ "in ascending order; expected each key to be larger than those preceding it.");
				} else {
					curMax = Integer.parseInt(curKey); //update max key
				}
			}
		} catch (Exception e) {
			//fail the test if exceptions are thrown
			fail("test08ascendingOrder Failed: unexpectedly threw " + e.getClass().getName() + " when testing print() method.");
		}
	}
	
	/**
	 * Tests the functionality of series of insert-remove pairs
	 */
	@Test
	public void test09insertDeleteInsert() {
		try {
			//insert 3 nodes and then delete all
			tree.insert(1);
			tree.insert(2);
			tree.insert(3);
			tree.delete(3);
			tree.delete(2);
			tree.delete(1);
			//should have an empty tree at this point
			assertTrue("test09insertDelteInsert Failed: isEmpty() returned false after deleting all three keys "
					+ "that were previously inserted; expected true", tree.isEmpty());
			//insert another 3 nodes and delete all
			tree.insert(4);
			tree.insert(5);
			tree.insert(6);
			tree.delete(5);
			tree.delete(6);
			tree.delete(4);
			//assert tree's emptiness
			assertTrue("test09insertDelteInsert Failed: isEmpty() returned false after deleting all three keys "
					+ "that were previously inserted; expected true.", tree.isEmpty());
		} catch (Exception e) {
			//fail the test if any exceptions are caught
			fail("test09insertDelteInsert Failed: unexpectedly threw " + e.getClass().getName() + " when inserting "
					+ "and deleting multiple keys.");
		}
	}
	
	/**
	 * Tests the functionality of running delete() on an empty tree
	 */
	@Test
	public void test10deleteEmpty() {
		try {
			//should not generate any exceptions when running delete() on an empty tree
			tree.delete(1);
		} catch (Exception e) {
			//fail the test if any exceptions are thrown
			fail("test10deleteEmpty Failed: search() unexpectedly threw " + e.getClass().getName() + " when running delete() on an empty tree.");
		}
	}
	
	/**
	 * Tests the if the AVLTree correctly handles inserting null.
	 */
	@Test
	public void test11insertNull() {
		try {
			// insert null
			tree.insert(null);
		} catch (IllegalArgumentException e) { //expected to throw IllegalArgumentException
			//passes the test
		} catch (Exception e) {
			//fail the test if any exceptions are thrown
			fail("test11insertNull Failed: insert() unexpectedly threw " + e.getClass().getName() + " when "
					+ "inserting null; expected an IllegalArgumentException.");
		}
	}
}
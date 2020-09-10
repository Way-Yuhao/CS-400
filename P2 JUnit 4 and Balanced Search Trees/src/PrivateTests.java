import java.lang.IllegalArgumentException;
import java.lang.Integer;
import java.lang.String;
import java.lang.System;

public class PrivateTests {

    public static void main(String[] args) {
        System.out.println("test00 insert: " + testInsert());
        System.out.println("test01 delete: " + testDelete());
       // System.out.println("test02 CheckBalance: " + testCheckBalance());
        //System.out.println("test03 CheckBST: " + testCheckBST());
        System.out.println("test04 insertDuplicate: " + testInsertDuplicate());
        System.out.println("test05 IllegalArgumentExceptionInsert: " + testIllegalArgumentInsert());
        System.out.println("test06 IllegalArgumentExceptionDelete: " + testIllegalArgumentDelete());
        System.out.println("test07 search: " + testSearch());

    }
    private static boolean testInsert() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        try{
            tree.insert(1);
            tree.insert(2);
            tree.insert(5);
            tree.insert(4);
            tree.insert(3);
            tree.insert(6);
            tree.insert(7);
            if (!tree.isEmpty()) {
                System.out.println("    " + tree.print());
                return true;
            }
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return false;
    }
    private static boolean testDelete() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        try {
            tree.insert(1);
            tree.delete(1);
            //System.out.println("    " + tree.print());
            if (tree.isEmpty()) {
                return true;
            }
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return false;
    }
    private static boolean testCheckBalance() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        AVLTree<Integer> tree2 = new AVLTree<Integer>();
        try {
            tree.insert(1);
            tree.insert(2);
            tree.insert(5);
            tree.insert(4);
            tree.insert(3);
            tree.insert(6);
            tree.insert(7);
            if (!tree.checkForBalancedTree()) {
                return false;
            }
            tree2.insert(1);
            //tree2.rootGetter();
            System.out.println("    " + tree2.print());
            if (tree2.checkForBalancedTree()) {
                return false;
            }
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return true;
    }
    private static boolean testCheckBST() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        AVLTree<Integer> tree2 = new AVLTree<Integer>();
        try {
            tree.insert(1);
            tree.insert(2);
            tree.insert(5);
            tree.insert(4);
            tree.insert(3);
            tree.insert(6);
            tree.insert(7);
            if (!tree.checkForBinarySearchTree()) {
                return false;
            }
            tree2.insert(2);
            //tree2.notBSTSetter();
            if (tree2.checkForBinarySearchTree()) {
                return false;
            }
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return true;
    }
    private static boolean testInsertDuplicate() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        try {
            tree.insert(1);
            tree.insert(1);
        } catch (DuplicateKeyException e) {
            System.out.println("    After inserting duplicate, it fails, and the tree content is: " + tree.print());
            return true;
        }
        return false;
    }
    private static boolean testIllegalArgumentInsert() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        try {
            tree.insert(null);
            
        } catch (IllegalArgumentException e) {
            return true;
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return false;
    }
    private static boolean testIllegalArgumentDelete() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        try {
            tree.insert(1);
            tree.delete(null);
            
        } catch (IllegalArgumentException e) {
            return true;
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return false;
    }
    private static boolean testSearch() {
        AVLTree<Integer> tree = new AVLTree<Integer>();
        try {
            tree.insert(1);
            tree.insert(2);
            tree.insert(5);
            tree.insert(4);
            tree.insert(3);
            tree.insert(6);
            tree.insert(7);
            if (!tree.search(4)) {
                return false;
            }
            if (tree.search(8)) {
                return false;
            }
        } catch (DuplicateKeyException e) {
            System.out.println("WTF");
        }
        return true;
    }
// Use the following to test test02, test03
//    public void rootGetter(){
//        root.left = null;
//        root.right = (AVLTree<K>.BSTNode<K>) new BSTNode<Integer>(2);
//        root.right.height = 2;
//        root.right.right = (AVLTree<K>.BSTNode<K>) new BSTNode<Integer>(3);
//        root.height = 3;
//        return;
//    }
//    public void notBSTSetter() {
//        root.left = (AVLTree<K>.BSTNode<K>) new BSTNode<Integer>(3);
//        root.right = (AVLTree<K>.BSTNode<K>) new BSTNode<Integer>(1);
//        root.height = 2;
//    }
}

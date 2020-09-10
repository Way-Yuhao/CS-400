
/**
 * Filename:   TestPQ.java
 * Project:    p1TestPQ
 * Authors:    Debra Deppeler, Yuhao Liu
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * Lecture:    001
 * 
 * Note: Warnings are suppressed on methods that construct new instances of 
 * generic PriorityQueue types.  The exceptions that occur are caught and 
 * such types are not able to be tested with this program.
 * 
 * Due Date:   Before 10pm on September 17, 2018
 * Version:    2.0
 * 
 * Credits:    
 * 
 * Bugs:       no known bugs
 */


import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.lang.NullPointerException;

/**
 * Runs black-box unit tests on the priority queue implementations
 * passed in as command-line arguments (CLA).
 * 
 * If a class with the specified class name does not exist 
 * or does not implement the PriorityQueueADT interface,
 * the class name is reported as unable to test.
 * 
 * If the class exists, but does not have a default constructor,
 * it will also be reported as unable to test.
 * 
 * If the class exists and implements PriorityQueueADT, 
 * and has a default constructor, the tests will be run.  
 * 
 * Successful tests will be reported as passed.
 * 
 * Unsuccessful tests will include:
 *     input, expected output, and actual output
 *     
 * Example Output:
 * Testing priority queue class: PQ01
 *    5 PASSED
 *    0 FAILED
 *    5 TOTAL TESTS RUN
 * Testing priority queue class: PQ02
 *    FAILED test00isEmpty: unexpectedly threw java.lang.NullPointerException
 *    FAILED test04insertRemoveMany: unexpectedly threw java.lang.ArrayIndexOutOfBoundsException
 *    3 PASSED
 *    2 FAILED
 *    5 TOTAL TESTS RUN
 * 
 *   ... more test results here
 * 
 * @author deppeler
 */
public class TestPQ {

	// set to true to see call stack trace for exceptions
	private static final boolean DEBUG = false;

	/**
	 * Run tests to determine if each Priority Queue implementation
	 * works as specified. User names the Priority Queue types to test.
	 * If there are no command-line arguments, nothing will be tested.
	 * 
	 * @param args names of PriorityQueueADT implementation class types 
	 * to be tested.
	 */
	public static void main(String[] args) {
		for (int i=0; i < args.length; i++) 
			test(args[i]);

		if ( args.length < 1 ) 
			print("no PQs to test");
	}

	/** 
	 * Run all tests on each priority queue type that is passed as a classname.
	 * 
	 * If constructing the priority queue in the first test causes exceptions, 
	 * then no other tests are run.
	 * 
	 * @param className the name of the class that contains the 
	 * priority queue implementation.
	 */
	private static void test(String className) {
		print("Testing priority queue class: "+className);
		int passCount = 0;
		int failCount = 0;
		try {
			//call each test on each PriorityQueue
			if (test00isEmpty(className)) passCount++; else failCount++;		
			if (test01getMaxEXCEPTION(className)) passCount++; else failCount++;
			if (test02removeMaxEXCEPTION(className)) passCount++; else failCount++;
			if (test03insertRemoveOne(className)) passCount++; else failCount++;
			if (test04insertRemoveMany(className)) passCount++; else failCount++;
			if (test05duplicatesAllowed(className)) passCount++; else failCount++;
			if (test06manyDataItems(className)) passCount++; else failCount++;
			if (test07getMaxRemovesNone(className)) passCount++; else failCount++;
			if (test08insertNull(className)) passCount++; else failCount++;
			if (test09getMaxAndRemoveMax(className)) passCount++; else failCount++;
			String passMsg = String.format("%4d PASSED", passCount);
			String failMsg = String.format("%4d FAILED", failCount);
			print(passMsg);
			print(failMsg);
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
			if (DEBUG) e.printStackTrace();
			print(className + " FAIL: Unable to construct instance of " + className);
		} finally {
			String msg = String.format("%4d TOTAL TESTS RUN", passCount+failCount);
			print(msg);
		}
	}

	/**
	 * Confirm that getMax() and removeMax() return the same item
	 * @param className
	 * @return true if getMax() and removeMax() return the same item
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test09getMaxAndRemoveMax(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //stores all PQs
		Comparable [] keys = {new Integer(-1), new Double(-22.45124), "ASampleString234!"}; //items to be inserted
		// construct PQs of type Integer, Double, and String
		PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
		PriorityQueueADT<Double> pqDou = newDoublePQ(className);
		PriorityQueueADT<String> pqStr = newStringPQ(className);
		PQs[0] = pqInt;
		PQs[1] = pqDou;
		PQs[2] = pqStr;
		Comparable getTemp, removeTemp; //used to store values returned by getMax() and removeMax()
		try {
			for (int i = 0; i <= 2; i++) {
				//insert one item for each PQ
				PQs[i].insert(keys[i]);
				getTemp = PQs[i].getMax();
				removeTemp = PQs[i].removeMax();
				//if the value returned from getMax() and removeMax does not match, or either function 
				//incorrectly returns null, throw an exception to indicate a failure
				if (getTemp == null || removeTemp == null)
					throw new NullPointerException("FAILED test09getMaxAndRemoveMax: getMax() or removeMax() returned null when "
							+ keys[i] + " has been inserted. Both return values are expected  to be " + keys[i] + ".");
				if (!getTemp.equals(removeTemp))
					throw new InputMismatchException("FAILED test09getMaxAndRemoveMax: when " + keys[i] + "has been inserted,"
							+ " getMax() returned " + getTemp + ", while a subsequent removeMax() returned a different value: " + removeTemp 
							+ ". Both return values are expected to be " + keys[i]);
			}
		return true;
		} catch (NullPointerException e) {
			if (DEBUG) e.printStackTrace();
			print(e.getMessage());
		}catch (InputMismatchException e) {
			if (DEBUG) e.printStackTrace();
			print(e.getMessage());
		}
		return false;
	}
	
	/**
	 * Confirm that when attempting to insert null, PQ can either throw an relevant exception 
	 * or prevent null(s) from being inserted.
	 * @param className
	 * @return true if null(s) are not inserted
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test08insertNull(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //stores all PQs
		// construct PQs of type Integer, Double, and String
		PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
		PriorityQueueADT<Double> pqDou = newDoublePQ(className);
		PriorityQueueADT<String> pqStr = newStringPQ(className);
		PQs[0] = pqInt;
		PQs[1] = pqDou;
		PQs[2] = pqStr;
		int i = 0, j = 0; //for-loop indexes
		try {
			for (i = 0; i <= 2; i++) {
				for (j = 0; j <= 6; j++) {
					//six attempts of inserting null
					PQs[i].insert(null);
				}
				if (PQs[i] == null) throw new Exception();
				if (!PQs[i].isEmpty())
					//if PQ is NOT empty, throw exceptions to indicate failures
					throw new InputMismatchException();
			}
		} catch (InputMismatchException e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test08inserNull: isEmpty() incorrectly returned false after multiple nulls have been inserted, expected true." );
		} catch (IllegalArgumentException e) {
			return true;
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test08inserNull: unexceptedly threw " + e.getClass().getName() + " when inserting " + j + " null(s) into the PriorityQueue." );
		}
		return false;
	}
	
	/**
	 * Confirm that getMax() does not change the content of the PQ.
	 * @param className
	 * @return true if the content of PQ remains the same after getMax() is called
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test07getMaxRemovesNone(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //stores all PQs
		Comparable [] keys = {new Integer(-1), new Double(-22.45124), "ASampleString234!"};
		// construct PQs of type Integer, Double, and String
		PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
		PriorityQueueADT<Double> pqDou = newDoublePQ(className);
		PriorityQueueADT<String> pqStr = newStringPQ(className);
		PQs[0] = pqInt;
		PQs[1] = pqDou;
		PQs[2] = pqStr;
		int  i = 0; //for-loop index
		try {
			for (i = 0; i <= 2; i++) {
				PQs[i].insert(keys[i]);
				//each insert is followed by 3 getMax() attempts
				PQs[i].getMax();
				PQs[i].getMax();
				Comparable max = PQs[i].getMax();
				if (!max.equals(keys[i]))
					//throw exception if max item changes
					throw new InputMismatchException();
			}
			return true;
		} catch (NoSuchElementException e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test07getMaxRemovesNone: unexpectedly changed the content of the PriorityQueue when its getMax()"
					+ " function is called, expceted the only value that has been inserted " + keys[i].toString() + ", got " + e.getClass().getName() );
		} catch (NullPointerException e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test07getMaxRemovesNone: unexpectedly threw " + e.getClass().getName() + " when its getMax()"
					+ " function is called, expceted the only value that has been inserted: "  + keys[i].toString() + "." );
		}
		return false;
	}
	
	/**
	 * Confirm that the internal data structure does not expand properly to allow many items to be 
	 * added and removed in max to min order.
	 * @param className
	 * @return true if PQs returns items in the correct order
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test06manyDataItems(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Random rand = new Random(); //used to generate random input
		final int CAPACITY = 10000; //number of items to be inserted
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //used to store all PQs
		// construct PQs of type Integer, Double, and String
		PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
		PriorityQueueADT<Double> pqDou = newDoublePQ(className);
		PriorityQueueADT<String> pqStr = newStringPQ(className);
		PQs[0] = pqInt;
		PQs[1] = pqDou;
		PQs[2] = pqStr;
		int maxInt, curInt;
		double maxDouble, curDouble;
		String maxStr, curStr;
		try {
			//Testing on PQ<Integer>
			for (int i = 0; i < CAPACITY; i++) {
				//generates a random integer of value between 0 and specified capacity
				pqInt.insert(new Integer(rand.nextInt(CAPACITY)));
			}
			maxInt = pqInt.getMax();
			for (int i = 0; i < CAPACITY; i++) {
				curInt = pqInt.removeMax();
				//the current value returned by removeMax() should never be larger than the previous one
				if (curInt > maxInt)
					//otherwise, throw exceptions to indicate a failure
					throw new InputMismatchException("FAILED test06manyDataItems: PriorityQueue failed to handle data when its"
							+ " capacity exceeds " + CAPACITY + ". Keys used to insert are Integers ranging from 0 to " + CAPACITY + ".");
				else 
					maxInt = curInt;
			}
			
			//Testing on PQ<Double>
			for (int i = 0; i < CAPACITY; i++) {
				//generates a random number of type double of value between 0 and specified capacity
				pqDou.insert(new Double(rand.nextDouble() + rand.nextInt(CAPACITY)));
			}
			maxDouble = pqDou.getMax();
			for (int i = 0; i < CAPACITY; i++) {
				curDouble = pqDou.removeMax();
				//the current value returned by removeMax() should never be larger than the previous one
				if (curDouble > maxDouble)
					//otherwise, throw exceptions to indicate a failure
					throw new InputMismatchException("FAILED test06manyDataItems: PriorityQueue failed to handle data when its"
							+ " capacity exceeds " + CAPACITY + ". Keys used to insert are Doubles ranging from 0 to " + CAPACITY + ".");
				else 
					maxDouble = curDouble;
			}
			
			//Testing on PQ<String>
			for (int i = 0; i < CAPACITY; i++) {
				pqStr.insert(new Integer(rand.nextInt(CAPACITY)).toString());
			}
			maxStr = pqStr.getMax();
			for (int i = 0; i < CAPACITY; i++) {
				curStr = pqStr.removeMax();
				if (curStr.compareTo(maxStr) > 0)
					throw new InputMismatchException("FAILED test06manyDataItems: PriorityQueue failed to handle data when its"
							+ " capacity exceeds " + CAPACITY + ". Keys used to insert are Strings of combinations of numbers.");
				else 
					maxStr = curStr;
			}
			return true;
		} catch (ArrayIndexOutOfBoundsException |InputMismatchException | NullPointerException e) {
			if (DEBUG) e.printStackTrace();
			String message = e.getMessage();
			if (message == null || message.length() < 10)
				message = ("FAILED test06manyDataItems: PriorityQueue failed to handle data when its"
					+ " capacity exceeds " + CAPACITY + ". Keys used to insert are Integers ranging from 0 to " + CAPACITY + ".");
			print(message);
		}
		return false;
	}
	
	/**
	 * Confirm that PQs allow duplicate values to be stored and can correctly handle duplicates. 
	 * @param className 
	 * @return true if duplicate items are handled correctly
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test05duplicatesAllowed(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //stores all PQs
		// 2-d array stores the set of items to be inserted, where duplicates are included
		Comparable [][] keys = {{new Integer(-1), new Integer(-1), new Integer(3), new Integer(2)},
								{new Double(2398.2990), new Double(3948.23), new Double(0.00), new Double(2398.2990)}, 
								{"Audi", "BMW", "Mercedes", "Audi"}};
		//expected values to be returned when calling getMax() multiple times
		Comparable [][] expectedOutputs  = {{new Integer(3), new Integer(2), new Integer(-1), new Integer(-1)},
											{new Double(3948.23), new Double(2398.2990), new Double(2398.2990), new Double(0.00)}, 
											{"Mercedes", "BMW", "Audi", "Audi"}};
		Comparable [][] actualOutputs = new Comparable[3][4]; //stores the actual outputs
		int i = 0, j= 0; //for-loop indexes
		try {
			// construct PQs of type Integer, Double, and String
			PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
			PriorityQueueADT<Double> pqDou = newDoublePQ(className);
			PriorityQueueADT<String> pqStr = newStringPQ(className);
			PQs[0] = pqInt;
			PQs[1] = pqDou;
			PQs[2] = pqStr;
			//insert all items
			for (i = 0; i <= 2; i++) {
				for (j = 0; j < keys[i].length; j++) {
					PQs[i].insert(keys[i][j]);
				}
			}
			
			for (i = 0; i <= 2; i++) { 
				for (j = 0; j < keys[i].length; j++) {
					Comparable curNode = PQs[i].removeMax();
					//when removeMax() returns null, it suggests that the internals
					//is unable to deal with duplicates
					if (curNode == null)
						throw new NullPointerException();
					else {
						actualOutputs[i][j] = curNode;
					}
				}
			}
			//compare actual and expected outputs
			if (!actualOutputs.equals(expectedOutputs))
				//throw exceptions if there is an mismatch
				throw new InputMismatchException();
		return true;
		} catch (NoSuchElementException e) {
			if (DEBUG) e.printStackTrace();
			String input = Arrays.deepToString(keys[--i]); //decrease i by 1 since it was incremented once more after the loop
			String expectedOutput = Arrays.deepToString(expectedOutputs[i]);
			String output = actualOutputs == null? null: Arrays.deepToString(actualOutputs[i]);
			print("FAILED test05duplicatesAllowed: PriorityQueue failed to handle duplicate values, returned " + output + " when keys " + input + " are suppplied."
					+ " Was expecting " + expectedOutput + " to be returned in order.");
		} catch (NullPointerException e) {
			if (DEBUG) e.printStackTrace();
			String input = Arrays.deepToString(keys[i]); //decrease i by 1 since it was incremented once more after the loop
			String expectedOutput = Arrays.deepToString(expectedOutputs[i]);
			print("FAILED test05duplicatesAllowed: PriorityQueue failed to handle duplicate values, returned null when keys " + input + " are suppplied."
					+ " Was expecting " + expectedOutput + " to be returned in order.");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw e;
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test05duplicatesAllowed: unexpectedly threw " + e.getClass().getName());
		}
		return false;
	}

	/**
	 * Confirm that removeMax returns the max values in the priority order. A failure is indicated otherwise.
	 * @param className
	 * @return true if removeMax returns the correct max value 
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test04insertRemoveMany(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //stores all PQs
		// 2-d array stores the set of items to be inserted
		Comparable [][] keys = {{new Integer(-1), new Integer(-1), new Integer(3), new Integer(982)},
								{new Double(-23.2938), new Double(3948.23), new Double(0.00), new Double(2398.2990)}, 
								{"Str1", "CaaA", "Babashiwo", "A"}};
		Comparable [] outputs = new Comparable[3]; //stores actual outputs
		Comparable [] maxKeys = {new Integer(982), new Double(2398.2990), "Str1"}; //expected values to be returned by removeMax()
		int i = 0, j = 0; //indexes for for-loops
		try {
			// construct PQs of type Integer, Double, and String
			PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
			PriorityQueueADT<Double> pqDou = newDoublePQ(className);
			PriorityQueueADT<String> pqStr = newStringPQ(className);
			PQs[0] = pqInt;
			PQs[1] = pqDou;
			PQs[2] = pqStr;
			//insert all items
			for (i = 0; i <= 2; i++) {
				for (j = 0; j <= 3; j++) {
					PQs[i].insert(keys[i][j]);
					outputs[i] = PQs[i].removeMax();
				} //throw exceptions when an unexpected return value is encountered
				if (!maxKeys[i].equals(outputs[i]))
					throw new InputMismatchException("" + i);
			}
		return true;
		} catch (InputMismatchException e) {
			int index = Integer.parseInt(e.getMessage());
			if (DEBUG) e.printStackTrace();
			String maxKey = maxKeys[index].toString();
			String input = Arrays.deepToString(keys[i]); 
			String output = (outputs[index] == null) ? null: outputs[index].toString();
			print("FAILED test04insertRemoveMany: getMax incorrectly returned " + output + " while " + maxKey + " is expected. "
					+ "The set of keys used to insert was " + input + ".");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw e;
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			int index = Integer.parseInt(e.getMessage());
			String maxKey = maxKeys[index].toString();
			String input = Arrays.deepToString(keys[i]); 
			String output = (outputs[index] == null) ? null: outputs[index].toString();
			print("FAILED test04insertRemoveMany: unexpectedly threw " + e.getClass().getName() + " when calling removeMax() after "
					+ input + "has been inserted.");
		}
		return false;
	}

	/**
	 * Confirm when one item is inserted, the same item will be returned when removeMax is called. 
	 * @param className
	 * @return true if the item returned by removeMax matches with the item inserted
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws ClassNotFoundException
	 */
	private static boolean test03insertRemoveOne(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT [] PQs = new PriorityQueueADT[3]; //stores all PQs
		Comparable [] keys = {new Integer(-1), new Double(-22.45124), "ASampleString234!"}; //keys to be inserted
		Comparable [] outputs = new Comparable[3];
		try {
			// construct PQs of type Integer, Double, and String
			PriorityQueueADT<Integer> pqInt = newIntegerPQ(className);
			PriorityQueueADT<Double> pqDou = newDoublePQ(className);
			PriorityQueueADT<String> pqStr = newStringPQ(className);
			PQs[0] = pqInt;
			PQs[1] = pqDou;
			PQs[2] = pqStr;
			//insert contents stored in keys in order
			for (int i = 0; i <= 2; i++) {
				PQs[i].insert(keys[i]);
				outputs[i] = PQs[i].removeMax(); // call removeMax() on each PQ
				if (! keys[i].equals(outputs[i])) //if input does not match with output, throw exceptions
					throw new InputMismatchException("" + i);
			}
		return true;
		} catch (InputMismatchException e) {
			int index = Integer.parseInt(e.getMessage());
			if (DEBUG) e.printStackTrace();
			String key = keys[index].toString();
			String output = (outputs[index] == null) ? null: outputs[index].toString();
			print("FAILED test03insertRemoveOne: getMax incorrectly returned " + output + " while " + key + " has been inserted and "
					+ "is excpeted to be returned.");
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw e; //raise these exceptions to the parent function
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			int index = Integer.parseInt(e.getMessage());
			String key = keys[index].toString();
			print("FAILED test03insertRemoveOne: unexpectedly threw " + e.getClass().getName() + " when " + key + "has been "
					+ "inserted and removeMax() is called.");
		}
		return false;
	}

	/** Confirm that a newly constructed PriorityQueue throws a NoSuchElementException upon calling removeMax. 
	 * Otherwise a failure is indicated.
	 * @param className
	 * @return true if removeMax on empty PQ throws NoSuchElementException
	 */
	private static boolean test02removeMaxEXCEPTION(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException{
		try {
			// construct a PQ of type Integer
			PriorityQueueADT<Integer> pq = newIntegerPQ(className);
			pq.removeMax();
		} catch (NoSuchElementException e) {
			return true;
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw e; //raise these exceptions to the parent function
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test02removeMaxEXCEPTION: unexpectedly threw " + e.getClass().getName());
		}
		print("FAILED test02removeMaxEXCEPTION: did not throw NoSuchElementException on newly constructed PQ.");
		return false;
	}

	/** DO NOT EDIT -- provided as an example
	 * Confirm that getMax throws NoSuchElementException if called on 
	 * an empty priority queue.  Any other exception indicates a fail.
	 * 
	 * @param className name of the priority queue implementation to test.
	 * @return true if getMax on empty priority queue throws NoSuchElementException
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test01getMaxEXCEPTION(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT<Integer> pq = newIntegerPQ(className);
		try {
			pq.getMax();
		} catch (NoSuchElementException e) {
			return true;			
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test01getMaxEXCEPTION: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test01getMaxEXCEPTION: getMax did not throw NoSuchElement exception on newly constructed PQ");
		return false;
	}

	/** DO NOT EDIT THIS METHOD
	 * @return true if able to construct Integer priority queue and 
	 * the instance isEmpty.
	 * 
	 * @throws ClassNotFoundException 
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 */
	private static boolean test00isEmpty(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		PriorityQueueADT<Integer> pq = newIntegerPQ(className);
		try {
		if (pq.isEmpty()) 
			return true;
		} catch (Exception e) {
			if (DEBUG) e.printStackTrace();
			print("FAILED test00isEmpty: unexpectedly threw " + e.getClass().getName());
			return false;
		}
		print("FAILED test00isEmpty: isEmpty returned false on newly constructed PQ");
		return false;
	}

	/** DO NOT EDIT THIS METHOD
	 * Constructs a max Priority Queue of Integer using the class that is name.
	 * @param className The specific Priority Queue to construct.
	 * @return a PriorityQueue
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked" })
	public static final PriorityQueueADT<Integer> newIntegerPQ(String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> pqClass = Class.forName(className);
		Object obj = pqClass.newInstance();	
		if (obj instanceof PriorityQueueADT) {
			return (PriorityQueueADT<Integer>) obj;
		}
		return null;
	}

	/** DO NOT EDIT THIS METHOD
	 * Constructs a max Priority Queue of Double using the class that is named.
	 * @param className The specific Priority Queue to construct.
	 * @return a PriorityQueue
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked" })
	public static final PriorityQueueADT<Double> newDoublePQ(final String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> pqClass = Class.forName(className);
		Object obj = pqClass.newInstance();	
		if (obj instanceof PriorityQueueADT) {
			return (PriorityQueueADT<Double>) obj;
		}
		return null;
	}

	/** DO NOT EDIT THIS METHOD
	 * Constructs a max Priority Queue of String using the class that is named.
	 * @param className The specific Priority Queue to construct.
	 * @return a PriorityQueue
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws ClassNotFoundException 
	 */
	@SuppressWarnings({ "unchecked" })
	public static final PriorityQueueADT<String> newStringPQ(final String className) throws InstantiationException, IllegalAccessException, ClassNotFoundException {
		Class<?> pqClass = Class.forName(className);
		Object obj = pqClass.newInstance();	
		if (obj instanceof PriorityQueueADT) {
			return (PriorityQueueADT<String>) obj;
		}
		return null;
	}

	/** DO NOT EDIT THIS METHOD
	 * Write the message to the standard output stream.
	 * Always adds a new line to ensure each message is on its own line.
	 * @param message Text string to be output to screen or other.
	 */
	private static void print(String message) {
		System.out.println(message);
	}
}
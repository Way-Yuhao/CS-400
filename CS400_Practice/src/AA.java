import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;

interface it {
	enum yooo {aye}
	public String message = "Shit homie";
	 static void method1() {
		System.out.println(message);
	}
	
	default void method2() {
		System.out.println(message);
	}
	
	abstract void method3();
}
	
class AA implements it {
	/*
	static void method1() {
		System.out.println("jiu bu");
	}
	*/
	public void method3(){}
	
	public static void main(String[] args) {
		it aa = new AA();
		it.method1();
		aa.method2();
		System.out.println(it.yooo.valueOf("aye").ordinal());
	}

}

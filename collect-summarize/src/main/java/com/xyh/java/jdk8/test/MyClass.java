package com.xyh.java.jdk8.test;

public class MyClass {
	
	MyInterface myInterface;
	
	public MyClass(MyInterface m) {
		super();
		this.myInterface = m;
	}
	
	public void start() {
		myInterface.inter("");
	}
	
}

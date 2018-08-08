package com.xyh.java.proxy.dynamicproxy.jdk;

public class RealSubject implements Subject{

	@Override
	public void request() {
		// TODO Auto-generated method stub
		System.out.println("do real method for jdk proxy!");
	}

}

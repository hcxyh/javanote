package com.xyh.java.proxy.dynamicproxy.cglib;

public class Client {
	public static void main(String[] args) {
		CglibSubject cglib = new CglibSubject();
		RealSubject subject = (RealSubject) cglib.getInstance(new RealSubject());
		subject.request();
	}
}

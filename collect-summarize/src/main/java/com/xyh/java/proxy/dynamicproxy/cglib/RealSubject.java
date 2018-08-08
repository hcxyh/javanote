package com.xyh.java.proxy.dynamicproxy.cglib;

//真實主題類
public class RealSubject {
	public void request() {
		System.out.println("Do real business!");
	}
}

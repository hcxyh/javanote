package com.xyh.java.proxy.dynamicproxy.jdk;

public class Client {
	
	/**
	 * 要实现JDK动态代理的首先条件是：被代理类必须实现一个接口。
	 * @param args
	 */
	public static void main(String[] args) {
		// 定义一个主题
		Subject subject = new RealSubject();

		// 定义一个代理
		Subject proxy = DynamicProxy.newProxyInstance(subject);

		// 执行代理的行为
		proxy.request();
	}

}

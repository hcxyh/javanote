/**
 * 
 */
package com.xyh.test;

/**
 * @author hcxyh  2018年8月16日
 * java初始化顺序
 */
public class ObjectTest {
	
	static {
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run() {
				synchronized (ObjectTest.class) {
					System.out.println("init static block");
					ObjectTest.class.notifyAll();
				}
			}
		}));
	}
	
	public static void main(String[] args) {
		
		synchronized (ObjectTest.class) {
			while(true) {
				try {
					System.out.println("init static mainMethod");
					ObjectTest.class.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}
}

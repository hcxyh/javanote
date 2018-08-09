package com.xyh.java.thread;

/**
 * 
 * @author hcxyh 2018年8月9日
 *
 */
public class ThreadStatus {
	
	//不使用final,对象改变可能导致notify无效
	private final ThreadToGo threadToGo = new ThreadToGo();

	class ThreadToGo {   //生产者消费者媒介
		boolean value = true;  //多线程共享变量
		int whileNum = 100;
		public ThreadToGo() {
		}
	}

	private final Integer shareInt = 1;

	public class ThreadOne implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					synchronized (threadToGo) {
						while (threadToGo.value == true) {
							threadToGo.wait();  
							System.out.println(Thread.currentThread().getName() + "is be wait");
						}
						System.out.println("---------------------");
						threadToGo.value = true;
						threadToGo.notify();
						System.out.println(Thread.currentThread().getName() + "is be notify");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public class ThreadTwo implements Runnable {
		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					synchronized (threadToGo) {
						while (threadToGo.value == false) {
							threadToGo.wait(); 
							System.out.println(Thread.currentThread().getName() + "is be wait");
						}
						System.out.println("*******************");
						threadToGo.value = false;
						threadToGo.notify();
						System.out.println(Thread.currentThread().getName() + "is be notify");
					}
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		ThreadStatus threadStatus = new ThreadStatus();
		new Thread(threadStatus.new ThreadOne()).start();
		new Thread(threadStatus.new ThreadTwo()).start();
	}
}

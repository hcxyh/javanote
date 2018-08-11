package com.xyh.java.concurrent.sharelock;

import java.util.concurrent.CountDownLatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 一个await()任务等到其他任务通过countdown()表明就绪后,才能执行.
 * 类似计数器的功能。比如有一个任务A，它要等待其他4个任务执行完毕之后才能执行，
 * @author hcxyh  2018年8月10日
 */
public class CountDownNote {
	
	private static final Logger logger = LoggerFactory.getLogger(CountDownNote.class);
	
	
	/**
	 * //调用await()方法的线程会被挂起，它会等待直到count值为0才继续执行
	 * public void await() throws InterruptedException { };   
	 * //和await()类似，只不过等待一定的时间后count值还没变为0的话就会继续执行
	 * public boolean await(long timeout, TimeUnit unit) throws InterruptedException { };
	 * //将count值减1 
	 * public void countDown() { };  
	 */
	
	public static void main(String[] args) throws InterruptedException {
		
		CountDownLatch countDownLatch = new CountDownLatch(3);
		
		new ThreadOne(countDownLatch).start();
		Thread.sleep(5000);
		new ThreadTwo(countDownLatch).start();
		new ThreadTwo(countDownLatch).start();
		new ThreadTwo(countDownLatch).start();
		
	}
	
	
	public static class ThreadOne extends Thread{
		CountDownLatch 	countDownLatch  = null;

		public ThreadOne(CountDownLatch countDownLatch) {
			super();
			this.countDownLatch = countDownLatch;
		}
		
		public void run() {
			try {
				logger.debug(Thread.currentThread().getName() + "is await");
				countDownLatch.await();
				System.out.println("always begin run");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	public static class ThreadTwo extends Thread{
		CountDownLatch 	countDownLatch  = null;

		public ThreadTwo(CountDownLatch countDownLatch) {
			super();
			this.countDownLatch = countDownLatch;
		}
		
		public void run() {
			logger.debug(Thread.currentThread().getName() + "is await");
			countDownLatch.countDown();
		}
	}
}

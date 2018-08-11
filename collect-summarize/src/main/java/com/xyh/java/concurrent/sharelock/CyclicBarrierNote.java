package com.xyh.java.concurrent.sharelock;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 * N个任务,通过调研await(),全部达到barrier状态后,统一开始执行. 
 * 回环栅栏，通过它可以实现让一组线程等待至某个状态之后再全部同时执行。
 * CyclicBarrier可以被重用。我们暂且把这个状态就叫做barrier，当调用await()方法之后，线程就处于barrier了。
 * @author hcxyh 2018年8月10日
 *
 */
public class CyclicBarrierNote {

	/**
	 * 参数parties指让多少个线程或者任务等待至barrier状态；
	 * 参数barrierAction为当这些线程都达到barrier状态时会执行的内容。
	 * public CyclicBarrier(int parties, Runnable barrierAction) {} 
	 * public CyclicBarrier(int parties) {}
	 * 
	 * 用来挂起当前线程，直至所有线程都到达barrier状态再同时执行后续任务； 
	 * public int await() throws InterruptedException, BrokenBarrierException { };
	 * 让这些线程等待至一定的时间，如果还有线程没有到达barrier状态就直接让到达barrier的线程执行后续任务。 
	 * public int await(long timeout, TimeUnit unit)throws InterruptedException,BrokenBarrierException,TimeoutException { };
	 */

	public static void main(String[] args) {
		int N = 4;
		CyclicBarrier barrier = new CyclicBarrier(N, new Runnable() {
			@Override
			public void run() {
				System.out.println("When" + N + "Thread was barrier,can execute");
			}
		});
		for (int i = 0; i < N; i++)
			new Writer(barrier).start();
		
		//CyclicBarrier进行重用
		try {
			Thread.sleep(25000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("CyclicBarrier重用");

		for (int i = 0; i < N; i++) {
			new Writer(barrier).start();
		}
	}

	static class Writer extends Thread {
		private CyclicBarrier cyclicBarrier;

		public Writer(CyclicBarrier cyclicBarrier) {
			this.cyclicBarrier = cyclicBarrier;
		}

		@Override
		public void run() {
			System.out.println("线程" + Thread.currentThread().getName() + "正在写入数据...");
			try {
				Thread.sleep(5000); // 以睡眠来模拟写入数据操作
				System.out.println("线程" + Thread.currentThread().getName() + "写入数据完毕，等待其他线程写入完毕");
				cyclicBarrier.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}
			System.out.println("所有线程写入完毕，继续处理其他任务...");
		}
	}

}

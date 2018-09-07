package com.xyh.java.thread;

import java.util.LinkedList;

/**
 * @ClassName: WorkQueue
 * @author xueyh
 * @date 2018年8月9日 下午9:34:32
 * 
 */
public class WorkQueue {

	private final int nThreads;// 线程池的大小
	private final PoolWorker[] threads;// 用数组实现线程池
	private final LinkedList queue;//任务队列

	public WorkQueue(int nThreads) {
		this.nThreads = nThreads;
		queue = new LinkedList();
		threads = new PoolWorker[nThreads];
		for (int i = 0; i < nThreads; i++) {
			threads[i] = new PoolWorker();
			threads[i].start();
		}
	}

	public void execute(Runnable r) {
		synchronized (queue) {
			queue.addLast(r);
			queue.notify();
		}
	}

	private class PoolWorker extends Thread {
		public void run() {
			Runnable r;
			while (true) {
				synchronized (queue) {
					while (queue.isEmpty()) {
						try {
							queue.wait();
						} catch (InterruptedException ignored) {
							
						}
					}
					r = (Runnable) queue.removeFirst();
				}
				try {
					r.run();// 执行任务
				} catch (RuntimeException e) {
					// You might want to log something here
				}
			}
		}
	}

	public static void main(String args[]) {
		WorkQueue wq = new WorkQueue(10);// 10个工作线程
		Mytask r[] = new Mytask[20];// 20个任务

		for (int i = 0; i < 20; i++) {
			r[i] = new Mytask();
			wq.execute(r[i]);
		}
	}
}

class Mytask implements Runnable {
	public void run() {
		String name = Thread.currentThread().getName();
		try {
			Thread.sleep(100);// 模拟任务执行的时间
		} catch (InterruptedException e) {
		}
		System.out.println(name + " executed OK");
	}

}

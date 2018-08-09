package com.xyh.java.thread.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConditionLockNote {

	public class MiddleStorage {
		private boolean flag = true;
		private int cyclic = 10;
		// 重入锁
		private final Lock reentrantLock = new ReentrantLock();
		// 读写锁
		private final ReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock(true);

		private Condition condition = reentrantLock.newCondition();
	}

	public class ThreadOne implements Runnable {
		MiddleStorage middleStorage;

		public ThreadOne(MiddleStorage middleStorage) {
			super();
			this.middleStorage = middleStorage;
		}
		@Override
		public void run() {
			try {
				for (int i = 0; i < middleStorage.cyclic; i++) {
					middleStorage.reentrantLock.lock();
					while (middleStorage.flag) {
						middleStorage.condition.await();
					}
					System.out.println(Thread.currentThread().getName() + "-- invoke");
					System.out.println("-------------------------------------------");
					middleStorage.flag = true;
					middleStorage.condition.signal();
				}
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				middleStorage.reentrantLock.unlock();
			}
		}
	}

	public class ThreadTwo implements Runnable {
		MiddleStorage middleStorage;

		public ThreadTwo(MiddleStorage middleStorage) {
			super();
			this.middleStorage = middleStorage;
		}
		@Override
		public void run() {
			try {
				for (int i = 0; i < middleStorage.cyclic; i++) {
					middleStorage.reentrantLock.lock();
					while (!middleStorage.flag) {
						middleStorage.condition.await();
					}
					System.out.println(Thread.currentThread().getName() + "-- invoke");
					System.out.println("********************************************");
					middleStorage.flag = false;
					middleStorage.condition.signal();
				}
			} catch (Exception e) {
				e.getStackTrace();
			} finally {
				middleStorage.reentrantLock.unlock();
			}
		}
	}

	public static void main(String[] args) {
		ConditionLockNote conditionLockNote = new ConditionLockNote();
		MiddleStorage middleStorage = new ConditionLockNote().new MiddleStorage();
		new Thread(conditionLockNote.new ThreadOne(middleStorage)).start();
		new Thread(conditionLockNote.new ThreadTwo(middleStorage)).start();
	}
}

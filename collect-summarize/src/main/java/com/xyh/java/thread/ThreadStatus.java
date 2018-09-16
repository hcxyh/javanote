package com.xyh.java.thread;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 线程中的几种状态,及其切换.
 * @author hcxyh 2018年8月9日
 *
 */
public class ThreadStatus {
	
	/**
	 * 线程状态
	 * 想要通过jstack命令来分析线程的情况的话，首先要知道线程都有哪些状态，下面这些状态是我们使用jstack命令查看线程堆栈信息时可能会看到的线程的几种状态：
	 * NEW,未启动的。不会出现在Dump中。
	 * RUNNABLE,在虚拟机内执行的。
	 * BLOCKED,受阻塞并等待监视器锁。   （有的线程已经notify）
	 * WATING,无限期等待另一个线程执行特定操作。 
	 * TIMED_WATING,有时限的等待另一个线程的特定操作。
	 * TERMINATED,已退出的。
	 * 线程动作
	 * 线程状态产生的原因：
	 * runnable:状态一般为RUNNABLE。
	 * in Object.wait():等待区等待,状态为WAITING或TIMED_WAITING。
	 * waiting for monitor entry:进入区等待,状态为BLOCKED。
	 * waiting on condition:等待区等待、被park。
	 * sleeping:休眠的线程,调用了Thread.sleep()。
	 */
	
	/**
	 * 调用修饰
	 * locked <地址> 目标：使用synchronized申请对象锁成功,监视器的拥有者。
	 * waiting to lock <地址> 目标：使用synchronized申请对象锁未成功,在迚入区等待。
	 * waiting on <地址> 目标：使用synchronized申请对象锁成功后,释放锁幵在等待区等待。
	 * parking to wait for <地址> 目标
	 * 1、locked
	 * at oracle.jdbc.driver.PhysicalConnection.prepareStatement
	 * - locked <0x00002aab63bf7f58> (a oracle.jdbc.driver.T4CConnection)
	 * at oracle.jdbc.driver.PhysicalConnection.prepareStatement
	 * - locked <0x00002aab63bf7f58> (a oracle.jdbc.driver.T4CConnection)
	 * at com.jiuqi.dna.core.internal.db.datasource.PooledConnection.prepareStatement
	 * 通过synchronized关键字,成功获取到了对象的锁,成为监视器的拥有者,在临界区内操作。对象锁是可以线程重入的。
	 * 2、waiting to lock
	 * at com.jiuqi.dna.core.impl.CacheHolder.isVisibleIn(CacheHolder.java:165)
	 * - waiting to lock <0x0000000097ba9aa8> (a CacheHolder)
	 * at com.jiuqi.dna.core.impl.CacheGroup$Index.findHolder
	 * at com.jiuqi.dna.core.impl.ContextImpl.find
	 * at com.jiuqi.dna.bap.basedata.common.util.BaseDataCenter.findInfo
	 * 通过synchronized关键字,没有获取到了对象的锁,线程在监视器的进入区等待。在调用栈顶出现,线程状态为Blocked。
	 * 3、waiting on
	 * at java.lang.Object.wait(Native Method)
	 * - waiting on <0x00000000da2defb0> (a WorkingThread)
	 * at com.jiuqi.dna.core.impl.WorkingManager.getWorkToDo
     * - locked <0x00000000da2defb0> (a WorkingThread)
	 * at com.jiuqi.dna.core.impl.WorkingThread.run
	 * 通过synchronized关键字,成功获取到了对象的锁后,调用了wait方法,进入对象的等待区等待。在调用栈顶出现,线程状态为WAITING或TIMED_WATING。
	 * 4、parking to wait for
	 * park是基本的线程阻塞原语,不通过监视器在对象上阻塞。随concurrent包会出现的新的机制,不synchronized体系不同。
	 */
	
	
	
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
					System.out.println(Thread.currentThread()+"is running");
					synchronized (threadToGo) {
						System.out.println(Thread.currentThread().getName() + "获得锁");
						while (threadToGo.value == true) {
							System.out.println(Thread.currentThread().getName()+"调用wait");
//							System.out.println(Thread.currentThread().getState());
							threadToGo.wait();   //进入阻塞状态
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
		
		private Thread t;
		
		
		
		public ThreadTwo(Thread t) {
			super();
			this.t = t;
		}


		@Override
		public void run() {
			for (int i = 0; i < 10; i++) {
				try {
					System.out.println(Thread.currentThread()+"is running");
					synchronized (threadToGo) {
						System.out.println(Thread.currentThread().getName() + "获得锁");
						while (threadToGo.value == false) {
							System.out.println(Thread.currentThread().getName()+"调用wait");
							threadToGo.wait(); 
							System.out.println(Thread.currentThread().getName() + "is be wait");
						}
						System.out.println("*******************");
						threadToGo.value = false;
						System.out.println(t.getName() + t.getState());
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
//		new Thread(threadStatus.new ThreadOne()).start();
//		new Thread(threadStatus.new ThreadTwo()).start();
		
		
		Thread t1 = new Thread(threadStatus.new ThreadOne());
		Thread t2 = new Thread(threadStatus.new ThreadTwo(t1));
		t1.start();
		t2.start();
		
	}
	
	
	/**
	 * 结果：TODO RUNNABLE状态
	 * 
In run method, state is RUNNABLE.
 
Try to connect socket address which not exist...
 
Main thread check the state is RUNNABLE.
	堆栈信息:
	
"IOThread" #10 prio=5 os_prio=0 tid=0x00000000187c7800 nid=0x8b0 runnable [0x00000000192ee000]
   java.lang.Thread.State: RUNNABLE
	at java.net.DualStackPlainSocketImpl.connect0(Native Method)
	at java.net.DualStackPlainSocketImpl.socketConnect(DualStackPlainSocketImpl.java:79)
	at java.net.AbstractPlainSocketImpl.doConnect(AbstractPlainSocketImpl.java:350)
	- locked <0x00000000eb6c0fa8> (a java.net.DualStackPlainSocketImpl)
	at java.net.AbstractPlainSocketImpl.connectToAddress(AbstractPlainSocketImpl.java:206)
	at java.net.AbstractPlainSocketImpl.connect(AbstractPlainSocketImpl.java:188)
	at java.net.PlainSocketImpl.connect(PlainSocketImpl.java:172)
	at java.net.SocksSocketImpl.connect(SocksSocketImpl.java:392)
	at java.net.Socket.connect(Socket.java:589)
	at java.net.Socket.connect(Socket.java:538)
	at com.test.threadpool.TestThreadState$IOThread.run(TestThreadState.java:83)
	 */
	public static void testStateRunnable() {
		IOThread simpleThread = new IOThread("IOThread");
		simpleThread.start();
 
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		System.out.println("Main thread check the state is " + simpleThread.getState() + "."); // RUNNABLE
	}
 
	static class IOThread extends Thread {
 
		public IOThread(String name) {
			super(name);
		}
 
		@Override
		public void run() {
			System.out.println("In run method, state is " + getState() + "."); // RUNNABLE
			Socket socket = new Socket();
			try {
				System.out.println("Try to connect socket address which not exist...");
				socket.connect(new InetSocketAddress(
						InetAddress.getByAddress(new byte[] { (byte) 192, (byte) 168, 1, 14 }), 5678));
			} catch (UnknownHostException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
				try {
					socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	
	/**
	 * TODO BLOCKED状态
	 * 模拟两个线程抢锁，当一个线程抢到锁之后进入sleep，sleep状态下不会释放锁，所以另外一个线程被阻塞。从堆栈信息可以看到，locked和waiting to lock都是同一个对象。
	 * 结果：
	 * 
Thread:t2 in run.
 
Thread:t1 in run.
 
Thread:t2 hold the lock.
 
Thread t1's state BLOCKED
Thread t2's state TIMED_WAITING

堆栈信息：
"t2" #11 prio=5 os_prio=0 tid=0x0000000018604800 nid=0x934 waiting on condition [0x000000001920f000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at com.test.threadpool.TestThreadState$SleepThread.run(TestThreadState.java:274)
        - locked <0x00000000eb64b910> (a java.lang.Object)
"t1" #10 prio=5 os_prio=0 tid=0x000000001860b000 nid=0x3528 waiting for monitor entry [0x000000001910f000]
   java.lang.Thread.State: BLOCKED (on object monitor)
        at com.test.threadpool.TestThreadState$SleepThread.run(TestThreadState.java:271)
        - waiting to lock <0x00000000eb64b910> (a java.lang.Object)

	 */
	public static void testBlockedState() {
		Object lock = new Object();
		SleepThread t1 = new SleepThread("t1", lock);
		SleepThread t2 = new SleepThread("t2", lock);
		t1.start();
		t2.start();
 
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		System.out.println("Thread t1's state " + t1.getState());
		System.out.println("Thread t2's state " + t2.getState());
	}
 
	static class SleepThread extends Thread {
		private String name;
		private Object lock;
 
		public SleepThread(String name, Object lock) {
			super(name);
			this.name = name;
			this.lock = lock;
		}
 
		@Override
		public void run() {
			System.out.println("Thread:" + name + " in run.");
 
			synchronized (lock) {
				System.out.println("Thread:" + name + " hold the lock.");
 
				try {
					Thread.sleep(1000 * 1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
 
				System.out.println("Thread:" + name + " return the lock.");
			}
		}
	}
	
	
	/**
	 * TODO 调用wait()方法导致的WAITING状态。
	 * 线程调用wait方法，状态变成WAITING。
	 * 测试结果：

Try to wait.
 
Main thread check the state is WAITING.
堆栈信息：

"WaitingThread" #10 prio=5 os_prio=0 tid=0x0000000018dea000 nid=0x1220 in Object.wait() [0x00000000198ee000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000eb6477e8> (a java.lang.Object)
        at java.lang.Object.wait(Object.java:502)
        at com.test.threadpool.TestThreadState$WaitingThread.run(TestThreadState.java:138)
        - locked <0x00000000eb6477e8> (a java.lang.Object)
	 */
	public static void testStateWatingByWait() {
		Object lock = new Object();
		WaitingThread waitingThread = new WaitingThread("WaitingThread", lock);
		waitingThread.start();
 
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		System.out.println("Main thread check the state is " + waitingThread.getState() + "."); // WAITING
	}
 
	static class WaitingThread extends Thread {
		private int timeout = 0;
		private Object lock;
 
		public WaitingThread(String name, Object lock) {
			this(name, lock, 0);
		}
 
		public WaitingThread(String name, Object lock, int timeout) {
			super(name);
			this.timeout = timeout;
			this.lock = lock;
		}
 
		@Override
		public void run() {
			synchronized (lock) {
				if (timeout == 0) {
					try {
						System.out.println("Try to wait.");
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						System.out.println("Try to wait in " + timeout + ".");
						lock.wait(timeout);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
 
			System.out.println("Over thread.");
		}
	}
	
	
	
	
	
	/**
	 * TODO 调用join()方法导致的WAITING状态。
	 * 线程调用join方法，状态变成WAITING。
	 * 测试结果：

Try to wait.
 
Try to join.
 
Main thread check the state is WAITING.
堆栈信息：

"JoinThread" #11 prio=5 os_prio=0 tid=0x0000000019007000 nid=0x33c0 in Object.wait() [0x0000000019c1f000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000eb64a498> (a com.test.threadpool.TestThreadState$WaitingThread)
        at java.lang.Thread.join(Thread.java:1252)
        - locked <0x00000000eb64a498> (a com.test.threadpool.TestThreadState$WaitingThread)
        at java.lang.Thread.join(Thread.java:1326)
        at com.test.threadpool.TestThreadState$JoinThread.run(TestThreadState.java:194)
"WaitingThread" #10 prio=5 os_prio=0 tid=0x0000000019006000 nid=0x35ac in Object.wait() [0x0000000019b1f000]
   java.lang.Thread.State: WAITING (on object monitor)
        at java.lang.Object.wait(Native Method)
        - waiting on <0x00000000eb64a468> (a java.lang.Object)
        at java.lang.Object.wait(Object.java:502)
        at com.test.threadpool.TestThreadState$WaitingThread.run(TestThreadState.java:138)
        - locked <0x00000000eb64a468> (a java.lang.Object)
	 * 
	 */
	public static void testStateWatingByJoin() {
		Object lock = new Object();
		WaitingThread waitingThread = new WaitingThread("WaitingThread", lock);
		waitingThread.start();
		JoinThread joinThread = new JoinThread("JoinThread", waitingThread);
		joinThread.start();
		
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		System.out.println("Main thread check the join thread's state is " + joinThread.getState() + "."); // WAITING
	}
 
	static class JoinThread extends Thread {
		private int timeout = 0;
		private Thread thread;
 
		public JoinThread(String name, Thread thread) {
			this(name, thread, 0);
		}
 
		public JoinThread(String name, Thread thread, int timeout) {
			super(name);
			this.timeout = timeout;
			this.thread = thread;
		}
 
		@Override
		public void run() {
			if (timeout == 0) {
				try {
					System.out.println("Try to join.");
					thread.join();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			} else {
				try {
					System.out.println("Try to join in " + timeout + ".");
					thread.join(timeout);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Over join.");
		}
	}
	static class WaitingThread1 extends Thread {
		private int timeout = 0;
		private Object lock;
 
		public WaitingThread1(String name, Object lock) {
			this(name, lock, 0);
		}
 
		public WaitingThread1(String name, Object lock, int timeout) {
			super(name);
			this.timeout = timeout;
			this.lock = lock;
		}
 
		@Override
		public void run() {
			synchronized (lock) {
				if (timeout == 0) {
					try {
						System.out.println("Try to wait.");
						lock.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				} else {
					try {
						System.out.println("Try to wait in " + timeout + ".");
						lock.wait(timeout);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
 
			System.out.println("Over thread.");
		}
	}
	
	/**
	 * TODO
	 * 调用LockSupport.park方法导致的WAITING状态。
使用线程池的时候经常会遇到这种状态，当线程池里面的任务都执行完毕，会等待获取任务。

"pool-1-thread-1" #10 prio=5 os_prio=0 tid=0x0000000018f9c000 nid=0x2e88 waiting on condition [0x0000000019aaf000]
   java.lang.Thread.State: WAITING (parking)
        at sun.misc.Unsafe.park(Native Method)
        - parking to wait for  <0x00000000eb64cc30> (a java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject)
        at java.util.concurrent.locks.LockSupport.park(LockSupport.java:175)
        at java.util.concurrent.locks.AbstractQueuedSynchronizer$ConditionObject.await(AbstractQueuedSynchronizer.java:2039)
        at java.util.concurrent.LinkedBlockingQueue.take(LinkedBlockingQueue.java:442)
        at java.util.concurrent.ThreadPoolExecutor.getTask(ThreadPoolExecutor.java:1074)
        at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1134)
        at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
        at java.lang.Thread.run(Thread.java:748)
	 */
	public static void testStateWatingByThreadExecutor() {
		ExecutorService executeService = Executors.newSingleThreadExecutor();
		executeService.submit(new Runnable() {
			@Override
			public void run() {
				System.out.println("Over Run.");
			}
		});
 
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * TIMED_WAITING状态
只测试sleep()方法，其余参照WAITING状态。

"main" #1 prio=5 os_prio=0 tid=0x0000000004f80800 nid=0x34bc waiting on condition [0x0000000004e7f000]
   java.lang.Thread.State: TIMED_WAITING (sleeping)
        at java.lang.Thread.sleep(Native Method)
        at com.test.threadpool.TestThreadState.testSleep(TestThreadState.java:233)
        at com.test.threadpool.TestThreadState.main(TestThreadState.java:53)
	 */
	public static void testSleep() {
		try {
			Thread.sleep(1000 * 100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * NEW和TERMINATED状态
	 * 
	 */
	public static void testNewAndTerminatedState() {
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				System.out.println("Over Run.");
			}
		});
 
		System.out.println("State " + thread.getState() + ".");
		thread.start();
 
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
 
		System.out.println("State " + thread.getState() + ".");
	}
}

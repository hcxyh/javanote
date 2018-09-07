package com.xyh.java.thread;

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

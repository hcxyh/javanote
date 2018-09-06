package com.xyh.java.concurrent.executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 自定义创建线程
 * 每个创建出来的线程设置更有意义的名字，如果出现并发问题，也方便查找问题原因
 * @author hcxyh  2018年8月11日
 *
 */
public class MyThreadFactory implements ThreadFactory{
	
	private static final AtomicInteger POOL_NUMBER = new AtomicInteger(1);
	private final ThreadGroup group;
	private final AtomicInteger threadnNumber = new AtomicInteger(1);
	private final String namePrefix; 
	
	
	
	public MyThreadFactory() {
		super();
		SecurityManager s = System.getSecurityManager();
		group = (s!=null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
		//设置自定义的线程名
		namePrefix = "custom-pool-" + POOL_NUMBER.getAndIncrement()+ "-thread-";
	}



	@Override
	public Thread newThread(Runnable r) {
		//设置线程名字
		Thread t = new Thread(group,r,namePrefix +  threadnNumber.getAndIncrement(),0);
		if(t.isDaemon()) {
			t.setDaemon(false);
		}
		if(t.getPriority() != Thread.NORM_PRIORITY) {
			t.setPriority(Thread.NORM_PRIORITY);
		}
		return t;
	}

}

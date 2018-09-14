package com.xyh.java.concurrent.executor.extend;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

/**
 *  copy tomcat ThreadPoolExecutor
 *  当core线程无法应付请求的时候,如果当前线程池中的线程数量还小于MAX线程数的时候,
 *  继续创建新的线程处理任务,一直到线程数量到达MAX后,才将任务插入到队列里
 *  减少频繁出入队列所造成的消耗
 */
public class ThreadPoolExecutorExtends extends ThreadPoolExecutor {
	
	private final ThreadLocal<Long> startTime = new ThreadLocal<Long>();
	private final Logger log = Logger.getAnonymousLogger();
	
	/**
     * 计数器,用于表示已经提交到队列里面的task的数量,这里task特指还未完成的task。
     * 当task执行完后,submittedTaskCount会减1的。
     */
    private final AtomicInteger submittedTaskCount = new AtomicInteger(0);

    public ThreadPoolExecutorExtends(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit, TaskQueue workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, new ThreadPoolExecutor.AbortPolicy());
        workQueue.setExecutor(this);
    }

    /**
     * 覆盖父类的afterExecute方法,当task执行完成后,将计数器减1
     */
    @Override
    protected void afterExecute(Runnable r, Throwable t) {
        submittedTaskCount.decrementAndGet();
    }


    public int getSubmittedTaskCount() {
        return submittedTaskCount.get();
    }


    /**
     * 覆盖父类的execute方法,在任务开始执行之前,计数器加1。
     */
    @Override
    public void execute(Runnable command) {
        submittedTaskCount.incrementAndGet();
        try {
            super.execute(command);
        } catch (RejectedExecutionException rx) {
            //当发生RejectedExecutionException,尝试再次将task丢到队列里面,如果还是发生RejectedExecutionException,则直接抛出异常。
            BlockingQueue<Runnable> taskQueue = super.getQueue();
            
            //还有另一种解决方案,就是使用另外一个线程池来执行任务,当第一个线程池抛出Reject异常时,catch住它,并使用第二个线程池处理任务。
            
            if (taskQueue instanceof TaskQueue) {
                final TaskQueue queue = (TaskQueue)taskQueue;
                if (!queue.forceTaskIntoQueue(command)) {
                    submittedTaskCount.decrementAndGet();
                    throw new RejectedExecutionException("队列已满");
                }
            } else {
                submittedTaskCount.decrementAndGet();
                throw rx;
            }
        }
    }
	
    protected void beforeExecute(Thread t, Runnable r) {
    	
    	super.beforeExecute(t, r);
		log.info(String.format("Thread %s: start %s", t,r));
		startTime.set(System.nanoTime());
    	
    }
    
    //protected void afterExecute(Runnable r, Throwable t) { }
    
    protected void terminated() { }

}

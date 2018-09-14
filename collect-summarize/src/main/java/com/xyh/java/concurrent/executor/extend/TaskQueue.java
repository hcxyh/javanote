package com.xyh.java.concurrent.executor.extend;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionException;

public class TaskQueue extends LinkedBlockingQueue<Runnable>{
	
	private ThreadPoolExecutorExtends executor;

    public TaskQueue(int capacity) {
        super(capacity);
    }

    public void setExecutor(ThreadPoolExecutorExtends exec) {
        executor = exec;
    }

    public boolean forceTaskIntoQueue(Runnable o) {
        if (executor.isShutdown()) {
            throw new RejectedExecutionException("Executor已经关闭了,不能将task添加到队列里面");
        }
        return super.offer(o);
    }

    @Override
    public  boolean offer(Runnable o) {
        int currentPoolThreadSize = executor.getPoolSize();
        //如果线程池里的线程数量已经到达最大,将任务添加到队列中
        if (currentPoolThreadSize == executor.getMaximumPoolSize()) {
            return super.offer(o);
        }
        //说明有空闲的线程,这个时候无需创建core线程之外的线程,而是把任务直接丢到队列里即可
        if (executor.getSubmittedTaskCount() < currentPoolThreadSize) {
            return super.offer(o);
        }

        //如果线程池里的线程数量还没有到达最大,直接创建线程,而不是把任务丢到队列里面
        if (currentPoolThreadSize < executor.getMaximumPoolSize()) {
            return false;
        }

        return super.offer(o);
    }
	
}

package com.xyh.java.concurrent.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ScheduledThreadPoolExecutor：程序员使用的接口。
 * DelayedWorkQueue ： 存储任务的队列。
 * ScheduledFutureTask ： 执行任务的线程。
 * @author hcxyh  2018年8月11日
 */
public abstract class ScheduledThreadPoolExecutorNote extends ScheduledThreadPoolExecutor{
	
	/**
	 * 
	 * ScheduledThreadPoolExecutor最多支持 3 个参数：核心线程数量，线程工厂，拒绝策略。
	 * 
	 */
	public ScheduledThreadPoolExecutorNote(int corePoolSize) {
		super(corePoolSize);
	}
	
	 /**
	  *    a调度一个 Runnable 类型的 task，经过 delay(时间单位由参数 unit 决定)后开始进行调度。
      */
    public abstract ScheduledFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit);

    /**
     *  a调度一个 Callable 类型的 task，经过 delay(时间单位由参数 unit 决定)后开始进行调度。
     */
    public abstract <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay, TimeUnit unit);

    /**
     * a周期性调度一个 Runnable 类型的 task，在 delay 后开始调度。
     */
    public abstract ScheduledFuture<?> scheduleAtFixedRate(Runnable command,
                                                  long initialDelay,
                                                  long period,
                                                  TimeUnit unit);

    /**
     * ScheduledExecutorService 相对于 Timer 所特有的方法，固定延迟调度。
     */
    public abstract ScheduledFuture<?> scheduleWithFixedDelay(Runnable command,
                                                     long initialDelay,
                                                     long delay,
                                                     TimeUnit unit);

	
}

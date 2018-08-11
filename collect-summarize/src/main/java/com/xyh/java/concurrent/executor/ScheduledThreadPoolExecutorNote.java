package com.xyh.java.concurrent.executor;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 
 * @author hcxyh  2018年8月11日
 *
 */
public abstract class ScheduledThreadPoolExecutorNote extends ScheduledThreadPoolExecutor{

	public ScheduledThreadPoolExecutorNote(int corePoolSize) {
		super(corePoolSize);
	}
	
	 /**
     * 调度一个 Runnable 类型的 task，经过 delay(时间单位由参数 unit 决定)后开始进行调度。
     */
    public abstract ScheduledFuture<?> schedule(Runnable command,
                                       long delay, TimeUnit unit);

    /**
     * 调度一个 Callable 类型的 task，经过 delay(时间单位由参数 unit 决定)后开始进行调度。
     */
    public abstract <V> ScheduledFuture<V> schedule(Callable<V> callable,
                                           long delay, TimeUnit unit);

    /**
     * 周期性调度一个 Runnable 类型的 task，在 delay 后开始调度。
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

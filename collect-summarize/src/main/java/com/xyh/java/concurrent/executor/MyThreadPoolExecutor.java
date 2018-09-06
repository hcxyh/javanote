package com.xyh.java.concurrent.executor;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 根据自己情况设计线程池
 * @author xyh
 *
 */
public class MyThreadPoolExecutor {
	
	private static MyThreadFactory myThreadFactory = new MyThreadFactory();
	private static MyRejectedExecutionHandler myRejectedExecutionHandler = new MyRejectedExecutionHandler();
	
	//有界的工作队列
	BlockingQueue<? extends Runnable> queue = new ArrayBlockingQueue<>(100);
	
	
	/**
	 * 我们在使用线程池的时候，需要根据使用场景来自行选择。通过corePoolSize和maximumPoolSize的搭配，
	 * 存活时间的选择，以及改变队列的实现方式，如：选择延迟队列，来实现定时任务的功能。并发包Executors中提供的一些方法确实好用，
	 * 但我们仍需有保留地去使用，这样在项目中就不会挖太多的坑。
	 * 
	 * 对于一些耗时的IO任务，盲目选择线程池往往不是最佳方案。通过异步+单线程轮询，上层再配合上一个固定的线程池，效果可能更好。
	 * 类似与Reactor模型中selector轮询处理。
	 */

}

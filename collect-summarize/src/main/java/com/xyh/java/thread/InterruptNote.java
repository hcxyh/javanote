package com.xyh.java.thread;

/**
 * 线程中断机制
 * @author hcxyh  2018年8月13日
 *
 */
public class InterruptNote {
	
	/**
	 1.关闭线程
	 下面简单的举例情况:
		比如我们会启动多个线程做同一件事，比如抢12306的火车票，我们可能开启多个线程从多个渠道买火车票，
		只要有一个渠道买到了，我们会通知取消其他渠道。这个时候需要关闭其他线程
		很多线程的运行模式是死循环，比如在生产者/消费者模式中，消费者主体就是一个死循环，它不停的从队列中接受任务，
		执行任务，在停止程序时，我们需要一种”优雅”的方法以关闭该线程
		在一些场景中，比如从第三方服务器查询一个结果，我们希望在限定的时间内得到结果，如果得不到，我们会希望取消该任务。
		总之，很多情况下我们都有关闭一个线程的需求，那么如何正确的关闭一个线程就是我们要研究的事情，
		这个事情在上一篇文章中已经讨论过了。
	线程对中断的反应
		RUNNABLE：线程在运行或具备运行条件只是在等待操作系统调度
		WAITING/TIMED_WAITING：线程在等待某个条件或超时
		BLOCKED：线程在等待锁，试图进入同步块
		NEW/TERMINATED：线程还未启动或已结束
	如果线程堵塞在object.wait、Thread.join和Thread.sleep，将会抛出InterruptedException,同时清除线程的中断状态;
	如果线程堵塞在java.nio.channels.InterruptibleChannel的IO上，Channel将会被关闭，线程被置为中断状态，并抛出java.nio.channels.ClosedByInterruptException；
	如果线程堵塞在java.nio.channels.Selector上，线程被置为中断状态，select方法会马上返回，类似调用wakeup的效果；

	 
	 如何正确地取消/关闭线程
		1. 以上，我们可以看出，interrupt方法不一定会真正”中断”线程，它只是一种协作机制，如果 不明白线程在做什么，不应该贸然的调用线程的interrupt方法，以为这样就能取消线程。
		2. 对于以线程提供服务的程序模块而言，它应该封装取消/关闭操作，提供单独的取消/关闭方法给调用者，类似于InterruptReadDemo中演示的cancel方法，外部调用者应该调用这些方法而不是直接调用interrupt。
		3. Java并发库的一些代码就提供了单独的取消/关闭方法，比如说，Future接口提供了如下方法以取消任务：boolean cancel(boolean mayInterruptIfRunning);
		4. 再比如，ExecutorService提供了如下两个关闭方法：
			void shutdown();
			List<Runnable> shutdownNow();
		5. Future和ExecutorService的API文档对这些方法都进行了详细说明，这是我们应该学习的方式。
	 
	 */
	
}

package com.xyh.java.thread;

import java.util.Map;

/**
 * 
 * @author hcxyh  2018年8月10日
 *
 */
public class ThreadNote {
	
	
	public void testStackTraceElement() {
		
		//StackTraceElement类：含有能够获得文件名和当前执行的代码行号的方法；同时， 还有能够获得类名和方法名的方法； 
		
		Throwable t = new Throwable();
		StackTraceElement[] frames = t.getStackTrace() ;
		for(StackTraceElement frame : frames ) {
			System.out.println();
		}
				   
		
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		for(Thread t1 : map.keySet())
		{
		    StackTraceElement[] framesStack = map.get(t1);
		       System.out.println();
		}
	}
	
	/**
	1.Runnable
	2.Callable  --> executorService.submit(Callable) 返回 future.get() 
					即使使用submit提交runnable,future.get取得为Null.
		1.通过自己维护一个Collections,来接收多个线程执行后返回的Future,通过在mainThread里面遍历,
		获取到线程的执行返回值.
		2.使用CompletionService类，它整合了Executor和BlockingQueue的功能。
		你可以将Callable任务提交给它去执行，然后使用类似于队列中的take方法获取线程的返回值
		CompletionService<String> cService = new ExecutorCompletionService<String>(pool);
			cService.submit(callable);
			Future<?> future = cService.take();
	3.submit()  --> 可以处理run()里面的Exception
	FutureTask<V> implements RunnableFuture<V> extends Runnable, Future<V>
	 */
	
	/**
	 * 1.yield
	 * 2.join
	 * 3.interrupt
	 * 4.dumpStack
	 */
	public static void main(String[] args) {
		
		//线程栈信息
		for (Map.Entry<Thread, StackTraceElement[]> entry : new Thread().getAllStackTraces().entrySet()) {
            System.out.print("key= " + entry.getKey() + " and value= "
                    + entry.getValue());
            for (StackTraceElement string : entry.getValue()) {
				System.out.println("----"+string);
			}
        }
	}
	
	/**

1.实现线程的方式
	extends 	Thread
	implement   Runnable
	或者使用  		TimerTask 与 Timer.scheduld().
	future   callable  
	Thread(new futureTask(new callable)).strat;
	内部类不能使用和定义static变量.
	内部类不能访问局部变量,访问需要加final.
2.线程互斥(传统)----共享的变量,要排他性
	synchronized  同步锁.
		同步代码块 		synchronized (lock){}
		同步方法     		在方法的返回值前  	synchronized.
		[
			同步方法上边用的锁就是this对象
			静态同步方法使用的锁是该方法所在的class文件对象
		]
注意： 使用synchronized关键字实现互斥，要保证同步的地方使用的是同一个锁对象，否则极易引起死锁.
小结: 锁只要是上在共享的资源方法上,并且使用同一个new出来的对象.
	  加锁之后,当这块资源使用结束之后，会让所有的其他线程抢资源.

3.线程同步
	wait   notify   notifyAll	wait 
	这几个方法都应该在synchronize 的结构体里面使用.
	等待和唤醒必须是同一把锁 。
{
	monitor:监视器，Java中的每个对象都有一个监视器，来监测并发代码的重入。
	在非多线程编码时该监视器不发挥作用，反之如果在synchronized 范围内，监视器发挥作用。
	wait/notify必须存在于synchronized块中。并且，这三个关键字针对的是同一个监视器（某对象的监视器）。
	这意味着wait之后，其他线程可以进入同步块执行。
}	
demo: 对于两个线程交替执行,可以在他们的共同用方法加锁sync,
			如果一个线程的任务执行结束,就notify唤醒其他线程.	
注意：判断唤醒等待标记时使用while增加程序健壮性，防止伪唤醒(虚假唤醒)	
4.	多个线程共享数据可以使用  static .
	线程内部独立数据,使用map<ThreadName,data> 来存取.
		/\
		||
		||
		\/
	对应的就是  ThreadLocal.
	eg:类比的就是daoUtil里面,每个线程都有自己的conn,此处使用ThreadLocal最合适.
5.  join()方法的应用场景:
		在很多情况下，主线程生成并起动了子线程，如果子线程里要进行大量的耗时的运算，主线程往往将于子线程之前结束，
		但是如果主线程处理完其他的事务后，需要用到子线程的处理结果，也就是主线程需要等待子线程执行完成之后再结束，
		这个时候就要用到join()方法了。
	join()方法的作用:
		当该线程调用join()方法时，其他线程加入到该线程,当然运行也必须在此线程结束后才能运行.
6.	Thread.yield()方法作用是：暂停当前正在执行的线程对象，并执行其他线程。
		 yield()应该做的是让当前运行线程回到可运行状态，以允许具有相同优先级的其他线程获得运行机会。
		 因此，使用yield()的目的是让相同优先级的线程之间能适当的轮转执行。
		 但是，实际中无法保证yield()达到让步目的，因为让步的线程还有可能被线程调度程序再次选中。	
		yield()从未导致线程转到等待/睡眠/阻塞状态。在大多数情况下，
		yield()将导致线程从运行状态转到可运行状态，但有可能没有效果。
	 sleep()和yield()的区别):sleep()使当前线程进入停滞状态，所以执行sleep()的线程在指定的时间内肯定不会被执行；
	 yield()只是使当前线程重新回到可执行状态，所以执行yield()的线程有可能在进入到可执行状态后马上又被执行。
7.   interrupt()的作用:中断某个线程

	 
	 */
}

package com.xyh.java.thread;


/**
 * 
 * @author hcxyh  2018年8月10日
 *
 */
public class ThreadNote {
	
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
	
	
}

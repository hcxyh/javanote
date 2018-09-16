package com.xyh.java.concurrent.executor;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.dangdang.ddframe.job.event.rdb.JobEventRdbSearch.Result;

/*
 * 获取线程池任务执行结果
 */

public class ExecutorCompletionServiceNote {

	/**
	 * 获取单个结果
	 * 过submit()向线程池提交任务后会返回一个Future，调用V Future.get()方法能够阻塞等待执行结果，
	 * V get(long timeout, TimeUnit unit)方法可以指定等待的超时时间。
	 *  获取多个结果
	 *  如果向线程池提交了多个任务，要获取这些任务的执行结果，可以依次调用Future.get()获得。
	 *  但对于这种场景，我们更应该使用ExecutorCompletionService，该类的take()方法总是阻塞等待某一个任务完成，
	 *  然后返回该任务的Future对象。向CompletionService批量提交任务后，只需调用相同次数的CompletionService.take()方法，
	 *  就能获取所有任务的执行结果，获取顺序是任意的，取决于任务的完成顺序：
	 */
	void solve(Executor executor, Collection<Callable<Result>> solvers)
			   throws InterruptedException, ExecutionException {
			    
			   CompletionService<Result> ecs = new ExecutorCompletionService<Result>(executor);// 构造器
			    
			   for (Callable<Result> s : solvers)// 提交所有任务
			       ecs.submit(s);
			        
			   int n = solvers.size();
			   for (int i = 0; i < n; ++i) {// 获取每一个完成的任务
			       Result r = ecs.take().get();
			       if (r != null) {
//			           use(r);
			       }
			   }
			}
	
//	单个任务的超时时间
//
//	V Future.get(long timeout, TimeUnit unit)方法可以指定等待的超时时间，超时未完成会抛出TimeoutException。
//
//	多个任务的超时时间
//
//	等待多个任务完成，并设置最大等待时间，可以通过CountDownLatch完成：
	public void testLatch(ExecutorService executorService, List<Runnable> tasks) 
	    throws InterruptedException{
	       
	    CountDownLatch latch = new CountDownLatch(tasks.size());
	      for(Runnable r : tasks){
	          executorService.submit(new Runnable() {
	              @Override
	              public void run() {
	                  try{
	                      r.run();
	                  }finally {
	                      latch.countDown();// countDown
	                  }
	              }
	          });
	      }
	      latch.await(10, TimeUnit.SECONDS); // 指定超时时间
	  }
	
}

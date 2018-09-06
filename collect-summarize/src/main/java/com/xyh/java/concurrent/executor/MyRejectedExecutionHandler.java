package com.xyh.java.concurrent.executor;

import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 自定义RejectedExecutionHandler。
 * 记录异常信息，选择不同处理逻辑，有交由当前线程执行任务，有直接抛出异常，再或者等待后继续添加任务等。
 * @author xyh
 * 
 *
 */
public class MyRejectedExecutionHandler implements RejectedExecutionHandler{
	
	private static final Logger Log = LoggerFactory.getLogger(MyRejectedExecutionHandler.class);

	@Override
	public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
		Log.error("向线程池添加任务被拒绝");

		//case 1.  由当前前程执行
		r.run();
		//case 2 异常抛出
		throw new RejectedExecutionException("Task"+r.toString() + "reject from "+executor.toString() );
		
	}

}

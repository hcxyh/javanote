package com.xyh.java.Exception;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 
 * @author hcxyh  2018年8月11日
 *
 */
public class StackTraceException extends Exception{
	/**
	 * Throwable
	 * 在出现异常时，就会对当前的栈帧进行快照并生成一个异常对象「这是一个比较重的操作」，
	 * 因此，对性能要求比较高的系统需要对异常设计进行优化。
	 * 重寫 fillInStackTrace()
	 * eg：JVM 的哪一个内存区不会抛异常？其它内存区会抛哪些异常？
	 */
	//org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace(e);
	public static String getStackTrace(Throwable t) {
	    StringWriter sw = new StringWriter();
	    PrintWriter pw = new PrintWriter(sw);
	    try {
	        t.printStackTrace(pw);
	        return sw.toString();
	    } finally {
	        pw.close();
	    }
	}
	
	public static long catchException() {
		long start = System.nanoTime();
		for (int i = 0; i < 100 ; i++) {
			try {
				throw new Exception();
			}catch(Exception e) {
				/**
				 * 异常发生时，会在堆上生成一个异常对象「包含当前栈帧的快照」；
				 * 然后停止当前的执行流程，将上面的异常对象从当前的 context 丢出「便于卸掉解决问题的职责」；
				 * 此刻便由异常处理机制接手，寻找能继续执行的适当地点「即异常处理函数」
				 */
			}
		}
		long end = System.nanoTime();
		return (end - start) ;
	} 
	
	@Override
	public Throwable fillInStackTrace() {
		return this;
	}

}

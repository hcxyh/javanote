package com.xyh.java.thread;


/**
 * 参照jmm,理解volatile
 * @author hcxyh  2018年8月11日
 *
 */
public class VolatileNote {
	
	/**
	 * 每个Thread都拥有自己的线程存储空间.
	 * Thread何时同步本地存储空间的数据到主存是不确定的.
	 * 
	 * 每次修改volatile变量都会同步到主存中
	 * 每次读取volatile变量的值都强制从主存读取最新的值(强制JVM不可优化volatile变量,
	 * 如JVM优化后变量读取会使用cpu缓存而不从主存中读取)
	 * 线程 A 中写入 volatile 变量之前可见的变量, 在线程 B 中读取该 volatile 变量以后, 
	 * 线程 B 对其他在 A 中的可见变量也可见. 换句话说, 写 volatile 类似于退出同步块, 而读取 volatile 类似于进入同步块
	 */
	
	/**
	 * i++ 
	 * 原因是i++和++i并非原子操作,我们若查看字节码,会发现
	 * {
	 		void f1();    
			Code:    
			0: aload_0    
			1: dup    
			2: getfield #2; //Field i:I    
			5: iconst_1    
			6: iadd    
			7: putfield #2; //Field i:I    
			10: return   
			可见i++执行了多部操作,从变量i中读取读取i的值->值+1 ->将+1后的值写回i中,
	 * }
	 *  可知加了volatile和没加volatile都无法解决非原子操作的线程同步问题。
	 */
	 private static volatile int count = 0;  
	    private static final int times = 10000;  
	  
	    public static void main(String[] args) {  
	  
	        long curTime = System.nanoTime();  
	  
	        Thread decThread = new DecThread();  
	        decThread.start();  
	  
	        System.out.println("Start thread: " + Thread.currentThread() + " i++");  
	  
	        for (int i = 0; i < times; i++) {  
	            count++;  
	        }  
	  
	        System.out.println("End thread: " + Thread.currentThread() + " i--");  
	  
	        // 等待decThread结束  
	        while (decThread.isAlive())  
	            ;  
	  
	        long duration = System.nanoTime() - curTime;  
	        System.out.println("Result: " + count);  
	        System.out.format("Duration: %.2fs\n", duration / 1.0e9);  
	    }  
	  
	    private static class DecThread extends Thread {  
	  
	        @Override  
	        public void run() {  
	            System.out.println("Start thread: " + Thread.currentThread()  
	                    + " i--");  
	            for (int i = 0; i < times; i++) {  
	                count--;  
	            }  
	            System.out  
	                    .println("End thread: " + Thread.currentThread() + " i--");  
	        }  
	    }  

}

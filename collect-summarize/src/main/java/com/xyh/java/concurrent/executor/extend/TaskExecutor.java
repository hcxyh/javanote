package com.xyh.java.concurrent.executor.extend;

import java.util.concurrent.TimeUnit;

public class TaskExecutor {
	
	private static final int CORE_SIZE = 5;

    private static final int MAX_SIZE = 10;

    private static final long KEEP_ALIVE_TIME = 30;

    private static final int QUEUE_SIZE = 5;

    static ThreadPoolExecutorExtends executor = new ThreadPoolExecutorExtends(CORE_SIZE,MAX_SIZE,KEEP_ALIVE_TIME, TimeUnit.SECONDS , new TaskQueue(QUEUE_SIZE));

    public static void main(String[] args){
        for (int i = 0; i < 15; i++) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.currentThread().sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            System.out.println("线程池中现在的线程数目是："+executor.getPoolSize()+",  队列中正在等待执行的任务数量为："+ executor.getQueue().size());
        }
    }
	

}

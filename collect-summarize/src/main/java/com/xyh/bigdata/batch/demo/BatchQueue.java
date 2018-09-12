package com.xyh.bigdata.batch.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

/**
 * 
 * 允许多个线程同时往队列里面添加任务,并且累加到一定的数量进行处理。
 * @author xyh
 *
 */
public class BatchQueue<T>{

	  private final int batchSize;
	    private final Consumer<List<T>> consumer;
	    private final int timeoutInMs;
	 
	    private AtomicBoolean isLooping = new AtomicBoolean(false);
	    private BlockingQueue<T> queue = new LinkedBlockingQueue<>();
	 
	    private AtomicLong start = new AtomicLong(System.currentTimeMillis());
	 
	    public BatchQueue(int batchSize, int timeoutInMs, Consumer<List<T>> consumer) {
	        this.batchSize = batchSize;
	        this.timeoutInMs = timeoutInMs;
	        this.consumer = consumer;
	    }
	 
	    public BatchQueue(int batchSize, Consumer<List<T>> consumer) {
	        this(batchSize, 500, consumer);
	    }
	 
	    public boolean add(T t) {
	        boolean result = queue.add(t);
	        if(!isLooping.get() && result) {
	            isLooping.set(true);
	            startLoop();
	        }
	        return result;
	    }
	 
	    public void completeAll() {
	        while (!queue.isEmpty()) {
	            drainToConsume();
	        }
	    }
	 
	    private void startLoop() {
	        new Thread(() -> {
	            start = new AtomicLong(System.currentTimeMillis());
	            while(true) {
	                long last = System.currentTimeMillis() - start.get() ;
	                if (queue.size() >= batchSize || (!queue.isEmpty() && last > timeoutInMs)) {
	                    drainToConsume();
	                } else if(queue.isEmpty()) {
	                    isLooping.set(false);
	                    break;
	                }
	            }
	        }).start();
	    }
	 
	    private void drainToConsume() {
	        List<T> drained = new ArrayList<>();
	        int num = queue.drainTo(drained, batchSize);
	        if(num > 0) {
	            consumer.accept(drained);
	            start.set(System.currentTimeMillis());
	        }
	    }
}

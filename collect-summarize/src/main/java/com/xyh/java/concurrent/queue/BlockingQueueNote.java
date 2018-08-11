package com.xyh.java.concurrent.queue;

import java.util.Collection;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * 阻塞队列
 * @author hcxyh  2018年8月9日
 * 生产者--消费者
 */
public class BlockingQueueNote {
	
	/**
	 			抛异常                  		特定值                     		 阻塞                             超时
	 插入			add(o)    		offer(o)   		put(o)       offer(o,timeout,timeunit)
	移除		    remove(o) 		poll(o)	 		take(o)      poll(o,timeout,timeunit)
	检查			element(o)		peek(o)
	
	BlockingQueue extends Queue<E> extends Collection<E>,实现类包括
		{	
			ArrayBlockingQueue(有界队列,FIFO,初始化限定数组大小)
			DelayBlockingQueue()
			LinkedBlockingQueue
			PriorityBlockingQueue
			SynchrnonusQueue : 内部同时只能够容纳单个元素,当该队列存在元素时,插入会阻塞.
		}
	 */
	
	public static void main(String[] args) {
	}
	
	public static <E> void testLinkedBlockingQueue() {
		BlockingQueue<E> linkedBlockingQueue = new LinkedBlockingQueue<>();
		
		/**
		 * String implements compareable,队列内的优先级依赖于compare.
		 * PriorityBlockingQueue 获得一个 Iterator 的话，该 Iterator 并不能保证它对元素的遍历是以优先级为序的
		 */
		BlockingQueue<String> priorityBlockingQueue = new PriorityBlockingQueue();
	}
	
	@SuppressWarnings("unchecked")
	public static void testDelayQueue() {
		DelayQueue queue = new DelayQueue<MyDelayObject>();
		queue.add(new MyDelayObject(1,10));      //remove
		queue.offer(new MyDelayObject(2, 10));	 //poll
		queue.put(new MyDelayObject(3, 10));	 //take
		queue.offer(new MyDelayObject(4, 10),1,TimeUnit.SECONDS);
	}
	
	public static void testArrayBlockingQueue() {
		BlockingQueue<String> b = new ArrayBlockingQueue<>(100);
	}
	
}

class MyDelayObject implements Delayed{
	
	private int delayId;
	private long timeout;
	
	//队列里面的排序规则
	@Override
	public int compareTo(Delayed other) {
		MyDelayObject otherDelayObject = (MyDelayObject)other;
		return this.delayId > otherDelayObject.delayId ?  1 : -1 ;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		//返回距离自定义的超时还剩多长时间
		return unit.convert(timeout - System.nanoTime(), TimeUnit.NANOSECONDS);
	}

	public MyDelayObject(int delayId, long timeout) {
		super();
		this.delayId = delayId;
		this.timeout = timeout;
	}
	
	
}


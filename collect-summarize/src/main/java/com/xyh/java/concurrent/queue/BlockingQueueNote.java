package com.xyh.java.concurrent.queue;

import java.util.Queue;
import java.util.concurrent.BlockingQueue;

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
	
	BlockingQueue是个接口,实现类包括
		{
			ArrayBlockingQueue(有界队列,FIFO,初始化限定数组大小)
			DelayBlockingQueue()
			LinkedBlockingQueue
			PriorityBlockingQueue
			SynchrnonusQueue
		}
	 */
	
	public static void main(String[] args) {
		
		
	}
	
	
	
	
}

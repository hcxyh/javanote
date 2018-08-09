package com.xyh.java.collection.queue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.TimeUnit;

/**
 * 测试生成订单,未支付自动过期.
 * @author hcxyh 2018年8月9日
 */
public class DelayQueueDemo {
	public static void main(String[] args) {
		List<String> list = new ArrayList<String>();
		list.add("00000001");
		list.add("00000002");
		list.add("00000003");
		list.add("00000004");
		list.add("00000005");

		DelayQueue<OrderDelay> queue = new DelayQueue<OrderDelay>();

		long start = System.currentTimeMillis();

		for (int i = 0; i < 5; i++) {
			System.out.println("order build : id = " + i);

			queue.put(new OrderDelay(list.get(i), TimeUnit.NANOSECONDS.convert(10, TimeUnit.SECONDS)));
		}

		while (true) {
			try {
				queue.take().print();  
				System.out.println("After " + (System.currentTimeMillis() - start) + " MilliSeconds");
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}

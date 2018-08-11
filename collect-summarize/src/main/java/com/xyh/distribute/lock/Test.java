package com.xyh.distribute.lock;

public class Test {
	
	/**
	 * 1.分布式锁产生的原因?
	 * 在我们将应用拆分为分布式应用之前的单机系统中，对一些并发场景读取公共资源时如扣库存，
	 * 卖车票之类的需求可以简单的使用同步（http://crossoverjie.top/2018/01/14/Synchronize/）
	 * 或者是加锁（http://crossoverjie.top/2018/01/25/ReentrantLock/）就可以实现。
	 * 2.但是应用分布式了之后系统由以前的单进程多线程的程序变为了多进程多线程，这时使用以上的解决方案明显就不够了
	 3.解决方案
	 	1.基于 DB 的唯一索引。
		2.基于 ZK 的临时有序节点。
		3.基于 Redis 的 NX EX 参数。
	 
	 
	 
	 */
}

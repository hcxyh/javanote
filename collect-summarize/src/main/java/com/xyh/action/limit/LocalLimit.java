package com.xyh.action.limit;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.common.util.concurrent.RateLimiter;

/**
 * TODO 可供参考的限流框架  https://github.com/wangzheng0822/ratelimiter4j 
 * @author hcxyh  2018年8月8日
 * 限制总并发数（比如数据库连接池、线程池）、限制瞬时并发数（如nginx的limit_conn模块，
 * 用来限制瞬时并发连接数）、限制时间窗口内的平均速率
 * （如Guava的RateLimiter、nginx的limit_req模块，限制每秒的平均速率）；
 * 其他还有如限制远程接口调用速率、限制MQ的消费速率。
 * 另外还可以根据网络连接数、网络流量、CPU或内存负载等来限流。
 */
public class LocalLimit {
	
	/**
	 * 比较 ：
	 令牌桶和漏桶对比：
		令牌桶是按照固定速率往桶中添加令牌，请求是否被处理需要看桶中令牌是否足够，当令牌数减为零时则拒绝新的请求；
		漏桶则是按照常量固定速率流出请求，流入请求速率任意，当流入的请求数累积到漏桶容量时，则新流入的请求被拒绝；
		令牌桶限制的是平均流入速率（允许突发请求，只要有令牌就可以处理，支持一次拿3个令牌，4个令牌），并允许一定程度突发流量；
		漏桶限制的是常量流出速率（即流出速率是一个固定常量值，比如都是1的速率流出，而不能一次是1，下次又是2），从而平滑突发流入速率；
		令牌桶允许一定程度的突发，而漏桶主要目的是平滑流入速率；
		两个算法实现可以一样，但是方向是相反的，对于相同的参数得到的限流效果是一样的。
		另外有时候我们还使用计数器来进行限流，主要用来限制总并发数，比如数据库连接池、线程池、秒杀的并发数；只要全局总请求数或者一定时间段的总请求数设定的阀值则进行限流，是简单粗暴的总数量限流，而不是平均速率限流。
	 */
	
	
	/**
	 * 1.令牌桶算法
		令牌桶算法的原理是系统会以一个恒定的速度往桶里放入令牌，而如果请求需要被处理，则需要先从桶里获取一个令牌，
		当桶里没有令牌可取时，则拒绝服务。 当桶满时，新添加的令牌被丢弃或拒绝。	
		在 GoogleGuava中提供了一个RateLimiter 工具类，就是基于 令牌桶算法实现平滑突发的限流策略，
		令牌桶的好处是可以方便的改变速度. 一旦需要提高速率,则按需提高放入桶中的令牌的速率. 
		一般会定时(比如1000毫秒)往桶中增加一定数量的令牌, 有些变种算法则可以实时的计算应该增加的令牌的数量
	 */
	public static class TokenSegment{
		//令牌桶算法,每秒生成两个令牌
		private static final RateLimiter limiter = RateLimiter.create(1);
		
		private static void rateLimiter() {
			//不阻塞,尝试获取令牌.
			limiter.tryAcquire();
			//获取令牌,返回值为阻塞的时间.
			final double acquire = limiter.acquire(1);
			System.out.println("当前时间-" + LocalDateTime.now() + "-" + Thread.currentThread().getName() + "-阻塞-" +acquire+ "通过");
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		TokenSegment tokenSegment =  new TokenSegment();
		final ExecutorService executorService = Executors.newFixedThreadPool(5);
		for (int i = 0; i < 10; i++) {
			tokenSegment.rateLimiter();
		}
		TimeUnit.SECONDS.sleep(5);
	}
	
	
	/**
	 * 漏桶算法
	         其主要目的是控制数据注入到网络的速率，平滑网络上的突发流量，数据可以以任意速度流入到漏桶中。
	         漏桶算法提供了一种机制，通过它，突发流量可以被整形以便为网络提供一个稳定的流量。
	          漏桶可以看作是一个带有常量服务时间的单服务器队列，如果漏桶为空，则不需要流出水滴，如果漏桶（包缓存）溢出，那么水滴会被溢出丢弃。
	 */
	public static class SemaphoreLimiterTest{
		/**
		 * 计数器限流算法(允许将任务放入到缓冲队列)
		 * 信号量,用来达到削峰的目的
		 */
		//初始信号量为3，表示最多可以同时处理 3 个任务
		private static final Semaphore semphore = new Semaphore(3);
		
		public void semphoreLimiter() {
			//队列中允许存活的任务个数不超过5个
			if(semphore.getQueueLength() > 5) {
				System.out.println(LocalDateTime.now() + "-" + Thread.currentThread().getName() + "- 拒绝");
			} else {
				try {
					semphore.acquire();
					System.out.println(LocalDateTime.now() + "-" + Thread.currentThread().getName() + "- 通过");
					//处理核心逻辑
					TimeUnit.SECONDS.sleep(2);
				}catch(InterruptedException e) {
					e.printStackTrace();
				} finally {
					semphore.release();
				}
			}
		}
	}
	
	/**
	 * 计数器限流
	 * 计数器限流算法是比较常用一种的限流方案也是最为粗暴直接的，主要用来限制总并发数，
	 * 比如数据库连接池大小、线程池大小、接口访问并发数等都是使用计数器算法...
	 * 使用 AomicInteger 来进行统计当前正在并发执行的次数，如果超过域值就直接拒绝请求，提示系统繁忙
	 */
	public static class AtomicLimiterTest{
		private static final AtomicInteger atomicInteger = new AtomicInteger(0);
		
		public void atomicLimter() {
			if(atomicInteger.get() >= 3) {
				System.out.println(LocalDateTime.now() + "-" + Thread.currentThread().getName() + "- 拒绝");
			} else {
				try {
					atomicInteger.getAndIncrement();
					//处理核心逻辑
					System.out.println(LocalDateTime.now() + "-" + Thread.currentThread().getName() + "- 通过");
					TimeUnit.SECONDS.sleep(1);
				}catch(InterruptedException e) {
					e.printStackTrace();
				} finally {
					atomicInteger.decrementAndGet();
				}
			}
		}
	}
}

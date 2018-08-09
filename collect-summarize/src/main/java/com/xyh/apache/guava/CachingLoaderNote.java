package com.xyh.apache.guava;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalCause;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.Weigher;
import com.google.common.util.concurrent.RateLimiter;

/**
 * 
 * @author hcxyh  2018年8月9日
 *
 */
public class CachingLoaderNote{
	
	private static final Logger log = LoggerFactory.getLogger(CachingLoaderNote.class); 
	
	/**
	   加载:
	 	1.显示加载缓存数据   
	 		cache.put   //显示加载
	 	2.自动加载
	 		LoadingCache(CacheLoader)在 cache.get(key)不存在的时候,会自动加载值得信息并放入缓存中.
	 	3.权重（感觉用的比较少）不同的缓存项有不同的“权重”（weights）——例如，如果你的缓存值，占据完全不同的内存空间，
	 	你可以使用CacheBuilder.weigher(Weigher)指定一个权重函数，
	 	并且用CacheBuilder.maximumWeight(long)指定最大总重。在权重限定场景中，
	 	除了要注意回收也是在重量逼近限定值时就进行了，还要知道重量是在缓存创建时计算的，因此要考虑重量计算的复杂度
	 	4.缓存回收
	 		1.容量回收、-->
	 			如果要规定缓存项的数目不超过固定值，只需使用CacheBuilder.maximumSize(long)。
	 			缓存将尝试回收最近没有使用或总体上很少使用的缓存项。——警告：在缓存项的数目达到限定值之前，缓存就可能进行回收操作
	 			——通常来说，这种情况发生在缓存项的数目逼近限定值时。
	 		2.定时回收
	 			1.expireAfterAccess(long, TimeUnit)：缓存项在给定时间内没有被读/写访问，则回收。
	 				请注意这种缓存的回收顺序和基于大小回收一样。
				2.expireAfterWrite(long, TimeUnit)：缓存项在给定时间内没有被写访问（创建或覆盖），则回收。
					如果认为缓存数据总是在固定时候后变得陈旧不可用，这种回收方式是可取的。
	 		3.引用回收
	 	5.显示清除
	 		个别清除：Cache.invalidate(key) 
			批量清除：Cache.invalidateAll(keys) 
			清除所有缓存项：Cache.invalidateAll()
	 	6.移除监听器
	 	7.统计
	 		CacheBuilder.recordStats()用来开启Guava Cache的统计功能。
	 		统计打开后，Cache.stats()方法会返回CacheStats对象以提供如下统计信息：
				1.hitRate()：缓存命中率；
				2.averageLoadPenalty()：加载新值的平均时间，单位为纳秒；
				3.evictionCount()：缓存项被回收的总数，不包括显式清除。
	 */
	
	private static Cache<String, Object> cache = CacheBuilder.newBuilder().build();
	
	public static void getTest() throws ExecutionException{
		/**
		 * 所有类型的Guava Cache，不管有没有自动加载功能，都支持get(K, Callable)方法。
		 * 这个方法返回缓存中相应的值，或者用给定的Callable运算并把结果加入到缓存中。在整个加载方法完成前，
		 * 缓存项相关的可观察状态都不会更改。这个方法简便地实现了模式”如果有缓存则返回；否则运算、缓存、然后返回”。
		 */
		try {
			cache.get("key", new Callable<Object>() {
				@Override
				public Object call() throws Exception {
					//dosomething();
					return null;
				}
			});
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
		
		/**
		 * 不会重新加载创建cache
		 */
		rateLimiterCache.get("key");
		rateLimiterCache.getUnchecked("key");
		cache.getIfPresent("new key"); 
	}
	
	
	private static LoadingCache<String, RateLimiter> rateLimiterCache = CacheBuilder
			.newBuilder()
			// 设置缓存最大容量为10000，超过100之后就会按照LRU最近最少使用算法来移除缓存项
			.maximumSize(10000)
			// 设置缓存容器的初始容量为100
			.initialCapacity(100)
			// 设置并发级别为100，并发级别是指可以同时写缓存的线程数
			.concurrencyLevel(100)
			// 设置写缓存后1800秒钟过期
			.expireAfterWrite(1800, TimeUnit.SECONDS)	//Write time => write/update
			.expireAfterAccess(1800, TimeUnit.SECONDS)  //Access time => Write/Update/Read
			.build(new CacheLoader<String, RateLimiter>() {
				@Override
				public RateLimiter load(String key) throws Exception {
					return RateLimiter.create(100);
				}
			});
	
	public static boolean rateLimit(String resource) {
		RateLimiter limit = null;
		try {
			limit = rateLimiterCache.get(resource);
			return limit.tryAcquire();
		} catch (ExecutionException e) {
			System.out.println("限流创建失败：" + e.getMessage());
		}
		return false;
	}

	public void testWeight() throws ExecutionException, InterruptedException {
        //如果不同的高速缓存条目有不同的“权重”，例如，如果你的缓存值有着完全不同的记忆的足迹--你可以用cachebuilder指定一个权重函数。秤（秤）和一个cachebuilder最大缓存量的最大重量（长）
        LoadingCache<String, CacheObj> cache = CacheBuilder.newBuilder()
                .maximumWeight(150)
                .weigher(new Weigher<String, CacheObj>() {
                    public int weigh(String key, CacheObj cacheObj) {
                        int weight = cacheObj.getCacheName().length() + cacheObj.getCacheId().length() ;//权重计算器
                        log.info("weight is :" + weight);
                        return weight;
                    }
                })
                .build(CachingLoaderNote.createCacheLoader());
        cache.get("xyh");
        log.info("cacheSize：" + cache.size());
        cache.get("xyh01");
        log.info("cacheSize：" + cache.size());
        cache.get("xyh02");
        log.info("cacheSize：" + cache.size());
        cache.get("xyh03");
        log.info("cacheSize：" + cache.size());
    }
	
	
    public static void testSize() throws ExecutionException, InterruptedException {
        LoadingCache<String, CacheObj> cache = CacheBuilder.newBuilder()
                .maximumSize(3)
                .build(CachingLoaderNote.createCacheLoader());
        /**
         * 如果你定义的CacheLoader没有声明任何检查型异常，则可以通过getUnchecked(K)查找缓存；
         * 但必须注意，一旦CacheLoader声明了检查型异常，就不可以调用getUnchecked(K)。
         */
        cache.getUnchecked("xyh");
        cache.getUnchecked("xyh01");
        cache.getUnchecked("xyh02");
        System.out.println(cache.size());

        cache.getUnchecked("xyh03");
        CacheObj cacheObj = cache.getIfPresent("xyh03"); //不会重新加载创建cache
        log.info("最新的把老的替换掉：" + (cacheObj == null ? "是的" : "否"));
        CacheObj newCacheObj = cache.getIfPresent("xyh04"); //不会重新加载创建cache
        log.info("获取结果：" + newCacheObj);
    }
	
	
	 public static CacheLoader<String, CacheObj> createCacheLoader() {
	       return new CacheLoader<String, CacheObj>() {
	           @Override
	           public CacheObj load(String key) throws Exception {
	               log.info("加载创建key:" + key);
	               return new CacheObj(key, key + "id", key + "group");
	           }
	       };
	  }
	
	 public void testEvictionByAccessTime() throws ExecutionException, InterruptedException {
	      LoadingCache<String, CacheObj> cache = CacheBuilder.newBuilder()
	              .expireAfterAccess(2, TimeUnit.SECONDS)
	              .build(CachingLoaderNote.createCacheLoader());
	      cache.getUnchecked("nana");
	      TimeUnit.SECONDS.sleep(3);
	      CacheObj cacheObj = cache.getIfPresent("xyh"); //不会重新加载创建cache
	      log.info("被销毁：" + (cacheObj == null ? "是的" : "否"));
	      cache.getUnchecked("guava");

	      TimeUnit.SECONDS.sleep(2);
	      cacheObj = cache.getIfPresent("guava"); //不会重新加载创建cache
	      log.info("被销毁：" + (cacheObj == null ? "是的" : "否"));

	      TimeUnit.SECONDS.sleep(2);
	      cacheObj = cache.getIfPresent("guava"); //不会重新加载创建cache
	      log.info("被销毁：" + (cacheObj == null ? "是的" : "否"));
	  }
	 
	 /**
	  * 基于引用的回收（Reference-based Eviction）强（strong）、软（soft）、弱（weak）、虚（phantom）
	  * 通过使用弱引用的键、或弱引用的值、或软引用的值，Guava Cache可以把缓存设置为允许垃圾回收：
	  * CacheBuilder.weakKeys()：使用弱引用存储键。当键没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用键的缓存用==而不是equals比较键。 
	  * CacheBuilder.weakValues()：使用弱引用存储值。当值没有其它（强或软）引用时，缓存项可以被垃圾回收。因为垃圾回收仅依赖恒等式（==），使用弱引用值的缓存用==而不是equals比较值。 
	  * CacheBuilder.softValues()：使用软引用存储值。软引用只有在响应内存需要时，才按照全局最近最少使用的顺序回收。考虑到使用软引用的性能影响，我们通常建议使用更有性能预测性的缓存大小限定（见上文，基于容量回收）。使用软引用值的缓存同样用==而不是equals比较值。
	  */
	 public void testWeakKey() throws ExecutionException, InterruptedException {
	      LoadingCache<String, CacheObj> cache = CacheBuilder.newBuilder()
	              .weakValues()
	              .weakKeys()
	              .softValues()
	              .build(CachingLoaderNote.createCacheLoader());
	      cache.getUnchecked("guava");
	      cache.getUnchecked("xyh");

	      System.gc();
	      TimeUnit.MILLISECONDS.sleep(100);
	      CacheObj cacheObj = cache.getIfPresent("guava"); //不会重新加载创建cache
	      log.info("被销毁：" + (cacheObj == null ? "是的" : "否"));
	  }
	 
	 /**
	  * 通过CacheBuilder.removalListener(RemovalListener)，你可以声明一个监听器，以便缓存项被移除时做一些额外操作。
	  * 缓存项被移除时，RemovalListener会获取移除通知[RemovalNotification]，其中包含移除原因[RemovalCause]、键和值。
	  */
	 public void testCacheRemovedNotification() {
	        CacheLoader<String, String> loader = CacheLoader.from(String::toUpperCase);
	        RemovalListener<String, String> listener = notification ->
	        {
	            if (notification.wasEvicted()) {
	                RemovalCause cause = notification.getCause();
	                log.info("remove cacase is :" + cause.toString());
	                log.info("key:" + notification.getKey() + "value:" + notification.getValue());
	            }
	        };
	        LoadingCache<String, String> cache = CacheBuilder.newBuilder()
	                .maximumSize(3)
	                .removalListener(listener)// 添加删除监听
	                .build(loader);
	        cache.getUnchecked("xyh");
	        cache.getUnchecked("xyh01");
	        cache.getUnchecked("guava");
	        cache.getUnchecked("test");
	        cache.getUnchecked("test1");
	    }
	 
	 
	 /**
	  * TODO main
	  */
	 public static void main(String[] args) throws ExecutionException, InterruptedException {
		 testSize();
	}
}

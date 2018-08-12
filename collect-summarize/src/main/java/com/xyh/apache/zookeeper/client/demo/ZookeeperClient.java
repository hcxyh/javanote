package com.xyh.apache.zookeeper.client.demo;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCache.StartMode;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
* @ClassName: ZookeeperClient
* @author xueyh
* @date 2018年1月30日 下午6:57:29
* 
*/
public class ZookeeperClient {
	
	 public static CuratorFrameworkFactory.Builder builder = CuratorFrameworkFactory  
	            .builder().connectionTimeoutMs(1000)  
	            .connectString(CommParam.CONNECT_URL).defaultData("0".getBytes())  
	            .namespace("curator")  
	            .retryPolicy(new ExponentialBackoffRetry(3000, 3))  
	            //.maxCloseWaitMs(5000)
	            .threadFactory(new ThreadFactory() {  
	                public final AtomicInteger counter = new AtomicInteger(0);  
	                public Thread newThread(Runnable r) {  
	                    Thread thread = new Thread(r, "curator-"  
	                            + counter.getAndIncrement());  
	                    thread.setDaemon(true);  
	                    return thread;  
	                }  
	            });;  
	  
	    ZookeeperClient() {  
	    }  
	  
	    public CuratorFrameworkFactory.Builder getZookeeperInstance() {  
	        return builder;  
	    }  
}

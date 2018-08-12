package com.xyh.apache.zookeeper.client.demo;

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
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

/**
* @ClassName: CuratorClient
* @Description: TODO http://www.cnblogs.com/leesf456/p/6032716.html
* @author xueyh
* @date 2018年1月24日 下午4:40:51
* 
*/
public class CuratorClient {
	
	public static void main(String[] args) throws Exception {
		
		String path = "/curator-file";
		
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 5000, 3000, retryPolicy);
        client.start();
        System.out.println("Zookeeper session1 established. ");
        
        /**
         * 值得注意的是session2会话含有隔离命名空间，
         * 即客户端对Zookeeper上数据节点的任何操作都是相对/base目录进行的，
         * 这有利于实现不同的Zookeeper的业务之间的隔离。
         */
        
        //创建会话并声明重试机制
        CuratorFramework client1 = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .sessionTimeoutMs(5000).retryPolicy(retryPolicy).namespace("base").build();
        client1.start();
        System.out.println("Zookeeper session2 established. ");  
        
        //创建节点
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
        System.out.println("success create znode: " + path);
        
        //获取数据
        Stat stat = new Stat();
        System.out.println(new String(client.getData().storingStatIn(stat).forPath(path)));
        
        //删除节点
        client.delete().deletingChildrenIfNeeded().withVersion(stat.getVersion()).forPath(path);
        System.out.println("success delete znode " + path);
        
        //更新数据(结果表明当携带数据版本不一致时，无法完成更新操作。)
        client.getData().storingStatIn(stat).forPath(path);
        System.out.println("Success set node for : " + path + ", new version: "
                + client.setData().withVersion(stat.getVersion()).forPath(path).getVersion());
        try {
            client.setData().withVersion(stat.getVersion()).forPath(path);
        } catch (Exception e) {
            System.out.println("Fail set node due to " + e.getMessage());
        }
        
        //节点监听
        client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(path, "init".getBytes());
        final NodeCache cache = new NodeCache(client, path, false); //获取该节点
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            public void nodeChanged() throws Exception {
                System.out.println("Node data update, new data: " + new String(cache.getCurrentData().getData()));
            }
        });
        client.setData().forPath(path, "u".getBytes());
        Thread.sleep(1000);
        client.delete().deletingChildrenIfNeeded().forPath(path);
        Thread.sleep(Integer.MAX_VALUE);
    }
	
	//监听子节点(监听节点的子节点，包括新增、数据变化、删除三类事件)
	public static void listenSonNode(CuratorFramework client,String path) throws Exception{
		 client.start();
	        PathChildrenCache cache = new PathChildrenCache(client, path, true);
	        cache.start(StartMode.POST_INITIALIZED_EVENT);
	        cache.getListenable().addListener(new PathChildrenCacheListener() {
	            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
	                switch (event.getType()) {
	                case CHILD_ADDED:
	                    System.out.println("CHILD_ADDED," + event.getData().getPath());
	                    break;
	                case CHILD_UPDATED:
	                    System.out.println("CHILD_UPDATED," + event.getData().getPath());
	                    break;
	                case CHILD_REMOVED:
	                    System.out.println("CHILD_REMOVED," + event.getData().getPath());
	                    break;
	                default:
	                    break;
	                }
	            }
	        });
	        client.create().withMode(CreateMode.PERSISTENT).forPath(path);
	        Thread.sleep(1000);
	        client.create().withMode(CreateMode.PERSISTENT).forPath(path + "/c1");
	        Thread.sleep(1000);
	        client.delete().forPath(path + "/c1");
	        Thread.sleep(1000);
	        client.delete().forPath(path);
	        Thread.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * 借助Zookeeper，开发者可以很方便地实现Master选举功能，
	 * 其大体思路如下：选择一个根节点，如/master_select，
	 * 多台机器同时向该节点创建一个子节点/master_select/lock，
	 * 利用Zookeeper特性，最终只有一台机器能够成功创建，成功的那台机器就是Master。
	 * 
	 * 
	 * 以上结果会反复循环，并且当一个应用程序完成Master逻辑后，
	 * 另外一个应用程序的相应方法才会被调用，即当一个应用实例成为Master后，
	 * 其他应用实例会进入等待，直到当前Master挂了或者推出后才会开始选举Master
	 */
	public static void recipesMasterSelect(CuratorFramework client,String path) throws InterruptedException{
		client.start();
        LeaderSelector selector = new LeaderSelector(client, path, new LeaderSelectorListenerAdapter() {
            public void takeLeadership(CuratorFramework client) throws Exception {
                System.out.println("成为Master角色");
                Thread.sleep(3000);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
	}
	
	
	
}

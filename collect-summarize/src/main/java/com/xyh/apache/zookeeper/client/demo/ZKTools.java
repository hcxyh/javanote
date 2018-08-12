package com.xyh.apache.zookeeper.client.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.barriers.DistributedBarrier;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.retry.ExponentialBackoffRetry;

/**
* @ClassName: ZKTools
* @author xueyh
* @date 2018年1月25日 下午3:27:34
*/
public class ZKTools {
	
	/**
	 * 1.节点监听
	 * 2.子节点监听
	 * http://www.cnblogs.com/leesf456/p/6032716.html
	 */
	
	
	/**
	 * TODO master选举：
	 * 选择一个根节点，如/master_select，
	 * 多台机器同时向该节点创建一个子节点/master_select/lock，
	 * 利用Zookeeper特性，最终只有一台机器能够成功创建，成功的那台机器就是Master。
	 */
    public static void masterSelect(String masterPath) throws InterruptedException{
    	
    	String master_path = "/curator_recipes_master_path";
    	
        CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
        
        client.start();
        LeaderSelector selector = new LeaderSelector(client, master_path, new LeaderSelectorListenerAdapter() {
        	public void takeLeadership(CuratorFramework client) throws Exception {
        		//当该方法称为master的时候才会进入
                System.out.println("成为Master角色");
                Thread.sleep(3000);
                System.out.println("完成Master操作，释放Master权利");
            }
        });
        selector.autoRequeue();
        selector.start();
        Thread.sleep(Integer.MAX_VALUE);
    }
    
    /**
     * 分布式锁
     * 场景:生成流水号
     */
    public static void recipesLock(){
    	 String lock_path = "/curator_recipes_lock_path";
         CuratorFramework client = CuratorFrameworkFactory.builder().connectString("127.0.0.1:2181")
                .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
         
         client.start();
         //类比这semphore来学习
         final InterProcessMutex lock = new InterProcessMutex(client, lock_path);
         final CountDownLatch down = new CountDownLatch(1);
         
         for (int i = 0; i < 30; i++) {
             new Thread(new Runnable() {
                 public void run() {
                     try {
                         down.await();
                         lock.acquire();
                     } catch (Exception e) {
                     }
                     SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss|SSS");
                     String orderNo = sdf.format(new Date());
                     System.out.println("生成的订单号是 : " + orderNo);
                     try {
                         lock.release();
                     } catch (Exception e) {
                     }
                 }
             }).start();
         }
         down.countDown();
    }
    
    
    public static void recipesBarrier() throws Exception{
    	 final String barrier_path = "/curator_recipes_barrier_path";
         DistributedBarrier barrier = null;

            for (int i = 0; i < 5; i++) {
                new Thread(new Runnable() {
                	
                	DistributedBarrier barrier = null;
                	
                    public void run() {
                        try {
                            CuratorFramework client = CuratorFrameworkFactory.builder()
                                    .connectString("127.0.0.1:2181")
                                    .retryPolicy(new ExponentialBackoffRetry(1000, 3)).build();
                            client.start();
                            barrier = new DistributedBarrier(client, barrier_path);
                            System.out.println(Thread.currentThread().getName() + "号barrier设置");
                            barrier.setBarrier();
                            barrier.waitOnBarrier();
                            System.err.println("启动...");
                        } catch (Exception e) {
                        }
                    }
                }).start();
            }
            Thread.sleep(2000);
            barrier.removeBarrier();
    }
	
}

package com.xyh.apache.zookeeper.client.demo;

import java.util.concurrent.TimeUnit;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.locks.InterProcessMutex;
import org.apache.curator.utils.CloseableUtils;

/**
* @ClassName: SeqTask
* @author xueyh
* @date 2018年1月30日 下午6:55:40
*/
public class SeqTask {
	
	public static String getSequence(){  
	       CuratorFramework client = new ZookeeperClient().getZookeeperInstance().build();  
	       client.start();  
	       InterProcessMutex lock = new InterProcessMutex(client, CommParam.LOCK_ZNODE);  
	       try {  
	           boolean retry=true;  
	           byte[] newData = null;  
	           do{  
	               if (lock.acquire(1000, TimeUnit.MILLISECONDS)) {  
	                   byte[] oldData = client.getData().forPath(CommParam.LOCK_ZNODE);  
	                   String s = new String(oldData);  
	                   if("".equals(s)){  
	                    s=CommParam.INIT_VAL;  
	                   }  
	                   int d = Integer.parseInt(s);  
	                   d = d + 1;  
	                   s = String.valueOf(d);  
	                   newData = s.getBytes();  
	                   client.setData().forPath(CommParam.LOCK_ZNODE, newData);  
	                   retry=false;  
	               }  
	           }while(retry);  
	             
	           return new String(newData);  
	             
	       } catch (Exception e) {  
	           e.printStackTrace();  
	             
	           return null;  
	       } finally {  
	           try {  
	               if (lock.isAcquiredInThisProcess()) {  
	                   lock.release();  
	               }  
	           } catch (Exception e) {  
	               e.printStackTrace();  
	           } finally {  
	               CloseableUtils.closeQuietly(client);  
	           }  
	       }  
	   }  
	
	
}

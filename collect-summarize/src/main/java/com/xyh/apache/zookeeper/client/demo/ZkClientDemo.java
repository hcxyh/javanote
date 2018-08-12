package com.xyh.apache.zookeeper.client.demo;

import java.util.List;

import org.I0Itec.zkclient.IZkChildListener;
import org.I0Itec.zkclient.IZkDataListener;
import org.I0Itec.zkclient.ZkClient;
import org.apache.zookeeper.CreateMode;

/**
 * 
 * @ClassName: ZkClientDemo
 * https://www.jianshu.com/p/80053406f08e?utm_campaign=maleskine&utm_content=note&utm_medium=pc_all_hots&utm_source=recommendation
 * @author xueyh
 * @date 2018年1月1日 上午8:37:40
 * http://www.cnblogs.com/leesf456/p/6032716.html
 */
public class ZkClientDemo {
	
	private static final String ZK_SERVER_URL = "127.0.0.1:2181";
	
	private static final int sessionTimeOut = 5000;
	
	private static final String PATH = "/zk-client";
	
	public static void main(String[] args) {
		
		//创建会话
		ZkClient zkClient = new ZkClient(ZK_SERVER_URL,sessionTimeOut);
		
		zkClient.create(PATH+"/child", "data", CreateMode.EPHEMERAL);
		
		zkClient.readData(PATH+"");
		
		zkClient.writeData(PATH+"", "data");
		
		//节点是否存在
		zkClient.exists(PATH);
		
		/**
		 * zkCliet解决了watch一次性注册的问题。
		 * 将所有的事件都归并为子节点的变化.数据的变化，和连接状态的变化三类
		 * IZkChildListener,IZkDataListener,IZkConnection.
		 */
		
		
		/**
		 * 客户端可以对一个不存在的节点进行子节点变更的监听。
		 * 一旦客户端对一个节点注册了子节点列表变更监听之后，
		 * 那么当该节点的子节点列表发生变更时，服务端都会通知客户端，并将最新的子节点列表发送给客户端
		 */
		zkClient.subscribeChildChanges(PATH, new IZkChildListener() {
			//获取子节点
			@Override
			public void handleChildChange(String parentPath, List<String> currentChilds) throws Exception {
                System.out.println(parentPath + " 's child changed, currentChilds:" + currentChilds);
            }
        });
		
		zkClient.subscribeDataChanges(PATH, new IZkDataListener() {
			//获取数据
            public void handleDataDeleted(String dataPath) throws Exception {
                System.out.println("Node " + dataPath + " deleted.");
            }
            public void handleDataChange(String dataPath, Object data) throws Exception {
                System.out.println("Node " + dataPath + " changed, new data: " + data);
            }
        });
		
		//直接删除存在子节点的父节点
		zkClient.deleteRecursive(PATH+"");
		zkClient.delete(PATH);
	}
	
	/**
	 * ZkClient提供了递归创建节点的接口，即其帮助开发者完成父节点的创建，再创建子节点。
	 */
	public static void createZnode(ZkClient zkClient,String path){
		zkClient.createPersistent(path, true);
        System.out.println("success create znode.");
        zkClient.createEphemeral(path, "123");
	} 
	
}	

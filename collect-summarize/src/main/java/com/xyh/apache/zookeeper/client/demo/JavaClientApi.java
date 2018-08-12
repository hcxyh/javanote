package com.xyh.apache.zookeeper.client.demo;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.AsyncCallback;
import org.apache.zookeeper.AsyncCallback.VoidCallback;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
* @ClassName: JavaClientApi
* @author xueyh
* @date 2018年1月24日 上午11:16:12
* TODO https://www.cnblogs.com/leesf456/p/6028416.html 
*/
public class JavaClientApi implements Watcher{
	
	//Exception in thread "main" org.apache.zookeeper.KeeperException$ConnectionLossException:
	//KeeperErrorCode = ConnectionLoss for /test
	public static ZooKeeper getInstance() throws IOException, InterruptedException {  
        //--------------------------------------------------------------  
        // 为避免连接还未完成就执行zookeeper的get/create/exists操作引起的（KeeperErrorCode = ConnectionLoss)  
        // 这里等Zookeeper的连接完成才返回实例  
        //--------------------------------------------------------------  
        final CountDownLatch connectedSignal = new CountDownLatch(1);  
        ZooKeeper zk = new ZooKeeper(zk_url, timeout, new Watcher() {  
             @Override  
             public void process(WatchedEvent event) {  
                 if  (event.getState()  ==  Event.KeeperState.SyncConnected) {  
                     connectedSignal.countDown();  
                 }  
             }  
         });  
        connectedSignal.await();  
        return zk;  
    }  
	
	
	
	/**
	 * Watcher，Zookeeper允许用户在指定节点上注册一些Watcher，并且在一些特定事件触发的时候，Zookeeper服务端会将事件通知到感兴趣的客户端。
	 * Watcher通知是一次性的，即一旦触发一次通知后，该Watcher就失效了，
	 * 因此客户端需要反复注册Watcher，即程序中在process里面又注册了Watcher，
	 * 否则，将无法获取c3节点的创建而导致子节点变化的事件。
	 */
	
	private static CountDownLatch connectedSemaphore = new CountDownLatch(1);
	private static ZooKeeper zookeeper;
	/**
	 * Stat记录了这个ZNode的三个数据版本，分别是version（当前ZNode的版本）、
	 * cversion（当前ZNode子节点的版本）、aversion（当前ZNode的ACL版本）。
	 */
	private static Stat stat = new Stat();
	private static final int timeout = 5000;
	private static final String zk_url = "127.0.0.1:2181";
	private static final String path = "/zk-demo";
	//节点类型 		CreateMode.EPHEMERAL.
	//int version 默认值-1 表示执行最新的版本
	//rc（ResultCode）为0，表明成功更新节点数据。
	//Ids.OPEN_ACL_UNSAFE 权限控制
	/**
	 * ACL，Zookeeper采用ACL（Access Control Lists）策略来进行权限控制，其定义了如下五种权限：
	 * · CREATE：创建子节点的权限。
	 * · READ：获取节点数据和子节点列表的权限。
	 * · WRITE：更新节点数据的权限。
	 * · DELETE：删除子节点的权限。
	 * · ADMIN：设置节点ACL的权限。
	 * 节点类型:
	 * CreateMode.PERSISTENT	永久性节点
	 * CreateMode.PERSISTENT_SEQUENTIAL	永久性序列节点
	 * CreateMode.EPHEMERAL	临时节点，会话断开或过期时会删除此节点
	 * CreateMode.PERSISTENT_SEQUENTIAL	临时序列节点，会话断开或过期时会删除此节点
	 */
	 
	@Override
	public void process(WatchedEvent event) {
		//Zookeeper的监控只有在使用getData(),exists(),getChildren()这几个方法时才会触发watcher
		if (KeeperState.SyncConnected == event.getState()) {
            if (EventType.None == event.getType() && null == event.getPath()) { //获取路径
                connectedSemaphore.countDown();
            } else if (event.getType() == EventType.NodeChildrenChanged) { //获取子节点
                try {
                    System.out.println("ReGet Child:" + zookeeper.getChildren(event.getPath(), true));
                } catch (Exception e) {
                	
                }
            } else if (event.getType() == EventType.NodeDataChanged) {//节点数据变化
                try {
                    System.out.println("the data of znode " + event.getPath() + " is : " + new String(zookeeper.getData(event.getPath(), true, stat)));
                    System.out.println("czxID: " + stat.getCzxid() + ", mzxID: " + stat.getMzxid() + ", version: " + stat.getVersion());
                } catch (Exception e) {
                	
                }
            } else if (EventType.NodeCreated == event.getType()) {//创建节点
                System.out.println("success create znode: " + event.getPath());
                try {
					zookeeper.exists(event.getPath(), true);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
            } else if (EventType.NodeDeleted == event.getType()) {//删除节点
                System.out.println("success delete znode: " + event.getPath());
                try {
					zookeeper.exists(event.getPath(), true);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
            } else if (EventType.NodeDataChanged == event.getType()) {
                System.out.println("data changed of znode: " + event.getPath());
                try {
					zookeeper.exists(event.getPath(), true);
				} catch (KeeperException | InterruptedException e) {
					e.printStackTrace();
				}
            }
        }else if(KeeperState.Disconnected == event.getState()){
        	
        }
	}
	
	
	public static void main(String[] args) throws InterruptedException {
		try {
			//实例化zk时,注册watcher时间监听.也可以对不同的节点注册不同的watch.
			zookeeper = new ZooKeeper(zk_url, timeout, new JavaClientApi()); 
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(zookeeper.getState()); //zk的状态state
		try {
			connectedSemaphore.await(); //线程等待
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			zookeeper.close();//关闭连接
		}
		System.out.println("Zookeeper session established");
	}
	
	/**
	 * TODO 创建节点
	 * 创建节点有异步和同步两种方式。无论是异步或者同步，Zookeeper都不支持递归调用，
	 * 即无法在父节点不存在的情况下创建一个子节点，
	 * 如在/zk-ephemeral节点不存在的情况下创建/zk-ephemeral/ch1节点；
	 * 并且如果一个节点已经存在，那么创建同名节点时，会抛出NodeExistsException异常。
	 */
	public static void creatSyncZnode(ZooKeeper zookeeper,String path) throws KeeperException, InterruptedException{
		
	 	String path1 = zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);    
        System.out.println("Success create znode: " + path1);
        //临时节点 或者 临时顺序节点(zoo会在末尾追加序号)
        String path2 = zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);    
        System.out.println("Success create znode: " + path2);
        
	}
	
	/**
	 * 只允许删除叶子节点，即一个节点如果有子节点，
	 * 那么该节点将无法直接删除，必须先删掉其所有子节点。同样也有同步和异步两种方式。　
	 */
	public static void deleteSyncZnode(ZooKeeper zk,String path) throws IOException, InterruptedException, KeeperException{
		 
			connectedSemaphore.await();

	        try {
	            zk.delete(path, -1);
	        } catch (Exception e) {
	            System.out.println("fail to delete znode: " + path);
	        }
	        
	        zk.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	        System.out.println("success create znode: " + path + "/c1");
	        zk.delete(path + "/c1", -1);
	        System.out.println("success delete znode: " + path + "/c1");
	        Thread.sleep(Integer.MAX_VALUE);
	}
	
	//异步删除节点
	public static void deleteZnode(ZooKeeper zookeeper,String path) throws IOException, InterruptedException, KeeperException{

			connectedSemaphore.await();

			zookeeper.delete(path, -1, new IVoidCallback(), null); 
	        zookeeper.create(path + "/c1", "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	        System.out.println("success create znode: " + path + "/c1");
	        zookeeper.delete(path + "/c1", -1, new IVoidCallback(), null);
	        //没有节点删除是否报错
	        zookeeper.delete(path, -1, new IVoidCallback(), null);        
	        Thread.sleep(Integer.MAX_VALUE);
	} 
	
	
	//异步调用不会直接抛出异常,
	public static void createZnode(ZooKeeper zookeeper,String path) throws InterruptedException{
		zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL,
                new IStringCallback(), "I am context. ");

        zookeeper.create(path, "".getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL,
                new IStringCallback(), "I am context. ");
        Thread.sleep(Integer.MAX_VALUE);
    }
	
	
	/**
	 * TODO 获取节点
	 * 读取节点的子节点列表，
	 */
	public static void getSyncZnode(ZooKeeper zk,String path) throws KeeperException, InterruptedException{
		 
		List<String> childrenList = zk.getChildren(path, true);
		 
		zk.getChildren(path, true, new IChildren2Callback(), null);//异步获取子节点
		 
	}
	
	/**
	 * TODO 获取节点数据
	 */
	public static void getZnodeData(ZooKeeper zk,String path) throws KeeperException, InterruptedException{
		 System.out.println("the data of znode " + path + " is : " + new String(zk.getData(path, true, stat)));
	        System.out.println("czxID: " + stat.getCzxid() + ", mzxID: " + stat.getMzxid() + ", version: " + stat.getVersion());
	        
	        zk.setData(path, "123".getBytes(), -1);
	        //zk.getData(path, true, new IDataCallback(), null);
	        Thread.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * TODO 更新数据
	 * setData方法存在一个version参数，其用于指定节点的数据版本，表明本次更新操作是针对指定的数据版本进行的，
	 * 但是，在getData方法中，并没有提供根据指定数据版本来获取数据的接口，
	 * 那么，这里为何要指定数据更新版本呢，这里方便理解，
	 * 可以等效于CAS（compare and swap），对于值V，
	 * 每次更新之前都会比较其值是否是预期值A，只有符合预期，才会将V原子化地更新到新值B。
	 * Zookeeper的setData接口中的version参数可以对应预期值，
	 * 表明是针对哪个数据版本进行更新，假如一个客户端试图进行更新操作，
	 * 它会携带上次获取到的version值进行更新，而如果这段时间内，
	 * Zookeeper服务器上该节点的数据已经被其他客户端更新，
	 * 那么其数据版本也会相应更新，而客户端携带的version将无法匹配，
	 * 无法更新成功，因此可以有效地避免分布式更新的并发问题。
	 * @throws InterruptedException 
	 */
	public static void updateZnodeData(ZooKeeper zk,String path) throws InterruptedException{
		
		connectedSemaphore.await();
		 try {
            zk.setData(path, "456".getBytes(), stat.getVersion());
        } catch (KeeperException e) {
            System.out.println("Error: " + e.code() + "," + e.getMessage());
        }
	    Thread.sleep(Integer.MAX_VALUE);
	}
	
	/**
	 * TODO
	 * 节点是否存在
	 * · 无论节点是否存在，都可以通过exists接口注册Watcher。
	 * · 注册的Watcher，对节点创建、删除、数据更新事件进行监听。
	 * · 对于指定节点的子节点的各种变化，不会通知客户端。
	 */
	public static void isExist(ZooKeeper zk,String path) throws KeeperException, InterruptedException{
		 
		zk.exists(path, true);
		zk.exists(path, true, new IIStatCallback(), null);
	}
	/**
	 * TODO 权限控制
	 * 通过设置Zookeeper服务器上数据节点的ACL控制，
	 * 就可以对其客户端对该数据节点的访问权限：如果符合ACL控制，
	 * 则可以进行访问，否则无法访问。
	 * @throws InterruptedException 
	 * @throws KeeperException 
	 * @throws IOException 
	 */
	public static void authInfo(ZooKeeper zk,String path) throws KeeperException, InterruptedException, IOException{
		zk.addAuthInfo("digest", "foo:true".getBytes());
		zk.create(path, "init".getBytes(), Ids.CREATOR_ALL_ACL, CreateMode.EPHEMERAL);
        System.out.println("success create znode: " + path);
        ZooKeeper zookeeper2 = new ZooKeeper("127.0.0.1:2181", 5000, null);
        zookeeper2.getData(path, false, null);
	}
	
	//创建节点回调方法
	public static class IStringCallback implements AsyncCallback.StringCallback{
		@Override
		public void processResult(int rc, String path, Object ctx, String name) {
			  System.out.println("Create path result: [" + rc + ", " + path + ", " + ctx + ", real path name: " + name);
		}
	}
	
	//删除节点回调方法
	public static class IVoidCallback implements VoidCallback{
		@Override
		public void processResult(int rc, String path, Object ctx) {
        	System.out.println(rc + ", " + path + ", " + ctx);
		}
	}
	//获取子节点回调方法
	public static class IChildren2Callback implements AsyncCallback.Children2Callback{
		@Override
		public void processResult(int rc, String path, Object ctx, List<String> children, Stat stat) {
			System.out.println("Get Children znode result: [response code: " + rc + ", param path: " + path + ", ctx: "
	                + ctx + ", children list: " + children + ", stat: " + stat);
		}
	}
	//获取节点数据回调方法
	public static class IDataCallback implements  AsyncCallback.DataCallback{
		@Override
		public void processResult(int rc, String path, Object ctx, byte[] data, Stat stat) {
			System.out.println("rc: " + rc + ", path: " + path + ", data: " + new String(data));
	        System.out.println("czxID: " + stat.getCzxid() + ", mzxID: " + stat.getMzxid() + ", version: " + stat.getVersion());
		}
	}
	//节点存在回调事件
	public static class IIStatCallback implements AsyncCallback.StatCallback{
		@Override
		public void processResult(int rc, String path, Object ctx, Stat stat) {
			 System.out.println("rc: " + rc + ", path: " + path + ", stat: " + stat);
		}
	}
}

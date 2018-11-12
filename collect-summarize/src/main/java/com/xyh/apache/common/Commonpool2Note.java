package com.xyh.apache.common;

/**
 * apache的对象池,类似的实现还有netty中的recycle轻量对象池。
 * 在查看fastDfs的client代码时接触到的
 * @author xyh
 * https://www.cnblogs.com/jinzhiming/p/5120623.html
 */
public class Commonpool2Note {

	/**
	 Apache Common-pool2包提供了一个通用的对象池技术的实现。可以很方便的基于它来实现自己的对象池，比如DBCP和Jedis他们的内部对象池的实现就是依赖于Common-pool2。
		对象的创建和销毁在一定程度上会消耗系统的资源，虽然jvm的性能在近几年已经得到了很大的提高，对于多数对象来说，没有必要利用对象池技术来进行对象的创建和管理。但是对于有些对象来说，其创建的代价还是比较昂贵的，比如线程、tcp连接、数据库连接等对象，因此对象池技术还是有其存在的意义。
		实现分析
		Common-pool2由三大模块组成：ObjectPool、PooledObject和PooledObjectFactory。
		ObjectPool：提供所有对象的存取管理。
		PooledObject：池化的对象，是对对象的一个包装，加上了对象的一些其他信息，包括对象的状态（已用、空闲），对象的创建时间等。
		PooledObjectFactory：工厂类，负责池化对象的创建，对象的初始化，对象状态的销毁和对象状态的验证。
		ObjectPool会持有PooledObjectFactory，将具体的对象的创建、初始化、销毁等任务交给它处理，其操作对象是PooledObject，即具体的Object的包装类。
		org.apache.commons.pool2.impl 包提供了一个默认的对象池实现。
		主要还是这三个模块的实现，其中PooledObjectFactory在包里没有具体实现，因为这涉及到具体对象的创建，需要应用本身去实现，这也体现了设计上的解耦合性。
		BaseGenericObjectPool
		它主要定义了对象池的一些配置信息和实现jmx注册注销等功能。
		以下是对象池的相关配置
		GenericObjectPool
		数据结构：ConcurrentHashMap和LinkedBlockingDeque。前者用于存储所有的对象（不含销毁的对象），后者用于存储空闲的对象。
		borrowObject()大体思路如下
		1 从LinkedBlockingDeque中pollFirst
		2 若为空，检查对象池对象是否达到上限，若是重复1，若否，则调用PooledObjectFactory的makeObject去创建一个对象
		3 得到对象之后，对对象进行初始化和一些配置的计数处理，同时将对象加入到ConcurrentHashMap。
		returnObject(T obj)大体思路如下
		1 根据obj从ConcurrentHashMap拿到其对应的PooledObject p
		2 判空；将p状态置为RETURN
		3 若getTestOnReturn参数为true，进行validateObject
		4 对p进行passivateObject，与初始化相反
		5 更新p状态为IDLE
		6 归还Pool：Pool的idle实例达到上限或者Pool已经关闭，销毁之，否则将p加入到LinkedBlockingDeque中。
		DefaultPooledObject
		默认的PooledObject实现，维护池化对象的一系列状态参数。

	 */
	
	
}

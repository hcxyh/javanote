package com.xyh.cache.redis.HA;

public class RedisCluster {
	/**
	 
	 Redis集群方案应该怎么做？都有哪些方案？

		1.twemproxy，大概概念是，它类似于一个代理方式，使用方法和普通redis无任何区别，设置好它下属的多个redis实例后，使用时在本需要连接redis的地方改为连接twemproxy，它会以一个代理的身份接收请求并使用一致性hash算法，将请求转接到具体redis，将结果再返回twemproxy。使用方式简便(相对redis只需修改连接端口)，对旧项目扩展的首选。 问题：twemproxy自身单端口实例的压力，使用一致性hash后，对redis节点数量改变时候的计算值的改变，数据无法自动移动到新的节点。
		
		2.codis，目前用的最多的集群方案，基本和twemproxy一致的效果，但它支持在 节点数量改变情况下，旧节点数据可恢复到新hash节点。
		
		3.redis cluster3.0自带的集群，特点在于他的分布式算法不是一致性hash，而是hash槽的概念，以及自身支持节点设置从节点。具体看官方文档介绍。
		
		4.在业务代码层实现，起几个毫无关联的redis实例，在代码层，对key 进行hash计算，然后去对应的redis实例操作数据。 这种方式对hash层代码要求比较高，考虑部分包括，节点失效后的替代算法方案，数据震荡后的自动脚本恢复，实例的监控，等等。
	 
	 */
	
	
	/**
	 1. 分布式数据库把整个数据按分区规则映射到多个节点，即把数据划分到多个节点上，每个节点负责整体数据的一个子集。
	 	比如我们库有900条用户数据，有3个redis节点，将900条分成3份，分别存入到3个redis节点
	 2. 分区规则：
   		常见的分区规则哈希分区和顺序分区，redis集群使用了哈希分区，顺序分区暂用不到，不做具体说明；
   		rediscluster采用了哈希分区的“虚拟槽分区”方式（哈希分区分节点取余、一致性哈希分区和虚拟槽分区），其它两种也不做介绍，有兴趣可以百度了解一下
	 3. 虚拟槽分区(槽：slot)
   		RedisCluster采用此分区，所有的键根据哈希函数(CRC16[key]&16383)映射到0－16383槽内，共16384个槽位，
   		每个节点维护部分槽及槽所映射的键值数据
   		哈希函数: Hash()=CRC16[key]&16383 按位与.
   		槽与节点的关系如下:
   			0~5460       ---->    redis-1主节点
   			5461~10922   ---->    redis-2主节点
   			10923~16383  ---->    redis-3主节点
   		key --> hash(key)  --->  redisCluster（根据hashkey路由到不同的槽）
   		redis用虚拟槽分区原因：解耦数据与节点关系，节点自身维护槽映射关系，分布式存储
   	 4. redisCluster的缺陷：
		a，键的批量操作支持有限，比如mset, mget，如果多个键映射在不同的槽，就不支持了
		b，键事务支持有限，当多个key分布在不同节点时无法使用事务，同一节点是支持事务
		c，键是数据分区的最小粒度，不能将一个很大的键值对映射到不同的节点
		d，不支持多数据库，只有0，select 0
		e，复制结构只支持单层结构，不支持树型结构。
	 *
	 */
	
	
	/**
	 二、集群环境搭建-手动篇  [http://www.cnblogs.com/leeSmall/p/8414687.html]
	 	三主三从的结构.
	 	
	 	自动篇 (ruby)
	 */
}
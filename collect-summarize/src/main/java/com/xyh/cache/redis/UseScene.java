package com.xyh.cache.redis;

/**
 * redis 使用场景
 * @author hcxyh  2018年8月7日
 *
 */
public class UseScene {
	
	
	/**
	 * 特性feather:
	 	1.速度快（基于内存，C 语言，单线程）
		2.基于 key value 键值对的数据结构服务器。全称 Remote Dictionary Server。包含 String，Hash, List, Set, SotrSet. 同时在字符串的基础上演变出位图（BitMaps） 和 HyperLogLog 两种数据数据结构。3.2 版本中加入 GEO（地理信息位置）
		3.丰富的功能。例如：键过期（缓存），发布订阅（消息队列）， Lua 脚本（自己实现 Redis 命令），事务，流水线（Pipeline 减少网络开销）
		4.简单稳定
		5.客户端语言多
		6.持久化（RDB，AOF）
		7.主从复制（分布式的基础）  --> 故障自动切换master节点，哨兵
		8.高可用和分布式（Cluster）
	 */
	
	
	/**
	 TODO 使用场景:
	 	1. 缓存
		合理的使用缓存能够明显加快访问的速度，同时降低数据源的压力。这也是 Redis 最常用的功能。Redis 提供了键值过期时间（ EXPIRE key seconds）设置，并且也提供了灵活控制最大内存和内存溢出后的淘汰策略。
		2. 排行榜
		每个网站都有自己的排行榜，例如按照热度排名的排行榜，发布时间的排行榜，答题排行榜等等。Redis 提供了列表（List）和有序集合（sorted set）数据结构，合理的使用这些数据结构可以很方便的构建各种排行榜系统。
		3. 计数器
		计数器在网站应用中非常重要，例如，点赞数加一，浏览数加一，还有常用的限流操作，限制每个用户每秒访问系统的次数等等，Redis 支持计数功能（INCR key）而且计数的性能也非常好，计数的同时也可以设置超时时间，这样就可以实现限流。
		4. 社交网络
		赞/踩，粉丝，共同好友/喜好，推送，下拉刷新等是社交网站必备的功能，由于社交网站访问量通常比较大，而且传统的数据库不太适合保存这类数据，Redis 提供的数据结构可以相对比较容易实现这些功能。
		链接：[Redis实战：如何构建类微博的亿级社交平台 https://blog.csdn.net/younger_z/article/details/51692720]
		5. 消息队列
		Redis 提供的发布订阅（PUB/SUB） 和阻塞队列（blpop key1...keyN timeout ）的功能，虽然和专业的消息队列比，还不够强大，但对于一般的消息队列功能基本满足。
		
		（1）、会话缓存（Session Cache）
			最常用的一种使用Redis的情景是会话缓存（session cache）。用Redis缓存会话比其他存储（如Memcached）的优势在于：Redis提供持久化。当维护一个不是严格要求一致性的缓存时，如果用户的购物车信息全部丢失，大部分人都会不高兴的，现在，他们还会这样吗？
			幸运的是，随着 Redis 这些年的改进，很容易找到怎么恰当的使用Redis来缓存会话的文档。甚至广为人知的商业平台Magento也提供Redis的插件。
		（2）、全页缓存（FPC）
			除基本的会话token之外，Redis还提供很简便的FPC平台。回到一致性问题，即使重启了Redis实例，因为有磁盘的持久化，用户也不会看到页面加载速度的下降，这是一个极大改进，类似PHP本地FPC。
			再次以Magento为例，Magento提供一个插件来使用Redis作为全页缓存后端。
			此外，对WordPress的用户来说，Pantheon有一个非常好的插件 wp-redis，这个插件能帮助你以最快速度加载你曾浏览过的页面。
		（3）、队列
			Reids在内存存储引擎领域的一大优点是提供 list 和 set 操作，这使得Redis能作为一个很好的消息队列平台来使用。Redis作为队列使用的操作，就类似于本地程序语言（如Python）对 list 的 push/pop 操作。
			如果你快速的在Google中搜索“Redis queues”，你马上就能找到大量的开源项目，这些项目的目的就是利用Redis创建非常好的后端工具，以满足各种队列需求。例如，Celery有一个后台就是使用Redis作为broker，你可以从这里去查看。
		（4），排行榜/计数器
			Redis在内存中对数字进行递增或递减的操作实现的非常好。集合（Set）和有序集合（Sorted Set）也使得我们在执行这些操作的时候变的非常简单，Redis只是正好提供了这两种数据结构。所以，我们要从排序集合中获取到排名最靠前的10个用户–我们称之为“user_scores”，我们只需要像下面一样执行即可：
			当然，这是假定你是根据你用户的分数做递增的排序。如果你想返回用户及用户的分数，你需要这样执行：
			ZRANGE user_scores 0 10 WITHSCORES
			Agora Games就是一个很好的例子，用Ruby实现的，它的排行榜就是使用Redis来存储数据的，你可以在这里看到。
			
	 *
	 *
	 */
	
	/**
	 	可执行文件:
	 	redis-server			启动 redis 服务
		redis-cli				redis 命令行客户端
		redis-benchmark			redis 基准测试工具
		redis-check-aof			redis AOF 持久化文件检测和修复工具
		redis-check-dump		redis RDB 持久化文件检测和修复工具
		redis-sentinel			启动 redis sentinel
	 */
	
	/**
	 redis 启停服务:
	 1.建议使用配置文件
	 	将配置写到配置文件中，例如写到 /opt/redis/redis.conf 中，执行 redis-server /opt/redis/redis.conf 即可启动 redis。
	 2.关闭服务
	 	redis 提供了 shutDown 命令来停止 redis 服务，例如停掉127.0.0.1：6379 服务，使用：redis-cli shutdown 即可。
	 	{
	 		redis 关闭的过程：断开和客户端的连接，持久化文件生成。相对而言比较优雅。
			不用使用 kill -9，过于粗暴，不但不会做持久化操作，还会造成缓冲区等资源不会优雅关闭。极端情况下造成 AOF 和复制丢失数据的情况。
			shutdown 还有一个参数，代表是否在关闭 redis 前，生成持久化文件： redis-cli shutdown nosave|save
	 	}
	 */
	
}

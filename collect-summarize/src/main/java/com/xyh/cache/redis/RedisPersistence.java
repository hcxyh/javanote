package com.xyh.cache.redis;

/**
 * Redis持久化
 * @author hcxyh  2018年8月7日
 */
public class RedisPersistence {
	
	/**
	 * 	TODO 持久化
	 	一、RDB持久化
	 	RDB持久化把当前进程数据生成快照（.rdb）文件保存到硬盘的过程，有手动触发和自动触发
		手动触发有save和bgsave两命令 
		save命令：阻塞当前Redis，直到RDB持久化过程完成为止，若内存实例比较大会造成长时间阻塞，线上环境不建议用它
		bgsave命令：redis进程执行fork操作创建子线程，由子线程完成持久化，阻塞时间很短（微秒级），是save的优化,
		在执行redis-cli shutdown关闭redis服务时，如果没有开启AOF持久化，自动执行bgsave;
		显然bgsave是对save的优化。
		
		RDB文件的操作
		   命令：config set dir /usr/local  //设置rdb文件保存路径
		   备份：bgsave  //将dump.rdb保存到usr/local下
		   恢复：将dump.rdb放到redis安装目录与redis.conf同级目录，重启redis即可
		   优点：1，压缩后的二进制文文件适用于备份、全量复制，用于灾难恢复
		     2，加载RDB恢复数据远快于AOF方式
		   缺点：1，无法做到实时持久化，每次都要创建子进程，频繁操作成本过高
		     2，保存后的二进制文件，存在老版本不兼容新版本rdb文件的问题  
		二、AOF持久化
			针对RDB不适合实时持久化，redis提供了AOF持久化方式来解决
			开启：redis.conf设置：appendonly yes  (默认不开启，为no)
			默认文件名：appendfilename "appendonly.aof"   
      		流程说明：
　　  			1，所有的写入命令(set hset)会append追加到aof_buf缓冲区中
         	2，AOF缓冲区向硬盘做sync同步
         	3，随着AOF文件越来越大，需定期对AOF文件rewrite重写，达到压缩
         	4，当redis服务重启，可load加载AOF文件进行恢复
			AOF持久化流程：命令写入(append),文件同步(sync),文件重写(rewrite),重启加载(load)
		AOF配置详解：
			appendonly yes     //启用aof持久化方式
			# appendfsync always //每收到写命令就立即强制写入磁盘，最慢的，但是保证完全的持久化，不推荐使用
			appendfsync everysec //每秒强制写入磁盘一次，性能和持久化方面做了折中，推荐
			# appendfsync no    //完全依赖os，性能最好,持久化没保证（操作系统自身的同步）
			no-appendfsync-on-rewrite  yes  //正在导出rdb快照的过程中,要不要停止同步aof
			auto-aof-rewrite-percentage 100  //aof文件大小比起上次重写时的大小,增长率100%时,重写
			auto-aof-rewrite-min-size 64mb   //aof文件,至少超过64M时,重写
		如何从AOF恢复？
			1. 设置appendonly yes；
			2. 将appendonly.aof放到dir参数指定的目录；
			3. 启动Redis，Redis会自动加载appendonly.aof文件。
		redis重启时恢复加载AOF与RDB顺序及流程：
			1，当AOF和RDB文件同时存在时，优先加载AOF
			2，若关闭了AOF，加载RDB文件
			3，加载AOF/RDB成功，redis重启成功
			4，AOF/RDB存在错误，redis启动失败并打印错误信息
	 */
	
	/**
	 * TODO 基准性能测试
	   redis-benchmark：基准性测试，测试redis的性能
	   100个客户端同时请求redis,共执行10000次,会对各类数据结构的命令进行测试：
	   ./redis-benchmark -h 127.0.0.1 -c 100 -n 10000  //100个并发连接，100000个请求，检测host为localhost 端口为6379的redis服务器性能
	 */
	
	/**
	 * TODO  pipeline
	   没有pipeline之前，一般的redis命令的执行过程都是：发送命令－〉命令排队－〉命令执行－〉返回结果。
	   这个时候需要pipeline来解决这个问题：使用pipeline来打包执行N条命令，
	   这样的话就只需简历一次网络连接，网络开销就少了
	 
	 pipeline是多条命令的组合，为了保证它的原子性，redis提供了简单的事务；
	 redis的事物与mysql事物的最大区别是redis事物不支持事物回滚
	  事务：事务是指一组动作的执行，这一组动作要么都成功，要么都失败。
	 1. redis的简单事务，将一组需要一起执行的命令放到multi和exec两个命令之间，
	   其中multi代表事务开始，exec代表事务结束
	   
	 */
	
	/**
	 	发布和订阅:
	 		redis提供了“发布、订阅”模式的消息机制，其中消息订阅者与发布者不直接通信，发布者向指定的频道（channel）发布消息，订阅该频道的每个客户端都可以接收到消息
	 		redis主要提供发布消息、订阅频道、取消订阅以及按照模式订阅和取消订阅
			1.发布消息
			publish channel:test "hello world"
			2.订阅消息
			subscrible channel:test
			此时另一个客户端发布一个消息:publish channel:test "james test"
		和很多专业的消息队列（kafka rabbitmq）,redis的发布订阅显得很lower, 比如无法实现消息规程和回溯， 但就是简单，如果能满足应用场景，用这个也可以
			3.查看订阅数：
			pubsub numsub channel:test // 频道channel:test的订阅数
			4.取消订阅
			unsubscribe channel:test
			客户端可以通过unsubscribe命令取消对指定频道的订阅，取消后，不会再收到该频道的消息
			5.按模式订阅和取消订阅
			psubscribe ch* //订阅以ch开头的所有频道
			punsubscribe ch*  //取消以ch开头的所有频道
			6.应用场景：
			1、今日头条订阅号、微信订阅公众号、新浪微博关注、邮件订阅系统
			2、即时通信系统
			3、群聊部落系统（微信群）
	 */
}

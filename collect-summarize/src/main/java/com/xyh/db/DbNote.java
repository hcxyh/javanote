package com.xyh.db;

/**
 * 
 * @author hcxyh  2018年8月11日
 *
 */
public class DbNote {
	
	/**
	 * 1.事务
	 * 2.主从复制,读写分离
	 * 3.分库分表
	 * 4.集群
	 * 5.slowlog 慢日志优化
	 */
	
	/**
	 * 1.sql执行性能分析
	 * 		MySQL性能分析工具之PROFILE 
	 * 2.https://www.cnblogs.com/xueweihan/p/6864401.html
	 * 		1.show processlist
	 * 		2.profiling
	 * 			select @@profiling
	 * 			set profiling = 1
	 * 			show profiles
	 * 3.	 set global slow_query_log=1
	 * 		 show variables  like '%slow_query_log%';
	 * 
	 * 4.druid.starter源码分析  
	 * 		-->  https://mp.weixin.qq.com/s/PAwIKYRQJQd4QCaq_vNTQA
	 */
	
}

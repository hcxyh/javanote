package com.xyh.middleware.elasticjob.demo;

import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * 入门笔记,有些东西时间长了就慢慢忘了.
 * @author hcxyh  2018年8月13日
 *
 */
public class ElasticJobStart implements SimpleJob{

	@Override
	public void execute(ShardingContext shardingContext) {
		shardingContext.getJobName();
		shardingContext.getJobParameter();
		shardingContext.getShardingParameter();
		shardingContext.getTaskId();
		
		switch (shardingContext.getShardingItem()) {
		case 0:
			System.out.println("My ElasticJob - 0");
			break;
		case 1:
			System.out.println("My ElasticJob - 1");
			break;
		case 2:
			System.out.println("My ElasticJob - 2");
			break;
		default:
			break;
		/**
		 * 分片:	
		 	任务的分布式执行，需要将一个任务拆分为多个独立的任务项，然后由分布式的服务器分别执行某一个或几个分片项。
		 	例如：有一个遍历数据库某张表的作业，现有2台服务器。为了快速的执行作业，那么每台服务器应执行作业的50%。
		 	为满足此需求，可将作业分成2片，每台服务器执行1片。作业遍历数据的逻辑应为：服务器A遍历ID以奇数结尾的数据；
		 	服务器B遍历ID以偶数结尾的数据。如果分成10片，则作业遍历数据的逻辑应为：每片分到的分片项应为ID%10，
		 	而服务器A被分配到分片项0,1,2,3,4；服务器B被分配到分片项5,6,7,8,9，
		 	直接的结果就是服务器A遍历ID以0-4结尾的数据；服务器B遍历ID以5-9结尾的数据。
		 */
		}
	}
	
	/**
	 * Elastic-Job支持 JAVA API 和 Spring 配置两种方式配置任务，
	 * 这里我使用 JAVA API 的形式来创建一个简单的任务入门，现在都是 Spring Boot 时代了，
	 * 所以不建议使用 Spring 配置文件(xml)的形式。
	 */
	
	/**
	 * 1.创建作业
		Elastic-Job 提供 Simple、Dataflow 和 Script 3种作业类型。
		方法参数 shardingContext 包含作业配置、片和运行时信息。
		可通过 getShardingTotalCount(), getShardingItem() 等方法
		分别获取分片总数，运行在本作业服务器的分片序列号等。
	   2.配置作业
	   	 Elastic-Job 配置分为3个层级，分别是 Core, Type 和 Root，每个层级使用相似于装饰者模式的方式装配。
	   	 	1.Core 对应 JobCoreConfiguration，用于提供作业核心配置信息，如：作业名称、分片总数、CRON表达式等。
			2.Type 对应 JobTypeConfiguration，有3个子类分别对应 SIMPLE, DATAFLOW 和 SCRIPT 类型作业，提供3种作业需要的不同配置，如：DATAFLOW 类型是否流式处理或 SCRIPT 类型的命令行等。
			3.Root 对应 JobRootConfiguration，有2个子类分别对应 Lite 和 Cloud 部署类型，提供不同部署类型所需的配置，如：Lite类型的是否需要覆盖本地配置或 Cloud 占用 CPU 或 Memory 数量等。
	 */
	
}

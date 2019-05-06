package com.xyh.spring.batch;

public class SpringBatchNote {



	
	/**
	  *	批处理作业两个典型特征是批量执行与自动执行（需要无人值守）：
	  *	前者能够处理大批量数据的导入、导出和业务逻辑计算；
	  *	后者无需人工干预，能够自动化执行批量任务。 
	  *批量的每个单元都需要错误处理和回退；
	  *-------------------------
	 *	每个单元在不同平台中运行；
	 *	需要有分支选择；
	 *	每个单元需要监控和获取单元处理日志；
	 *	提供多种触发规则，按日期，日历，周期触发；
	 * 	--------------------------------------
	 * 	定期提交批处理任务（日终处理）
	 *	并行批处理：并行处理任务
	 *	企业消息驱动处理     
	 *	大规模的并行处理
	 *	手动或定时重启
	 *	按顺序处理依赖的任务(可扩展为工作流驱动的批处理)  --->  有状态的taskJob
	 *	部分处理：忽略记录(例如在回滚时)
	 *	完整的批处理事务
	 * ------------------------------------
	 *	个人总结：
	 *	分布式调度定时任务:
	 *	1.elastic-job可以提供平滑扩容,基于zk,进行多分片作业,工作单元以主机为单元.
	 *	不进行分片，只是相当于一个定时任务.
	 *	2.xx-job,通过quartz基于数据库的集群来实现分布式.
	 *	3.spring-batch 的并行是基于线程的. 
	 *		a)- 单个进程，多线程 
	 *		b)- 多个进程
	 *
	 * 	若分片是以pc为基本单元,则我倾向于使用elastic-job,
	 * 	不能提供fork-join,或者mapreduce这种.以处理器多核为单位分片.
	 *  ----------------------------------------------------------------------
	 *  技术选型考虑点：
	 *  健壮性：不会因为无效数据或错误数据导致程序崩溃；
	 *  可靠性：通过跟踪、监控、日志及相关的处理策略（重试、跳过、重启）实现批作业的可靠执行；
	 *  扩展性：通过并发或者并行技术实现应用的纵向和横向扩展，满足海量数据处理的性能需求；
	 * 
	 */




	/**
	*2019.03.06:
	* 再次看hadoop中也遇到了调度任务的需求。
	*
	* Spring Batch是一个轻量级的，完全面向Spring的批处理框架，可以应用于企业级大量的数据处理系统。
	* Spring Batch以POJO和大家熟知的Spring框架为基础，使开发者更容易的访问和利用企业级服务。
	* Spring Batch可以提供大量的，可重复的数据处理功能，包括日志记录/跟踪，事务管理，作业处理统计工作重新启动、跳过，和资源管理等重要功能。
	*
	* springBatch是批处理框架，重点是在考虑如何能并发快速的执行完所调度的任务，首先将任务拆分为几个step，然后会采用并发或者并行等几个方式执行。
	*
	*
		elasticjob的分片可以提供以pc为单位的任务调度.
	*
	*
	*
		pom引用：
		com.taobao.pamirs.schedule
		tbschedule
		3.2.16

		1、tbschedule的目的是让一种批量任务或者不断变化的任务，能够被动态的分配到多个主机的JVM中，不同的线程组中并行执行。所有的任务能够被不重复，不遗漏的快速处理。
		2、调度的Manager可以动态的随意增加和停止
		3、可以通过JMX控制调度服务的创建和停止
		4、可以指定调度的时间区间：
		PERMIT_RUN_START_TIME ：允许执行时段的开始时间crontab的时间格式.以startrun:开始，则表示开机立即启动调度
		PERMIT_RUN_END_TIME ：允许执行时段的结束时间crontab的时间格式,如果不设置，表示取不到数据就停止
		PERMIT_RUN_START_TIME =’0 * * * * ?’ 表示在每分钟的0秒开始
		PERMIT_RUN_START_TIME =’20 * * * * ?’ 表示在每分钟的20秒终止
		就是每分钟的0-20秒执行，其它时间休眠

		JobServer 简介信息

		JobServer是一个基于Web的Java作业调度引擎和工作流服务器。它有许多调度规则并支持程序授权用户进行企业级的作业配置，运行，监控。
		JobServer的特性：不需要编程就能够设定和调度作业。易于使用完全基于Web的GUI。
		具备构造和处理上万个作业的能力。能对作业按组划分进行创建和管理。作业依赖调度。
		支持多种数据库包括：Oracle,PostgreSQL和MYSQL。高级搜索功能-能快速查找到自己要的作业。支持SSL等。

	 1：工作流调度系统的作用：
	 	（1）：一个完整的数据分析系统通常都是由大量任务单元组成：比如，shell脚本程序，java程序，mapreduce程序、hive脚本等；
		（2）：各任务单元之间存在时间先后及前后依赖关系；
		（3）：为了很好地组织起这样的复杂执行计划，需要一个工作流调度系统来调度执行；

		（4）：举例说明工作流调度系统的具体作用：

		　　我们可能有这样一个需求，某个业务系统每天产生20G原始数据，我们每天都要对其进行处理，处理步骤如下所示：
		　　　　a、通过Hadoop先将原始数据同步到HDFS上；
		　　　　b、借助MapReduce计算框架对原始数据进行转换，生成的数据以分区表的形式存储到多张Hive表中；
		　　　　c、需要对Hive中多个表的数据进行JOIN处理，得到一个明细数据Hive大表；
		　　　　d、将明细数据进行复杂的统计分析，得到结果报表信息；
		　　　　e、需要将统计分析得到的结果数据同步到业务系统中，供业务调用使用。

		（5）：工作流调度实现方式：

		　　a：简单的任务调度：直接使用linux的crontab来定义；
		　　b：复杂的任务调度：开发调度平台，或使用现成的开源调度系统，比如ooize、azkaban等

		（6）：常见工作流调度系统：

		　　市面上目前有许多工作流调度器：
		　　　　在hadoop领域，常见的工作流调度器有Oozie, Azkaban,Cascading,Hamake等

	  2：各种调度工具特性对比：
	  	下面的表格对上述四种hadoop工作流调度器的关键特性进行了比较，尽管这些工作流调度器能够解决的需求场景基本一致，但在设计理念，目标用户，应用场景等方面还是存在显著的区别，在做技术选型的时候，可以提供参考：
	  	特性
								Hamake		Oozie					Azkaban	Cascading
			 工作流描述语言	     XML	 XML (xPDL based)	 text file with key/value pairs	 Java API
			 依赖机制	      data-driven	 explicit	 explicit	 explicit
			 是否要web容器	 No	 Yes	 Yes	 No
			 进度跟踪	 console/log messages	 web page	 web page	 Java API
			 Hadoop job调度支持	 no	 yes	 yes	 yes
			 运行模式	 command line utility	 daemon	 daemon	 API
			 Pig支持	 yes	 yes	 yes	 yes
			 事件通知	 no	 no	 no	 yes
			 需要安装	 no	 yes	 yes	 no
			 支持的hadoop版本	 0.18+	 0.20+	 currently unknown	 0.18+
			 重试支持	 no	 workflownode evel	 yes	 yes
			 运行任意命令	 yes	 yes	 yes	 yes
			 Amazon EMR支持	 yes	 no	 currently unknown	 yes

	3：Azkaban与Oozie对比：
		（1）：对市面上最流行的两种调度器，给出以下详细对比，以供技术选型参考。总体来说，ooize相比azkaban是一个重量级的任务调度系统，功能全面，但配置使用也更复杂。如果可以不在意某些功能的缺失，轻量级调度器azkaban是很不错的候选对象。

		（2）：功能：
		　　两者均可以调度mapreduce,pig,java,脚本工作流任务；
		　　两者均可以定时执行工作流任务；

		（3）：工作流定义：
		　　Azkaban使用Properties文件定义工作流；
		　　Oozie使用XML文件定义工作流；

		（4）：工作流传参：
		　　Azkaban支持直接传参，例如${input}；
		　　Oozie支持参数和EL表达式，例如${fs:dirSize(myInputDir)}；

		（5）：定时执行：
		　　Azkaban的定时执行任务是基于时间的；
		　　Oozie的定时执行任务基于时间和输入数据；

		（6）：资源管理：
		　　Azkaban有较严格的权限控制，如用户对工作流进行读/写/执行等操作；
		　　Oozie暂无严格的权限控制；

		（7）：工作流执行：
		　　Azkaban有两种运行模式，分别是solo server mode(executor server和web server部署在同一台节点)和multi server mode(executor server和web server可以部署在不同节点)；
		　　Oozie作为工作流服务器运行，支持多用户和多工作流；

		（8）：工作流管理：
		　　Azkaban支持浏览器以及ajax方式操作工作流；
		　　Oozie支持命令行、HTTP REST、Java API、浏览器操作工作流；
	 4：Azkaban介绍：
	　　Azkaban是由Linkedin开源的一个批量工作流任务调度器。用于在一个工作流内以一个特定的顺序运行一组工作和流程。Azkaban定义了一种KV文件格式来建立任务之间的依赖关系，并提供一个易于使用的web用户界面维护和跟踪你的工作流。
	　　它有如下功能特点：
	　　　  Web用户界面，方便上传工作流，方便设置任务之间的关系，调度工作流，认证/授权(权限的工作)，能够杀死并重新启动工作流，模块化和可插拔的插件机制，项目工作区，工作流和任务的日志记录和审计。
	
	 


	* 
 	*
	*
	*/
}

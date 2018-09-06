package org.xyh.quartz;

public class CreateTable {
	
	/**
	 * TODO
	 * 分布式集群部署
	 * https://tech.meituan.com/mt-crm-quartz.html
	 */
	
	/**
	 *  1  以 Blob 类型存储 Quartz 的 Calendar 信息    ---> qrtz_calendars
	 *  2. 存储程序的悲观锁的信息(假如使用了悲观锁)   qrtz_locks
	 *  3.qrtz_paused_trigger_grps
	 *  4.存储与已触发的 Trigger 相关的状态信息，以及相联 Job 的执行信息    qrtz_fired_triggers
	 *  5.qrtz_job_details
	 *  6.调度容器 状态信息记录表    qrtz_scheduler_state
	 *  7.定时器信息记录表     定时器的类型 cron代表 CronTriggerFactoryBean调用 ，simp 代表 SimpleTriggerFactoryBean 调用
	 *     qrtz_triggers
	 *  8.qrtz_blob_triggers
	 *  9.存储 Cron Trigger，包括 Cron 表达式和时区信息     qrtz_cron_triggers
	 *  10.qrtz_simple_triggers
	 *  11.qrtz_simprop_triggers
	 *  
	 *  
	 *  # quartz 与Spring集成使用到下面的六个表：其它5张表内无数据，或者其它模式可能用到
	 *  qrtz_scheduler_state,
		qrtz_triggers,
		qrtz_locks,
		qrtz_fired_triggers,
		qrtz_job_details,
		qrtz_cron_triggers
	 *  
	 */
	
	
	/**
	 * Job用于表示被调度的任务。主要有两种类型的job：无状态的（stateless）和有状态的（stateful）。
	 * 【这块就是互金纠结于使用 springBatch中的step的原因,让批量任务有先后顺序】
	 * 对于同一个trigger来说，有状态的job不能被并行执行，只有上一次触发的任务被执行完之后，才能触发下一次执行。
	 * Job主要有两种属性：volatility和durability，其中volatility表示任务是否被持久化到数据库存储，
	 * 而durability表示在没有trigger关联的时候任务是否被保留。两者都是在值为true的时候任务被持久化或保留。
	 * 一个job可以被多个trigger关联，但是一个trigger只能关联一个job。
	 */
	
	/**
	 * 	Table Name	Description
		QRTZ_CALENDARS	存储Quartz的Calendar信息
		QRTZ_CRON_TRIGGERS	存储CronTrigger，包括Cron表达式和时区信息
		QRTZ_FIRED_TRIGGERS	存储与已触发的Trigger相关的状态信息，以及相联Job的执行信息
		QRTZ_PAUSED_TRIGGER_GRPS	存储已暂停的Trigger组的信息
		QRTZ_SCHEDULER_STATE	存储少量的有关Scheduler的状态信息，和别的Scheduler实例
		QRTZ_LOCKS	存储程序的悲观锁的信息
		QRTZ_JOB_DETAILS	存储每一个已配置的Job的详细信息
		QRTZ_JOB_LISTENERS	存储有关已配置的JobListener的信息
		QRTZ_SIMPLE_TRIGGERS	存储简单的Trigger，包括重复次数、间隔、以及已触的次数
		QRTZ_BLOG_TRIGGERS	Trigger作为Blob类型存储
		QRTZ_TRIGGER_LISTENERS	存储已配置的TriggerListener的信息
		QRTZ_TRIGGERS	存储已配置的Trigger的信息
	 */
	
}

# spring-quartz-cluster-sample

Spring整合Quartz基于数据库的分布式定时任务，可动态添加、删除、修改定时任务。

创建quartz数据库

执行src/main/resources/scripts下的建表语句

执行src/main/resources/create-schema.sql语句

修改数据库连接信息


 <!-- 配置quartz调度器 -->
    <bean id="scheduler" class="org.springframework.scheduling.quartz.SchedulerFactoryBean">
        <property name="dataSource" ref="dataSource" />
        <!--可选，QuartzScheduler 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了 -->
        <property name="overwriteExistingJobs" value="true" />
        <!--必须的，QuartzScheduler 延时启动，应用启动完后 QuartzScheduler 再启动 -->
        <property name="startupDelay" value="3" />
        <!-- 设置自动启动 -->
        <property name="autoStartup" value="true" />
        <!-- 把spring上下 文以key/value的方式存放在了quartz的上下文中了 -->
        <property name="applicationContextSchedulerContextKey" value="applicationContext" />
        <!-- scheduler的名称 -->
        <property name="schedulerName" value="ClusterScheduler" />
        <property name="configLocation" value="classpath:quartz.properties" />
<!--         <property name="quartzProperties"> -->
<!--             <props> -->
<!--                 <prop key="org.quartz.scheduler.instanceName">ClusterScheduler</prop> -->
<!--                 <prop key="org.quartz.scheduler.instanceId">AUTO</prop> -->
<!--                 线程池配置 -->
<!--                 <prop key="org.quartz.threadPool.class">org.quartz.simpl.SimpleThreadPool</prop> -->
<!--                 <prop key="org.quartz.threadPool.threadCount">20</prop> -->
<!--                 <prop key="org.quartz.threadPool.threadPriority">5</prop> -->
<!--                 JobStore 配置 -->
<!--                 <prop key="org.quartz.jobStore.class">org.quartz.impl.jdbcjobstore.JobStoreTX</prop> -->

<!--                 集群配置 -->
<!--                 <prop key="org.quartz.jobStore.isClustered">true</prop> -->
<!--                 <prop key="org.quartz.jobStore.clusterCheckinInterval">15000</prop> -->
<!--                 <prop key="org.quartz.jobStore.maxMisfiresToHandleAtATime">1</prop> -->

<!--                 <prop key="org.quartz.jobStore.misfireThreshold">120000</prop> -->

<!--                 <prop key="org.quartz.jobStore.tablePrefix">QRTZ_</prop> -->
<!--             </props> -->
<!--         </property> -->
    </bean>
    
    
    
    #配置参数详解，查看http://haiziwoainixx.iteye.com/blog/1838055
#============================================================================
# Configure Main Scheduler Properties  
#============================================================================
# 可为任何值,用在jdbc jobstrore中来唯一标识实例，但是在所有集群中必须相同
org.quartz.scheduler.instanceName: MyClusteredScheduler
#AUTO即可，基于主机名和时间戳来产生实例ID
#集群中的每一个实例都必须有一个唯一的"instance id",应该有相同的"scheduler instance name"
org.quartz.scheduler.instanceId: AUTO
#禁用quartz软件更新
org.quartz.scheduler.skipUpdateCheck: true

#============================================================================
# Configure ThreadPool  执行任务线程池配置
#============================================================================
#线程池类型，执行任务的线程
org.quartz.threadPool.class: org.quartz.simpl.SimpleThreadPool
#线程数量
org.quartz.threadPool.threadCount: 10
#线程优先级
org.quartz.threadPool.threadPriority: 5
org.quartz.threadPool.threadsInheritContextClassLoaderOfInitializingThread = true 

#============================================================================
# Configure JobStore  任务存储方式
#============================================================================

org.quartz.jobStore.misfireThreshold: 60000
#可以设置两种属性，存储在内存的RAMJobStore和存储在数据库的JobStoreSupport
#(包括JobStoreTX和JobStoreCMT两种实现JobStoreCMT是依赖于容器来进行事务的管理，而JobStoreTX是自己管理事务)
#这里的属性为 JobStoreTX，将任务持久化到数据中。
#因为集群中节点依赖于数据库来传播 Scheduler 实例的状态，你只能在使用 JDBC JobStore 时应用Quartz 集群。    
#这意味着你必须使用 JobStoreTX 或是 JobStoreCMT 作为 Job 存储；你不能在集群中使用 RAMJobStore。
org.quartz.jobStore.class=org.quartz.impl.jdbcjobstore.JobStoreTX

#JobStoreSupport 使用一个驱动代理来操作 trigger 和 job 的数据存储
org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#若要设置为true，则将JobDataMaps中的值当作string
org.quartz.jobStore.useProperties=false
#对应下方的数据源配置，与spring结合不需要这个配置
#org.quartz.jobStore.dataSource=myDS
#org.quartz.jobStore.tablePrefix=QRTZ_

#你就告诉了Scheduler实例要它参与到一个集群当中。这一属性会贯穿于调度框架的始终，用于修改集群环境中操作的默认行为。
org.quartz.jobStore.isClustered=true
#属性定义了Scheduler实例检入到数据库中的频率(单位：毫秒)。默认值是 15000 (即15 秒)。
org.quartz.jobStore.clusterCheckinInterval = 20000

#这是 JobStore 能处理的错过触发的 Trigger 的最大数量。
#处理太多(超过两打) 很快会导致数据库表被锁定够长的时间，这样就妨碍了触发别的(还未错过触发) trigger 执行的性能。
org.quartz.jobStore.maxMisfiresToHandleAtATime = 1
org.quartz.jobStore.misfireThreshold = 120000
org.quartz.jobStore.txIsolationLevelSerializable = false

#============================================================================
# Other Example Delegates 其他的数据库驱动管理委托类
#============================================================================
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.DB2v6Delegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.DB2v7Delegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.DriverDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.HSQLDBDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.MSSQLDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.PointbaseDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.PostgreSQLDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.StdJDBCDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.WebLogicDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.oracle.OracleDelegate
#org.quartz.jobStore.driverDelegateClass=org.quartz.impl.jdbcjobstore.oracle.WebLogicOracleDelegate

#============================================================================
# Configure Datasources  数据源配置，与spring结合不需要这个配置
#============================================================================

#org.quartz.dataSource.myDS.driver: com.mysql.jdbc.Driver
#org.quartz.dataSource.myDS.URL: jdbc:mysql://localhost:3306/quartz
#org.quartz.dataSource.myDS.user: root
#org.quartz.dataSource.myDS.password: 123
#org.quartz.dataSource.myDS.maxConnections: 5
#org.quartz.dataSource.myDS.validationQuery: select 0

#============================================================================
# Configure Plugins 
#============================================================================
#当事件的JVM终止后，在调度器上也将此事件终止
#the shutdown-hook plugin catches the event of the JVM terminating, and calls shutdown on the scheduler.
org.quartz.plugin.shutdownHook.class: org.quartz.plugins.management.ShutdownHookPlugin
org.quartz.plugin.shutdownHook.cleanShutdown: true

#记录trigger历史的日志插件
#org.quartz.plugin.triggHistory.class: org.quartz.plugins.history.LoggingJobHistoryPlugin



Quartz 实际并不关心你是在相同的还是不同的机器上运行节点。当集群是放置在不同的机器上时，通常称之为水平集群。节点是跑在同一台机器是，称之为垂直集群。对于垂直集群，存在着单点故障的问题。这对高可用性的应用来说是个坏消息，因为一旦机器崩溃了，所有的节点也就被有效的终止了。

当你运行水平集群时，时钟应当要同步，以免出现离奇且不可预知的行为。假如时钟没能够同步，Scheduler 实例将对其他节点的状态产生混乱。有几种简单的方法来保证时钟何持同步，而且也没有理由不这么做。最简单的同步计算机时钟的方式是使用某一个 Internet 时间服务器(Internet Time Server ITS)。

没什么会阻止你在相同环境中使用集群的和非集群的 Quartz 应用。唯一要注意的是这两个环境不要混用在相同的数据库表。意思是非集群环境不要使用与集群应用相同的一套数据库表；否则将得到希奇古怪的结果，集群和非集群的 Job 都会遇到问题。




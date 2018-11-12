package com.xyh.mq.activemq;

/**
 * 
 * @author hcxyh  2018年8月7日
 *
 */
public class ActiveMqNote {
	
	/**
	 * 重点就是 ：
	 * 1.事务消息
	 * 2.消息的确认机制,重发机制和死信队列
	 * 3.消息的持久化
	 */
	
	
	/**
	 1.特点
		支持多种语言编写客户端
		对Spring的支持，很容易和Spring整合
		支持多种传输协议：TCP，SSL，NIO，UDP等
		支持Ajax请求
	  2.p2p 点对点通过queue进行通讯。    -->  Queue，不可重复消费
	  	点对点模型是一个基于拉取（pull）或轮询（polling）的消息传送模型，这种模型从队列中请求消息，而不是自动地将消息推送到客户端。 
	  	{	
	  		消息生产者生产消息发送到queue中，然后消息消费者从queue中取出并且消费消息。
			消息被消费以后，queue中不再有存储，所以消息消费者不可能消费到已经被消费的消息。
			Queue支持存在多个消费者，但是对一个消息而言，只会有一个消费者可以消费、其它的则不能消费此消息了。
			当消费者不存在时，消息会一直保存，直到有消费消费
	  		消息首先被传送至消息服务器端特定的队列中，然后从此对列中将消息传送至对此队列进行监听的某个消费者。
	  		同一个队列可以关联多个消息生产者和消息消费者，但一条消息仅能传递给一个消息消费者。
	  		如果多个消息消费者正在监听队列上的消息，
	  		JMS消息服务器将根据“先来者优先”的原则确定由哪个消息消费者接收下一条消息。
	  		如果没有消息消费者在监听队列，消息将保留在队列中，直至消息消费者连接到队列为止。
	  		这种消息传递模型是传统意义上的懒模型或轮询模型。在此模型中，消息不是自动推动给消息消费者的，
	  		而是要由消息消费者从队列中请求获得。 
	  	}
	   	Pub/Sub(Publish/Subscribe，即发布-订阅)模型，通过topic通讯.
	   	{
	   		pub/sub消息传递模型允许多个主题订阅者接收同一条消息。
	   		JMS一直保留消息，直至所有主题订阅者都接收到消息为止。pub/sub消息传递模型基本上是一个推模型。
	   		在该模型中，消息会自动广播，消息消费者无须通过主动请求或轮询主题的方法来获得新的消息。 
	   	}
	   	{
	   		类型 					Topic											Queue
			概要		Publish Subscribe messaging 发布订阅消息				Point-to-Point 点对点
			有无状态	topic数据默认不落地，是无状态的。								Queue数据默认会在mq服务器上以文件形式保存，
																		比如Active MQ一般保存在$AMQ_HOME\data\kr-store\data下面。
																		也可以配置成DB存储。
		
		完整性保障		并不保证publisher发布的每条数据，Subscriber都能接受到。			Queue保证每条数据都能被receiver接收。

		消息是否会丢失	一般来说publisher发布消息到某一个topic时，					Sender发送消息到目标Queue，receiver可以异步接收这个Queue上的消息。
					只有正在监听该topic地址的sub能够接收到消息；					Queue上的消息如果暂时没有receiver来取，也不会丢失。
					如果没有sub在监听，该topic就丢失了。

		消息发布接收策略	一对多的消息发布接收策略，监听同一个topic地址的多个sub都                                    一对一的消息发布接收策略，一个sender发送的消息，只能有一个receiver接收。
					能收到publisher发送的消息。Sub接收完通知mq服务器				receiver接收完后，通知mq服务器已接收，mq服务器对queue里的消息采取删除或其他操作。
	   	}
	  3.activeMq目录结构
	  	bin目录包含ActiveMQ的启动脚本
		conf目录包含ActiveMQ的所有配置文件
		data目录包含日志文件和持久性消息数据
		example: ActiveMQ的示例
		lib: ActiveMQ运行所需要的lib
		webapps: ActiveMQ的web控制台和一些相关的demo
	  4.項目中的使用
	  	在最近一个批量删除公会的需求里，由于批量删除操作涉及大量耗时操作，很容易造成超时和失败。我们通过使用消息队列，
	  	把删除请求和实际删除操作解耦开来， 异步处理。Server只负责把接受到删除的请求放进消息队列和限制用户的请求频率，
	  	请求放进MQ后迅速返回成功给前端，并不进行实际的业务删除操作。后台的daemon负责不断从消息队列中获取请求并执行删除操作，
	  	操作完成后通过公众号消息推送结果给用户。
	  5.同步异步消费消息
	  	因为针对消费比较快的消费者，我们使用同步（可以减少异步发送消息时产生的上下文切换），针对消费比较慢的消费者，我们使用异步。
	  	 同步发送消息的缺点是，对于生产者发送的消息，如果消费者消费的比较慢，那么生产者就会被阻塞。
	  	{
	  		通过 ConnectionFactory 设置异步
	  		((ActiveMQConnectionFactory)connectionFactory).setDispatchAsync(true);
	  		通过 Connection 设置异步
	  		
	  	}
	  6.假死
	  	activemq 消息堆积一段时间后，消息不再发送，也无法接收
		1.打开conf/activemq.xml  查看 <storeUsage limit="40 gb"/> activemq 的磁盘大小为40gb , 执
		行命令 df -h 查看linux 磁盘是否使用完，进入 data 目录 执行 du-h 命令 查看 activemq data 使用了多少(注意:建议不要把日志放到data 目录)，
		如果linux 磁盘空间不够或者 data目录大小已经超过 activemq 分配的磁盘空间，都会导致假死
		2. 由于消息堆积最后大量消息进入年老代，full gc会导致jvm 停顿  也会导致假死，建议配置流量控制 ，以及清理死信队列 ，如过期消息不进入死信队列 配置 
		流量控制以及 过期消息不进入死信队列 配置请参考 http://blog.csdn.net/lizehua123/article/details/50489213
		jmx 监控参考http://blog.csdn.net/lizehua123/article/details/50193819
	  7.优化
	  	1.对队列大小进行限制，进行流控设置
	  	2.过期消息不进入死信队列
	  8.重发机制和确认机制
	  	 <!-- 定义ReDelivery(重发机制)机制 ，重发时间间隔是100毫秒，最大重发次数是3次 http://www.kuqin.com/shuoit/20140419/339344.html -->
		<bean id="activeMQRedeliveryPolicy" class="org.apache.activemq.RedeliveryPolicy">
			<!--是否在每次尝试重新发送失败后,增长这个等待时间 -->
			<property name="useExponentialBackOff" value="true"></property>
			<!--重发次数,默认为6次   这里设置为1次 -->
			<property name="maximumRedeliveries" value="1"></property>
			<!--重发时间间隔,默认为1秒 -->
			<property name="initialRedeliveryDelay" value="1000"></property>
			<!--第一次失败后重新发送之前等待500毫秒,第二次失败再等待500 * 2毫秒,这里的2就是value -->
			<property name="backOffMultiplier" value="2"></property>
			<!--最大传送延迟，只在useExponentialBackOff为true时有效（V5.5），假设首次重连间隔为10ms，倍数为2，那么第 
				二次重连时间间隔为 20ms，第三次重连时间间隔为40ms，当重连时间间隔大的最大重连时间间隔时，以后每次重连时间间隔都为最大重连时间间隔。 -->
			<property name="maximumRedeliveryDelay" value="1000"></property>
		</bean>
		 <!--创建连接工厂 -->
		<bean id="connectionFactory" class="org.apache.activemq.ActiveMQConnectionFactory">
			<property name="brokerURL" value="tcp://localhost:61616"></property>
			<property name="redeliveryPolicy" ref="activeMQRedeliveryPolicy" />  <!-- 引用重发机制 -->
		</bean>
		
		TODO  重发控制和死信队列
		1.使用一个事务session，并且调用了rollback()方法；
		2.一个事务session，关闭之前调用了commit；
		3.在session中使用CLIENT_ACKNOWLEDGE签收模式，并且调用了Session.recover()方法。
		Broker根据自己的规则，通过BrokerInfo命令包和客户端建立连接，向客户端传送缺省发送策略。
		但是客户端可以使用ActiveMQConnection.getRedeliveryPolicy()方法覆盖override这个策略设置。
		RedeliveryPolicy policy = connection.getRedeliveryPolicy();  
		policy.setInitialRedeliveryDelay(500);  
		policy.setBackOffMultiplier(2);  
		policy.setUseExponentialBackOff(true);  
		policy.setMaximumRedeliveries(2);  
		一旦消息重发尝试超过重发策略中配置的maximumRedeliveries(缺省为6次)时，会给broker发送一个"Poison ack"，通知它，这个消息被认为是一个毒丸(a poison pill)，接着broker会将这个消息发送到DLQ(Dead Letter Queue)，以便后续分析处理。
		缺省死信队列(Dead Letter Queue)叫做ActiveMQ.DLQ;所有的未送达消息都会被发送到这个队列，以致会非常难于管理。你可以设置activemq.xml文件中的destination policy map的"individualDeadLetterStrategy"属性来修改.
		
		自动丢弃过期消息(Expired Messages)
		一些应用可能只是简单的丢弃过期消息，而不想将它们放到DLQ中，完全跳过了DLQ。在dead letter strategy死信策略上配置processExpired属性为false，可以实现这个功能。
		
	  9.ack机制
	  	{
	  		JMS API中约定了Client端可以使用四种ACK_MODE,在javax.jms.Session接口中:
		         AUTO_ACKNOWLEDGE = 1    自动确认
		         CLIENT_ACKNOWLEDGE = 2    客户端手动确认   
		         DUPS_OK_ACKNOWLEDGE = 3    自动批量确认
		         SESSION_TRANSACTED = 0    事务提交并确认
		         此外AcitveMQ补充了一个自定义的ACK_MODE:    INDIVIDUAL_ACKNOWLEDGE = 4    单条消息确认。
		   ACK_MODE描述了Consumer与broker确认消息的方式(时机),比如当消息被Consumer接收之后,
		   Consumer将在何时确认消息。对于broker而言，只有接收到ACK指令,才会认为消息被正确的接收或者处理成功了,
		         通过ACK，可以在consumer与Broker之间建立一种简单的“担保”机制. 
	  	}
	  	Client端指定了ACK_MODE,但是在Client与broker在交换ACK指令的时候,还需要告知ACK_TYPE,ACK_TYPE表示此确认指令的类型，
	  	不同的ACK_TYPE将传递着消息的状态，broker可以根据不同的ACK_TYPE对消息进行不同的操作。
	  	在JMS API中并没有定义ACT_TYPE,因为它通常是一种内部机制,并不会面向开发者。ActiveMQ中定义了如下几种ACK_TYPE(参看MessageAck类):
	  	{
	  		 DELIVERED_ACK_TYPE = 0    消息"已接收"，但尚未处理结束
	         STANDARD_ACK_TYPE = 2    "标准"类型,通常表示为消息"处理成功"，broker端可以删除消息了
	         POSION_ACK_TYPE = 1    消息"错误",通常表示"抛弃"此消息，比如消息重发多次后，都无法正确处理时，消息将会被删除或者DLQ(死信队列)
	         REDELIVERED_ACK_TYPE = 3    消息需"重发"，比如consumer处理消息时抛出了异常，broker稍后会重新发送此消息
	         INDIVIDUAL_ACK_TYPE = 4    表示只确认"单条消息",无论在任何ACK_MODE下    
	         UNMATCHED_ACK_TYPE = 5    BROKER间转发消息时,接收端"拒绝"消息
	  	}
	  	Client端在不同的ACK_MODE时,将意味着在不同的时机发送ACK指令,每个ACK Command中会包含ACK_TYPE,那么broker端就可以根据ACK_TYPE来决定此消息的后续操作.
	    
	    <!-- 消息监听容器 消息接收监听器用于异步接收消息 -->
		<bean id="jmsContainerOne" class="org.springframework.jms.listener.DefaultMessageListenerContainer">
			<property name="connectionFactory" ref="connectionFactory" />
			<property name="destination" ref="destinationOne" />
			<property name="messageListener" ref="consumerMessageListenerOfOne" />
		</bean>
	 10,事务消息
	 	消息事务是在生产者producer到broker或broker到consumer过程中同一个session中发生的，保证几条消息在发送过程中的原子性。
	 	(Broker:消息队列核心，相当于一个控制中心，负责路由消息、保存订阅和连接、消息确认和控制事务)
	1）、带事务的session
             如果session带有事务，并且事务成功提交，则消息被自动签收。如果事务回滚，则消息会被再次传送。
             消息事务是在生产者producer到broker或broker到consumer过程中同一个session中发生的，
             保证几条消息在发送过程中的原子性。
             在支持事务的session中，producer发送message时在message中带有transactionID。
    broker收到message后判断是否有transactionID，如果有就把message保存在transaction store中，
             等待commit或者rollback消息。
    2）、不带事务的session
             不带事务的session的签收方式，取决于session的配置。
             Activemq支持一下三種模式：
             Session.AUTO_ACKNOWLEDGE  消息自动签收
             Session.CLIENT_ACKNOWLEDGE  客戶端调用acknowledge方法手动签收
             Session.DUPS_OK_ACKNOWLEDGE 不是必须签收，消息可能会重复发送。在第二次重新传送消息的时候，消息
             头的JmsDelivered会被置为true标示当前消息已经传送过一次，客户端需要进行消息的重复处理控制。
             代码示例如下：
             session = connection.createSession(true, Session.CLIENT_ACKNOWLEDGE);
             textMsg.acknowledge();
     
     11.消息持久化
     1.ActiveMQ的几种消息持久化机制
		为了避免意外宕机以后丢失信息，需要做到重启后可以恢复消息队列，消息系统一般都会采用持久化机制。
		ActiveMQ的消息持久化机制有JDBC，AMQ，KahaDB和LevelDB，无论使用哪种持久化方式，
		消息的存储逻辑都是一致的。
		就是在发送者将消息发送出去后，消息中心首先将消息存储到本地数据文件、内存数据库或者远程数据库等，
		然后试图将消息发送给接收者，发送成功则将消息从存储中删除，失败则继续尝试。
		消息中心启动以后首先要检查指定的存储位置，如果有未发送成功的消息，则需要把消息发送出去。
		>> JDBC持久化方式
		使用JDBC持久化方式，数据库会创建3个表：activemq_msgs，activemq_acks和activemq_lock。
		activemq_msgs用于存储消息，Queue和Topic都存储在这个表中。
		（1）配置方式
		配置持久化的方式，都是修改安装目录下conf/acticvemq.xml文件，
		首先定义一个mysql-ds的MySQL数据源，然后在persistenceAdapter节点中配置jdbcPersistenceAdapter并且引用刚才定义的数据源
		<persistenceAdapter>
		        <jdbcPersistenceAdapter dataSource="#mysql-ds" createTablesOnStartup="false" />
		</persistenceAdapter>
		dataSource指定持久化数据库的bean，createTablesOnStartup是否在启动的时候创建数据表，默认值是true，
		这样每次启动都会去创建数据表了，一般是第一次启动的时候设置为true，之后改成false。
		使用MySQL配置JDBC持久化: 
		<beans>
        <broker brokerName="test-broker" persistent="true" xmlns="http://activemq.apache.org/schema/core">
            <persistenceAdapter>
                <jdbcPersistenceAdapter dataSource="#mysql-ds" createTablesOnStartup="false"/>
            </persistenceAdapter>
        </broker>
        <bean id="mysql-ds" class="org.apache.commons.dbcp.BasicDataSource" destroy-method="close">
            <property name="driverClassName" value="com.mysql.jdbc.Driver"/>
            <property name="url" value="jdbc:mysql://localhost/activemq?relaxAutoCommit=true"/>
            <property name="username" value="activemq"/>
            <property name="password" value="activemq"/>
            <property name="maxActive" value="200"/>
            <property name="poolPreparedStatements" value="true"/>
        </bean>
    	</beans>
		（2）数据库表信息
		activemq_msgs用于存储消息，Queue和Topic都存储在这个表中：
		ID：自增的数据库主键
		CONTAINER：消息的Destination
		MSGID_PROD：消息发送者客户端的主键
		MSG_SEQ：是发送消息的顺序，MSGID_PROD+MSG_SEQ可以组成JMS的MessageID
		EXPIRATION：消息的过期时间，存储的是从1970-01-01到现在的毫秒数
		MSG：消息本体的Java序列化对象的二进制数据
		PRIORITY：优先级，从0-9，数值越大优先级越高
		
		activemq_acks用于存储订阅关系。如果是持久化Topic，订阅者和服务器的订阅关系在这个表保存：
		主要的数据库字段如下：
		    CONTAINER：消息的Destination
		    SUB_DEST：如果是使用Static集群，这个字段会有集群其他系统的信息
		    CLIENT_ID：每个订阅者都必须有一个唯一的客户端ID用以区分
		    SUB_NAME：订阅者名称
		    SELECTOR：选择器，可以选择只消费满足条件的消息。条件可以用自定义属性实现，可支持多属性AND和OR操作
		    LAST_ACKED_ID：记录消费过的消息的ID。
		
		    表activemq_lock在集群环境中才有用，只有一个Broker可以获得消息，称为Master Broker，
		    其他的只能作为备份等待Master Broker不可用，才可能成为下一个Master Broker。
		    这个表用于记录哪个Broker是当前的Master Broker。
	 >> AMQ方式
	 	性能高于JDBC，写入消息时，会将消息写入日志文件，由于是顺序追加写，性能很高。为了提升性能，创建消息主键索引，
	 	并且提供缓存机制，进一步提升性能。每个日志文件的大小都是有限制的（默认32m，可自行配置）。
		当超过这个大小，系统会重新建立一个文件。当所有的消息都消费完成，系统会删除这个文件或者归档（取决于配置）。
		主要的缺点是AMQ Message会为每一个Destination创建一个索引，如果使用了大量的Queue，索引文件的大小会占用很多磁盘空间。
		 而且由于索引巨大，一旦Broker崩溃，重建索引的速度会非常慢。
		 配置片段如下：
		<persistenceAdapter>
         <amqPersistenceAdapter directory="${activemq.data}/activemq-data" maxFileLength="32mb"/>
    	</persistenceAdapter>
    	虽然AMQ性能略高于下面的Kaha DB方式，但是由于其重建索引时间过长，而且索引文件占用磁盘空间过大，所以已经不推荐使用。
	 >> KahaDB方式
	 	KahaDB是从ActiveMQ 5.4开始默认的持久化插件，也是我们项目现在使用的持久化方式。
	    KahaDb恢复时间远远小于其前身AMQ并且使用更少的数据文件，所以可以完全代替AMQ。
	    kahaDB的持久化机制同样是基于日志文件，索引和缓存。
	    配置方式：
	    <persistenceAdapter>
        <kahaDB directory="${activemq.data}/activemq-data" journalMaxFileLength="16mb"/>
    	</persistenceAdapter>
		directory : 指定持久化消息的存储目录
    	journalMaxFileLength : 指定保存消息的日志文件大小，具体根据你的实际应用配置 　
		
		2018.11.7
		1.1.1、点对点：Queue，不可重复消费
消息生产者生产消息发送到queue中，然后消息消费者从queue中取出并且消费消息。
消息被消费以后，queue中不再有存储，所以消息消费者不可能消费到已经被消费的消息。Queue支持存在多个消费者，但是对一个消息而言，只会有一个消费者可以消费。
		1.2、发布/订阅：Topic，可以重复消费
消息生产者（发布）将消息发布到topic中，同时有多个消息消费者（订阅）消费该消息。和点对点方式不同，发布到topic的消息会被所有订阅者消费。

2.1、点对点模式
生产者发送一条消息到queue，一个queue可以有很多消费者，但是一个消息只能被一个消费者接受，当没有消费者可用时，这个消息会被保存直到有 一个可用的消费者，所以Queue实现了一个可靠的负载均衡。

2.2、发布订阅模式
发布者发送到topic的消息，只有订阅了topic的订阅者才会收到消息。topic实现了发布和订阅，当你发布一个消息，所有订阅这个topic的服务都能得到这个消息，所以从1到N个订阅者都能得到这个消息的拷贝。

对于消费者而言有两种方式从消息中间件获取消息：
①Push方式：由消息中间件主动地将消息推送给消费者；
②Pull方式：由消费者主动向消息中间件拉取消息。
比较：
采用Push方式，可以尽可能快地将消息发送给消费者(stream messages to consumers as fast as possible)
而采用Pull方式，会增加消息的延迟，即消息到达消费者的时间有点长(adds significant latency per message)。
但是，Push方式会有一个坏处：
如果消费者的处理消息的能力很弱(一条消息需要很长的时间处理)，而消息中间件不断地向消费者Push消息，消费者的缓冲区可能会溢出。
		
		ActiveMQ是怎么解决这个问题的呢？那就是  prefetch limit

prefetch limit 规定了一次可以向消费者Push(推送)多少条消息。

Once the prefetch limit is reached, no more messages are dispatched to the consumer 
until the consumer starts sending back acknowledgements of messages (to indicate that the message has been processed)

当推送消息的数量到达了perfetch limit规定的数值时，消费者还没有向消息中间件返回ACK，消息中间件将不再继续向消费者推送消息。
prefetch limit设置的大小根据场景而定：
那prefetch limit的值设置为多少合适？视具体的应用场景而定。

If you have very few messages and each message takes a very long time to process 
you might want to set the prefetch value to 1 so that a consumer is given one message at a time. 

如果消息的数量很少(生产者生产消息的速率不快)，但是每条消息 消费者需要很长的时间处理，那么prefetch limit设置为1比较合适。
这样，消费者每次只会收到一条消息，当它处理完这条消息之后，向消息中间件发送ACK，此时消息中间件再向消费者推送下一条消息。

prefetch limit 设置成0意味着什么？意味着变成 拉pull模式。

Specifying a prefetch limit of zero means the consumer will poll for more messages, one at a time, 
instead of the message being pushed to the consumer.
意味着此时，消费者去轮询消息中间件获取消息。不再是Push方式了，而是Pull方式了。即消费者主动去消息中间件拉取消息。
prefetch Limit>0即为prefetch，=0为Pull，看起来没有不prefetch的push，push都要设置prefetch。
另外，对于prefetch模式（，那么消费需要进行响应ACK。因为服务器需要知道consumer消费的情况。
perfetch limit是“消息预取”的值，这是针对消息中间件如何向消费者发消息 而设置的。
与之相关的还有针对 消费者以何种方式向消息中间件返回确认ACK(响应)：
比如消费者是每次消费一条消息之后就向消息中间件确认呢？还是采用“延迟确认”---即采用批量确认的方式(消费了若干条消息之后，统一再发ACK)。
		
		
在程序中如何采用Push方式或者Pull方式呢？
	1.从是否阻塞来看，消费者有两种方式获取消息。同步方式和异步方式。
	同步方式使用的是ActiveMQMessageConsumer的receive()方法。而异步方式则是采用消费者实现MessageListener接口，监听消息。
	同步方式：
		使用同步方式receive()方法获取消息时，prefetch limit即可以设置为0，也可以设置为大于0
		prefetch limit为零 意味着：
			“receive()方法将会首先发送一个PULL指令并阻塞，直到broker端返回消息为止，这也意味着消息只能逐个获取(类似于Request<->Response)”
		prefetch limit 大于零 意味着：
			“broker端将会批量push给client一定数量的消息(<= prefetch)，client端会把这些消息(unconsumed Message)放入到本地的队列中，
		只要此队列有消息，那么receive方法将会立即返回（并消费），当一定量的消息ACK之后，broker端会继续批量push消息给client端。” 
	异步方式：
	当使用MessageListener异步获取消息时，prefetch limit必须大于零了。
	因为，prefetch limit 等于零 意味着消息中间件不会主动给消费者Push消息，而此时消费者又用MessageListener被动获取消息(不会主动去轮询消息)。
	这二者是矛盾的。
	此外，还有一个要注意的地方，即消费者采用同步获取消息(receive方法) 与 异步获取消息的方法(MessageListener) ，对消息的确认时机是不同的。
	这里提到了这篇文章：http://shift-alt-ctrl.iteye.com/blog/2020182 文章名《ActiveMQ消息传送机制以及ACK机制详解》
		
	Producer客户端使用来发送消息的， Consumer客户端用来消费消息；
	它们的协同中心就是ActiveMQ broker,broker也是让producer和consumer调用过程解耦的工具，最终实现了异步RPC/数据交换的功能。
	随着ActiveMQ的不断发展，支持了越来越多的特性，也解决开发者在各种场景下使用ActiveMQ的需求。
	比如producer支持异步调用；
		使用flow control机制让broker协同consumer的消费速率；
		consumer端可以使用prefetchACK来最大化消息消费的速率；
		提供"重发策略"等来提高消息的安全性等。
		
	 *
	 */

}

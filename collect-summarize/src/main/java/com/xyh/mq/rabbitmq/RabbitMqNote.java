package com.xyh.mq.rabbitmq;

/**
 * 
 * rabbitMq,我是跟着朱小厮的rabbit那本书看的.
 * https://blog.csdn.net/u013256816
 * @author hcxyh  2018年8月13日
 *
 */
public class RabbitMqNote {

	/**
	 	ConnectionFactory（连接管理器）：应用程序与Rabbit之间建立连接的管理器，程序代码中使用；
		Channel（信道）：消息推送使用的通道；
		Exchange（交换器）：用于接受、分配消息；
		Queue（队列）：用于存储生产者的消息；
		RoutingKey（路由键）：用于把生成者的数据分配到交换器上；
		BindingKey（绑定键）：用于把交换器的消息绑定到队列上；
		Broker:它提供一种传输服务,它的角色就是维护一条从生产者到消费者的路线，保证数据能按照指定的方式进行传输, 
		
		
		
		Exchange：消息交换机,它指定消息按什么规则,路由到哪个队列。 
		Queue:消息的载体,每个消息都会被投到一个或多个队列。 
		Binding:绑定，它的作用就是把exchange和queue按照路由规则绑定起来. 
		Routing Key:路由关键字,exchange根据这个关键字进行消息投递。 
		vhost:虚拟主机,一个broker里可以有多个vhost，用作不同用户的权限分离。 
		Producer:消息生产者,就是投递消息的程序. 
		Consumer:消息消费者,就是接受消息的程序. 
		Channel:消息通道,在客户端的每个连接里,可建立多个channel.
		
		1.任务分发机制
			1.Round-robin dispathching循环分发
				RabbbitMQ的分发机制非常适合扩展,而且它是专门为并发程序设计的,如果现在load加重,那么只需要创建更多的Consumer来进行任务处理。
			2.Message acknowledgment消息确认	
				为了保证数据不被丢失,RabbitMQ支持消息确认机制,为了保证数据能被正确处理而不仅仅是被Consumer收到,那么我们不能采用no-ack，而应该是在处理完数据之后发送ack. 
				在处理完数据之后发送ack,就是告诉RabbitMQ数据已经被接收,处理完成,RabbitMQ可以安全的删除它了. 
				如果Consumer退出了但是没有发送ack,那么RabbitMQ就会把这个Message发送到下一个Consumer，这样就保证在Consumer异常退出情况下数据也不会丢失. 
				RabbitMQ它没有用到超时机制.RabbitMQ仅仅通过Consumer的连接中断来确认该Message并没有正确处理，也就是说RabbitMQ给了Consumer足够长的时间做数据处理。 
				如果忘记ack,那么当Consumer退出时,Mesage会重新分发,然后RabbitMQ会占用越来越多的内存.
		2.Message durability消息持久化
			
		3.Fair dispath 公平分发
		
		4.分发到多个Consumer
		
		5.消息序列化
			RabbitMQ使用ProtoBuf序列化消息,它可作为RabbitMQ的Message的数据格式进行传输,由于是结构化的数据,这样就极大的方便了Consumer的数据高效处理
			
	 */
	
}

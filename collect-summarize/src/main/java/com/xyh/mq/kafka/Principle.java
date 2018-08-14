package com.xyh.mq.kafka;

/**
 * 
 * @author hcxyh  2018年8月7日
 * TODO https://mp.weixin.qq.com/mp/homepage?__biz=MzU5ODUwNzY1Nw==&hid=1&sn=4dc4f37ace0edaece205f28912d6a1c1&scene=1&devicetype=iOS11.2.6&version=16070026&lang=zh_CN&nettype=3G+&ascene=1&session_us=gh_14dd347dfc9e&wx_header=1&from=groupmessage&isappinstalled=0&fontScale=100
 * 
 *
 */
public class Principle {
	
	/**
	 Q:
	 	1.Kafka的topic和分区内部是如何存储的，有什么特点？
		2.与传统的消息系统相比,Kafka的消费模型有什么优点?
		3.Kafka如何实现分布式的数据存储与数据读取?
	
	 名词解释:
	 Broker : 消息中间件处理节点，一个Kafka节点就是一个broker，一个或者多个Broker可以组成一个Kafka集群
	 Topic  : 主题，Kafka根据topic对消息进行归类，发布到Kafka集群的每条消息都需要指定一个topic
	 Producer : 消息生产者，向Broker发送消息的客户端
	 Consumer : 消息消费者，从Broker读取消息的客户端
	 ConsumerGroup : 每个Consumer属于一个特定的Consumer Group，一条消息可以发送到多个不同的Consumer Group，但是一个Consumer Group中只能有一个Consumer能够消费该消息
	 Partition : 物理上的概念，一个topic可以分为多个partition，每个partition内部是有序的
	 
	 2.Topic和Partition
	   在Kafka中的每一条消息都有一个topic。一般来说在我们应用中产生不同类型的数据，都可以设置不同的主题。
	   一个主题一般会有多个消息的订阅者，当生产者发布消息到某个主题时，订阅了这个主题的消费者都可以接收到生产者写入的新消息。
	 kafka为每个主题维护了分布式的分区(partition)日志文件，每个partition在kafka存储层面是append log。
	   任何发布到此partition的消息都会被追加到log文件的尾部，在分区中的每条消息都会按照时间顺序分配到一个单调递增的顺序编号，
	   也就是我们的offset,offset是一个long型的数字，我们通过这个offset可以确定一条在该partition下的唯一消息。
	   在partition下面是保证了有序性，但是在topic下面没有保证有序性。
	 {
	 	每条消息中,如果没有key值得话,会轮询发送到各个Partition中.
	 	有key值，对key进行hash取余,保证了同一个key被路由到同一个分区中.
	 	如果希望队列强顺序一致,所有的消息使用同一个Key.
	   }
	 3.消费模型
	   消息由生产者发送到kafka集群后，会被消费者消费。一般来说我们的消费模型有两种:推送模型(push)和拉取模型(pull)
	   基于推送模型的消息系统，由消息代理记录消费状态。消息代理将消息推送到消费者后，标记这条消息为已经被消费，
	   但是这种方式无法很好地保证消费的处理语义。比如当我们把已经把消息发送给消费者之后，由于消费进程挂掉或者由于网络原因没有收到这条消息，
	   如果我们在消费代理将其标记为已消费，这个消息就永久丢失了。如果我们利用生产者收到消息后回复这种方法，
	   消息代理需要记录消费状态，这种不可取。如果采用push，消息消费的速率就完全由消费代理控制，一旦消费者发生阻塞，就会出现问题。
	 Kafka采取拉取模型(poll)，由自己控制消费速度，以及消费的进度，消费者可以按照任意的偏移量进行消费。
	   比如消费者可以消费已经消费过的消息进行重新处理，或者消费最近的消息等等。
	 */
}

package com.xyh.mq.activemq.queue;

import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * QueuProduce
 * 
 * @author hcxyh 2018年8月7日
 *
 */
@Controller
public class ProduceQueueController {

	@RequestMapping("/queueProduceMessage")

	@ResponseBody

	public Map<String, Object> queueProduceMessage() throws Exception {

		// JMS的使用比较类似于JDBC与Hibernate
		// 1. 创建一个连接工厂（类似于JDBC中的注册驱动），需要传入TCP协议的ActiveMQ服务地址
		//ActiveMQConnection.DEFAULT_USER,ActiveMQConnection.DEFAULT_PASSWORD,用户名密码可选

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");

		// 2. 创建连接（类似于DriverManager.getConnection）

		Connection connection = connectionFactory.createConnection();

		// 3. 开启连接（ActiveMQ创建的连接是需要手动开启的）

		connection.start(); // 注意不是open。。。

		// 4. 获取session（类似于Hibernate中的session，都是用会话来进行操作），一个发送或者接受消息的线程

		// 里面有两个参数，参数1为是否开启事务，参数2为消息确认模式

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 5. 创建一对一的消息队列

		Queue queue = session.createQueue("test_queue");

		// 6. 创建一条消息

		String text = "test queue message" + Math.random();

		TextMessage message = session.createTextMessage(text);

		// 7. 消息需要发送方，要创建消息发送方（生产者），并绑定到某个消息队列上

		MessageProducer producer = session.createProducer(queue);

		// 8. 发送消息

		producer.send(message);

		// 9. 关闭连接

		producer.close();

		session.close();

		connection.close();

		// ------显示发送的消息到视图上------

		Map<String, Object> map = new HashMap<>();

		map.put("message", text);

		return map;

	}

}

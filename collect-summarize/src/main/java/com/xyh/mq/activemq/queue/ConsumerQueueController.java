package com.xyh.mq.activemq.queue;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ConsumerQueueController {

	@RequestMapping("/queueGetMessage1")

	public void queueGetMessage1() throws Exception {

		// 1. 创建一个连接工厂，需要传入TCP协议的ActiveMQ服务地址

		ActiveMQConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://127.0.0.1:61616");

		// 2. 创建连接

		Connection connection = connectionFactory.createConnection();

		// 3. 开启连接

		connection.start(); 

		// 4. 获取session
		// 里面有两个参数，参数1为是否开启事务，参数2为消息确认模式

		Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

		// 5. 创建一对一的消息队列

		Queue queue = session.createQueue("test_queue");

		// ------------前5步都是相同的，以下为不同----------------

		// 6. 创建消费者

		MessageConsumer consumer = session.createConsumer(queue);

		// 7. 使用监听器监听队列中的消息

		consumer.setMessageListener(new MessageListener() {

			@Override

			public void onMessage(Message message) {

				TextMessage textMessage = (TextMessage) message;

				try {

					String text = textMessage.getText();

					System.out.println("收到消息：" + text);

				} catch (JMSException e) {

					e.printStackTrace();

				}

			}

		});

		// 由于设置监听器后不能马上结束方法，要在这里加一个等待点

		System.in.read();

		// 8. 关闭连接

		consumer.close();

		session.close();

		connection.close();

	}

}

package com.xyh.mq.activemq.base;

import java.io.Serializable;
import java.util.HashMap;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.DeliveryMode;
import javax.jms.Destination;
import javax.jms.MessageProducer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * @author hcxyh  2018年8月7日
 *
 */
public class RequestSubmit
{
    //消息发送者
    private MessageProducer producer;
    //一个发送或者接受消息的线程
    private Session session;

    public void init() throws Exception
    {
        //ConnectionFactory连接工厂，JMS用它创建连接
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://127.0.0.1:61616"); //使用阿里云测试
        //Connection：JMS客户端到JMS Provider的连接，从构造工厂中得到连接对象
        Connection connection = connectionFactory.createConnection();
        //启动
        connection.start();
        
        /**
         	创建Session时有两个非常重要的参数，第一个boolean类型的参数用来表示是否采用事务消息。
         	如果是事务消息，对于的参数设置为true，此时消息的提交自动有comit处理，消息的回滚则自动由rollback处理。
         	加入消息不是事务的，则对应的该参数设置为false，此时分为三种情况：
         	Session.AUTO_ACKNOWLEDGE表示Session会自动确认所接收到的消息。
			Session.CLIENT_ACKNOWLEDGE表示由客户端程序通过调用消息的确认方法来确认所接收到的消息。
			Session.DUPS_OK_ACKNOWLEDGE使得Session将“懒惰”地确认消息，即不会立即确认消息，这样有可能导致消息重复投递
         */
        //获取连接操作
        session = connection.createSession(Boolean.TRUE, Session.AUTO_ACKNOWLEDGE);
        Destination destinatin = session.createQueue("RequestQueue");
        //得到消息生成（发送）者
        producer = session.createProducer(destinatin);
        //设置不持久化
        producer.setDeliveryMode(DeliveryMode.NON_PERSISTENT);
    }

    public void submit(HashMap<Serializable,Serializable> requestParam) throws Exception
    {
        ObjectMessage message = session.createObjectMessage(requestParam);
        producer.send(message);
        session.commit();
    }

    public static void main(String[] args) throws Exception{
        RequestSubmit submit = new RequestSubmit();
        submit.init();
        HashMap<Serializable,Serializable> requestParam = new HashMap<Serializable,Serializable>();
        requestParam.put("薛煜辉", "xyh");
        submit.submit(requestParam);
        //TODO 资源释放
    }
}
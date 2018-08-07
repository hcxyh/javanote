package com.xyh.mq.activemq.base;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.MessageConsumer;
import javax.jms.ObjectMessage;
import javax.jms.Session;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

/**
 * 
 * @author hcxyh  2018年8月7日
 *
 */
public class RequestProcessor
{
    public void requestHandler(HashMap<Serializable,Serializable> requestParam) throws Exception
    {
        System.out.println("requestHandler....."+requestParam.toString());
        for(Map.Entry<Serializable, Serializable> entry : requestParam.entrySet())
        {
            System.out.println(entry.getKey()+":"+entry.getValue());
        }
    }

    public static void main(String[] args) throws Exception
    {
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(
                ActiveMQConnection.DEFAULT_USER,
                ActiveMQConnection.DEFAULT_PASSWORD,
                "tcp://10.10.195.187:61616");
        Connection connection = connectionFactory.createConnection();
        connection.start();
        Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
        Destination destination = session.createQueue("RequestQueue");
        //消息消费（接收）者
        MessageConsumer consumer = session.createConsumer(destination);

        RequestProcessor processor = new RequestProcessor();

        while(true)
        {	//同步
            ObjectMessage message = (ObjectMessage) consumer.receive(1000);  
            if(null != message)
            {
                System.out.println(message);
                HashMap<Serializable,Serializable> requestParam = (HashMap<Serializable,Serializable>) message.getObject();
                processor.requestHandler(requestParam);
            }
            else
            {
                break;
            }
        }
    }
}
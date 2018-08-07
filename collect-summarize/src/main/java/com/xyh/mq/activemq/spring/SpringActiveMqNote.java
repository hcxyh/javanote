package com.xyh.mq.activemq.spring;

/**
 * activeMq整合spring
 * 
 * @author hcxyh 2018年8月7日
 *
 */
public class SpringActiveMqNote {

	/**
	 * 1.需要依赖的包
	 * activeio-core-3.1.4.jar，activemq-all-5.13.2.jar，activemq-pool-5.13.2.jar，
	 * commons-pool2-2.4.2.jar，这些jar可以在ActiveMQ的安装包中的/lib/optional/下找到
	 * 2.ConnectionFactory 用于产生到JMS服务器的链接,，Spring为我们提供了多个ConnectionFactory，
	 * 有SingleConnectionFactory和CachingConnectionFactory.
	 * SingleConnectionFactory对于建立JMS服务器链接的请求会一直返回同一个链接，并且会忽略Connection的close方法调用。
	 * CachingConnectionFactory继承了SingleConnectionFactory，所以它拥有SingleConnectionFactory的所有功能，同时它还新增了缓存功能，可以缓存Session,
	 * MessageProducer和MessageConsumer。 {
	 * Spring提供的ConnectionFactory只是Spring用于管理ConnectionFactory的，
	 * 真正产生到JMS服务器链接的ConnectionFactory还得是JMS服务厂商提供的，
	 * 并且需要把它注入到Spring提供的ConnectionFactory中，这里使用ActiveMQ提供的ConnectionFactory
	 * <bean id="targetConnectionFactory" class=
	 * "org.apache.activemq.ActiveMQConnectionFactory">
	 * <property name="brokerURL" value="tcp://10.10.195.187:61616" /> </bean>
	 * <bean id="connectionFactory" class=
	 * "org.springframework.jms.connection.SingleConnectionFactory">
	 * <property name="targetConnectionFactory" ref="targetConnectionFactory"/>
	 * </bean>
	 * 
	 * @Bean public ConnectionFactory
	 *       targetConnectionFactory(@Value("${mq.broker.url}") String
	 *       brokerUrl, @Value("${mq.username}") String
	 *       userName, @Value("${mq.password})") String
	 *       password, @Value("${mq.connection.max:10}") int maxConnections) {
	 *       ActiveMQConnectionFactory activeMQConnectionFactory = new
	 *       ActiveMQConnectionFactory();
	 *       activeMQConnectionFactory.setBrokerURL(brokerUrl);
	 *       activeMQConnectionFactory.setUserName(userName);
	 *       activeMQConnectionFactory.setPassword(password);
	 *       PooledConnectionFactory pooledConnectionFactory = new
	 *       PooledConnectionFactory();
	 *       pooledConnectionFactory.setConnectionFactory(activeMQConnectionFactory);
	 *       pooledConnectionFactory.setMaxConnections(maxConnections); return
	 *       pooledConnectionFactory; }
	 * 
	 * @Bean
	 * @Autowired public ConnectionFactory
	 *            connectionFactory(@Qualifier("targetConnectionFactory")
	 *            ConnectionFactory connectionFactory) { return new
	 *            SingleConnectionFactory(connectionFactory); }
	 * 
	 *            }
	 */

	/**
	 * TODO connectionFactory 2.PooledConnectionFactory
	 * ActiveMQ为我们提供了一个PooledConnectionFactory，通过往里面注入一个ActiveMQConnectionFactory可以用来将Connection,
	 * Session和MessageProducer池化， 这样可以大大的减少我们的资源消耗。 {
	 * <bean id="targetConnectionFactory" class=
	 * "org.apache.activemq.ActiveMQConnectionFactory">
	 * <property name="brokerURL" value="tcp://10.10.195.187:61616" /> </bean>
	 * <bean id="poolConnectionFactory" class=
	 * "org.apache.activemq.pool.PooledConnectionFactory" >
	 * <property name="connectionFactory" ref="targetConnectionFactory" />
	 * <property name="maxConnections" value="10"/> </bean>
	 * <bean id="connectionFactory" class=
	 * "org.springframework.jms.connection.SingleConnectionFactory">
	 * <property name="targetConnectionFactory" ref="poolConnectionFactory"/>
	 * </bean> }
	 */

	/**
	 * TODO 生产者 3.配置消息生产者 配置好ConnectionFactory之后我们就需要配置生产者。生产者负责生产消息并发送到JMS服务器，
	 * 这通常对应的是我们的一个业务逻辑服务实现类。但是我们的服务实现类是怎么进行消息的发送的呢？
	 * 这通常是利用Spring为我们提供的JmsTemplate类来实现，
	 * 所以配置生产者其实最核心的就是配置进行消息发送的JmsTemplate。对于消息发送者而言， 它在发送消息的时候要知道自己该往哪里发，为此，
	 * 我们在定义JmsTemplate的时候需要往里面注入一个Spring提供的ConnectionFactory对象。
	 * 
	 * <bean id="jmsTemplate" class="org.springframework.jms.core.JmsTemplate">
	 * <property name="connectionFactory" ref="poolConnectionFactory"/> </bean>
	 * 
	 * @Bean
	 * @Autowired public JmsTemplate
	 *            reportJmsTemplate(@Qualifier("connectionFactory")
	 *            ConnectionFactory
	 *            connectionFactory, @Value("${mq.queue.message.report}") String
	 *            queueName) { JmsTemplate jmsTemplate = new
	 *            JmsTemplate(connectionFactory); ActiveMQQueue activeMQQueue = new
	 *            ActiveMQQueue(queueName);
	 *            jmsTemplate.setDefaultDestination(activeMQQueue); return
	 *            jmsTemplate; }
	 * 
	 *            真正利用JmsTemplate进行消息发送的时候，我们需要知道消息发送的目的地，即Destination。
	 *            在Jms中有一个用来表示目的地的Destination接口，它里面没有任何方法定义，只是用来做一个标志而已。
	 *            当我们在使用JmsTemplate进行消息发送时没有指定destination的时候将使用默认的Destination。
	 *            默认Destination可以通过在定义jmsTemplate bean对象时通过属性defaultDestination或
	 *            defaultDestinationName来进行诸如，defaultDestinationName对于的就是一个普通字符串。
	 *            在ActiveMQ中实现了两种类型的Destination，一个是点对点的ActiveMQQueue,
	 *            另一个就是支持订阅-发布模式的ActiveMQTopic。 <bean id="jmsTemplate" class=
	 *            "org.springframework.jms.core.JmsTemplate">
	 *            <property name="connectionFactory" ref="connectionFactory"/>
	 *            <property name="defaultDestination" ref="queueDestination"/>
	 *            </bean>
	 * 
	 *            <bean id="jmsTemplate" class=
	 *            "org.springframework.jms.core.JmsTemplate">
	 *            <property name="connectionFactory" ref="connectionFactory"/>
	 *            <property name="defaultDestinationName" value="sqlQueue"/> </bean>
	 *            直接在java程序中调用: jmsTemplate.convertAndSend(sql);
	 *            //根据业务需求reqId等对其进行封装 jmsTemplate.send(new MessageCreator() {
	 * @Override public Message createMessage(Session session) throws JMSException {
	 *           TextMessage respondMessage = session.createTextMessage(); } }
	 * 
	 */

	/**
	 * TODO 消费者 生产者往指定目的地Destination发送消息后，接下来就是消费者对指定目的地的消息进行消费了。
	 * 那么消费者是如何知道有生产者发送消息到指定目的地Destination了呢？
	 * 这是通过Spring为我们封装的消息监听容器MessageListenerContainer实现的，它负责接收信息，
	 * 并把接收到的信息分发给真正的MessageListener进行处理。
	 * 每个消费者对应每个目的地都需要有对应的MessageListenerContainer。
	 * 对于消息监听容器而言，除了要知道监听哪个目的地之外，还需要知道到哪里去监听， 也就是说它还需要知道去监听那个JMS服务器，
	 * 这是通过在配置MessageConnectionFactory的时候往里面注入一个ConnectionFactory来实现的。
	 * 所以我们在配置一个MessageListenerContainer的时候有三个属性必须指定，
	 * 一个是表示从哪里监听的ConnectionFactory，一个是表示监听什么的Destination，
	 * 一个是接收到消息以后进行消息处理的MessageListener.
	 * 
	 * @Bean
	 * @Autowired public MessageListenerContainer
	 *            smsMessageListenerContainer( @Qualifier("connectionFactory")
	 *            ConnectionFactory
	 *            connectionFactory, @Value("${mq.queue.sms.send}") String
	 *            smsQueue, @Qualifier("smsMessageListener") MessageListener
	 *            smsMessageListener, @Value("${mq.consumer.max:50}") int
	 *            maxConsumers) { ActiveMQQueue queue = new ActiveMQQueue(smsQueue);
	 *            DefaultMessageListenerContainer defaultMessageListenerContainer =
	 *            new DefaultMessageListenerContainer();
	 *            defaultMessageListenerContainer.setConnectionFactory(connectionFactory);
	 *            defaultMessageListenerContainer.setDestination(queue);
	 *            defaultMessageListenerContainer.setMessageListener(smsMessageListener);
	 *            defaultMessageListenerContainer.setConcurrentConsumers((maxConsumers
	 *            / 2) + 1);
	 *            defaultMessageListenerContainer.setMaxConcurrentConsumers(maxConsumers);
	 *            return defaultMessageListenerContainer; }
	 * 
	 *            Spring为我们听过了两种类型的MessageListenerContainer：
	 *            SimpleMessageListenerContainer和DefaultMessageListenerContainer。
	 *            MessageListenerContainer：
	 *            SimpleMessageListenerContainer会在一开始的时候就创建一个会话Session和消费者Consumer，
	 *            并且会适用标准的JMS的MessageConsumer.setMessageListener()方法注册监听器让JMS提供调用监听器的回调函数。
	 *            它不会动态的适应运行时需要和参与外部的事务管理。兼容性方面，它非常接近于独立的JMS规范，但一般不兼容J2EE的JMS限制。
	 *            大多数情况下，我们还是使用DefaultMessageListenerContainer，
	 *            跟MessageListenerContainer：SimpleMessageListenerContainer想比，
	 *            它会动态的适应运行时的需求，并且能够参与外部的事务管理。它很好的平衡了JMS提供者要求低， 先进功能如事务参与和兼容J2EE环境。
	 * 
	 *            <bean id="queueDestination" class=
	 *            "org.apache.activemq.command.ActiveMQQueue"> <constructor-arg>
	 *            <value>sqlQueue</value> </constructor-arg> </bean>
	 *            <bean id="listenerContainer" class=
	 *            "org.springframework.jms.listener.DefaultMessageListenerContainer">
	 *            <property name="connectionFactory" ref="poolConnectionFactory" />
	 *            <property name="messageListener" ref="jmsQueueReceiver" />
	 *            <property name="destination" ref="queueDestination" /> </bean>
	 * 
	 * 
	 *            import javax.jms.JMSException; import javax.jms.Message; import
	 *            javax.jms.MessageListener; import javax.jms.TextMessage;
	 * 
	 *            import org.springframework.stereotype.Component;
	 * 
	 * 			@Component("jmsQueueReceiver") public class JmsQueueReceiver
	 *            implements MessageListener {
	 * @Override public void onMessage(Message messgae) { if(messgae instanceof
	 *           TextMessage) { TextMessage textMessage = (TextMessage) messgae; try
	 *           { String text = textMessage.getText();
	 *           System.out.println("######################["+text+"]######################");
	 *           } catch (JMSException e) { e.printStackTrace(); } } } }
	 * 
	 */

	/**
	 * TODO 事务管理 Spring提供了一个JmsTransactionManager用于对JMS ConnectionFactory做事务管理。
	 * 这将允许JMS应用利用Spring的事务管理特性。
	 * JmsTransactionManager在执行本地资源事务管理时将从指定的ConnectionFactory
	 * 绑定一个ConnectionFactory/Session这样的配对到线程中。 JmsTemplate会自动检测这样的事务资源，并对他们进行相应的操作。
	 * 在J2EE环境中，ConnectionFactory会池化Connection和Session，
	 * 这样这些资源将会在整个事务中被有效地重复利用。在一个独立的环境中，
	 * 使用Spring的SingleConnectionFactory时所有的事务将公用一个Connection， 但是每个事务将保留自己独立的Session.
	 * JmsTemplate可以利用JtaTransactionManager和能够进行 分布式的JMS ConnectionFactory处理分布式事务。
	 * 在Spring整合JMS的应用中，如果我们要进行本地的事务管理的话非常简单，
	 * 只需要在定义对于的消息监听容器时指定其sessionTransacted属性为true(默认为false)：
	 * <bean id="listenerContainer" class=
	 * "org.springframework.jms.listener.DefaultMessageListenerContainer">
	 * <property name="connectionFactory" ref="connectionFactory" />
	 * <property name="messageListener" ref="jmsQueueReceiver" />
	 * <property name="destination" ref="queueDestination" />
	 * <property name="sessionTransacted" value="true"/> </bean>
	 * 这样JMS在进行消息监听的时候就会进行事务控制，当在接收消息时监听器执行失败时JMS就会对接收到的消息进行回滚。
	 * 这里需要注意的是对于其他操作如数据库等访问不属于该事务控制。 @Component("jmsQueueReceiver") public class
	 * JmsQueueReceiver implements MessageListener {
	 * 
	 * @Override public void onMessage(Message messgae) { if(messgae instanceof
	 *           TextMessage) { TextMessage textMessage = (TextMessage) messgae; try
	 *           { String text = textMessage.getText();
	 *           System.out.println("######################["+text+"]######################");
	 *           if(true) { throw new RuntimeException("Error"); } } catch
	 *           (JMSException e) { e.printStackTrace(); } } } }
	 *           如果想要接收消息和数据库访问处于同一事务中，那么我们就可以配置一个外部的事务管理同时配置一个支持外部事务管理的消息监听容器
	 *           （如DefaultMessageListenerContainer)。要配置这样一个参与分布式事务管理的消息监听容器，
	 *           我们可以配置一个JtaTransactionManager，当然底层的JMS
	 *           ConnectionFactory需要能够支持分布式事务管理， 并正确地注册我们的JtaTransactionManager。
	 *           这样消息监听器进行消息接收和对应的数据库访问就会处于同一数据库控制下， 当消息接收失败或数据库访问失败都会进行事务回滚操作。
	 *           <bean id="listenerContainer" class=
	 *           "org.springframework.jms.listener.DefaultMessageListenerContainer">
	 *           <property name="connectionFactory" ref="connectionFactory" />
	 *           <property name="messageListener" ref="jmsQueueReceiver" />
	 *           <property name="destination" ref="queueDestination" />
	 *           <property name="sessionTransacted" value="true"/>
	 *           <property name="transactionManager" ref="jtaTransactionManager"/>
	 *           </bean> <bean id="jtaTransactionManager" class=
	 *           "org.springframework.transaction.jta.JtaTransactionManager"/>
	 *         
	 */
}

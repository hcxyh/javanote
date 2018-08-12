/**
* @ClassName: package-info
* @author xueyh
* @date 2018年8月12日 下午4:03:26
* 
*/
package com.xyh.spring.eventlistener;

/**
 * 
	//定义用户注册服务(事件发布者)
@Service // 
public class UserService implements ApplicationEventPublisherAware { // <2>
    public void register(String name) {
        System.out.println("用户：" + name + " 已注册！");
        applicationEventPublisher.publishEvent(new UserRegisterEvent(name));// <3>
    }
    private ApplicationEventPublisher applicationEventPublisher; // <2>
    @Override
    public void setApplicationEventPublisher(ApplicationEventPublisher applicationEventPublisher) { // <2>
        this.applicationEventPublisher = applicationEventPublisher;
    }
   
}

定义邮件服务，积分服务，其他服务(事件订阅者)

@Service // <1>
public class EmailService implements ApplicationListener<UserRegisterEvent> { // <2>
    @Override
    public void onApplicationEvent(UserRegisterEvent userRegisterEvent) {
        System.out.println("邮件服务接到通知，给 " + userRegisterEvent.getSource() + " 发送邮件...");// <3>
    }
}
<1> 事件订阅者的服务同样需要托管于Spring容器
<2> ApplicationListener<E extends ApplicationEvent>接口是由Spring提供的事件订阅者必须实现的接口，我们一般把该Service关心的事件类型作为泛型传入。
<3> 处理事件，通过event.getSource()即可拿到事件的具体内容，在本例中便是用户的姓名。
其他两个Service，也同样编写，实际的业务操作仅仅是打印一句内容即可，篇幅限制，这里省略。


编写启动类

@SpringBootApplication
@RestController
public class EventDemoApp {
    public static void main(String[] args) {
        SpringApplication.run(EventDemoApp.class, args);
    }
    @Autowired
Spring中事件的应用

在以往阅读Spring源码的经验中，接触了不少使用事件的地方，大概列了以下几个，加深以下印象：

Spring Security中使用AuthenticationEventPublisher处理用户认证成功，认证失败的消息处理。

public interface AuthenticationEventPublisher {
   void publishAuthenticationSuccess(Authentication authentication);
   void publishAuthenticationFailure(AuthenticationException exception,
         Authentication authentication);
}

Hibernate中持久化对象属性的修改是如何被框架得知的？正是采用了一系列持久化相关的事件，如DefaultSaveEventListener，DefaultUpdateEventListener,事件非常多，有兴趣可以去org.hibernate.event包下查看。

Spring Cloud Zuul中刷新路由信息使用到的ZuulRefreshListener

private static class ZuulRefreshListener implements ApplicationListener<ApplicationEvent> {
       ...
        public void onApplicationEvent(ApplicationEvent event) {
            if(!(event instanceof ContextRefreshedEvent) && !(event instanceof RefreshScopeRefreshedEvent) && !(event instanceof RoutesRefreshedEvent)) {
                if(event instanceof HeartbeatEvent && this.heartbeatMonitor.update(((HeartbeatEvent)event).getValue())) {
                    this.zuulHandlerMapping.setDirty(true);
                }
            } else {
                this.zuulHandlerMapping.setDirty(true);
            }
        }
    }

Spring容器生命周期相关的一些默认Event

ContextRefreshedEvent,ContextStartedEvent,ContextStoppedEvent,ContextClosedEvent,RequestHandledEvent

。。。其实吧，非常多。。。

总结
	
	
	
 * 
 * 
 * 
 * 
 * 
 */

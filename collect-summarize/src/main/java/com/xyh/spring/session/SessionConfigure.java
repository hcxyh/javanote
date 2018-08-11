package com.xyh.spring.session;

import org.springframework.context.annotation.Bean;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.CookieSerializer;
import org.springframework.session.web.http.DefaultCookieSerializer;
import org.springframework.session.web.http.HeaderHttpSessionStrategy;
import org.springframework.session.web.http.HttpSessionStrategy;

/**
 * session的最大存活时间 和 在redis里存储session的空间名称还有
 * session更新策略，有ON_SAVE、IMMEDIATE，
 * 前者是在调用SessionRepository#save(org.springframework.session.Session)时，
 * 在response commit前刷新缓存，后者是只要有任何更新就会刷新缓存
 */
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 100, redisNamespace = "xxxx")
public class SessionConfigure {

	/**
	 * 1.xml方式 (需要配置filter) 
	 * 2.注解配置方式
	 * 其中注解 EnableRedisHttpSession 创建了一个名为springSessionRepositoryFilter的SpringBean,
	 * 该Bean实现了Filter接口。该filter负责通过 Spring Session 替换HttpSession 从哪里返回。
	 * 这里Spring Session是通过 redis返回。 
	 * 类中的方法 httpSessionStrategy(),
	 * 用来定义SpringSession的HttpSession集成使用HTTP的头来取代使用cookie传送当前session信息。
	 * 如果使用下面的代码，则是使用cookie来传送session信息。
	 */
	@Bean
	public HttpSessionStrategy httpSessionStrategy() {
		//使用header来读取session
		HeaderHttpSessionStrategy headerHttpSessionStrategy = new HeaderHttpSessionStrategy();
	    /**
	     * 通过添加filter,在dofilter中,直接从httpRequest中获取token,进行authority.
	     */
		headerHttpSessionStrategy.setHeaderName("Token");
	    return headerHttpSessionStrategy;
		// return new CookieHttpSessionStrategy();
	}
	
	//默认使用cookie来存储session
	@Bean
	public CookieSerializer cookieSerializer() {
		DefaultCookieSerializer serializer = new DefaultCookieSerializer();
		serializer.setCookieName("JSESSIONID");
		serializer.setCookiePath("/");
		serializer.setDomainNamePattern("^.+?\\.(\\w+\\.[a-z]+)$");
		return serializer;
	}
	
	
	/**
	 * TODO 
	 * spring-session
	 * 将session所保存的状态卸载到特定的外部session存储中,
	 * 如Redis或Apache Geode中，它们能够以独立于应用服务器的方式提供高质量的集群。
	 * 当用户使用WebSocket发送请求的时候，能够保持HttpSession处于活跃状态。
	 * 在非Web请求的处理代码中，能够访问session数据，比如在JMS消息的处理代码中。
	 * 支持每个浏览器上使用多个session，从而能够很容易地构建更加丰富的终端用户体验。
	 * 控制session id如何在客户端和服务器之间进行交换，
	 * 这样的话就能很容易地编写Restful API，
	 * 因为它可以从HTTP 头信息中获取session id，而不必再依赖于cookie。
	 */
	
}

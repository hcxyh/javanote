package com.xyh.spring.boot;

/**
 * springboot 管理中心
 * @author hcxyh  2018年8月11日
 *
 */
public class ActuatorNote {
	
	/*
	 * 1.endpoints(配置重启,优雅停机)
			在重启之前首先发送重启命令到endpoint,或者用kill 进程ID的方式,千万不要用kill -9。
			然后循环检测进程是否存在,如果服务正常停止了,进程也就不存在了,如果进程还在,证明还有未处理完的请求，停止1秒，继续检测。
			关于重启服务,建议用kill方式，这样就不用依赖spring-boot-starter-actuator,如果用endpoint方式，则需要控制好权限，不然随时都有可能被人重启了，可以用security来控制权限，我这边是自己用过滤器来控制的。在重启之前首先发送重启命令到endpoint，或者用kill 进程ID的方式，千万不要用kill -9。
			然后循环检测进程是否存在,如果服务正常停止了,进程也就不存在了,如果进程还在,证明还有未处理完的请求，停止1秒，继续检测。
			关于重启服务,建议用kill方式,这样就不用依赖spring-boot-starter-actuator,如果用endpoint方式，则需要控制好权限，不然随时都有可能被人重启了，可以用security来控制权限，我这边是自己用过滤器来控制的。
			如果用actuator方式重启的话需要配置启用重启功能:
			1.x配置如下：
			endpoints.shutdown.enabled=true
			2.x配置就比较多了，默认只暴露了几个常用的，而且访问地址也有变化，比如health, 以前是直接访问/health,现在需要 /actuator/health才能访问。我们可以通过配置来兼容之前的访问地址。
			shutdown默认是不暴露的，可以通过配置暴露并开始，配置如下：
			#访问路径，配置后就和1.x版本路径一样
			management.endpoints.web.base-path=/
			# 暴露所有，也可以暴露单个或多个
			management.endpoints.web.exposure.include=*
			# 开启shutdown
			management.endpoint.shutdown.enabled=true
	 		
	 */
	
}

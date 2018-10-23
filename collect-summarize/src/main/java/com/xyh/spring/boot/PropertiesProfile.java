package com.xyh.spring.boot;


/**
 * 
 * @author xyh
 *
 */
public class PropertiesProfile {

	
	/**
	 * springboot 配置文件 外置
	 * 
	 
	 1. 配置文件统一管理
		1.1 springboot核心配置文件
		Springboot读取核心配置文件（application.properties）的优先级为
		Jar包同级目录的config目录
		Jar包同级目录
		classPath(即resources目录)的config目录
		classpath目录
		上面是springboot默认去拿自己的核心配置文件的优先级，还有一种最高优先级的方式是项目启动时通过命令的方式指定项目加载核心配置文件，命令如下
		java –jar -Dspring.config.location=xxx/xxx/xxxx.properties xxxx.jar
		如果Spring Boot在优先级更高的位置找到了配置，那么它会无视优先级更低的配置
	 2.上面描述的Springboot核心文件已经能够提取出jar包外进行管理了，但是还有其他一些业务上的配置文件，
	 	如数据源配置文件，公共资源定义配置文件（常量，FTP信息等），quartz定时器，
	 	日志等配置文件我们如何去提取出来并确保能在代码中引用到呢
		我们知道Springboot项目可以通过注解方式来获取相关配置文件，
		所以我们也是通过注解方式让项目能够引用到jar包外部的配置文件的，
	 	
	 	@PropertySource里面的value有两个值，第一个是classpath下config目录下的数据源配置文件，
	 	第二个则是根据spring.profiles.path动态获取的目录，
	 	spring.profiles.path是我们在核心文件自定义的一个配置项，它的值是我们配置文件统一管理的文件夹路径，
	 	后面的ignoreResourceNotFound=true则是设定假如根据前面一个路径没有找到相关配置文件，
	 	则根据第二个路径去找。
	 	
	 3.
	 	
	 * 
	 */
	
}

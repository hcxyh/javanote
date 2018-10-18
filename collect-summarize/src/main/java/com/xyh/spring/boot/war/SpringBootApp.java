package com.xyh.spring.boot.war;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * war包部署
 * @author xyh
 *
 */
@SpringBootApplication
public class SpringBootApp extends SpringBootServletInitializer {
	
	/**
	 * SpringBootServletInitializer 类将在 Servlet 容器启动程序时允许我们对程序自定义配置,而这里我们将需要让 Servlet 容器启动程序时加载这个类.
	 * 
	 */
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
	    return builder.sources(SpringBootApp.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringBootApp.class, args);
	}

	
	
	/**
	 * 2.修改打包方式
	 * 接下来在 pom.xml 文件中,修改打包方式为 WAR,让 Maven 构建时以 WAR 方式生成
	 * 
	 * 另外要注意的是:为了确保嵌入式 servlet 容器不会影响部署war文件的servlet容器,此处为 Tomcat。
	 * 我们还需要将嵌入式 servlet 容器的依赖项标记为 provided 
	 * 
	 * <dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-tomcat</artifactId>
		<scope>provided</scope>
		</dependency>
	 *
	 *完成到这里, 不禁有个疑问: 为何继承了  SpringBootServletInitializer 类,
	 *并覆写其 configure 方法就能以 war 方式去部署了呢 ? 带着问题,我们从源码的角度上去寻找答案.
	 *在启动类 SpringbootTomcatApplication 覆写的方法进行断点,看下 Tomcat 运行项目时这个方法调用过程.
	 *通过 Debug 方式运行项目,当运行到这行代码时,可以看到两个重要的类  SpringBootServletInitializer 和 SpringServletContainerInitializer .
	 *configure 方法调用是在父类的 createRootApplicationContext,具体代码如下,非关键部分已省略,重要的已注释出来.
	 
	 protected WebApplicationContext createRootApplicationContext(
			ServletContext servletContext) {
		SpringApplicationBuilder builder = createSpringApplicationBuilder(); //  新建用于构建SpringApplication 实例的 builder
		builder.main(getClass());
		// ....
		builder.initializers(
				new ServletContextApplicationContextInitializer(servletContext));
		builder.contextClass(AnnotationConfigEmbeddedWebApplicationContext.class);
		builder = configure(builder); // 调用子类方法,配置当前 builder
		builder.listeners(new WebEnvironmentPropertySourceInitializer(servletContext));
		SpringApplication application = builder.build(); // 构建 SpringApplication 实例
		if (application.getSources().isEmpty() && AnnotationUtils
				.findAnnotation(getClass(), Configuration.class) != null) {
			application.getSources().add(getClass());
		}
    	//...
		return run(application);  // 运行 SpringApplication 实例
	}

	 *SpringApplicationBuilder 实例, 应该是遵循建造者设计模式,来完成 SpringApplication的构建组装.
	 *而 createRootApplicationContext方法的调用还是在这个类内完成的,这个就比较熟悉, 因为传统的 Spring Web 项目启动也会创建一个 WebApplicationContext 实例.
	 
	 @Override
	public void onStartup(ServletContext servletContext) throws ServletException {
		// Logger initialization is deferred in case a ordered
		// LogServletContextInitializer is being used
		this.logger = LogFactory.getLog(getClass());
		WebApplicationContext rootAppContext = createRootApplicationContext(
				servletContext); // 创建一个 WebApplicationContext 实例.
        // ...
	}

	 *问题又来了,这里的 onStartup 方法又是如何执行到的呢? SpringServletContainerInitializer 类就登场了.
	 *SpringServletContainerInitializer 类实现 Servlet 3.0 规范的 ServletContainerInitializer接口, 
	 *也就意味着当 Servlet 容器启动时,就以调用  ServletContainerInitializer 接口的 onStartup方法通知实现了这个接口的类.
	 
	public interface ServletContainerInitializer {
    	void onStartup(Set<Class<?>> c, ServletContext ctx) throws ServletException;
	}
	
	 *现在我们来看下 SpringServletContainerInitializer的 onStarup 方法的具体实现如下, 
	 *关键代码23~24行里 initializers  是一个 LinkedList 集合,有着所有实现 WebApplicationInitializer 接口的实例,
	 *这里进行循环遍历将调用各自的 onStartup方法传递 ServletContext 实例,以此来完成 Web 服务器的启动通知.
		
		@Override
public void onStartup(Set<Class<?>> webAppInitializerClasses, ServletContext servletContext)
      throws ServletException {
   List<WebApplicationInitializer> initializers = new LinkedList<WebApplicationInitializer>();
   if (webAppInitializerClasses != null) {
      for (Class<?> waiClass : webAppInitializerClasses) {
         if (!waiClass.isInterface() && !Modifier.isAbstract(waiClass.getModifiers()) &&
               WebApplicationInitializer.class.isAssignableFrom(waiClass)) {
            try {
                // 提取webAppInitializerClasses集合中 实现 WebApplicationInitializer 接口的实例
               initializers.add((WebApplicationInitializer) waiClass.newInstance());
            }
            catch (Throwable ex) {
               throw new ServletException("Failed to instantiate WebApplicationInitializer class", ex);
            }
         }
      }
   }
   // ...
    
   for (WebApplicationInitializer initializer : initializers) {
      initializer.onStartup(servletContext); // 调用所有实现 WebApplicationInitializer 实例的onStartup 方法
   }
}
	*追踪执行到SpringServletContainerInitializer类的22行, 我们可以看到集合里就包含了我们的启动类,因此最后调用了其父类的 onStartup 方法完成了 WebApplicationContext 实例的创建.
	*
	*
	*
	*看到这里,我们总结下这几个类调用流程,梳理下 Spring Boot 程序 WAR 方式启动过程:
SpringServletContainerInitializer#onStartup
​	 => SpringBootServletInitializer#onStartup
​	 => ``SpringBootServletInitializer#createRootApplicationContext​		=>SpringbootTomcatApplication#configure`
另外,我还收获了一点就是: 当执行 SpringBootServletInitializer 的 createRootApplicationContext 方法最后,调用了run(application).
这也说明了当 WAR方式部署 Spring Boot 项目时, 固定生成的 Main 方法不会再被执行到,是可以去掉.
	*
	*当项目以WAR方式部署时,这个方法就是无用代码
	public static void main(String[] args) {
	SpringApplication.run(SpringbootTomcatApplication.class, args);
	}
	 *
	 */
	
}

package com.xyh.spring.ioc;

public class MyApplicationContext {
	
	/**
	 1.Account@736e9adb  -->  ClassName + id.
	 2.Account$$BySpringCGLIB$$caa5f28f@4fccd51b
	 运行时,通过cglib生成的子类. 
	 
	 spring容器中的bean命名,并且负责bean的生命周期.
	 3.spring中在各个生命周期的开始和结束的时候,都可以通过钩子(hook)函数来进行扩展.
	 	eg:
	 	1.processPropertyValues()里面可以实现@AutoWire 注解，把一个Bean依赖的其他对象给注入进来。
	 	2.afterInitialization()，AOP ->在里边创建一个对象的代理出来。
	 	这个代理在调用真正对象的方法之前，可以执行一些事务，安全，日志等操作
	 */
	
}

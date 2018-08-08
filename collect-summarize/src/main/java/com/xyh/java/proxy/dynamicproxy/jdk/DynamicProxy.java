package com.xyh.java.proxy.dynamicproxy.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class DynamicProxy {
	public static <T> T newProxyInstance(Subject subject){

        //寻找AOP框架中定义的JoinPoint连接点
        boolean isBeforeAdviceDefined = true;
        if(isBeforeAdviceDefined){
            (new BeforeAdvice()).exec();
        }

        //定义一个Handler
        InvocationHandler handler = new MyInvocationHandler(subject);

        //定义主题的代理
        return (T)Proxy.newProxyInstance(subject.getClass().getClassLoader(),
                subject.getClass().getInterfaces(),handler);

    }
}

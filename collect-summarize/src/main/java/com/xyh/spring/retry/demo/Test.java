package com.xyh.spring.retry.demo;

import javax.annotation.PostConstruct;

import org.springframework.aop.framework.Advised;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.JdkRegexpMethodPointcut;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

public class Test {
	
	public static void main(String[] args) {
		
		  ProxyFactory factory = new ProxyFactory(HelloService.class.getClassLoader());
	      factory.setInterfaces(HelloService.class);
	      factory.setTarget(new HelloService() {
	        	@Retryable(maxAttempts=9,exclude = ArrayIndexOutOfBoundsException.class,value=Exception.class,backoff=@Backoff(delay = 10000))
	            public String say() {
	                System.err.println("hello");
	                if(1==1) throw new SecurityException();
	                return "a";
	            }
	        });
	        
	        HelloService service = (HelloService) factory.getProxy();
	        JdkRegexpMethodPointcut pointcut = new JdkRegexpMethodPointcut();
	        pointcut.setPatterns(".*say.*");
	        RetryOperationsInterceptor interceptor = new RetryOperationsInterceptor();
	        ((Advised) service).addAdvisor(new DefaultPointcutAdvisor(pointcut, interceptor));
	        
	        service.say();
	
	}
	
	
	   /**
     * @Retryable注解参数说明
     * maxAttempts 重试的次数
     * value 指定异常重试
     * exclude 排除某个异常不重试
     * 
     * @Backoff注解参数说明
     * backoff 重试的间隔时间
     */
    @Retryable(maxAttempts=9,exclude = ArrayIndexOutOfBoundsException.class,value=Exception.class,backoff=@Backoff(delay = 10000))
    public String getResult(String name){
    	int i = 0 ;
        System.out.println("尝试调用第"+i+++"次");
        name= name.split(",")[1111];//异常测试
        if(i!=8){
            name= name.split(",")[1111];//异常测试
        }
        return name+":你好！";
    }
	
	
    public String say() {
    	return "";
    }
    
}

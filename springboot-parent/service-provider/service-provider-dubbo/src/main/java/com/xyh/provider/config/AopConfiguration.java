/**
 * 
 */
package com.xyh.provider.config;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.xyh.service.model.resp.ServiceRes;


/**
 * @author hcxyh  2018年8月16日
 *
 */
@Order(1)
@Aspect
@Component
public class AopConfiguration {
	
	private static Logger logger = LoggerFactory.getLogger(AopConfiguration.class);
	
	/**
	 * 1.请求入口统一做系统参数校验,用户基础数据初始化.
	 * 2.请求出口统一做异常拦截.
	 */
	
    private final String POINT_CUT_NAME = "execution(* com.xx.xx..*(..))";
    private final String POINT_CUT_ANNOTATION = "@annotation(com.xyh.dubbo.annotation.AopAnnotation)";
    @Pointcut(POINT_CUT_NAME)
    private void pointcutByName(){}
    
    @Pointcut(POINT_CUT_ANNOTATION)
    private void pointcutByAnnotation() {};
	
    @Around("pointcutByAnnotation()")  
    public Object doAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{  
    	
    	System.out.println("aop aroud execute");
    	
    	Object result = null;
    	
//    	logger.info("环绕通知的目标方法名："+proceedingJoinPoint.getSignature().getName()); 
//        
//        if (!(proceedingJoinPoint.getSignature() instanceof MethodSignature)) {
//            return proceedingJoinPoint.proceed();
//        }
//        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        
        try {  
        	result = proceedingJoinPoint.proceed();  
            return result;  
        } catch (Throwable throwable) {  
            throwable.printStackTrace();  
            ServiceRes serviceRes = new ServiceRes<>();
            serviceRes.setResCode("发生异常");
            serviceRes.setResCode("000");
            result = serviceRes;
        }  
        return result;  
    }  
    
    
    
    @Before(value = POINT_CUT_NAME)
    public void before(JoinPoint joinPoint){
        logger.info("前置通知");
        //获取目标方法的参数信息  
        Object[] obj = joinPoint.getArgs();  
        //AOP代理类的信息  
        joinPoint.getThis();  
        //代理的目标对象  
        joinPoint.getTarget();  
        //用的最多 通知的签名  
        Signature signature = joinPoint.getSignature();  
        //代理的是哪一个方法  
        logger.info("代理的是哪一个方法"+signature.getName());  
        //AOP代理类的名字  
        logger.info("AOP代理类的名字"+signature.getDeclaringTypeName());  
        //AOP代理类的类（class）信息  
        signature.getDeclaringType();  
        //获取RequestAttributes  
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();  
        //从获取RequestAttributes中获取HttpServletRequest的信息  
        HttpServletRequest request = (HttpServletRequest) requestAttributes.resolveReference(RequestAttributes.REFERENCE_REQUEST);  
        //如果要获取Session信息的话，可以这样写：  
        //HttpSession session = (HttpSession) requestAttributes.resolveReference(RequestAttributes.REFERENCE_SESSION);  
        //获取请求参数
        Enumeration<String> enumeration = request.getParameterNames();  
        Map<String,String> parameterMap = new HashMap();  
        while (enumeration.hasMoreElements()){  
            String parameter = enumeration.nextElement();  
            parameterMap.put(parameter,request.getParameter(parameter));  
        }  
//        String str = JSON.toJSONString(parameterMap);  
//        if(obj.length > 0) {  
//            logger.info("请求的参数信息为："+str);
//        }  
    }
    
    
	
}

package com.xyh.spring.transaction;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.transaction.annotation.Transactional;

/**
* @ClassName: TransactionalAnnotation
* @Description: TODO 注解绑定
* @author xueyh
* @date 2017年12月20日 下午2:48:48
*/

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Transactional
public @interface TransactionalAnnotation {
	
	/**
	 * 避免多次频繁使用,可以与aop注解进行绑定
	 */
	String transaction() default "ice";
	boolean isUse() default true;
	
}

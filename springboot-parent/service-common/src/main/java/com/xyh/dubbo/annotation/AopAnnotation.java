/**
 * 
 */
package com.xyh.dubbo.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author hcxyh  2018年8月16日
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface AopAnnotation {
	
	/**
	 * 定义统一处理
	 */
	boolean init() default false;

}

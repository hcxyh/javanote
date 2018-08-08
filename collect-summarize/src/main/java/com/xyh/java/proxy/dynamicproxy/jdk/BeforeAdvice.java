package com.xyh.java.proxy.dynamicproxy.jdk;

/**
 * 具体通知类
 * @author hcxyh  2018年8月8日
 */
public class BeforeAdvice implements IAdvice{
	@Override
    public void exec() {
        System.out.println("Before Advice is Executed!");
    }
}

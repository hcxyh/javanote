package com.xyh.java.proxy.staticproxy;

/**
 * 真实主题类
 * @author hcxyh  2018年8月8日
 *
 */
public class RealSubject implements Subject{

	@Override
	public void request() {
		// TODO Auto-generated method stub
		System.out.println("do real method");
	}

}

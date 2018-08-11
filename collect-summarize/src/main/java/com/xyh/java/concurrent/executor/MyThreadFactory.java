package com.xyh.java.concurrent.executor;

import java.util.concurrent.ThreadFactory;

/**
 * 自定义创建线程
 * 每个创建出来的线程设置更有意义的名字，如果出现并发问题，也方便查找问题原因
 * @author hcxyh  2018年8月11日
 *
 */
public class MyThreadFactory implements ThreadFactory{

	@Override
	public Thread newThread(Runnable r) {
		//设置线程名字
		return new Thread(r);
	}

}

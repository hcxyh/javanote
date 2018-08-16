/**
 * 
 */
package com.xyh.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * COPY FROM dubbo.io
 * 
 * @author hcxyh 2018年8月16日
 *
 */
public class SpringStarter {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringStarter.class);
	private static boolean RUNNING = true;
	private static ClassPathXmlApplicationContext CONTEXT = null;

	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				synchronized (SpringStarter.class) {
					RUNNING = false;
					if (CONTEXT != null) {
						CONTEXT.close();
					}
					SpringStarter.class.notifyAll();
				}
			}
		});
	}

	private static String getApplicationName() {
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

		String applicationName = SpringStarter.class.getSimpleName();

		boolean preFound = false;
		for (StackTraceElement stackTrace : stackTraces) {
			if (preFound) {
				applicationName = stackTrace.getClassName();
				break;
			}
			if (SpringStarter.class.getName().equals(stackTrace.getClassName())
					&& "run".equals(stackTrace.getMethodName())) {
				preFound = true;
			}
		}
		return applicationName;
	}

	public static void run(String[] args) {
		long begin = System.currentTimeMillis();

		CONTEXT = new ClassPathXmlApplicationContext(args);
		CONTEXT.start();

		long end = System.currentTimeMillis();

		String applicationName = getApplicationName();
		LOGGER.info("Application {} started, total use {} ms", applicationName, (end - begin));

		synchronized (SpringStarter.class) {
			while (RUNNING) {
				try {
					SpringStarter.class.wait();
				} catch (InterruptedException e) {
				}
			}
		}
	}

}

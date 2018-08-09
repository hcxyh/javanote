package com.xyh.spring.jar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
* @ClassName: SpringGracefulShutdownStart
* @author xueyh
* @date 2018年8月9日 下午9:26:52
* copy from dubbo.io
*/
public class SpringGracefulShutdownStart {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SpringGracefulShutdownStart.class);
	  
	  private static boolean RUNNING = true;
	  private static ClassPathXmlApplicationContext CONTEXT = null;

	  static {
	    Runtime.getRuntime().addShutdownHook(new Thread() {
	      @Override
	      public void run() {
	        synchronized (SpringGracefulShutdownStart.class) {
	          RUNNING = false;
	          if (CONTEXT != null) {
	            CONTEXT.close();
	          }
	          SpringGracefulShutdownStart.class.notifyAll();
	        }
	      }
	    });
	  }

	  private static String getApplicationName() {
	    StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();

	    String applicationName =  SpringGracefulShutdownStart.class.getSimpleName();

	    boolean preFound = false;
	    for (StackTraceElement stackTrace : stackTraces) {
	      if (preFound) {
	        applicationName = stackTrace.getClassName();
	        break;
	      }
	      if (SpringGracefulShutdownStart.class.getName().equals(stackTrace.getClassName())
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

	    synchronized (SpringGracefulShutdownStart.class) {
	      while (RUNNING) {
	        try {
	        	SpringGracefulShutdownStart.class.wait();
	        } catch (InterruptedException e) {
	        }
	      }
	    }
	  }
	
}

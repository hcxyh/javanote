/**
 * 
 */
package com.xyh.dubbo.log;

import org.slf4j.MDC;

/**
 * @author hcxyh 2018年8月16日
 *
 */
public class LogContext {

	private LogContext() {
	}

	public static String get(String key) {
		return MDC.get(key);
	}

	public static void put(String key, String value) {
		MDC.put(key, value);
	}

	public static void remove(String key) {
		MDC.remove(key);
	}

	public static void clear() {
		MDC.clear();
	}

}

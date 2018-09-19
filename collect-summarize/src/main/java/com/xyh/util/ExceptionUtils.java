package com.xyh.util;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ExceptionUtils {
	
	
	/**
	 * 获取异常打印信息
	 */
	public String getExceptionInfo(Exception e) {
		
		String errorMessage = "";
		try {
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw,true));
			errorMessage = sw.toString();
		} catch (Exception e2) {
			e2.printStackTrace();
		}
		return errorMessage;
	}
}

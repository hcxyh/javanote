package com.xyh.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;

/**
 * springboot中的配置问价外置.
 * SpringApplication  将从以下位置加载 application.properties  文件，并把
	它们添加到Spring  Environment  中：
	1. 当前目录下的 /config  子目录。
	2. 当前目录。
	3. classpath下的 /config  包。
	4. classpath根路径（root）。
	该列表是按优先级排序的（列表中位置高的路径下定义的属性将覆盖位置低的）。
 * @author xyh
 *
 */
public class ConfigUtil {
	
	private static Properties p = new Properties();
	
	static {
		loadProperties();
	}
	
	/**
	 * myconfig.properties
	 * 		configAddress=/home/zyb/apps/config/
	 * 该文件夹下根据种类区分文件夹
	 * 	
	 */
	
	public static void loadProperties() {
		try {
			ClassLoader c1 = Thread.currentThread().getContextClassLoader();
			p.load(new InputStreamReader(c1.getResourceAsStream("resourece/myconfig.properties"), "UTF-8"));
			p.load(new InputStreamReader(new FileInputStream(p.getProperty("configAddress")+"xx1.properties"),"UTF-8"));
			p.load(new InputStreamReader(new FileInputStream(p.getProperty("configAddress")+"xx2.properties"),"UTF-8"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String getParam1() {
		return p.getProperty("param1");
	}
	
}

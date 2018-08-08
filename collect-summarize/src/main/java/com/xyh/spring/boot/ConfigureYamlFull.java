package com.xyh.spring.boot;

/**
 * springboot properties 配置大全
 * @author hcxyh  2018年8月8日
 */
public class ConfigureYamlFull {
	
	
	/**
	 * 1.配置文件中可以进行属性引用
	 * 2.@Value("${blog.author}")的形式获取属性值，blog.author=xyh.
	 * 3.随机数 ${random}
	 * 4.自定义配置文件 @PropertySource(value="classpath:my.properties",encoding="utf-8") 在application方法之上.
	 * 5.@ConfigurationProperties(prefix="config")  @commponent
	 * 
	 */
	
}

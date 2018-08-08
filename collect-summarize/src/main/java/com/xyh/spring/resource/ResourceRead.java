package com.xyh.spring.resource;

import java.net.URL;

/**
 * spring中文件读取
 * @author hcxyh  2018年8月8日
 *
 */
public class ResourceRead {
	/**
	  	最近在看spring的资源获取时发现JDK里存在几种不同方式的资源获取，因比较混乱特地总结起来帮助和我一样混乱的人理解。
	  	下面是我项目的类结构图，
	  	在 src/main/java 下有两个类 ResourceTest.java和Resource.java ， 
	  	resources 目录下有两个资源文件 request.xml和conf/sysConf.json.
	  	测试代码见:
	  	由于maven打包会把 src/main/java 和 src/main/resources 下的文件放到 target/classes 下，
	  	所以下面统一以根路径代表此目录，总结起来有以下几个规律：
		Class.getResource()的资源获取如果以 / 开头，则从根路径开始搜索资源。
		Class.getResource()的资源获取如果不以 / 开头，则从当前类所在的路径开始搜索资源。
		ClassLoader.getResource()的资源获取不能以 / 开头，统一从根路径开始搜索资源。
	 */
	
	/**
	 源码解析:
	 1.Class.getResource()
	 	   public java.net.URL getResource(String name) {
		        name = resolveName(name);
		        ClassLoader cl = getClassLoader0();
		        if (cl==null) {
		            // A system class.
		            return ClassLoader.getSystemResource(name);
		        }
		        return cl.getResource(name);
		    }
	 	1.解析文件路径，变成ClassLoader所支持的路径。
		2.获取该类的类加载器，默认为AppClassLoader，接着调用它的getResource方法。
		3.如果类加载器获取失败，直接走ClassLoader的getSystemResource方法来获取
	 	【
	 		private String resolveName(String name) {
		        if (name == null) {
		            return name;
		        }
		        if (!name.startsWith("/")) {
		            Class<?> c = this;
		            while (c.isArray()) {
		                //获取数组类型
		                c = c.getComponentType();
		            }
		            String baseName = c.getName();
		            int index = baseName.lastIndexOf('.');
		            if (index != -1) {
						// 截取当前类所在的包和name使用 / 进行拼接
		                name = baseName.substring(0, index).replace('.', '/')
		                    +"/"+name;
		            }
		        } else {
		            name = name.substring(1);
		        }
		        return name;
		    }
	 	】
	 */
	
	/**
	 * ClassLoader.getResource()
	       public URL getResource(String name) {
		        URL url;
		        if (parent != null) {
					// 递归调用
		            url = parent.getResource(name);
		        } else {
					// 使用BootstrapClassLoader发现资源
		            url = getBootstrapResource(name);
		        }
		        if (url == null) {
		        	// 真正去找对应的url
		            url = findResource(name);
		        }
		        return url;
		    }
	   getSystemResource和getResource的区别就在于你是否实现了自己的类加载器，如果都是使用的默认的AppClassLoader，这两个方法的作用一样。
	 
	 线程上下文加载方式
		这种加载方式对应于例子中的最后一种方式，它是使用 java.lang.Thread 中的方法 getContextClassLoader()和 setContextClassLoader(ClassLoadercl)
		用来获取和设置线程的上下文类加载器。如果没有通过 setContextClassLoader(ClassLoadercl)方法进行设置的话，
		线程将继承其父线程的上下文类加载器。Java 应用运行的初始线程的上下文类加载器是 AppClassLoader ，
		具体设置参考 sun.misc.Launcher 的构造函数。
	 */
	
}

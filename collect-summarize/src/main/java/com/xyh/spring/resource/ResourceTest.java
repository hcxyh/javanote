package com.xyh.spring.resource;

public class ResourceTest {

	public static void main(String[] args) {

		// 1、通过Class的getResource方法
		
		String a1 = ResourceTest.class.getResource("/com/xyh/spring/resource/Resource.class").getPath();

		String a2 = ResourceTest.class.getResource("Resource.class").getPath();

		String a3 = ResourceTest.class.getResource("/request.xml").getPath();

		String a4 = ResourceTest.class.getResource("../../request.xml").getPath();

		String a5 = ResourceTest.class.getResource("/config/sysconf.json").getPath();

		String a6 = ResourceTest.class.getResource("../../config/sysconf.json").getPath();

		// 2、通过本类的ClassLoader的getResource方法

		String b1 = ResourceTest.class.getClassLoader().getResource("com/xyh/spring/resource/Resource.class").getPath();

		String b2 = ResourceTest.class.getClassLoader().getResource("request.xml").getPath();

		String b3 = ResourceTest.class.getClassLoader().getResource("config/sysconf.json").getPath();

		// 3、通过ClassLoader的getSystemResource方法

		String c1 = ClassLoader.getSystemClassLoader().getResource("com/xyh/spring/resource/Resource.class").getPath();

		String c2 = ClassLoader.getSystemClassLoader().getResource("request.xml").getPath();

		String c3 = ClassLoader.getSystemClassLoader().getResource("config/sysconf.json").getPath();

		// 4、通过ClassLoader的getSystemResource方法

		String d1 = ClassLoader.getSystemResource("com/xyh/spring/resource/Resource.class").getPath();

		String d2 = ClassLoader.getSystemResource("request.xml").getPath();

		String d3 = ClassLoader.getSystemResource("config/sysconf.json").getPath();

		// 5、通过Thread方式

		String e1 = Thread.currentThread().getContextClassLoader().getResource("com/xyh/spring/resource/Resource.class").getPath();

		String e2 = Thread.currentThread().getContextClassLoader().getResource("request.xml").getPath();

		String e3 = Thread.currentThread().getContextClassLoader().getResource("config/sysconf.json").getPath();

	}
}

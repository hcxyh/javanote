package com.xyh.java.jdk8.test;

import java.util.Arrays;

/**
 * 方法引用
 * @author xyh
 *
 */
public class FunctionReference {

	
	public static void main(String[] args) {
		
		/**
		 * 方法引用是lambda表达式的一种简写形式。 
		 * 如果lambda表达式只是调用一个特定的已经存在的方法，则可以使用方法引用。
		 *  对象::实例方法
			类::静态方法
			类::实例方法
			类::new
		 */
		String[] strings = {};
		Arrays.sort(strings, String::compareToIgnoreCase);
		// 等价于
		Arrays.sort(strings, (s1, s2) -> s1.compareToIgnoreCase(s2));
		
		
		//第三种，特定类的方法调用()
		Integer[] a = new Integer[]{3, 1, 2, 4, 6, 5};
		Arrays.sort(a, Integer::compare);
		System.out.println("特定类的方法引用：" + Arrays.toString(a));
		
	}
	
}

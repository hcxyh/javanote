package com.xyh.java.jdk8.test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class LambdaDemo {
	
	public static void main(String[] args) {
		
		/**
		 * 在Java中传递代码并不是很容易，不可能将代码块到处传递。你不得不构建一个类的对象，由他的某个方法来包含所需的代码。
			而lambda表达式实际上就是代码块的传递的实现。其语法结构如下：
			(parameters) -> expression 或者 (parameters) -> {statements;}
		 */
		
		
		
//		final String a = "123";
//		///第二种，使用Lambda表达式来代替匿名接口方法
		
		new MyClass( (a) ->  { System.out.println(a); } ).start();
		
		//第三种，使用Lambda表达式调用类的静态方法
		new MyClass( MyObject::staticMethod ).start();
		Person[] personArr = new Person[] {};
		Arrays.asList(personArr);
		
		Arrays.sort(personArr , ( a,  b) -> {  
			if(a.getAge() > b.getAge()) {
				return 1;
			}else {
				return 2;
			}
		});
		
		
		Collections.sort(Arrays.asList(personArr), ( a,  b) -> {  
			if(a.getAge() > b.getAge()) {
				return 1;
			}else {
				return 2;
			}
		});
		
		Collections.sort(Arrays.asList(personArr), Person::sortByName);
		Collections.sort(Arrays.asList(personArr), (p,p1) -> new Person().sortByAge(p, p1));
		Collections.sort(Arrays.asList(personArr), (p,p1) -> Person.sortByName(p, p1));
		
//		Person::new;
		Integer[] intArr = new Integer[] {};
		String[] strArr = new String[] {};
		Collections.sort(Arrays.asList(intArr), Integer::compareTo);
		Collections.sort(Arrays.asList(strArr), String::compareTo);
		
		Integer[] a = new Integer[]{3, 1, 2, 4, 6, 5};
		Comparator<Integer> comparator = Integer::compare;

		Arrays.sort(a, comparator);
		System.out.println("升序：" + Arrays.toString(a));

		Arrays.sort(a,comparator.reversed());
		System.out.println("降序："+Arrays.toString(a));
		
		
		
		
	}

}

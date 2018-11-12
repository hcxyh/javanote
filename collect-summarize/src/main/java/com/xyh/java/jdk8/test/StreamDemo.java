package com.xyh.java.jdk8.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StreamDemo {

	public static void main(String[] args) {
		
		String[] strArrays = new String[] {"1","2","3"};
		Stream.of(strArrays).filter(w -> w.length() > 3).count();
		
		//并行执行
		//只需要把stream方法改成parallelStream，就可以让Stream去并行执行过滤和统计操作
		long count = Arrays.asList(strArrays).parallelStream().filter(w -> w.length() > 3).count();
		
		
		/**
		 * Stream很像Iterator，单向，只能遍历一遍。
		 * Stream创建：
		 */
		// 1. Individual values
			Stream stream = Stream.of("a", "b", "c");
			// 2. Arrays
			String [] strArray = new String[] {"a", "b", "c"};
			stream = Stream.of(strArray);
			stream = Arrays.stream(strArray);
			// 3. Collections
			List<String> list = Arrays.asList(strArray);
			stream = list.stream();

			List collentions = list.stream().collect(Collectors.toList());
		
		/**
		 中间操作包括：map (mapToInt, flatMap 等)、 filter、distinct、sorted、peek、limit、skip、parallel、sequential、unordered。
		终止操作包括：forEach、forEachOrdered、toArray、reduce、collect、min、max、count、anyMatch、allMatch、noneMatch、findFirst、findAny、iterator。	
		 */
			
	}
	
	
}

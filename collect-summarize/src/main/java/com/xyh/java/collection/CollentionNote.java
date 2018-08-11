package com.xyh.java.collection;

public class CollentionNote {
	
	/**
	 *  HashMap中的key为Object的时候,(遵照key应当唯一的规定,需要对obj的equals和hashcode进行Override)
	 * 
	 *	Set(无序,不能重复)：检索元素效率低下，删除和插入效率高，插入和删除不会引起元素位置改变。 
	 *	List(有序,可重复)：和数组类似，List可以动态增长，查找元素效率高，插入删除元素效率低，因为会引起其他元素位置改变。 
	 */
	
	/**	TODO 此处记录不包括concurrent中的集合类
	 	1.collection
	 		1.List
	 			1.ArrayList 
	 			2.Vector : 不推荐使用Vector类，即使需要考虑同步，即也可以通过其它方法实现。同样我们也可以通过ArrayDeque类或LinkedList类实现“栈”的相关功能。
	 				1.Stack (LIFO)
	 			3.LinkedList : 也实现了Dqueue,可以当队列和栈使用.
	 				Arrays.asList() --> 
	 		2.Set :	Set集合不允许包含相同的元素，而判断两个对象是否相同则是根据equals方法。
	 			1.HashSet
	 			2.LinkedHashSet
	 			3.SortedSet  : set排序接口
	 				1.TreeSet
	 			4.EnumSet
	 		3.Queue
	 			1.PriorityQueue
	 			2.Deque : 双端队列，可以当作一个双端队列使用，也可以当作“栈”来使用，因为它包含出栈pop()与入栈push()方法.
	 				1.ArrayDeque
	 	2.Map : key不允许重复
	 		1.HashMap
	 			1.LinkedHashMap
	 		2.HashTable
	 			1.Properties
	 		3.SortedMap : 通过Comparator对key进行排序
	 			1.TreeMap
	 		4.IdentityHashMap
	 		5.EnumMap
	 	
	 	3.Collections : utils 工具类支持
	 		1.排序(主要针对是List接口下) obj自己实现compareable或者显示注入comparator
	 		2.查找和替换（主要针对Collection接口相关）
	 		3.同步控制
	 		4.设置不可变集合
	 */
	
	/**
	  1.排序
	  	1.Comparable : 内比较器, obj.compareTo(otherObj)
		2.Comparator : 外比较器, obj.compare(obj1,obj2)
		1.List : Collections的sort或者Arrays的sort方法。
		2.Set  : HashSet是基于HashMap的，TreeSet是基于TreeMap的
			1.HashSet : 转为List,或者Arrays进行排序
			2.HashMap --> treeMap 基于红黑树,天生具有排序功能.
		3.Map
			1.key排序
				HashMap来构造一个TreeMap
			2.value排序
				Map的Entry提出成set结构，然后将set转成list，最后按照list进行排序
			{
				Set<Entry<String, Integer>> ks = us.entrySet();
		        List<Entry<String, Integer>> list = new ArrayList<Map.Entry<String, Integer>>(
		                ks);
		        Collections.sort(list, new Comparator<Entry<String, Integer>>() {
		 
		            @Override
		            public int compare(Entry<String, Integer> o1,
		                    Entry<String, Integer> o2) {
		                if (o1.getValue() < o2.getValue())
		                    return -1;
		                else if (o1.getValue() > o2.getValue())
		                    return 1;
		                return 0;
		            }
		        });
		        System.out.println(list);
			}
	  	2.遍历
	  		1.List
	  			1.foreach() 增强for循环
	  			2.Iterator : 
	  				Iterator<String> it = list.iterator(); 
	  				while(it.hasNext();){
	  					it.next();
	  				}
	  			3.针对LinkedList --> 既有stack,又有queue的特点.
	  				1.queue
	  					peek() , poll ()
	  				2.stack
	  					isEmpty() , removeFirst()
	  			4.Enumeration<String> e = Collections.enumeration(listObj);
	  				while(e.hasMoreElements()){
	  					e.nextElement()
	  				}
	  		2.Map
	  			1.Set<Integer> set = map.keySet()
	  				foreach(int i :set){
	  					map.get(i);
	  				}
	  			2.Iterator<Map.Entry<Integer, String>> it = map.entrySet().iterator();
	  				while(it.hashNext()){
	  					 Map.Entry<Integer, String> entry = it.next();
	  					 entry.getKey();
	  					 entry.getValue();
	  				}
	  			3.for (Map.Entry<Integer, String> entry : map.entrySet()) {
	  					entry.getKey();
	  					entry.getValue();
	  				}
	  			4.遍历MapValues
	 */
	
}

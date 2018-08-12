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
	  			
Comparable与Comparator的区别
Comparable和Comparator都是用来实现集合中元素的比较、排序的。
Comparable是在集合内部定义的方法实现的排序，位于java.util下。
Comparator是在集合外部实现的排序，位于java.lang下。
Comparable是一个对象本身就已经支持自比较所需要实现的接口，如String、Integer自己就实现了Comparable接口，可完成比较大小操作。自定义类要在加入list容器中后能够排序，也可以实现Comparable接口，在用Collections类的sort方法排序时若不指定Comparator，那就以自然顺序排序。所谓自然顺序就是实现Comparable接口设定的排序方式。
Comparator是一个专用的比较器，当这个对象不支持自比较或者自比较函数不能满足要求时，可写一个比较器来完成两个对象之间大小的比较。Comparator体现了一种策略模式(strategy design pattern)，就是不改变对象自身，而用一个策略对象(strategy object)来改变它的行为。
总而言之Comparable是自已完成比较，Comparator是外部程序实现比较。

分析二：
再接下来我用小结描述下二者的不同：
1、Comparator在集合（即你要实现比较的类）外进行定义的实现，而Comparable接口则是在你要比较的类内进行方法的实现。这样看来Comparator更像是一个专用的比较器。
2、Comparator实现了算法和数据的分离，从代码也可以看出，其实这和第一点是相辅相成的，因为Comparable依赖于某一个需要比较的类来实现。
3、Comparable支持自比较，自比较是指比如String等类里面本身就有CompareTo()方法，直接就可以进行String类对象的比较，这也可以从较之Comparator,Comparable中Arrays.sort()方法中只带数组参数的形式与书上例子更相似这点看出。 
4、从第3点延伸，我们可以看到当不满足于自比较函数，如String类时，我们试图改写规则要怎么办——通过Comparator因为它支持外比较，它是分离的。
5、当一个又一个类设计完成后，或许我们最初没有设想到类的比较问题，而没使用Comparable接口，那我们之后可以通过Comparator来完成，而同时无需改变之前完成的类的构建。
6、运用Arrays.sort()方法时，注意二者的参数不同，Comparator多了一个参数，这第二个参数是使用Comparator接口的那个被视为专用比较器的类的对象，如汪同学例子中的new ByWeightComparator()。
其实大部分情况下我们并不需要刻意去对二者做选择，哪个用得顺手就用哪个，但当你的习惯遭遇某种问题时，这样的区别分析可以让你不妨换个方向思考，不至于走入死胡同。
	 */
	
}

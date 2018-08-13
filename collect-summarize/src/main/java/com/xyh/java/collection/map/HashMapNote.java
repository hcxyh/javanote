package com.xyh.java.collection.map;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * https://mp.weixin.qq.com/s/wNmAi1FICNu7rkmCe1GDyw
 * @author hcxyh  2018年8月10日
 *
 */
public class HashMapNote {
	
	/**
	 *  底层机构  entry[] + 链表
	 *  1.8的优化
	 *  	1.7时,当 Hash冲突严重时，在桶上形成的链表会变的越来越长，这样在查询时的效率就会越来越低；
	 *  	时间复杂度为 O(N)
	 *  	{
	 *  		TREEIFY_THRESHOLD 用于判断是否需要将链表转换为红黑树的阈值。
	 *  		接着判断当前链表的大小是否大于预设的阈值，大于时就要转换为红黑树。
	 *  		HashEntry 修改为 Node。
	 *  	}
	 *  
	 *  key不能重复. 先判断hashcode,再判断equals.
	 */
	
	
	public static void testKeyUnique(String[] args) {
		HashMap<TestObject,String> hashMap = new HashMap<>();
		TestObject t1 = new TestObject();
		TestObject t2 = new TestObject();
//		TestObject t3 = new TestObject();
//		TestObject t4 = new TestObject();
		
		System.out.println(t1.equals(t2));
		
		hashMap.put(t1, "1");
		hashMap.put(t1, "2");
		hashMap.put(t1, "3");
		hashMap.put(t1, "4");
		
		Set<Entry<TestObject,String>> setKeys =  hashMap.entrySet(); 
		
		for (Entry<TestObject, String> entry : setKeys) {
			System.out.println(entry.getValue());
		}
	}

}


class TestObject{
	private String name;
}
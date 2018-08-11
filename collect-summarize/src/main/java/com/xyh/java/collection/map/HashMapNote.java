package com.xyh.java.collection.map;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

/**
 * 
 * @author hcxyh  2018年8月10日
 *
 */
public class HashMapNote {
	
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
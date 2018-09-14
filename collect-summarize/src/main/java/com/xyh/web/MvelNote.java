package com.xyh.web;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.mvel2.MVEL;

public class MvelNote {

	public static void main(String[] args) throws Exception {
		
		String express = "a > 7 || a == 3 && b == 2 && c > 1";
		
		String express1 = "a in [1,2,3]";
		
		/**
		 *
		 *	&&，||
		 *  
		 *  +，字符串连接运算，如："foo" +"bar"
			#，字符连接运算，如：1 # 2返回"12"
			in，投影整个项目集合，如：(foo in list)
			=，赋值运算符，如：var = "foobar"
		 *
		 *
		 * 
		 */
		
		
		Serializable exp4 = MVEL.compileExpression(express1);
		Map<String, Object> map = new HashMap<>();
		map.put("a", "3");
		map.put("b", "2");
		map.put("c", "3");
		System.err.println(MVEL.executeExpression(exp4, map, String.class));
		
		
	}
	
	
	public static void test() {
		System.err.println(1350 * 0.7 * (0.97 + 0.5 * 0.06));

		String exp3 = "a*b*(c+d*e)";
		Map<String, Object> map = new HashMap<>();
		map.put("a", 1350d);
		map.put("b", 0.7);
		map.put("c", 0.97);
		map.put("d", 0.5);
		map.put("e", 0.06);
		Serializable exp4 = MVEL.compileExpression(exp3);
		System.err.println(MVEL.executeExpression(exp4, map, Double.class));
	}
}

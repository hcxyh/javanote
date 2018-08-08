package com.xyh.java.base.jvm;

import javax.tools.JavaCompiler;
import javax.tools.ToolProvider;

/**
 * @ClassName: CompileNote
 * @author xueyh
 * @date 2018年8月8日 下午9:34:06
 * 
 */
public class CompileNote {
	/**
	 * javac 
	 */
	
	public static void main(String[] args) {
		
		String sourceCode = "public class CompileTest {	public static void main(String[] args)"
				+ " {System.out.println('hello world！');}}";
		
		JavaCompiler compiler = (JavaCompiler) ToolProvider.getSystemJavaCompiler();
	}
}

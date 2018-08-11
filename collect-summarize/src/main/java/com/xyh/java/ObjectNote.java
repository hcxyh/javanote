package com.xyh.java;

/**
* @ClassName: ObjectNote
* @author xueyh
* @date 2018年8月9日 下午9:43:58
* 
*/
public class ObjectNote<T> extends Object{
	
	/**	
	 * 
	 * (1) 用new语句创建对象，这是最常见的创建对象的方法。 
	 * (2) 运用反射手段,调用java.lang.Class或者java.lang.reflect.Constructor类的newInstance()实例方法。 
	 * (3) 调用对象的clone()方法。
	 * (4) 运用反序列化手段，调用java.io.ObjectInputStream对象的 readObject()方法。
	 * (1)和(2)都会明确的显式的调用构造函数 ；
	 * (3)是在内存上对已有对象的影印，所以不会调用构造函数 ；
	 * (4)是从文件中还原类的对象，也不会调用构造函数。
	 */
	
	/**
	 * hashCode()   
	 * 		1.同一个对象的hashCode一定相同.hashCode相同对象可以不同.
	 * 		2."=="是比较两个引用是否指向堆内存里的同一个地址（同一个对象）
	 * Java规范:
	 * 	1、如果两个对象equals，Java运行时环境会认为他们的hashcode一定相等。
	 *  2、如果两个对象不equals，他们的hashcode有可能相等。
	 *  3、如果两个对象hashcode相等，他们不一定equals。 
	 *  4、如果两个对象hashcode不相等，他们一定不equals。
	 */
	
	public Boolean equalsNote(T t) {
		//判断两个对象Heap上地址是否一致
		return this == t;
	}
	
	public int hashCodeNote() {
		//hashcode()方法返回一个int数，在Object类中的默认实现是“将该对象的内部地址转换成一个整数返回”。 
		return Integer.parseInt(this.toString());
	}
	
}

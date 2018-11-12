package com.xyh.java.jdk8.test;

//函数式接口（Functional Interface）是只包含一个方法的抽象接口。
//@FunctionalInterface注解不是必须的，只要接口只包含一个抽象方法，虚拟机会自动判断该接口为函数式接口
@FunctionalInterface
public interface MyInterface {
	
	//除去默认方法以及继承的抽象方法，只有显式声明一个抽象方法的接口。
	public void inter(String str);
	
	boolean equals(Object obj);
	
	//静态方法与默认方法均可以有多个，默认方法可以被覆盖。
	//接口静态方法
	public static void test() {
	};
	/**
	 * 默认方法
		在Java8之前的时代，为已存在接口增加一个通用的实现是十分困难的，接口一旦发布之后就等于定型，
		如果这时在接口内增加一个方法，那么就会破坏所有实现接口的对象。
		默认方法（之前被称为 虚拟扩展方法 或 守护方法）的目标即是解决这个问题，使得接口在发布之后仍能被逐步演化。
	 */
	
	default void print(){
	      System.out.println("我是一辆车!");
	   }
}

package com.xyh.java.jdk8.test;

public class Person {

	private int age;
	private String name;
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "Person [age=" + age + ", name=" + name + "]";
	}
	
	
	public static int sortByName(Person p, Person p2) {
		
		if(p.getAge() >  p2.getAge()) {
			return 1;
		}else {
			return 2;
		}
	}
	public static int sortByAge(Person p, Person p2) {
		
		if(p.getAge() >  p2.getAge()) {
			return 1;
		}else {
			return 2;
		}
	}
}

package com.xyh.provider.service.model;

import java.io.Serializable;

/**
 * 
 * @author hcxyh  2018年8月15日
 *
 */
public class ServiceProviderModel implements Serializable{
	
	private static final long serialVersionUID = -3195855308753564119L;
	
	private String id;
	private String name;
	private String age;
	private String address;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	
}

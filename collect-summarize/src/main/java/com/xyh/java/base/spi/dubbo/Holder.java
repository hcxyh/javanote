package com.xyh.java.base.spi.dubbo;

/**
 * copy from dubbo
 * @author xyh
 *
 */
public class Holder<T> {
	
	private volatile T value;
	
	public T get() {
		return this.value;
	}
	
	public void set(T value) {
		this.value = value;
	}

}

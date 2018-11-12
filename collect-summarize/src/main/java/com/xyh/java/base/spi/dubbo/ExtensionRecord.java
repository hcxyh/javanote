package com.xyh.java.base.spi.dubbo;


/**
 * copy from dubbo
 * @author xyh
 *
 */
public class ExtensionRecord {
	
	private String extensionName; 
	
	private Class<?> extensionClass;
	
	private volatile Object instance;
	
	public ExtensionRecord(String extensionName,Class<?> extensionClass) {
		this.extensionName = extensionName;
		this.extensionClass = extensionClass;
		this.instance = null;
	}

	public String getExtensionName() {
		return extensionName;
	}

	public void setExtensionName(String extensionName) {
		this.extensionName = extensionName;
	}

	public Class<?> getExtensionClass() {
		return extensionClass;
	}

	public void setExtensionClass(Class<?> extensionClass) {
		this.extensionClass = extensionClass;
	}

	public Object getInstance() {
		return instance;
	}

	public void setInstance(Object instance) {
		this.instance = instance;
	}

}

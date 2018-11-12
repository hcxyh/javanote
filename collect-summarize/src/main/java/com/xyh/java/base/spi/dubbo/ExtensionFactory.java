package com.xyh.java.base.spi.dubbo;

import java.util.List;

@Spi
public interface ExtensionFactory {
	//根据扩展点class获取实现
	<T> T getExtension(Class<T> type, String name);

	<T> List<T> getExtension(Class<T> type);
	
}

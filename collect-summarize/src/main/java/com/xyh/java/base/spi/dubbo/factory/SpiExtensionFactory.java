package com.xyh.java.base.spi.dubbo.factory;

import java.util.List;

import com.xyh.java.base.spi.dubbo.ExtensionFactory;
import com.xyh.java.base.spi.dubbo.Spi;

/**
 * copy from dubbo
 * @author xyh
 *
 */
public class SpiExtensionFactory implements ExtensionFactory{

	@Override
	public <T> T getExtension(Class<T> type, String name) {
		
		if(type.isInterface() && type.isAnnotationPresent(Spi.class)) {
			
		}
		return null;
	}

	@Override
	public <T> List<T> getExtension(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

}

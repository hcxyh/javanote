package com.xyh.java.base.spi.dubbo.factory;

import java.util.List;

import com.xyh.java.base.spi.dubbo.Adaptive;
import com.xyh.java.base.spi.dubbo.ExtensionFactory;

/**
 * 
 * @author xyh
 */
@Adaptive
public class AdaptiveExtensionFactory implements ExtensionFactory{

	@Override
	public <T> T getExtension(Class<T> type, String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> List<T> getExtension(Class<T> type) {
		// TODO Auto-generated method stub
		return null;
	}

}

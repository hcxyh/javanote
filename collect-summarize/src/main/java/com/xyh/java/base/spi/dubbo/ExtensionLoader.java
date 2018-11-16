package com.xyh.java.base.spi.dubbo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.xyh.spring.util.Assert;

/**
 * copy from dubbo 
 * @author xyh
 *
 */
public class ExtensionLoader<T> {
	
	/**
	private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);
	
	private static final String SERVICE_DIR = "META-INF/services";
	
	private static final Pattern NAME_SEPARATOR = Pattern.compile("\\s*[,]+\\s*]");
	
	private static final ConcurrentHashMap<Class<?>, ExtensionLoader<?>> EXTENSION_LOADERS = new ConcurrentHashMap<>();
	
	private Class<T> type;
	
	private final ExtensionFactory objectFactory;
	
	private volatile Throwable createAdaptiveInstanceError;
	
	private volatile String defaultExtensionName;
	
	private volatile Class<?> cachedAdaptiveClass;
	
	private final Holder<Object> cachedAdaptiveInstance = new Holder<>();
	
	private final Holder<Map<String, ExtensionRecord>> cachedExtensions = new Holder<>();

	private final ConcurrentHashMap<String, IllegalStateException> cachedExceptions = new ConcurrentHashMap<>();
	
	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type){
		Assert.notNull(type,"Extension type mustn't be null");
		Assert.isTrue(type.isInterface(),"Extension type" + type.getName() + "is not a interface");
		Assert.isTrue(type.isAnnotationPresent(Spi.class) , "Extension type " + type.getName() + "is not a extension " + Spi.class.getName());
		
		if( EXTENSION_LOADERS.get(type) == null) {
			EXTENSION_LOADERS.computeIfAbsent(type, new ExtensionLoader<T>(type));
		}
		
		return (ExtensionLoader<T>) EXTENSION_LOADERS.get(type);
	}
	
	private ExtensionLoader(Class<T> type) {
		this.type = type;
		objectFactory = (type == ExtensionFactory.class) ? null : ExtensionLoader.getExtensionLoader(ExtensionFactory.class).
	}
	
	
	public T getDefaultExtension() throws Exception {
		getExtensionRecords();
		return null;
	}
	
	private Map<String,ExtensionRecord> getExtensionRecords() throws Exception{
		Map<String,ExtensionRecord> extensionCaches = cachedExtensions.get();
		
		if(extensionCaches != null) {
			return extensionCaches;
		}
		synchronized (cachedExtensions) {
			extensionCaches = cachedExtensions.get();
			if(extensionCaches != null) {
				return extensionCaches;
			}
			extensionCaches = loadExtensionRecords();
			cachedExtensions.set(extensionCaches);
			return extensionCaches;
		}
	}
	
	
	private Map<String,ExtensionRecord> loadExtensionRecords() throws Exception{
		final Spi meta = type.getAnnotation(Spi.class);
		if( meta != null ) {
			String value = meta.value();
			if( value != null &&	StringUtils.hasText(value) ) {
				String[] names = NAME_SEPARATOR.split(value);
				if(names.length > 1) {
					throw new IllegalStateException("more than one default extension name on extension");
				}
				defaultExtensionName = names[0];
			}
		}
		
		Map<String, ExtensionRecord> extensionRecords = new HashMap<String, ExtensionRecord>();
		
		loadFile(extensionRecords,SERVICE_DIR + type.getName());
		
		return extensionRecords;
	}
	
	private Map<String,ExtensionRecord> loadFile(Map<String, ExtensionRecord> records,String file) throws Exception{
		
		Enumeration<URL> urls;
		ClassLoader classLoader = findClassLoader();
		try {
			if(classLoader != null) {
				urls = classLoader.getResources(file);
			}else {
				urls = ClassLoader.getSystemResources(file);
			}
			
			while(urls.hasMoreElements()) {
				URL url = urls.nextElement();
				BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream(), UTF8));
				try {
					String line;
					while((line = reader.readLine()) != null) {
							ExtensionRecord record = parseOneLine(line);
					}
				}finally {
					
				}
			}
		}finally {
			
		}
		
	}
	
	
	private ExtensionRecord parseOneLine(String originLine) {
		
		String line = originLine;
		
		if( line == null || ! StringUtils.hasText(line)) {
			return null;
		}
		int commentIndex = line.indexOf('#');
		if( commentIndex >= 0) {
			line = line.substring(0, commentIndex);
		}
		line = line.trim();
		
		if(!StringUtils.hasText(line)) {
			return null;
		}
		
		int equalIndex = line.indexOf('=');
		String name = null;
		if( equalIndex > 0  ) {
			name = line.substring(0,equalIndex).trim();
			line = line.substring(equalIndex + 1).trim(); 
		}
		
		if(line.length() <= 0) {
			return null;
		}
		
		try {
			if( name ==null || !StringUtils.hasText(name)) {
				String ifName = type.getSimpleName();
				String implName = line;
				if( ifName.length() > implName.length() || !implName.endsWith(ifName)) {
					throw new IllegalStateException("实现类的名称配置有误");
				}
				name = implName.substring(0,implName.length() - ifName.length()).toLowerCase();
			}
			
			Class<?> extensionClass = Class.forName(line,true,findClassLoader());
			if( !type.isAssignableFrom(extensionClass) ) {
				throw new IllegalStateException(type.getName() + " 不是 " + line + "的父类");
			}
			
			if( extensionClass.isAnnotationPresent(Adaptive.class)) {
				if( cachedAdaptiveClass == null) {
					cachedAdaptiveClass = extensionClass;
					return null;
				}else {
					throw new IllegalStateException("一个扩展点只能有一个adaptive的默认扩展类");
				}
			}else {
				return new ExtensionRecord(name, extensionClass);
			}
			
		}catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private ClassLoader findClassLoader() {
		return ExtensionLoader.class.getClassLoader();
	}
	
	*/
	/**
	 * 定义系统的编码格式
	 
	public static final Charset UTF8 = Charset.forName("utf-8");
	public static final Charset GBK = Charset.forName("gbk");
	
	public static Charset defaultCharSet() {
		return UTF8;
	}
	*/
	
	
	
}


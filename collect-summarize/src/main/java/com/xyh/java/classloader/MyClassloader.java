package com.xyh.java.classloader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

public class MyClassloader extends ClassLoader{

	private String classPath;

	public MyClassloader(String classPath) {
		this.classPath = classPath;
	}
	
	@Override
	protected Class<?> findClass(String name) throws ClassNotFoundException {

		String classFileName = getFileName(name);
		File file = new File(classPath, classFileName);
		try {
			FileInputStream is = new FileInputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0 ;
			while( (len=is.read()) != -1) {
				bos.write(len);
			}
			byte[] data = bos.toByteArray();
			is.close();
			bos.close();
			if( data == null ) {
				throw new ClassNotFoundException();
			}
			return defineClass(name,data,0,data.length);
		}catch(IOException e) {
			e.printStackTrace();
		}
		return super.findClass(name);
	}
	
	
	private String getFileName(String name) {
		int index = name.lastIndexOf('.');
		if(index == -1) {
			return name+".class";
		}else {
			return name.substring(index + 1) + ".class";
		}
	}
	
	public static void main(String[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		MyClassloader  myClassloader  = new MyClassloader("项目的路径地址");
		Class<?> aClass = myClassloader.loadClass("class 的全路径名");
		aClass.getDeclaredMethod("methodName").invoke(aClass.getInterfaces());
	}
}

package com.xyh.optimize.coding;

import java.io.ByteArrayOutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 *  计算机中的编码格式总结
 * @author xyh
 *
 */
public class Coding {
	
	/**
	 1..计算机中存储信息的最小单元是一个字节，即8个bit，所以能表示的字符范围是0~255个
	 2..人类要表示的符号太多，无法用一个字节来完全表示
	 要解决这个矛盾必须要有一个新的数据结构char，从char到byte必须编码。
	 目前常用的编码方式有ASCII、ISO8859-1、GB2312、GBK、UTF-8、UTF-16等
	
	一.java的默认编码
	1、Java文件编译后形成class
		这里Java文件的编码可能有多种多样，但Java编译器会自动将这些编码按照Java文件的编码格式正确读取后产生class文件，
		这里的class文件编码是Unicode编码（具体说是UTF-16编码）。因此，在Java代码中定义一个字符串：
		String s="汉字";
	不管在编译前java文件使用何种编码，在编译后成class后，他们都是一样的----Unicode编码表示。
	2、JVM中的编码
		JVM加载class文件读取时候使用Unicode编码方式正确读取class文件，
		那么原来定义的String s="汉字";在内存中的表现形式是Unicode编码。
	 
	 
	 */
	
	
	/**
	 * file.encoding的值保存的是每个程序的main入口的那个java文件的保存编码，是.java文件的编码。
	 * Charset.defaultCharset()
	 * 1、如果使用了eclipse，由java文件的编码决定
	 * 2、如果没有使用eclipse，则有本地电脑语言环境决定，中国的都是默认GBK编码
	 * System.getProperty("user.language"));
	 * 用户使用的语言,而非编码方式
	 * @param args
	 * @throws UnsupportedEncodingException 
	 * @throws IOException 
	 */
	public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("file.encoding=" + System.getProperty("file.encoding"));
        System.out.println("Default Charset=" + Charset.defaultCharset());
        System.out.println("Default Charset in Use=" + getDefaultCharSet());
        
        
      //获取系统默认编码
      		System.out.println("系统默认编码：    "+System.getProperty("file.encoding"));//查询结果GBK
      		//系统默认字符编码
      		System.out.println("系统默认字符编码:"+Charset.defaultCharset()); //查询结果GBK
      		//操作系统用户使用的语言
      		System.out.println("系统默认语言:"+ System.getProperty("user.language")); //查询结果zh
      		//定义字符串包含数字和中文
            
      		String t = "1a我";
             //通过getBytes方法获取默认的编码
             System.out.println("默认编码格式:");
             byte[] b = t.getBytes();//ASCII,GBK,UTF-8对数字和英文字母的编码相同,对汉字的编码不同,unicode的编码跟前面三项都不同
            
             //打印默认编码
             for (byte c : b) {
          	   System.out.print(c+",\t");
             }
             
             System.out.println();
             //打印GBK编码
             System.out.println("GBK编码格式:");
             b = t.getBytes("GBK");
             for (byte c : b) {
         			System.out.print(c+",\t");
             }
             System.out.println();
           //打印GBK编码
             System.out.println("UTF-8编码格式:");
             b = t.getBytes("UTF-8");
             for (byte c : b) {
          	   System.out.print(c+",\t");
             }
             System.out.println();
             
           //打印ASCII编码
             System.out.println("ASCII编码格式:");
             b = t.getBytes("ASCII");
             for (byte c : b) {
          	   System.out.print(c+",\t");
             }
             System.out.println();
             
           //打印UNICODE编码
             System.out.println("UNICODE编码格式:");
             b = t.getBytes("UNICODE");
             for (byte c : b) {
          	   System.out.print(c+",\t");
             }
             
      /** 
       * 
         	系统默认编码和系统默认字符编码分别是通过两种方式获取的编码格式,
         	 本例中用Myeclipse编写的代码,并在Java文件的Properties->Resorce->Text file encoding 中修改
         	 为编码格式为UTF-8.结果中查询到的编码方式和String默认编码都是UTF-8.
			 说明String数据使用的编码格式同系统使用的编码相同.而系统的编码方式遵循以下原则:
				1、如果使用了eclipse，由java文件的编码决定
				2、如果没有使用eclipse，则有本地电脑语言环境决定，中国的都是默认GBK编码


       * 
       * 
       */
             
             
             

    }
	
	private static String getDefaultCharSet() {
        OutputStreamWriter writer = new OutputStreamWriter(new ByteArrayOutputStream());
        String enc = writer.getEncoding();
        return enc;
    }
	
}

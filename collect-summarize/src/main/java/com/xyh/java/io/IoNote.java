package com.xyh.java.io;

public class IoNote {
	
	/**
	   磁盘IO
	 IO：(走向是以内存为基准的，即往内存中读数据是输入流，从内存中往外写是输出流。)
	 	1.字节流
	 		1.InputStream 		-->  read(byte[]) 
	 			{
	 				1.ByteArrayInputStream、StringBufferInputStream、FileInputStream 是三种基本的介质流，
	 				它们分别从Byte 数组、StringBuffer、和本地文件中读取数据。
					2.PipedInputStream 是从与其它线程共用的管道中读取数据，与Piped 相关的知识后续单独介绍。
					3.ObjectInputStream 和所有FilterInputStream 的子类都是装饰流（装饰器模式的主角）。
	 			}
	 		2.OutputStream		-->  write(byte[])
	 			{
	 				1.ByteArrayOutputStream、FileOutputStream 是两种基本的介质流，它们分别向Byte 数组、和本地文件中写入数据。
					2.PipedOutputStream 是向与其它线程共用的管道中写入数据。
					3.ObjectOutputStream 和所有FilterOutputStream 的子类都是装饰流。
	 			}
	 	2.字符流
	 		1.Reader			-->  read(char[])
	 		2.Writer			-->  write(char[])
	 		
	 1.输入流：InputStream或者Reader：从文件中读到程序中；
	 2.输出流：OutputStream或者Writer：从程序中输出到文件中；		
	 
	 3.节点流：直接与数据源相连，读入或读出。直接使用节点流，读写不方便，为了更快的读写文件，才有了处理流。
	 	父　类 ：InputStream 、OutputStream、 Reader、 Writer
		文　件 ：FileInputStream 、 FileOutputStrean 、FileReader 、FileWriter 文件进行处理的节点流
		数　组 ：ByteArrayInputStream、 ByteArrayOutputStream、 CharArrayReader 、CharArrayWriter 对数组进行处理的节点流（对应的不再是文件，而是内存中的一个数组）
		字符串 ：StringReader、 StringWriter 对字符串进行处理的节点流
		管　道 ：PipedInputStream 、PipedOutputStream 、PipedReader 、PipedWriter 对管道进行处理的节点流
	 4.处理流:处理流和节点流一块使用，在节点流的基础上，再套接一层，套接在节点流上的就是处理流。如BufferedReader.处理流的构造方法总是要带一个其他的流对象做参数。一个流对象经过其他流的多次包装，称为流的链接。
	 	缓冲流：BufferedInputStrean 、BufferedOutputStream、 BufferedReader、 BufferedWriter 增加缓冲功能，避免频繁读写硬盘。
		转换流：InputStreamReader 、OutputStreamReader实现字节流和字符流之间的转换。
		数据流： DataInputStream 、DataOutputStream 等-提供将基础数据类型写入到文件中，或者读取出来。
	 	
	 	
	 */
	
	
}

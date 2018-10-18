package com.xyh.java.coding;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

/**
 * java中的编码总结
 * @author xyh
 *
 */
public class JavaCodeNote {
	
	/**
	 * 理解：
             1，Java编译器（即编译成class文件时） 用的是unicode字符集。
             2，乱码主要是由于不同的字符集相互转换导致的，理论上各个字符的编码规则是不同的，是不能相互转换的，
             所以根本解决乱码的方法就是不要转换编码方式，编码方式前 后统一。
            3，ASCII、GB2312、GBK、GB18030、Big5、Unicode都是字符集的名称。它们定义了采用1~2个字节的编码规范，
            为每个字符赋予了一个独一无二的编号。这个编号就是我们所说的“字符编码”。
     4, Unicode字符集定义的字符编码并不适合直接通过网络传输表达，因为它们必须转换成像0101这样的二进制字节流传输。
     所以就出现了不同的转换规范实现方式：UTF-8，TF-16等。这些不同的转换规范转换后的编码值和Unicode是不同的.
     （unicode是字符集，编码实现是utf-8，utf-16等，unicode到utf-8是有算法的，有统一的规则，
     但unicode和gbk等其他的编码方式是没有直接联系的不能转换）。
  	5,不要轻易地使用或滥用String类的getBytes(encoding)方法，更要尽量避免使用getBytes()方法。
  	因为这个方法是平台依赖的，在平台不可预知的情况下完全可能得到不同的结果。如果一定要进行字节编码，
  	则用户要确保encoding的方法就是当初字符串输入时的encoding(即知道以前的编码)。
  	6, http://825635381.iteye.com/blog/2087380（java 默认的Unicode和外部资源编码的理解）

              new String(input.getBytes("ISO-8859-1"), "GB18030")        　　
             上面这段代码代表什么？有人会说： “把input字符串从ISO-8859-1编码方式转换成GB18030编码方式”。      
              如果这种说法正确，那么又如何解释我们刚提到的java字符串都采用unicode编码呢？ 
　　　　　这种说法不仅是欠妥的，而且是大错特错的，让我们一一来分析，其实事实是这样的：我们本应该用       
　　　　　GB18030的编码来读取数据并解码成字符串，但结果却采用了ISO-8859-1的编码，导致生成一个错误的字       
  　　　　 符串。要恢复，就要先把字符串恢复成原始字节数组，然后通过正确的编码GB18030再次解码成字符串（即     
  　　　　  把以GB18030编码的数据转成unicode的字符串）。注意，字符串永远都是unicode编码的。          
  　　　　  但编码转换并不是负负得正那么简单，这里我们之所以可以正确地转换回来，是因为 ISO8859-1 是单字节编   
    　　　　码，所以每个字节被按照原样 转换为 String ，也就是说，虽然这是一个错误的转换，但编码没有改变，所以      
    　　　　我们仍然有机会把编码转换回来！      
          总结： 
         所以，我们在处理java的编码问题时，要分清楚三个概念：Java采用的编码：unicode，JVM平台默认字符 集和外部资源的编码。 
         
        7，假如我们需要从磁盘文件、数据库记录、网络传输一些字符，保存到Java的变量中，
        要经历由bytes-->encode字符-->Unicode字符的转换
        (例如new String(bytes, encode))；而要把Java变量保存到文件、数据库或者通过网络传输，
        系统要做一个Unicode字符-->encode字符-->bytes的转换(例如String.getBytes([encode]))   
   
    8,前面提到从ASCII、GB2312、GBK到GB18030的编码方法是向下兼容的。而Unicode只与ASCII兼容
    	（更准确地说，是与ISO-8859-1兼容），与GB码不兼容。例如“汉”字的Unicode编码是6C49，而GB码是BABA。
    	(简单来说有两大阵营ANSI和unicode不能相互转换除了ISO-8859-1，因为他有单字节的特殊性)
     9，  String name = "我来了"；
             String str = new String(name.getBytes("x"),"xxx")；
             不管jvm这里默认的编码是GBK还是UTF-8还是其他编码，name.getBytes("x")java都会自动帮你转成Unicode编码，
             然后再以x的编码方式转成xxx的编码字符集。
       	例如：如何GbK转成UTF-8，真正的核心问题是GBK怎么转成unicode（无法直接转只能找GBK和unicode对照表），
       	转成unicode以后转utf-8就好转了（因为有规律）。
       	java特殊因为java 字符串都是默认unicode的（生产的class文件都是 unicode字符集），
       	所以无论你name.getBytes("xx")是什么编码得到的unicode值都是unicode字符集的正确值。
       	（既然java字符默认都转成了unicode那么为什么GBK转UTF-8为什么还是乱码？
       	java都做到默认转unicode编码了可以实现不乱码了，不知内部为什么？），
       	所有有人在java语言中也有实现GBK转UTF-8，其实就是直接unicode转utf-8。
        
  	
	 */
	
	
	public static void main(String[] args) {
		
	}
	
	
	public static void test1() {
		 System.out.println("default charset : "+Charset.defaultCharset());      
         String str = "abc你好";//string with UTF-8 charset      
       
         byte[] bytes = str.getBytes(Charset.forName("UTF-8"));//convert to byte array with UTF-8 encode      
         for (byte b : bytes)      
         {      
             System.out.print(b + " ");      
         }      
         System.out.println();      
         try      
         {      
             String str1 = new String(bytes, "UTF-8");//to UTF-8 string      
             String str2 = new String(bytes, "ISO-8859-1");//to ISO-8859-1 string      
             String str3 = new String(bytes, "GBK");//to GBK string      
       
             System.out.println(str1);//abc你好      
             System.out.println(str2);//abc??????      
             System.out.println(str3);//abc浣犲ソ      
       
             System.out.println();      
             byte[] bytes2 = str2.getBytes(Charset.forName("ISO-8859-1"));      
             for (byte b : bytes2)      
             {      
                 System.out.print(b + " ");      
             }      
             System.out.println();      
             String str22 = new String(bytes2, "UTF-8");      
             System.out.println(str22);//abc你好      
                   
             System.out.println();      
             byte[] bytes3 = str3.getBytes(Charset.forName("GBK"));      
             for (byte b : bytes3)      
             {      
                 System.out.print(b + " ");      
             }      
             System.out.println();      
             String str33 = new String(bytes3, "UTF-8");      
             System.out.println(str33);//abc你好      
         } catch (UnsupportedEncodingException e)      
         {      
             e.printStackTrace();      
         }      
	}
	
	
	public static void test2() throws UnsupportedEncodingException {
		 String str = "中文";  
	        //str = new String("中文".getBytes("gbk"), "gbk");  
	        //System.out.println(Charset.defaultCharset());  
	          
	        System.out.println("中文unicode--->gbk");  
	        printBytes("中文".getBytes("gbk"));//unicode--->gbk  
	        System.out.println("中文unicode--->utf8");  
	        printBytes("中文".getBytes("utf8"));//unicode--->utf8  
	          
	        str = new String("中文".getBytes("gbk"), "gbk");//unicode---->gbk---->unicode  
	          
	        System.out.println("中文unicode---->gbk---->utf8---->unicode---->utf8");  
	        str = new String("中文".getBytes("utf8"), "gbk");//unicode---->gbk---->utf8---->unicode  
	        printBytes(str.getBytes("utf8"));  
	          
	        str = new String(str.getBytes("gbk"), "utf8");  
	        System.out.println(str);  
	          
	          
	        //bytes数组，charset是字节数组安装什么方式编码生成字符串  
	        //new String(bytes, charset)  
	        //System.out.println(new String(str.getBytes("utf8"), "utf8"));  
//	      byte[] jvmBytes = str.getBytes("unicode");  
//	      System.out.println("jvm Encoding: " + bytesToHexString(jvmBytes));  
//	        
//	      byte[] defaultBytes = str.getBytes();  
//	      System.out.println("page Encoding:" + bytesToHexString(defaultBytes));  
//	        
//	      byte[] utf8Bytes = str.getBytes("utf8");  
//	      System.out.println("uft8:         " + bytesToHexString(utf8Bytes));  
//	        
//	      byte[] gb2312Bytes = str.getBytes("gb2312");  
//	      System.out.println("gb2312:       " + bytesToHexString(gb2312Bytes));  
//	        
//	      byte[] gbkBytes = str.getBytes("gbk");  
//	      System.out.println("gbk:          " + bytesToHexString(gbkBytes));  
//	        
//	      byte[] gb18030Bytes = str.getBytes("gb18030");  
//	      System.out.println("gb18030Bytes: " + bytesToHexString(gb18030Bytes));  
//	        
//	      byte[] iso8859_1Bytes = str.getBytes("iso8859-1");  
//	      System.out.println("iso8859-1:    " + bytesToHexString(iso8859_1Bytes));  
//	        
//	      for(int i=0; i<iso8859_1Bytes.length; i++) {  
//	          System.out.println(iso8859_1Bytes[i]);  
//	      }  
	    }  
	      
	      
	    /**  
	     * byte数组转换成16进制字符串  
	     * @param src  
	     * @return  
	     */    
	    public static String bytesToHexString(byte[] src){         
	        StringBuilder stringBuilder = new StringBuilder();         
	        if (src == null || src.length <= 0) {         
	            return null;         
	        }         
	        for (int i = 0; i < src.length; i++) {         
	            int v = src[i] & 0xFF;        
	            String hv = Integer.toHexString(v);         
	            if (hv.length() < 2) {         
	                stringBuilder.append(0);         
	            }         
	            stringBuilder.append(hv);         
	        }         
	        return stringBuilder.toString();         
	    }    
	      
	    public static void printBytes(byte[] src) {  
	        for (byte b : src) {  
	            System.out.print(b + " ");  
	        }  
	        System.out.println();  
	    }  

}

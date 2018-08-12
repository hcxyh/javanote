package com.xyh.java.secrity;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
	
	
	public static void main(String[] args) throws Exception {
		 
		/**
		 * 
		 * 加密解密算法大全
		 * 直接拉到页面下面看所有的加解密详解.
		 * http://snowolf.iteye.com/blog/381767
		 */
		//公私钥保存的路径
		String filepath="c:/tmp/secrity";  
			//生成一对公私钥,并存放到指定的路径
	        //RSAEncrypt.genKeyPair(filepath);
	        //得到经过base64编码的String类型的公司钥字符串
	        System.out.println(RSAEncrypt.getPairKey());
	        System.out.println("--------------公钥加密私钥解密过程-------------------");  
	        String plainText="ihep_公钥加密私钥解密(加密的内容)";  
	        //公钥加密过程  (从文件中 或者字符串中读取     公私钥       )
	        /**
	        byte[] cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)),plainText.getBytes());  
	        String cipher=Base64.encode(cipherData);  
	        //私钥解密过程  
	        byte[] res=RSAEncrypt.decrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)), Base64.decode(cipher));  
	        String restr=new String(res);  
	        System.out.println("原文："+plainText);  
	        System.out.println("加密："+cipher);  
	        System.out.println("解密："+restr);  
	        System.out.println();  
	          
	        
	        System.out.println("--------------私钥加密公钥解密过程-------------------");  
	        plainText="ihep_私钥加密公钥解密(加密内容)";  
	        //私钥加密过程  
	        cipherData=RSAEncrypt.encrypt(RSAEncrypt.loadPrivateKeyByStr(RSAEncrypt.loadPrivateKeyByFile(filepath)),plainText.getBytes());  
	        cipher=Base64.encode(cipherData);  
	        //公钥解密过程  
	        res=RSAEncrypt.decrypt(RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.loadPublicKeyByFile(filepath)), Base64.decode(cipher));  
	        restr=new String(res);  
	        System.out.println("原文："+plainText);  
	        System.out.println("加密："+cipher);  
	        System.out.println("解密："+restr);  
	        System.out.println();  
	          
	        System.out.println("-----------------------------------------");
	        System.out.println("-----------------------------------------");
	        System.out.println("-----------------------------------------");
	        System.out.println("-----------------------------------------");
	        System.out.println("-----------------------------------------");
	        
	        
	        System.out.println("---------------私钥签名过程------------------");  
	        String content="ihep_这是用于签名的原始数据";  
	        String signstr=RSASignature.sign(content,RSAEncrypt.loadPrivateKeyByFile(filepath));  
	        System.out.println("签名原串："+content);  
	        System.out.println("签名串："+signstr);  
	        System.out.println();  
	          
	        System.out.println("---------------公钥校验签名------------------");  
	        System.out.println("签名原串："+content);  
	        System.out.println("签名串："+signstr);  
	          
	        System.out.println("验签结果："+RSASignature.doCheck(content, signstr, RSAEncrypt.loadPublicKeyByFile(filepath)));  
	        System.out.println();  
	        
	        */
	        
	        System.out.println("---------------私钥文件签名------------------");
	        System.out.println("文件大小" + new File("C:/ihuge.zip").length());
	        File file = new File("C:/ihuge.zip");
	        //file.mkdirs();
	        System.out.println("开始时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        String signstrFile = RSASignature.sign(file,RSAEncrypt.loadPrivateKeyByFile(filepath));
	        System.out.println("签名结果"+signstrFile);
	        System.out.println("结束时间"+new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        
	        
	}
}

package com.xyh.java.secrity;

import java.io.File;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * 一般都是使用私钥进行对文件进行加签.
 * 然后使用公钥进行验签.---- 对方也使用这种方式.（完成双认证鉴权）
 */
public class RSASignature {
	
	
	 /** 
	  * 
	  * 通过签名之后,会返回一段签名String信息.
	  * 然后请求的时候,必须要返回签名.在后台对签名进行验证(加密算法,签名摘要算法)
	  * 
	  * 可以对签名的其中某一部分,进行签名.或者对一个文件分段使用不同的签名算法.
	  * 
	  *  要好好了解签名的流程, 以及什么是对摘要进行加密.
	  * 
      * 签名算法 (可以使用其他的,自行网上copy)
      * 
      * 以下是几种不同的摘要算法
      */  
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";  
    public static final String SIGN_ALGORITHMS1 = "SHA256WithRSA";  
    public static final String SIGN_ALGORITHMS3 = "MD5withRSA";  
  
    /** 
    * RSA签名 
    * @param content 待签名数据 
    * @param privateKey 商户私钥 
    * @param encode 字符集编码 
    * @return 签名值 
    */  
    public static String sign(String content, String privateKey, String encode)  
    {  
    		/**
    		 * 1.对私钥进行读取byte操作
    		 */
    	try   
        {  	
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decode(privateKey) );   
              //取得秘钥生成的算法.
            KeyFactory keyf                 = KeyFactory.getInstance("RSA");  
            PrivateKey priKey               = keyf.generatePrivate(priPKCS8);  
            /**
             * 签名的摘要算法
             */
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);  
  
            signature.initSign(priKey);
            //取得不同的编码字节数组
            signature.update( content.getBytes(encode));  
  
            byte[] signed = signature.sign();  
              
            return Base64.encode(signed);  
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
          
        return null;  
    }  
      
    //使用RSA 加签(私钥)
    public static String sign(String content, String privateKey)  
    {  
        try   
        {  
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decode(privateKey) );   
            KeyFactory keyf = KeyFactory.getInstance("RSA");  
            PrivateKey priKey = keyf.generatePrivate(priPKCS8); 
            //使用验证签名算法 取得签名
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);  
            signature.initSign(priKey);
            //使用默认的编码,来获取内容的字节组
            signature.update( content.getBytes());  
            byte[] signed = signature.sign();  
            //返回 加签后的 签名String
            return Base64.encode(signed);  
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
    
    /**
     *  使用私钥对文件进行 加签
     * @param file
     * @param privateKey
     * @return
     */
    public static String sign(File file, String privateKey)  
    {  
        try   
        {   
        	
        	
        	byte[] bytes = FileHelper.getContent(file.getPath());
        	
        	System.out.println("加密的byte[]大小:"+bytes.length);
            PKCS8EncodedKeySpec priPKCS8    = new PKCS8EncodedKeySpec( Base64.decode(privateKey) );   
            KeyFactory keyf = KeyFactory.getInstance("RSA");  
            PrivateKey priKey = keyf.generatePrivate(priPKCS8); 
            //使用验证签名算法 取得签名
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);  
            signature.initSign(priKey);
            //使用默认的编码,来获取内容的字节组
            signature.update( bytes);  
            byte[] signed = signature.sign();  
            //返回 加签后的 签名String
            return Base64.encode(signed);  
            
            
        }  
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
        return null;  
    }  
      
    
    /** 
    * RSA验签名检查 
    * @param content 待签名数据 
    * @param sign 签名值 
    * @param publicKey 分配给开发商公钥 
    * @param encode 字符集编码 
    * @return 布尔值 
    */  
    public static boolean doCheck(String content, String sign, String publicKey,String encode)  
    {  
        try   
        {  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            byte[] encodedKey = Base64.decode(publicKey);  
            //使用包装类取得  公钥
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));  
  
          
            Signature signature = Signature.getInstance(SIGN_ALGORITHMS);  
          
            signature.initVerify(pubKey);  
            signature.update( content.getBytes(encode) );  
            
            //对签名进行验签(返回延签结果)
            boolean bverify = signature.verify( Base64.decode(sign) );  
            return bverify;  
              
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
          
        return false;  
    }  
      
    public static boolean doCheck(String content, String sign, String publicKey)  
    {  
        try   
        {  
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");  
            byte[] encodedKey = Base64.decode(publicKey);  
            PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));  
  
          
            java.security.Signature signature = java.security.Signature  
            .getInstance(SIGN_ALGORITHMS);  
          
            signature.initVerify(pubKey);  
            signature.update( content.getBytes() );  
          
            boolean bverify = signature.verify( Base64.decode(sign) );  
            return bverify;  
              
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();  
        }  
          
        return false;  
    }  
}

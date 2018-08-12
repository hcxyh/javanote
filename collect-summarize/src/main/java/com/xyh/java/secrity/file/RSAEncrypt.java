package com.xyh.java.secrity.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import javax.crypto.Cipher;

public class RSAEncrypt {
	
	
	private static RSAPrivateKey privateKey;
	private static RSAPublicKey publicKey;
	
	
	/**
     * 加密实现
     * * Encrypt String. *
     * 
     * @return byte[] 加密后的字节数组
     */
    protected byte[] encrypt(RSAPublicKey publicKey, byte[] obj) {
        if (publicKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.ENCRYPT_MODE, publicKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
 
    /**
     * 解密实现
     * * Basic decrypt method *
     * 
     * @return byte[] 解密后的字节数组
     */
    protected byte[] decrypt(RSAPrivateKey privateKey, byte[] obj) {
        if (privateKey != null) {
            try {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, privateKey);
                return cipher.doFinal(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    
    
    
    /**
     * 加密文件
     * @param encrypt RSAEncrypt对象
     * @param file 源文件
     * @param newFile 目标文件
     */
    public void encryptFile(RSAEncrypt encrypt, File file, File newFile) {
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
 
            byte[] bytes = new byte[53];
            while (is.read(bytes) > 0) {
                byte[] e = encrypt.encrypt(RSAEncrypt.publicKey, bytes);
                //bytes = new byte[53];
                os.write(e, 0, e.length);
            }
            os.close();
            is.close();
            System.out.println("write success");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
 
    /**
     * 解密文件
     * @param encrypt RSAEncrypt对象
     * @param file
     * @param newFile
     */
    public void decryptFile(RSAEncrypt encrypt, File file, File newFile) {
        try {
            InputStream is = new FileInputStream(file);
            OutputStream os = new FileOutputStream(newFile);
            byte[] bytes1 = new byte[64];
            while (is.read(bytes1) > 0) {
                byte[] de = encrypt.decrypt(RSAEncrypt.privateKey, bytes1);
                bytes1 = new byte[64];
                os.write(de, 0, de.length);
            }
            os.close();
            is.close();
            System.out.println("write success");
 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    
    
	
	
}

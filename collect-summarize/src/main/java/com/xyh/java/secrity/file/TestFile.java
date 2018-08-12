package com.xyh.java.secrity.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import javax.crypto.Cipher;

public class TestFile {

	/**
	 * 文件加密解密 加解密需要依靠以下四个属性， static KeyPairGenerator keyPairGen; static KeyPair
	 * keyPair; static RSAPrivateKey privateKey; static RSAPublicKey publicKey;
	 * 
	 */

	static KeyPairGenerator keyPairGen;

	static KeyPair keyPair;

	static RSAPrivateKey privateKey;

	static RSAPublicKey publicKey;

	static {
		try {
			// 实例类型
			keyPairGen = KeyPairGenerator.getInstance("RSA");
			// 初始化长度
			keyPairGen.initialize(512);
			// 声场KeyPair
			keyPair = keyPairGen.generateKeyPair();
			// Generate keys
			privateKey = (RSAPrivateKey) keyPair.getPrivate();
			publicKey = (RSAPublicKey) keyPair.getPublic();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		RSAEncrypt encrypt = new RSAEncrypt();
		File file = new File("C:/sdf.txt");
		File newFile = new File("C:/sdf1.txt");
		encrypt.encryptFile(encrypt, file, newFile);
//		File file1 = new File("C:/sdf1.txt");
//		File newFile1 = new File("C:/sdf2.txt");
//		encrypt.decryptFile(encrypt, file1, newFile1);
	}

}

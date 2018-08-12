package com.xyh.java.secrity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FileHelper {
	
	
	public static List<File> filterFile(List<File> list, String ext) {
		if (ext == null || ext == "") {
			return list;
		}
		if (list != null) {
			for (int i = 0; i < list.size(); i++) {
				File file = list.get(i);
				String extTrue = getExt(file);
				if (!ext.equals(extTrue)) {
					list.remove(i);
					i--;
				}
			}
		}
		return list;
	}

	public static List<File> getChildFiles(File dir) {
		List<File> list = new ArrayList<File>();
		if (!dir.isDirectory()) {
			return list;
		}

		File[] files = dir.listFiles();
		if (files != null) {
			for (File file : files) {
				list.add(file);
				if (file.isDirectory()) {
					// when file is Directory
					list.addAll(getChildFiles(file));
				}
			}
		}

		return list;
	}

	public static String getExt(File file) {
		if (!file.isFile()) {
			return "";
		}
		String fileName = file.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".") + 1);
		return prefix;

	}

	/**
	 * 读取文件内容
	 * @param filePath
	 * @return
	 * @throws IOException
	 */
	public static byte[] getContent(String filePath) throws IOException {
		File file = new File(filePath);
		long fileSize = file.length();
		if (fileSize > Integer.MAX_VALUE) {
			System.out.println("file too big...");
			return null;
		}
		FileInputStream fi = new FileInputStream(file);
		byte[] buffer = new byte[(int) fileSize];
		int offset = 0;
		int numRead = 0;
		while (offset < buffer.length
		    && (numRead = fi.read(buffer, offset, buffer.length - offset)) >= 0) {
			offset += numRead;
		}
		// 确保所有数据均被读取
		if (offset != buffer.length) {
			throw new IOException("Could not completely read file " + file.getName());
		}
		fi.close();
		return buffer;
	}
	
	/**
	 * 文件保存
	 * @param data
	 * @param fileName
	 */
	public static void saveFile(byte[] data, String fileName) {

		try {
			FileOutputStream in = new FileOutputStream(fileName);
			try {
				in.write(data, 0, data.length);
				in.close();
				// boolean success=true;
				// System.out.println("写入文件成功");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	
	
}

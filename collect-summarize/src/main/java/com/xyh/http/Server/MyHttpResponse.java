package com.xyh.http.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author hcxyh  2018年8月10日
 *
 */
public class MyHttpResponse {

	private OutputStream os = null;

	public MyHttpResponse(OutputStream os) {
        this.os = os;
    }

	public void writerFile(String path) throws IOException {
		FileInputStream fileInputStream = new FileInputStream(path);
		byte[] buff = new byte[1024];
		int len = 0;
		// 构建响应信息
		StringBuffer sb = new StringBuffer();
		// sb.append("HTTP/1.1 200 OK\n");
		// sb.append("Content-Type: text/html; charset=UTF-8\n");
		// sb.append("\n");
		if (path.endsWith("css")) {
			sb.append("HTTP/1.1 200 OK\n");
			sb.append("Content-Type: text/css; charset=UTF-8\n");
			sb.append("\n");
		} else {
			sb.append("HTTP/1.1 200 OK\n");
			sb.append("Content-Type: text/html; charset=UTF-8\n");
			sb.append("\n");
		}
		os.write(sb.toString().getBytes());
		while ((len = fileInputStream.read(buff)) != -1) {
			os.write(buff, 0, len);
		}
		fileInputStream.close();
		os.flush();
		os.close();
	}

}

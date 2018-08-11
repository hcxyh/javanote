package com.xyh.http.Server;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author hcxyh  2018年8月10日
 *
 */
public class MyHttpRequest {
	
	/**
	 * 封装httpRequest的报文格式
	 */
	
	private String uri;
	
	private StringBuffer stringBuffer = new StringBuffer();

	public String getUri() {
		return uri;
	}

	public MyHttpRequest(InputStream in) throws IOException {
		// 对我们对请求字节流进行解析
		resolverRequest(in);
		System.out.println("success encode HttpRequest");
	}

	private void resolverRequest(InputStream in) throws IOException {
		
		/**
		 * TODO 
		 * 此处实际上对应的实现是SocketInputStream,
		 * https://blog.csdn.net/sz66cm/article/details/53308323
		 */
		byte[] buff = new byte[1024];
		int len = 0;
		while((len=in.read(buff)) != -1) {
			if (len > 0 ) {
				String msg = new String(buff);
				stringBuffer.append(msg); 
				// 解析出来 uri
				//uri = msg.substring(msg.indexOf("/"), msg.indexOf("HTTP/1.1") - 1);
			} else {
				System.out.println("bad Request!");
			}
		}
		System.out.println("====" + stringBuffer + "======");
	}
	
	
	public static void main(String[] args) {
		
		FileInputStream fis = null;
        try {
            fis = new FileInputStream("D:\\testData.txt");
            byte bytes[]=new byte[1024];
            int n=0;
            while((n=fis.read(bytes))!= -1){
                String str = new String(bytes,0,n);
                System.out.print(str);
            }
            System.out.println("读取完成");
        } catch (Exception e) {
            e.printStackTrace();
        } finally{
            try {
                fis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
		
	}
	
}

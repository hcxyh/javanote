package com.xyh.util;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * shell 调用执行工具类
 * @author $xueyh
 * @version $Id: ShellUtils.java, v 0.1 2016年11月13日 下午6:30:22 hspcadmin Exp $
 */
public class ShellUtils {

	public static final Log logger = LogFactory.getLog(ShellUtils.class);
	String shellFile = null;
	
	//测试
	public static void main(String[] args) {
		// String filePath = "cmd /c start
		// C:/Users/Administrator/Desktop/test.bat";
		// int i = callShell(filePath);
		// System.out.println(i);
		// execute(filePath, 5);
	}

	/**
	 * 执行脚本的方法。
	 * @param shellFile
	 * @param timeOutSecond
	 * @return
	 */
	public static String execute(String shellFile, long timeOutSecond) {
		// 脚本文件为NULL或空值
		if (null == shellFile || shellFile.equals("")) {
			
		}
		Process process = null;
		ExcuteShellThread est = new ShellUtils().new ExcuteShellThread(shellFile, process);
		FutureTask ft = new FutureTask<String>(est);
		System.out.println("futureTask开始执行计算:" + System.currentTimeMillis());
		new Thread(ft).start();
		String result = "";
		try {
			result = (String) ft.get(timeOutSecond, TimeUnit.SECONDS);
		} catch (InterruptedException ex) {
			logger.error("执行shell中断异常", ex);
		} catch (ExecutionException ex) {
			logger.error("执行shell执行异常", ex);
		} catch (TimeoutException ex) {
			logger.error("执行shell超时异常", ex);
		} catch (Exception ex) {
			logger.error("执行shell未知异常", ex);
		} finally {
			if (process != null)
				process.destroy(); // 关闭这个进程
		}
		return result;
	}

	@SuppressWarnings("rawtypes")
	class ExcuteShellThread implements Callable {
		private String shellFile;
		private Process process;

		public ExcuteShellThread(String shellFile, Process process) {
			this.shellFile = shellFile;
			this.process = process;
		}

		public Object call() throws Exception {
			String outPrint = "";
			if (shellFile == null || "".equals(shellFile.trim())) {
				return "不存在执行脚本:shellFile=" + shellFile;
			}
			if (shellFile.lastIndexOf(".sh") > 0) { // 如果是执行sh后缀的脚本，则在前面加上bash
				shellFile = "bash " + shellFile;
			}
			try {
				if (process == null) {
					logger.info("shell cmd =:" + shellFile);
					process = Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", shellFile }); // 启动一个进程执行shell脚本
				}
				outPrint = getShellOut(process); // 读取输出流数据
				logger.info("shell cmd result:" + outPrint);
			} catch (Exception ex) {
				outPrint = "shell语句执行报错。";
				logger.error("shell语句执行报错。", ex);
			} finally {
				if (process != null)
					process.destroy(); // 关闭这个进程
				process = null;
			}
			return outPrint;
		}
	}

	/**
	 * 读取输出流数据
	 *
	 * @param p
	 *            进程
	 * @return 从输出流中读取的数据
	 * @throws IOException
	 */
	public static final String getShellOut(Process p) throws IOException {

		StringBuilder sb = new StringBuilder();
		BufferedInputStream in = null;
		BufferedReader br = null;

		try {

			in = new BufferedInputStream(p.getInputStream());
			br = new BufferedReader(new InputStreamReader(in));
			String s;

			while ((s = br.readLine()) != null) {
				// 追加换行符
				// sb.append("\n");
				sb.append(s);
			}

		} catch (IOException e) {
			logger.error("读取shell执行输出时异常。", e);
		} finally {
			br.close();
			in.close();
		}

		return sb.toString();
	}

	/**
	 * 判读shell脚本时候发成功执行， 执行成功process.waitFor()返回0 仅做测试使用，未做线程安全处理。
	 * 
	 * @param shellString
	 */
	public static int callShell(String shellString) {
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(shellString);
			int exitValue = process.waitFor();
			if (0 != exitValue) {
				logger.error("call shell failed. error code is :" + exitValue);
				return 0;
			}
		} catch (Throwable e) {
			logger.error("call shell failed. ", e);
		} finally {
			if (null != process) {
				process.destroy();
			}
		}
		return 1;
	}

	/**
	 * 读取shell脚本执行的返回值 仅作为测试使用，未做线程安全处理。
	 * @param args
	 */
	public static void readCmdLine(String shellString) {
		Process process = null;
		List<String> processList = new ArrayList<String>();
		try {
			process = Runtime.getRuntime().exec(shellString);
			BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = input.readLine()) != null) {
				processList.add(line);
			}
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		for (String line : processList) {
			System.out.println(line);
		}
	}

}

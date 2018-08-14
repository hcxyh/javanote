package com.xyh.java.thread.volatileNote.test;

import java.util.Iterator;

/**
* @ClassName: FalseSharingTest
* @author xueyh
* @date 2018年8月13日 下午7:38:54
* 
*/
public class FalseSharingTest {

	public static void main(String[] args) throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			benchmark();
		}
	}
	
	public static void benchmark() throws InterruptedException{
		int size = Runtime.getRuntime().availableProcessors();
		
		SharingLong[] sharingLong = new SharingLong[size];
		
		for (int i = 0; i < size; i++) {
			sharingLong[i] = new SharingLong();
		}
		
		Thread[] threads = new Thread[size];
		
		for (int i = 0; i < size; i++) {
			threads[i] = new LightThread(sharingLong, i);
		}
		for(Thread t : threads){
			t.start();
		}
		long start = System.currentTimeMillis();
		for(Thread t : threads){
			t.join();
		}
		long end = System.currentTimeMillis();
		System.out.printf("total const %dms\n",end -start);
	}
	
}

class SharingLong{
	//https://mp.weixin.qq.com/s/ODJqoiHYwAhRCMnVjunsbQ
	volatile long v;
	//long v;
}

class LightThread extends Thread{
	SharingLong[] shares;
	int index;
	public LightThread(SharingLong[] shares, int index) {
		super();
		this.shares = shares;
		this.index = index;
	}
	public void run(){
		for (int i = 0; i < 1000000; i++) {
			shares[index].v++;
		}
	}
}

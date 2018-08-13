package com.xyh.operatingsystem.cache;

public class Test {
	
	static int sum = 0;
    static long[][] arr;

    public static void main(String[] args) {
        arr = new long[1024 * 1024][8];
        // 横向遍历
        long marked = System.currentTimeMillis();
        for (int i = 0; i < 1024 * 1024; i += 1) {
            for (int j = 0; j < 8; j++) {
                sum += arr[i][j];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");

        marked = System.currentTimeMillis();
        // 纵向遍历
        for (int i = 0; i < 8; i += 1) {
            for (int j = 0; j < 1024 * 1024; j++) {
                sum += arr[j][i];
            }
        }
        System.out.println("Loop times:" + (System.currentTimeMillis() - marked) + "ms");
    }
    
    /**
     * CPU高速缓存
     	在计算机系统中，CPU高速缓存是用于减少处理器访问内存所需平均时间的部件。
     	在金字塔式存储体系中它位于自顶向下的第二层，仅次于CPU寄存器。
     	其容量远小于内存，但速度却可以接近处理器的频率。
		当处理器发出内存访问请求时，会先查看缓存内是否有请求数据。如果存在（命中），则不经访问内存直接返回该数据；
		如果不存在（失效），则要先把内存中的相应数据载入缓存，再将其返回处理器。
		缓存之所以有效，主要是因为程序运行时对内存的访问呈现局部性（Locality）特征。
		这种局部性既包括空间局部性（Spatial Locality），也包括时间局部性（Temporal Locality）。
		有效利用这种局部性，缓存可以达到极高的命中率。
		在处理器看来，缓存是一个透明部件。因此，程序员通常无法直接干预对缓存的操作。
		但是，确实可以根据缓存的特点对程序代码实施特定优化，从而更好地利用缓存。
				— 维基百科
     
     */
    
}

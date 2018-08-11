package com.xyh.java.base.jvm;

import java.nio.ByteBuffer;

/**
 * 
 * @author hcxyh  2018年8月11日
 *
 */
public class HeapNote {
	
	/**
 	TODO
 	Java 中的堆是 JVM 所管理的最大的一块内存空间，主要用于存放各种类的实例对象。
	在 Java 中，堆被划分成两个不同的区域：
		1.年轻代 ( Young )、老年代 ( Tenured)。
		{
			年轻代 ( Young ) 又被划分为三个区域：
				1.Eden、
				2.From Survivor、
				3.To Survivor。
		}
	 这样划分的目的是为了使 JVM 能够更好的管理堆内存中的对象，包括内存的分配以及回收。
	1.年轻代
	年轻代用来存放新近创建的对象，尺寸随堆大小的增大和减小而相应的变化，默认值是保持为堆大小的1/15，
	可以通过 -Xmn 参数设置年轻代为固定大小，也可以通过 -XX:NewRatio 来设置年轻代与年老代的大小比例，
	年青代的特点是对象更新速度快，在短时间内产生大量的“死亡对象”。
	年轻代的特点是产生大量的死亡对象,并且要是产生连续可用的空间, 所以使用复制清除算法和并行收集器进行垃圾回收.
	对年轻代的垃圾回收称作初级回收 (minor gc)。
	年轻代分三个区。一个Eden区，两个Survivor区。大部分对象在Eden区中生成。
	当Eden区满时，还存活的对象将被复制到Survivor区（两个中的一个），
	当这个Survivor区满时，此区的存活对象将被复制到另外一个Survivor区，
	当这个Survivor去也满了的时候，从第一个Survivor区复制过来的并且此时还存活的对象，
	将被复制年老区(Tenured。需要注意，Survivor的两个区是对称的，
	没先后关系，所以同一个区中可能同时存在从Eden复制过来对象，
	和从前一个Survivor复制过来的对象，而复制到年老区的只有从第一个Survivor去过来的对象。
	而且，Survivor区总有一个是空的。
	2.老年代
	Full GC 是发生在老年代的垃圾收集动作，所采用的是标记-清除算法。
	现实的生活中，老年代的人通常会比新生代的人 “早死”。堆内存中的老年代(Old)不同于这个，
	老年代里面的对象几乎个个都是在 Survivor 区域中熬过来的，它们是不会那么容易就 “死掉” 了的。
	因此，Full GC 发生的次数不会有 Minor GC 那么频繁，并且做一次 Full GC 要比进行一次 Minor GC 的时间更长。
	 另外，标记-清除算法收集垃圾的时候会产生许多的内存碎片 ( 即不连续的内存空间 )，
	 此后需要为较大的对象分配内存空间时，若无法找到足够的连续的内存空间，就会提前触发一次 GC 的收集动作。
	3.永久代
	永久代是Hotspot虚拟机特有的概念，是方法区的一种实现，别的JVM都没有这个东西。在Java 8中，永久代被彻底移除，取而代之的是另一块与堆不相连的本地内存——元空间。
	永久代或者“Perm Gen”包含了JVM需要的应用元数据，这些元数据描述了在应用里使用的类和方法。注意，永久代不是Java堆内存的一部分。永久代存放JVM运行时使用的类。永久代同样包含了Java SE库的类和方法。永久代的对象在full GC时进行垃圾收集。
	用于存放静态文件，如今Java类、方法等。持久代对垃圾回收没有显著影响，但是有些应用可能动态生成或者调用一些class，例如Hibernate等，在这种时候需要设置一个比较大的持久代空间来存放这些运行过程中新增的类。持久代大小通过-XX:MaxPermSize=进行设置。
	
 */


/**
 * TODO
 * 直接内存 Direct Memory ,使用nio调用.
	堆外内存是在 JVM Heap 之外分配的内存块，并不是 JVM 规范中定义的内存区域，堆外内存用得并不多，但十分重要。
	读者也许会有一个疑问：既然已经有堆内存，为什么还要用堆外内存呢？这主要是因为堆外内存在 IO 操作方面的优势。
		举一个例子：在通信中，将存在于堆内存中的数据 flush 到远程时，需要首先将堆内存中的数据拷贝到堆外内存中，
		然后再写入 Socket 中；
		如果直接将数据存到堆外内存中就可以避免上述拷贝操作，提升性能。类似的例子还有读写文件。
		目前，很多 NIO 框架 （如 netty，rpc） 会采用 Java 的 DirectByteBuffer 类来操作堆外内存，
		DirectByteBuffer 类对象本身位于 Java 内存模型的堆中，由 JVM 直接管控、操纵。
		但是，DirectByteBuffer 中用于分配堆外内存的方法 unsafe.allocateMemory(size) 是个一个 native 方法，
		本质上是用 C 的 malloc 来进行分配的。
		分配的内存是系统本地的内存，并不在 Java 的内存中，也不属于 JVM 管控范围，
		所以在 DirectByteBuffer 一定会存在某种特别的方式来操纵堆外内存。
 */

	public static void main( String[] args )
    {        //分配一块1024Bytes的堆外内存(直接内存)
        //allocateDirect方法内部调用的是DirectByteBuffer
        ByteBuffer buffer=ByteBuffer.allocateDirect(1024);
        System.out.println(buffer.capacity());        //向堆外内存中读写数据
        buffer.putInt(0,2018);
        System.out.println(buffer.getInt(0));  
        
    }
	
	/**
	 * 直接内存和堆内存比较:
	 * 直接内存不属于 Java 堆，所以它不受堆大小限制，但是它受物理内存大小的限制。
	 * 可以通过 -XX:MaxDirectMemorySize 参数来设置最大可用直接内存，
	 * 如果启动时未设置则默认为最大堆内存大小，即与 -Xmx 相同。
	 * 即假如最大堆内存为1G，则默认直接内存也为1G，那么 JVM 最大需要的内存大小为2G多一些。
	 * 当直接内存达到最大限制时就会触发GC，如果回收失败则会引起OutOfMemoryError。
	 * 
	 * 测试结论:
	 * 理论上直接内存的机制访问速度要快一些，但也不能武断地直接说直接内存快，
	 * 另外，在内存分配操作上直接内存要慢一些。直接内存更适合在内存申请次数较少，
	 * 但读写操作较频繁的场景。
	 */
	//内存分配比较	
	public static void directMemoryAllocate() {
        long tsStart = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ByteBuffer buffer = ByteBuffer.allocateDirect(400);
        }
        System.out.println("direct memory allocate: " + (System.currentTimeMillis() - tsStart) + " ms");
        tsStart = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            ByteBuffer buffer = ByteBuffer.allocate(400);
        }
        System.out.println("heap memory allocate： " + (System.currentTimeMillis() - tsStart) + " ms");
    }
	
	//内存读写比较
	public static void memoryRW() {
        ByteBuffer buffer = ByteBuffer.allocateDirect(400);
        ByteBuffer buffer2 = ByteBuffer.allocate(400);
        long tsStart = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 100; j++) {
                buffer.putInt(j);
            }
            buffer.flip();
            for (byte j = 0; j < 100; j++) {
                buffer.getInt();
            }
            buffer.clear();
        }
        System.out.println("direct memory rw： " + (System.currentTimeMillis() - tsStart) + " ms");
        tsStart = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 100; j++) {
                buffer2.putInt(j);
            }
            buffer2.flip();
            for (byte j = 0; j < 100; j++) {
                buffer2.getInt();
            }
            buffer2.clear();
        }
        System.out.println("heap memory rw： " + (System.currentTimeMillis() - tsStart) + " ms");
    }


}

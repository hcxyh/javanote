package com.xyh.java.Exception;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 列举常见的虚拟机error
 * @author hcxyh  2018年8月11日
 *
 */
public class VirtualMachineErrorNote {

    /*
        java.lang.OutOfMemoryError: PermGen space 这个异常比较常见，
        是说ＪＶＭ里的Perm内存区的异常溢出，
        由于JVM在默认的情况下，Perm默认为64M，而很多程序需要大量的Perm区内存，
        尤其使用到像Spring等框架的时候，由于需要使用到动态生成类，而这些类不能被GC自动释放，
        所以导致OutOfMemoryError: PermGen space异常。解决方法很简单，增大JVM的
        -XX:MaxPermSize 启动参数，就可以解决这个问题，
        如过使用的是默认变量通常是64M[5.0 and newer: 64 bit VMs are scaled 30% larger; 1.4 amd64: 96m; 1.3.1 -client: 32m.]，改成128M就可以了，
        -XX:MaxPermSize=128m。如果已经是128m（Eclipse已经是128m了），
        就改成 256m。我一般在服务器上为安全起见，改成256m。

        java.lang.OutOfMemoryError：heap space或 其它OutOfMemoryError，
        这个异常实际上跟上面的异常是一个异常，但解决方法不同，所以分开来写。
        上面那个异常是因为JVM的perm区内存区分少了引起的
        （JVM的内存区分为 young,old,perm三种）。
        而这个异常是因为JVM堆内存或者说总体分少了。解决方法是更改 -Xms -Xmx 启动参数，
        通常是扩大1倍。xms是管理启动时最小内存量的，xmx是管里JVM最大的内存量的。
        注：OutOfMemoryError可能有很多种原因，根据JVM Specification, 可能有一下几种情况，
        我先简单列出。stack：stack分区不能动态扩展，或不足以生成新的线程。
        Heap:需要更多的内存，而不能获得。Method Area :如果不能满足分配需求。
        runtime constant pool(从Method Area分配内存)不足以创建class or interface。
        native method stacks不能够动态扩展，或生成新的本地线程。

        最后说说java.lang.StackOverflowError，老实说这个异常我也没碰见过，
        但JVM Specification就提一下，规范上说有一下几种境况可能抛出这个异常，
        一个是Stacks里的线程超过允许的时候，另一个是当native method要求更大的内存，
        而超过native method允许的内存的时候。根据SUN的文档，
        提高-XX:ThreadStackSize=512的值。

        总的来说调优JVM的内存，组要目的就是在使用内存尽可能小的，使程序运行正常，
        不抛出内纯溢出的bug。而且要调好最小内存，最大内存的比，避免GC时浪费太多时间，
        尤其是要尽量避免FULL GC。
     */
	
	//VM Args: -Xms20m -Xms20m - XX:+HeapDumpOnOutOfMemoryError
	//HeapOOM (存放的是new出来的对象实例,注意测试时防止gc)
	public static void testOutOfMemoryError() {
		while(true) {
			new Object();
		}
	} 
	
	//VM Args:- XX:PermSize=10m -XX:MaxPermSize=10m
	//虚拟机栈
	public static void testStackOverflowError() {
		while(true) {
			testStackOverflowError();
		}
	}
	
	//VM Args:- XX:PermSize=10m -XX:MaxPermSize=10m
	//方法区 ： 存储被虚拟机加载(尚未初始化),finall,staicParam, 即时编译器编译后的代码(动态生成class,像cglib)
	//可采用增强Class加载的方式。基本思路是运行时产生大量的类去填满方法区，直到溢出。借助第三方类库CGLib 直接操作字节码运行，生成大量的动态类。
	public static void testMethodZone() {
		while (true ){
            Enhancer enhancer = new Enhancer();
            enhancer.setSuperclass(Object. class );
            enhancer.setUseCache( false );
            enhancer.setCallback( new MethodInterceptor() {
                   @Override
                   public Object intercept(Object obj, Method method, Object[] args,
                              MethodProxy proxy) throws Throwable {
                         return proxy.invoke(obj, args);
                  }
            });
            enhancer.create();
		}
	}
	
	//VM Args:- XX:PermSize=10m -XX:MaxPermSize=10m
	//运行时常量池,直接通过String.intern
	public static void runtimeConstantPool() {
		List<String> list = new ArrayList<String>();
        int i = 0;
        while (true ){
             list.add(String. valueOf(i++).intern());
       }
	}
	
	/**
	 * 1.通过不断创建线程的方式可以产生OutOfMemoryError，因为每个线程都有自己的栈空间。
	 * 不过这个操作有危险就不做了，原因是Windows平台下，Java的线程是直接映射到操作系统的内核线程上的，
	 * 如果写个死循环无限产生线程，那么可能会造成操作系统的假死。
	 * 虚拟机提供了了参数来控制Java堆和方法区这两部分内存的最大值，剩余内存为2GB-最大堆容量-最大方法区容量，
	 * 程序计数器很小就忽略了，虚拟机进程本身的耗费也不算，剩下的内存就是栈的了。每个线程分配到的栈容量越大，
	 * 可建立的线程数自然就越少，建立线程时就越容易把剩下的内存耗尽。
	 * 2.递归调用会产生StackOverflowTest。
	 * 3.创建对象过多会在堆上Java heap space。
	 * 4.StackOverFlowError这个异常，有错误堆栈可以阅读，比较好定位。而且如果使用虚拟机默认参数，
	 * 栈深度在大多数情况下，达到1000~2000完全没有问题，正常方法的调用这个深度应该是完全够了。
	 * 但是如果建立过多线程导致的OutOfMemoryError，在不能减少线程数或者更换64位虚拟机的情况下，
	 * 就只能通过减小最大堆容量和减小栈容量来换取更多的线程了。
	 * 5.
	 */
	private int stackLength = 1;
    
    public void stackLeak()
    {
        stackLength++;
        stackLeak();
    }
     
    /**
     * 测试内容：栈溢出测试（递归调用导致栈深度不断增加）
     * 
     * 虚拟机参数：-Xss128k
     */
    public static void main(String[] args) throws Throwable
    {
    	VirtualMachineErrorNote stackOverflow = new VirtualMachineErrorNote();
        try
        {
            stackOverflow.stackLeak();
        }
        catch (Throwable e)
        {
            System.out.println("stack length:" + stackOverflow.stackLength);
            throw e;
        }        
    }
    
    /**
     * 测试内容：常量池溢出（这个例子也可以说明运行时常量池为方法区的一部分）
     * 
     * 虚拟机参数-XX:PermSize=10M -XX:MaxPermSize=10M
     * 运行时常量池也是方法区的一部分，所以这两个区域一起看就可以了。
     * 这个区域的OutOfMemoryError可以利用String.intern()方法来产生。
     * 这是一个Native方法，意思是如果常量池中有一个String对象的字符串就返回池中的这个字符串的String对象；
     * 否则，将此String对象包含的字符串添加到常量池中去，并且返回此String对象的引用。测试代码如下
     */
    public static void ConstantPoolOverflowTest(String[] args)
    {
        List<String> list = new ArrayList<String>();
        int i = 0;
        while (true)
        {
            list.add(String.valueOf(i++).intern());
        }
    }
	
}

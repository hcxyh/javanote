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

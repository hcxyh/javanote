package com.xyh.java.classloader;

/**
 * 
 * @author hcxyh  2018年8月11日
 *
 */
public class ClassLoaderNote {

	/**
	 * 1.Java中的所有类，必须被装载到jvm中才能运行，
	 * 这个装载工作是由jvm中的类装载器完成的，
	 * 类装载器所做的工作实质是把类文件从硬盘读取到内存中，	
	 * JVM在加载类的时候，都是通过ClassLoader的loadClass（）
	 * 方法来加载class的，loadClass使用双亲委派模式。
	 */
	
	/**
	 * 2.类加载器（class loader）是一个负责加载JAVA类（classes）的对象，
	 * ClassLoader类是一个抽象类，需要给出类的二进制名称，
	 * class loader尝试定位或者产生一个class的数据，
	 * 一个典型的策略是把二进制名字转换成文件名然后到文件系统中找到该文件。
	 */
	
	/**
	 * TODO 查看ClassLoader-->loadClass()的实现方式.
	 * 使用指定的二进制名称来加载类，这个方法的默认实现按照以下顺序查找类： 
	 1.调用findLoadedClass(String)方法检查这个类是否被加载过
	 2.使用父加载器调用loadClass(String)方法，如果父加载器为Null，类加载器装载虚拟机内置的加载器
	 3.调用findClass(String)方法装载类
	   如果，按照以上的步骤成功的找到对应的类，并且该方法接收的resolve参数的值为true,那么就调用resolveClass(Class)方法来处理类。 
	 ClassLoader的子类最好覆盖findClass(String)而不是这个方法(loadClass)。 除非被重写，这个方法默认在整个装载过程中都是同步的（线程安全的）
	 
	protected Class<?> loadClass(String name, boolean resolve) 该方法的访问控制符是protected，
	 也就是说该方法同包内和派生类中可用。返回值类型Class <?>，这里用到泛型。这里使用通配符?作为泛型实参表示对象可以接受任何类型(类类型)。
	 因为该方法不知道要加载的类到底是什么类，所以就用了通用的泛型。String name要查找的类的名字，
	boolean resolve，一个标志，true表示将调用resolveClass(c)处理该类.
	throws ClassNotFoundException 该方法会抛出找不到该类的异常，这是一个非运行时异常
	
	synchronized (getClassLoadingLock(name)) 看到这行代码，我们能知道的是，这是一个同步代码块，那么synchronized的括号中放的应该是一个对象。
	 
	 getClassLoadingLock(name)
	 看到这里用到变量parallelLockMap，根据这个变量的值进行不同的操作，如果这个变量是Null，
	 那么直接返回this，如果这个属性不为Null，那么就新建一个对象，然后在调用一个putIfAbsent(className, newLock);
	 方法来给刚刚创建好的对象赋值，这个方法的作用我们一会讲。那么这个parallelLockMap变量又是哪来的那，我们发现这个变量是ClassLoader类的成员变量：
	private final ConcurrentHashMap<String, Object> parallelLockMap;
	
	parallelLockMap是在构造函数根据一个属性ParallelLoaders的Registered状态的不同来给parallelLockMap 赋值。
	 我去，隐藏的好深，好，我们继续挖，看看这个ParallelLoaders又是在哪赋值的呢？我们发现，
	 在ClassLoader类中包含一个静态内部类private static class ParallelLoaders，
	 在ClassLoader被加载的时候这个静态内部类就被初始化。这个静态内部类的代码我就不贴了，
	 直接告诉大家什么意思，sun公司是这么说的：
	 Encapsulates the set of parallel capable loader types，
	 意识就是说：封装了并行的可装载的类型的集合。
	
	
	首先，在ClassLoader类中有一个静态内部类ParallelLoaders，他会指定的类的并行能力，如果当前的加载器被定位为具有并行能力，那么他就给parallelLockMap定义，就是new一个 ConcurrentHashMap<>()，那么这个时候，我们知道如果当前的加载器是具有并行能力的，那么parallelLockMap就不是Null，这个时候，我们判断parallelLockMap是不是Null，如果他是null，说明该加载器没有注册并行能力，那么我们没有必要给他一个加锁的对象，getClassLoadingLock方法直接返回this，就是当前的加载器的一个实例。

如果这个parallelLockMap不是null，那就说明该加载器是有并行能力的，那么就可能有并行情况，那就需要返回一个锁对象。然后就是创建一个新的Object对象，调用parallelLockMap的putIfAbsent(className, newLock)方法，这个方法的作用是：首先根据传进来的className,检查该名字是否已经关联了一个value值，如果已经关联过value值，那么直接把他关联的值返回，如果没有关联过值的话，那就把我们传进来的Object对象作为value值，className作为Key值组成一个map返回。然后无论putIfAbsent方法的返回值是什么，都把它赋值给我们刚刚生成的那个Object对象。 

这个时候，我们来简单说明下getClassLoadingLock(String className)的作用，就是： 为类的加载操作返回一个锁对象。为了向后兼容，这个方法这样实现:如果当前的classloader对象注册了并行能力，方法返回一个与指定的名字className相关联的特定对象，否则，直接返回当前的ClassLoader对象。
	
	 */
	
}

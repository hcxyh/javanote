
# ClassloaderNote

ClassLoader: 类加载器，负责将Class加载到JVM中，并把Class字节码重新解析成JVM统一要求的对象格式。
整个生命周期包括：加载、验证、准备、解析、初始化、使用和卸载7个阶段。

1.1、加载
通过一个类的全限定名获取描述此类的二进制字节流；
将这个字节流所代表的静态存储结构保存为方法区的运行时数据结构；
在java堆中生成一个代表这个类的java.lang.Class对象，作为访问方法区的入口；

1.2、验证
为了确保Class文件符合当前虚拟机要求，要经过以下几种验证：

格式验证：验证字节流是否符合class文件格式的规范，并且能被当前虚拟机处理，如是否以魔数0xCAFEBABE开头、主次版本号是否在当前虚拟机处理范围内、常量池是否有不支持的常量类型等。只有经过格式验证的字节流，才会存储到方法区的数据结构，剩余3个验证都基于方法区的数据进行。

元数据验证：对字节码描述的数据进行语义分析，以保证符合Java语言规范，如是否继承了final修饰的类、是否实现了父类的抽象方法、是否覆盖了父类的final方法或final字段等。

字节码验证：对类的方法体进行分析，确保在方法运行时不会有危害虚拟机的事件发生，如保证操作数栈的数据类型和指令代码序列的匹配、保证跳转指令的正确性、保证类型转换的有效性等。

符号引用验证：为了确保后续的解析动作能够正常执行，对符号引用进行验证，如通过字符串描述的全限定名是都能找到对应的类、在指定类中是否存在符合方法的字段描述符等。

1.3、准备
在准备阶段，为类变量（static修饰）在方法区中分配内存并设置初始值。
如果是boolean值默认赋值为false，如果是对象引用默认赋值为：null，以此类推...
private static int var = 100;
准备阶段完成后，var 值为0，而不是100。在初始化阶段，才会把100赋值给val，但是有个特殊情况：
private static final int VAL = 100;
在编译阶段会为VAL生成ConstantValue属性，在准备阶段虚拟机会根据ConstantValue属性将VAL赋值为100。
注意：只设置类中的静态变量（方法区中），不包括实例变量（堆内存中），实例变量是在对象实例化的时候初始化分配值的

1.4、解析
解析阶段是虚拟机将常量池内的符号引用替换为直接引用的过程。
符号引用：简单的理解就是字符串，比如引用一个类，java.util.ArrayList 这就是一个符号引用，字符串引用的对象不一定被加载。
直接引用：指针或者地址偏移量。引用对象一定在内存（已经加载）。

1.5、初始化
执行类构造器 <clinit>
初始化静态变量、静态块中的数据等（一个类加载器只会初始化一次）
子类的 <clinit>调用前保证父类的 <clinit>被调用
注意： <clinit>是线程安全的，执行 <clinit>的线程需要先获取锁才能进行初始化操作，保证只有一个线程能执行 <clinit>(利用此特性可以实现线程安全的懒汉单例模式)。

2、类初始化场景
虚拟机中严格规定了有且只有5种情况必须对类进行初始化。
	1.执行new、getstatic、putstatic和invokestatic指令；
	2.使用reflect对类进行反射调用；
	3.初始化一个类的时候，父类还没有初始化，会事先初始化父类；
	4.启动虚拟机时，需要初始化包含main方法的类；
	5.在JDK1.7中，如果java.lang.invoke.MethodHandler实例
	最后的解析结果REFgetStatic、REFputStatic、REF_invokeStatic的方法句柄，并且这个方法句柄对应的类没有进行初始化.
以下几种情况，不会触发类初始化
1、通过子类引用父类的静态字段，只会触发父类的初始化，而不会触发子类的初始化。
2、定义对象数组，不会触发该类的初始化。
3、常量在编译期间会存入调用类的常量池中，本质上并没有直接引用定义常量的类，不会触发定义常量所在的类。
4、通过类名获取Class对象，不会触发类的初始化。
5、通过Class.forName加载指定类时，如果指定参数initialize为false时，也不会触发类初始化，其实这个参数是告诉虚拟机，是否要对类进行初始化。
6、通过ClassLoader默认的loadClass方法，也不会触发初始化动作

3、JVM中的类加载器以及双亲委派机制
整个JVM平台提供了三层ClassLoader。
	1.Bootstrap ClassLoader，它主要加载JVM自身工作需要的类，这个ClassLoader完全是由JVM自己控制的，需要加载哪个类、怎么加载都由JVM自己控制，别人也访问不到这个类，所以这个ClassLoader是不遵守前面介绍的加载规则的，它仅仅是一个类的加载工具而已，既没有更高一级的父加载器，也没有子加载器。
	2.ExtClassLoader，这个类加载器有点特殊，它是JVM自身的一部分，但是它的血统也不是很纯正，它并不是由JVM亲自实现的，他加载的目标在System.getProperty("java.ext.dirs")目录下。
	3.AppClassLoader，它的父类是ExtClassLoader。它加载的目标在System.getProperty("java.class.path")目录下，这个目录就是我们经常用到的classpath。
	在这里，需要着重说明的是，JVM在加载类时默认采用的是双亲委派机制。通俗的讲，就是某个特定的类加载器在接到加载类的请求时，首先将加载任务委托给父类加载器，依次递归，如果父类加载器可以完成类加载任务，就成功返回；只有父类加载器无法完成此加载任务时，才自己去加载。

ExtClassLoader和AppClassLoader都位于sun.misc.Launcher类中，代码不是很多，我们可以来看一下。
{
	代码中Launcher的构造方法是主入口，很容易可以看到，JVM把AppClassLoader的父类加载器设置为ExtClassLoader，而ExtClassLoader却没有父类加载器。其实很多文章在介绍ClassLoader的等级结构时把Bootstrap ClassLoader也列在ExtClassLoader的上一级中，其实Bootstrap ClassLoader并不属于JVM的类等级层次，因为Bootstrap ClassLoader并没有遵守ClassLoader的加载规则。另外Bootstrap ClassLoader并没有子类，我们在应用中能提取到的顶层父类是ExtClassLoader。
}
如果我们要实现自己的类加载器，不管你是直接实现抽象类ClassLoader，还是继承URLClassLoader类，或者其他子类，它的父加载器都是AppClassLoader，因为不管调用哪个父类构造器，创建的对象都必须最终调用getSystemClassLoader()作为父加载器。而getSystemClassLoader()方法获取到的正是AppClassLoader。


/**
	 * java.lang.ClassLoader 类加载器的划分: 启动类加载器(Bootstrap ClassLoader):
	 * 这个类加载器负责将<JAVA_HOME>\lib目录下的类库加载到虚拟机内存中,用来加载java的核心库,此类加载器并不继承于java.lang.ClassLoader,
	 * 不能被java程序直接调用,代码是使用C++编写的.是虚拟机自身的一部分.
	 * 
	 * 扩展类加载器(Extendsion ClassLoader):
	 * 这个类加载器负责加载<JAVA_HOME>\lib\ext目录下的类库,用来加载java的扩展库,开发者可以直接使用这个类加载器.
	 * 
	 * 应用程序类加载器(Application ClassLoader):
	 * 这个类加载器负责加载用户类路径(CLASSPATH)下的类库,一般我们编写的java类都是由这个类加载器加载,
	 * 这个类加载器是CLassLoader中的getSystemClassLoader()方法的返回值,所以也称为系统类加载器.一般情况下这就是系统默认的类加载器.
	 * 
	 * 除此之外,我们还可以加入自己定义的类加载器,以满足特殊的需求,需要继承java.lang.ClassLoader类.
	 */
	
	/**
	 * 类加载器的双亲委派模型:
	 * 双亲委派模型是一种组织类加载器之间关系的一种规范,他的工作原理是:如果一个类加载器收到了类加载的请求,
	 * 它不会自己去尝试加载这个类,而是把这个请求委派给父类加载器去完成,这样层层递进,最终所有的加载请求都被传到最顶层的启动类加载器中,
	 * 只有当父类加载器无法完成这个加载请求(它的搜索范围内没有找到所需的类)时,才会交给子类加载器去尝试加载.
	 * 
	 * 这样的好处是:java类随着它的类加载器一起具备了带有优先级的层次关系.这是十分必要的,比如java.langObject,
	 * 它存放在\jre\lib\rt.jar中,它是所有java类的父类,因此无论哪个类加载都要加载这个类,
	 * 最终所有的加载请求都汇总到顶层的启动类加载器中,因此Object类会由启动类加载器来加载,所以加载的都是同一个类,
	 * 如果不使用双亲委派模型,由各个类加载器自行去加载的话,系统中就会出现不止一个Object类,应用程序就会全乱了.
	 */
	
	
	/**
	 * Class.forname()与ClassLoader.loadClass():
	 * Class.forname():是一个静态方法,最常用的是Class.forname(String className);
	 * 根据传入的类的全限定名返回一个Class对象.该方法在将Class文件加载到内存的同时,会执行类的初始化.
	 * 
	 * ClassLoader.loadClass():这是一个实例方法,需要一个ClassLoader对象来调用该方法,
	 * 该方法将Class文件加载到内存时,并不会执行类的初始化,直到这个类第一次使用时才进行初始化.
	 * 该方法因为需要得到一个ClassLoader对象,所以可以根据需要指定使用哪个类加载器.
	 */
	

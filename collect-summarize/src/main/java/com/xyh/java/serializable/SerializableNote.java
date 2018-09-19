package com.xyh.java.serializable;

/**
* @ClassName: SerializableNote
* @author xueyh
* @date 2018年8月12日 下午3:30:54
* 
*/
public class SerializableNote {
	
	/**
	 * Java中的序列化, 
	 	1、在Java中，只要一个类实现了java.io.Serializable接口，那么它就可以被序列化。
		2、通过ObjectOutputStream和ObjectInputStream对对象进行序列化及反序列化。
		3、虚拟机是否允许反序列化，不仅取决于类路径和功能代码是否一致，一个非常重要的一点是两个类的序列化 ID 是否一致（就是 private static final long serialVersionUID）
		4、序列化并不保存静态变量。
		5、要想将父类对象也序列化，就需要让父类也实现Serializable 接口。
		6、transient 关键字的作用是控制变量的序列化，在变量声明前加上该关键字，可以阻止该变量被序列化到文件中，在被反序列化后，transient 变量的值被设为初始值，如 int 型的是 0，对象型的是 null。
		7、服务器端给客户端发送序列化对象数据，对象中有一些数据是敏感的，比如密码字符串等，希望对该密码字段在序列化时，进行加密，而客户端如果拥有解密的密钥，只有在客户端进行反序列化时，才可以对密码进行读取，这样可以一定程度保证序列化对象的数据安全。

	 * 
	 * 
	 * 二进制序列
	 * 		1.byte 
	 * 	Java原生的二进制字节序列化.
	 * (优点：java原生支持，不需要提供第三方的类库，使用比较简单。缺点：无法跨语言，字节数占用比较大，某些情况下对于对象属性的变化比较敏感。)
	 * 
	 * 文本序列化
	 * 	 	1.json
	 * 			1.fastjson
	 * 			2.gson
	 * 			3.jackson
	 * 		2.xml
	 * 			1.xstream
	 * 
	 * 		3.kyro
	 * 
	 * 		4.protostuff   --> Google工具
	 * 		5.hession
	 */
	
	
	/**
	 * TODO 2018-09-17
	 * 1.Java序列化是指把Java对象保存为二进制字节码的过程，Java反序列化是指把二进制码重新转换成Java对象的过程。
	 * 那么为什么需要序列化呢？
	第一种情况是：一般情况下Java对象的声明周期都比Java虚拟机的要短，实际应用中我们希望在JVM停止运行之后能够持久化指定的对象，这时候就需要把对象进行序列化之后保存。
	第二种情况是：需要把Java对象通过网络进行传输的时候。因为数据只能够以二进制的形式在网络中进行传输，因此当把对象通过网络发送出去之前需要先序列化成二进制数据，在接收端读到二进制数据之后反序列化成Java对象。
	 * 2.序列化种类
	 	采用java对象的序列化和反序列化
这里简单说明一下java序列化所占用字节大小，具体可以参考http://blog.csdn.net/u013256816/article/details/50474678。


ByteArrayOutputStream os = new ByteArrayOutputStream();
       ObjectOutputStream oos = new ObjectOutputStream(os);
       oos.writeObject(user);
       oos.flush();
       oos.close();
       System.out.println(os.toByteArray().length);
序列化大小：205.
优点：java原生支持，不需要提供第三方的类库，使用比较简单。缺点：无法跨语言，字节数占用比较大，某些情况下对于对象属性的变化比较敏感。
	
	把对象包装成JSON字符串传输
	JSON工具类有许多种，这里列出三个比较流行的json工具类：Jackson,Gson,FastJson.
	.开源的Jackson
	Jackson社区相对比较活跃，更新速度也比较快。Jackson对于复杂类型的json转换bean会出现问题，
	一些集合Map，List的转换出现问题。Jackson对于复杂类型的bean转换Json，转换的json格式不是标准的Json格式。
	序列化大小：111.
	注意到这里Jackson会输出null，在Jackson的2.x版本中可以通过设置而使其不输出null的字段。
	
	2. Google的Gson
	Gson是目前功能最全的Json解析神器，Gson当初是为因应Google公司内部需求而由Google自行研发而来，
	但自从在2008年五月公开发布第一版后已被许多公司或用户应用。Gson的应用主要为toJson与fromJson两个转换函数，
	无依赖，不需要例外额外的jar，能够直接跑在JDK上。
	而在使用这种对象转换之前需先创建好对象的类型以及其成员才能成功的将JSON字符串成功转换成相对应的对象。
	类里面只要有get和set方法，Gson完全可以将复杂类型的json到bean或bean到json的转换，是JSON解析的神器。
	Gson在功能上面无可挑剔，但是性能上面比FastJson有所差距。
	Gson和Jackson的区别是：如果你的应用经常会处理大的JSON文件，那么Jackson应该是你的菜。GSON在大文件上表现得相当吃力。
	如果你主要是处理小文件请求，比如某个微服务或者分布式架构的初始化，那么GSON当是首选。Jackson在小文件上的表现则不如人意。
	
	3. 阿里巴巴的FastJson
	Fastjson是一个Java语言编写的高性能的JSON处理器,由阿里巴巴公司开发。无依赖，不需要例外额外的jar，能够直接跑在JDK上。
	FastJson在复杂类型的Bean转换Json上会出现一些问题，可能会出现引用的类型，导致Json转换出错，需要制定引用。
	FastJson采用独创的算法，将parse的速度提升到极致，超过所有json库。
	
	Google工具protoBuf
protocol buffers 是google内部得一种传输协议，目前项目已经开源。它定义了一种紧凑得可扩展得二进制协议格式，适合网络传输，并且针对多个语言有不同得版本可供选择。
protoBuf优点：1.性能好，效率高；2.代码生成机制，数据解析类自动生成；3.支持向前兼容和向后兼容；4.支持多种编程语言；5.字节数很小，适合网络传输节省io。缺点：1.应用不够广；2.二进制格式导致可读性差；3.缺乏自描述；
protoBuf是需要编译工具的，这里用的是window的系统。需要下载proto.exe和protobuf-java-2.4.1.jar；

	
	JSON	跨语言、格式清晰一目了然	字节数比较大，需要第三方类库
Object Serialize	java原生方法不依赖外部类库	字节数大，不能跨语言
Google protobuf	跨语言、字节数比较少	编写.proto配置用protoc工具生成对应的代码
	
	 */
	
}

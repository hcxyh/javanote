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
	 * 		4.protostuff
	 * 
	 */
	
}

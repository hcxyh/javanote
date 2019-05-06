package com.xyh.java.base.spi;

/**
 * 
 * @author hcxyh  2018年8月13日
 * TODO https://github.com/chenyurong/song-parser-spi-demo
 */
public class SpiNote {

	/**
	 1.spi：服务发现机制
	 	动态的为某个接口寻找服务实现，这种机制有点类似IOC思想，将装配的控制权移到程序之外，在模块化设计中这个机制尤其重要。
	 	当服务的提供者提供了服务接口的一种实现之后，必须根据SPI约定在 META-INF/services/ 目录里创建一个以服务接口命名的文件，
	 	该文件里写的就是实现该服务接口的具体实现类。
	 	当程序调用ServiceLoader的load方法的时候，ServiceLoader能够通过约定的目录找到指定的文件，并装载实例化，完成服务的发现。
	 2.实现 --> JDBC就是使用spi进行寻找对应数据库的连接包.
	 	JDBC使用了SPI机制，让所有的任务都交给不同的数据库厂商各自去完成，无论是实现Driver接口，还是SPI要求的接口文件，都做到了让用户不需要关心一点细节，一行代码建立连接
	 	ServiceLoader
	 
	 */
	
}

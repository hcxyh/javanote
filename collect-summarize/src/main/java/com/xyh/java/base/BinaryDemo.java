package com.xyh.java.base;

/**
* @ClassName: BinaryDemo
* @Description: TODO(这里用一句话描述这个类的作用)
* @date 2018年8月8日 下午9:19:48
* 1.二级制  
* 2.位移运算符 <<<左移  >>> 
* 3.位运算符  $与 |或 ~非
*/
public class BinaryDemo {
	
	
	/**
	 * 一个byte(字节数)由8个bit(位数)组成.
	 * 原码,补码(表示负数--原码的绝对值取反+1), 反码
	 * 第一位表示符号位.(0正1负)
	 * integer --4 个byte
	 * byte  -- 1
	 * short -- 2
	 * long  -- 8
	 */
public static void main(String[] args) {
	System.out.println("int最大正数：" + Integer.MAX_VALUE);
	System.out.println("int最大正数二进制表示：" + Integer.toBinaryString(Integer.MAX_VALUE));
	System.out.println("int最小负数：" + Integer.MIN_VALUE);
	System.out.println("int最小负数二进制表示：" + Integer.toBinaryString(Integer.MIN_VALUE));
		
	System.out.println("二进制定义打印int能表示的最大数：" + 0b01111111_11111111_11111111_11111111);
	System.out.println("二进制定义打印int能表示的最小数：" + 0b10000000_00000000_00000000_00000000);
		
	System.out.println("43的二进制表现：" + Integer.toBinaryString(43));//结果省略了前面的0
	System.out.println("-43的二进制表现：" + Integer.toBinaryString(-43));

	//下划线无意义，只是为了方便看，可以随意写
	int a = 0b00000000_00000000_00000000_00000000_00101011;//0b表示为二进制，a相当于十进制的43
	int a1 = 0b101011;//这也是十进制的43，只不过省略了上面的0
	System.out.println("打印a的值：" + a);
	System.out.println("打印a1的值：" + a1);
	int b = 0b11111111_11111111_111111111_1010101;//二进制43取反加1，变成-43，下划线无意义
	System.out.println("打印b的值：" + b);
		
	int i = 0b00000001_00110010_01100111_10101101;//随手写了个十进制的24274861
	System.out.println("打印10进制的i：" + i);
	System.out.println("打印强制转换为short的i：" + (short)i);
	System.out.println("打印short的二进制表示：" + Integer.toBinaryString((short)i));
}
	
}

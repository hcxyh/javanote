package com.xyh.java.base;

/**
* @ClassName: HashCodeNote
* @author xueyh
* @date 2018年8月9日 下午9:49:54
* 
*/
public class HashCodeNote {
	
	/**
	 * hashCode不同,对象一定不相同.
	 * hashCode相同,对象不一定相等.
	 * 
	 {
	 	<< : 左移运算符，num << 1,相当于num乘以2  低位补0
		>> : 右移运算符，num >> 1,相当于num除以2  高位补0
		>>> : 无符号右移，忽略符号位，空位都以0补齐
		 % : 模运算 取余
		^ :   位异或 第一个操作数的的第n位于第二个操作数的第n位相反，那么结果的第n为也为1，否则为0
		 & : 与运算 第一个操作数的的第n位于第二个操作数的第n位如果都是1，那么结果的第n为也为1，否则为0
		 | :  或运算 第一个操作数的的第n位于第二个操作数的第n位 只要有一个是1，那么结果的第n为也为1，否则为0
		 ~ : 非运算 操作数的第n位为1，那么结果的第n位为0，反之，也就是取反运算（一元操作符：只操作一个数）
	 }
	 
	 Eclipse工具：
	 	默认生成的 hashCode 方法实现也和 String 类型差不多。都是使用的 31 ，那么有没有想过：为什么要使用 31 呢？
		在名著 《Effective Java》第 42 页就有对 hashCode 为什么采用 31 做了说明：
		之所以使用 31， 是因为他是一个奇素数。如果乘数是偶数，并且乘法溢出的话，信息就会丢失，
		因为与2相乘等价于移位运算（低位补0）。使用素数的好处并不很明显，但是习惯上使用素数来计算散列结果。 
		31 有个很好的性能，即用移位和减法来代替乘法，可以得到更好的性能： 31 * i == (i << 5） - i， 
		现代的 VM 可以自动完成这种优化。这个公式可以很简单的推导出来。

	 1.object中的hashCode方法是本地方法，也就是用 c 语言或 c++ 实现的，该方法直接返回对象的 内存地址
	 2.String中的HashCode进行了重写.
	 	public int hashCode() {
	        int h = hash;
	        if (h == 0 && value.length > 0) {
	            char val[] = value;
	
	            for (int i = 0; i < value.length; i++) {
	                h = 31 * h + val[i];
	            }
	            hash = h;
	        }
	        return h;
	    }
	 3.hashMap的hash算法
	 	static final int hash(Object key) {
	        int h;
	        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
	    }
	 
	 HashMap的get方法:
	 
	 	final Node<K,V> getNode(int hash, Object key) {
	        Node<K,V>[] tab; Node<K,V> first, e; int n; K k;
	        if ((tab = table) != null && (n = tab.length) > 0 &&
	            // 我们需要关注下面这一行
	            (first = tab[(n - 1) & hash]) != null) {
	            if (first.hash == hash && // always check first node
	                ((k = first.key) == key || (key != null && key.equals(k))))
	                return first;
	            if ((e = first.next) != null) {
	                if (first instanceof TreeNode)
	                    return ((TreeNode<K,V>)first).getTreeNode(hash, key);
	                do {
	                    if (e.hash == hash &&
	                        ((k = e.key) == key || (key != null && key.equals(k))))
	                        return e;
	                } while ((e = e.next) != null);
	            }
	        }
	        return null;
	    }
	 注意first = tab[(n - 1) & hash])。
	 使用数组长度减一 与运算 hash 值。这行代码就是为什么要让前面的 hash 方法移位并异或。
	 
	 */
	
	

}

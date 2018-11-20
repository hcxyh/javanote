package com.xyh.java.jdk8.stream;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * 验证stream运行机制
 * 
 *  1. 所有操作是链式调用, 一个元素只迭代一次
 *  2. 每一个中间操作返回一个新的流. 流里面有一个属性sourceStage 指向同一个 地方,就是Head 
 *  3. Head->nextStage->nextStage->... -> null 
 *  4. 有状态操作会把无状态操作阶段,单独处理
 *  5. 并行环境下, 有状态的中间操作不一定能并行操作.
 *  6. parallel/ sequetial 这2个操作也是中间操作(也是返回stream) 但是他们不创建流, 他们只修改 Head的并行标志
 *  
 */
public class RunStream {
	
	/**
	 *  copy from 晓风轻
	 * 	stream流编程
		stream编程主要是学习API的使用，但前提是学好lambda，基础好了，看这些方法定义非常简单，要是没有打好基础，你会有很多东西需要记忆。
		
		内部迭代和外部迭代
		一般来说，我们之前的编码方法，叫外部迭代，stream的写法叫内部迭代。内部迭代代码更加可读更加优雅，
		关注点是做什么（外部迭代关注是怎么样做），也很容易让我们养成编程小函数的好习惯！这点在编程习惯里面非常重要！看例子
	 */
	
	public static void test() {
	    int[] nums = { 1, 2, 3 };
	    // 外部迭代
	    int sum = 0;
	    for (int i : nums) {
	      sum += i;
	    }
	    System.out.println("结果为：" + sum);

	    // 使用stream的内部迭代
	    // map就是中间操作（返回stream的操作）
	    // sum就是终止操作
	    int sum2 = IntStream.of(nums).map(RunStream::doubleNum).sum();
	    System.out.println("结果为：" + sum2);

	    System.out.println("惰性求值就是终止没有调用的情况下，中间操作不会执行");
	    IntStream.of(nums).map(RunStream::doubleNum);
	  }

	  public static int doubleNum(int i) {
	    System.out.println("执行了乘以2");
	    return i * 2;
	  }
	
	/**
	 	操作类型
		操作类型概念要理清楚。有几个维度。
			1.首先分为 中间操作 和 最终操作，在最终操作没有调用的情况下，所有的中级操作都不会执行。
			那么那些是中间操作那些是最终操作呢？ 简单来说，返回stream流的就是中间操作，可以继续链式调用下去，不是返回stream的就是最终操作。这点很好理解。
			2.最终操作里面分为短路操作和非短路操作，短路操作就是limit/findxxx/xxxMatch这种，就是找了符合条件的就终止，
			其他的就是非短路操作。在无限流里面需要调用短路操作，否则像炫迈口香糖一样根本停不下来！
			3..中间操作又分为 有状态操作 和 无状态操作，怎么样区分呢？ 一开始很多同学需要死记硬背，其实你主要掌握了状态这个关键字就不需要死记硬背。
			状态就是和其他数据有关系。我们可以看方法的参数，如果是一个参数的，就是无状态操作，因为只和自己有关，其他的就是有状态操作。
			如map/filter方法，只有一个参数就是自己，就是无状态操作；而distinct/sorted就是有状态操作，因为去重和排序都需要和其他数据比较，理解了这点，就不需要死记硬背了！
		为什么要知道有状态和无状态操作呢？在多个操作的时候，我们需要把无状态操作写在一起，有状态操作放到最后，这样效率会更加高。
	 	
	 */
	public static void main(String[] args) {
		Random random = new Random();
		// 随机产生数据
		Stream<Integer> stream = Stream.generate(() -> random.nextInt())
				// 产生500个 ( 无限流需要短路操作. )
				.limit(500)
				// 第1个无状态操作
				.peek(s -> print("peek: " + s))
				// 第2个无状态操作
				.filter(s -> {
					print("filter: " + s);
					return s > 1000000;
				})
				// 有状态操作
				.sorted((i1, i2) -> {
					print("排序: " + i1 + ", " + i2);
					return i1.compareTo(i2);
				})
				// 又一个无状态操作
				.peek(s -> {
					print("peek2: " + s);
				}).parallel();

		// 终止操作
		stream.count();
	}

	/**
	 * 打印日志并sleep 5 毫秒
	 * 
	 * @param s
	 */
	public static void print(String s) {
		// System.out.println(s);
		// 带线程名(测试并行情况)
		System.out.println(Thread.currentThread().getName() + " > " + s);
		try {
			TimeUnit.MILLISECONDS.sleep(5);
		} catch (InterruptedException e) {
		}
	}
	
	
	
	
}

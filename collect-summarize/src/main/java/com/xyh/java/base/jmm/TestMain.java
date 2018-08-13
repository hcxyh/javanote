package com.xyh.java.base.jmm;

import java.util.concurrent.TimeUnit;

import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.GenerateMicroBenchmark;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

/**
 * JMH 基准测试
 * @author hcxyh  2018年8月13日
 * TODO http://www.importnew.com/12548.html
 * 		http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/
 * 
 * RPC性能测试: https://www.jianshu.com/u/0a6dfa6a5f2d
 * https://github.com/hank-whu/rpc-benchmark
 */
public class TestMain {
	
	public static void main(String[] args) throws RunnerException {
		Options opt = new OptionsBuilder()
                .include(".*" + TestMain.class.getSimpleName() + ".*")
                .forks(1)
                .build();
  
		new Runner(opt).run();
	}
	
	/**
	 * 1.测试模式
	 * @GenerateMicroBenchmark
		Mode.Throughput			计算一个时间单位内操作数量
		Mode.AverageTime		计算平均运行时间
		Mode.SampleTime			计算一个方法的运行时间(包括百分位)
		Mode.SingleShotTime		方法仅运行一次(用于冷测试模式)。或者特定批量大小的迭代多次运行(具体查看后面的“`@Measurement“`注解)——这种情况下JMH将计算批处理运行时间(一次批处理所有调用的总时间)
		这些模式的任意组合			可以指定这些模式的任意组合——该测试运行多次(取决于请求模式的数量)
		Mode.All				所有模式依次运行
		2.时间单位
		@OutputTimeUnit指定时间单位，它需要一个标准Java类型java.util.concurrent.TimeUnit作为参数。
		3.测试参数状态
			测试方法可能接收参数。这需要提供单个的参数类，这个类遵循以下4条规则：
			1.有无参构造函数(默认构造函数)
			2.是公共类
			3.内部类应该是静态的
			4.该类必须使用@State注解
		@State注解定义了给定类实例的可用范围。JMH可以在多线程同时运行的环境测试，因此需要选择正确的状态。
		Scope.Thread		默认状态。实例将分配给运行给定测试的每个线程。
		Scope.Benchmark		运行相同测试的所有线程将共享实例。可以用来测试状态对象的多线程性能(或者仅标记该范围的基准)。
		Scope.Group			实例分配给每个线程组(查看后面的线程组部分)
		4.状态设置和清理
			与JUnit测试类似，使用@Setup和@TearDown注解标记状态类的方法(这些方法在JMH文档中称为fixtures)。setup/teardown方法的数量是任意的。这些方法不会影响测试时间(但是Level.Invocation可能影响测量精度)。
			@Setup/@TearDown注解使用Level参数来指定何时调用fixture：
				Level.Trial			默认level。全部benchmark运行(一组迭代)之前/之后
				Level.Iteration		一次迭代之前/之后(一组调用)
				Level.Invocation	每个方法调用之前/之后(不推荐使用，除非你清楚这样做的目的)
		5.
	*/
	
	@BenchmarkMode(Mode.Throughput)
	@OutputTimeUnit(TimeUnit.SECONDS)
	@GenerateMicroBenchmark()
	public void test() {
		System.out.println("test GenerateMicroBenchmark");
	}

}

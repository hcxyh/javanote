package com.xyh.java.thread.disruptor.demo;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.EventTranslatorOneArg;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SleepingWaitStrategy;
import com.lmax.disruptor.WaitStrategy;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;

/**
 * 
 * @author hcxyh
 *
 */
public class DisruptorDemo {

	/**
	 * Disruptor 是实现了“队列”的功能，而且是一个有界队列。
	 *  我们类别java中的blockQueue进行测试,.
	 * 1.BlockingQueue 是一个 FIFO 队列，生产者(Producer)往队列里发布(publish)一项事件
	 * (或称之为“消息”也可以)时，消费者(Consumer)能获得通知；如果没有事件时，消费者被堵塞，直到生产者发布了新的事件。
	 * Disruptor 能做更多：
		1.同一个“事件”可以有多个消费者，消费者之间既可以并行处理，也可以相互依赖形成处理的先后次序(形成一个依赖图)；
		2.预分配用于存储事件内容的内存空间；
		3.针对极高的性能目标而实现的极度优化和无锁的设计；
	 * 以上的描述虽然简单地指出了 Disruptor 是什么，但对于它“能做什么”还不是那么直截了当。一般性地来说，
	 * 当你需要在两个独立的处理过程(两个线程)之间交换数据时，就可以使用 Disruptor 。
	 * 当然使用队列（如上面提到的 BlockingQueue）也可以，只不过 Disruptor 做得更好。
	 * 
	 * 拿队列来作比较的做法弱化了对 Disruptor 有多强大的认识，如果想要对此有更多的了解，
	 * 可以仔细看看 Disruptor 在其东家 LMAX 交易平台(也是实现者) 是如何作为核心架构来使用的，
	 * 这方面就不做详述了，问度娘或谷哥都能找到。
	 * 
	 * 二、Disruptor 的核心概念
		1.当消费者等待在SequenceBarrier上时，有许多可选的等待策略，不同的等待策略在延迟和CPU资源的占用上有所不同，
		可以视应用场景选择：
			a)BusySpinWaitStrategy ： 自旋等待，类似Linux Kernel使用的自旋锁。低延迟但同时对CPU资源的占用也多。
			b)BlockingWaitStrategy ： 使用锁和条件变量。CPU资源的占用少，延迟大。
			c)SleepingWaitStrategy ： 在多次循环尝试不成功后，选择让出CPU，等待下次调度，多次调度后仍不成功，尝试前睡眠一个纳秒级别的时间再尝试。这种策略平衡了延迟和CPU资源占用，但延迟不均匀。
			d)YieldingWaitStrategy ： 在多次循环尝试不成功后，选择让出CPU，等待下次调。平衡了延迟和CPU资源占用，但延迟也比较均匀。
			e)PhasedBackoffWaitStrategy ： 上面多种策略的综合，CPU资源的占用少，延迟大。
	 * 
	 * 先从了解 Disruptor 的核心概念开始，来了解它是如何运作的。下面介绍的概念模型，既是领域对象，也是映射到代码实现上的核心对象。
		1.Ring Buffer
			如其名，环形的缓冲区。曾经 RingBuffer 是 Disruptor 中的最主要的对象，但从3.0版本开始，其职责被简化为仅仅负责对
			通过 Disruptor 进行交换的数据（事件）进行存储和更新。在一些更高级的应用场景中，Ring Buffer 可以由用户的自定义实现来完全替代。
		2.Sequence  Disruptor
			通过顺序递增的序号来编号管理通过其进行交换的数据（事件），对数据(事件)的处理过程总是沿着序号逐个递增处理。
			一个 Sequence 用于跟踪标识某个特定的事件处理者( RingBuffer/Consumer )的处理进度。虽然一个 AtomicLong 也可以用于标识进度，
			但定义 Sequence 来负责该问题还有另一个目的，那就是防止不同的 Sequence 之间的CPU缓存伪共享(Flase Sharing)问题。
			（注：这是 Disruptor 实现高性能的关键点之一，网上关于伪共享问题的介绍已经汗牛充栋，在此不再赘述）。
		3.Sequencer 
			Sequencer 是 Disruptor 的真正核心。此接口有两个实现类 SingleProducerSequencer、MultiProducerSequencer ，
			它们定义在生产者和消费者之间快速、正确地传递数据的并发算法。
		4.Sequence Barrier
			用于保持对RingBuffer的 main published Sequence 和Consumer依赖的其它Consumer的 Sequence 的引用。
			 Sequence Barrier 还定义了决定 Consumer 是否还有可处理的事件的逻辑。
		5.Wait Strategy
			定义 Consumer 如何进行等待下一个事件的策略。 （注：Disruptor 定义了多种不同的策略，针对不同的场景，提供了不一样的性能表现）
		6.Event
			在 Disruptor 的语义中，生产者和消费者之间进行交换的数据被称为事件(Event)。它不是一个被 Disruptor 定义的特定类型，
			而是由 Disruptor 的使用者定义并指定。
		7.EventProcessor
			EventProcessor 持有特定消费者(Consumer)的 Sequence，并提供用于调用事件处理实现的事件循环(Event Loop)。
		8.EventHandler
			Disruptor 定义的事件处理接口，由用户实现，用于处理事件，是 Consumer 的真正实现。
		9.Producer
			即生产者，只是泛指调用 Disruptor 发布事件的用户代码，Disruptor 没有定义特定接口或类型。
	 * 
	 */
	
	/**
	 * API使用
	 * 1.定义事件
		事件(Event)就是通过 Disruptor 进行交换的数据类型 
		类比于Blocking中的V
	 */
	public class LongEvent
	{
	    private long value;

	    public void set(long value)
	    {
	        this.value = value;
	    }
	    
	    public Long getEventData() {
	    	return value ;
	    }
	    
	}
	/**
	 * 2.定义事件工厂
		事件工厂(Event Factory)定义了如何实例化前面第1步中定义的事件(Event)，需要实现接口 com.lmax.disruptor.EventFactory<T>。
		Disruptor 通过 EventFactory 在 RingBuffer 中预创建 Event 的实例。
		一个 Event 实例实际上被用作一个“数据槽”，发布者发布前，先从 RingBuffer 获得一个 Event 的实例，
		然后往 Event 实例中填充数据，之后再发布到 RingBuffer 中，之后由 Consumer 获得该 Event 实例并从中读取数据。
	 */
	public class LongEventFactory implements EventFactory<LongEvent>
	{
	    public LongEvent newInstance()
	    {
	        return new LongEvent();
	    }
	}
	
	/**
	 * 3.定义事件处理的具体实现
		通过实现接口 com.lmax.disruptor.EventHandler<T> 定义事件处理的具体实现。
	 */
	public class LongEventHandler implements EventHandler<LongEvent>
	{
	    public void onEvent(LongEvent event, long sequence, boolean endOfBatch)
	    {
	        System.out.println("Event: " + event);
	    }
	}
	
	/**
	 *4. 定义用于事件处理的线程池
Disruptor 通过 java.util.concurrent.ExecutorService 提供的线程来触发 Consumer 的事件处理。例如：
	 */
	ExecutorService executor = Executors.newCachedThreadPool();
	
	/**
	 * 5.指定等待策略
			Disruptor 定义了 com.lmax.disruptor.WaitStrategy 接口用于抽象 Consumer 如何等待新事件，
			这是策略模式的应用。
			Disruptor 提供了多个 WaitStrategy 的实现，每种策略都具有不同性能和优缺点，
			根据实际运行环境的 CPU 的硬件特点选择恰当的策略，并配合特定的 JVM 的配置参数，能够实现不同的性能提升。
		例如，BlockingWaitStrategy、SleepingWaitStrategy、YieldingWaitStrategy 等，其中，
		BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现；
		SleepingWaitStrategy 的性能表现跟 BlockingWaitStrategy 差不多，对 CPU 的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景；
		YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于 CPU 逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性。
	 */
	WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();
	WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();
	WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
	
	/**
	 * 6.启动 Disruptor
	 */
	public static void main(String[] args) {
		
		DisruptorDemo disruptorDemo = new DisruptorDemo();
		EventFactory<LongEvent> eventFactory = disruptorDemo.new LongEventFactory();
		ExecutorService executor = Executors.newSingleThreadExecutor();
		int ringBufferSize = 1024 * 1024; // RingBuffer 大小，必须是 2 的 N 次方；
		        
		Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(eventFactory,
		                ringBufferSize, executor, ProducerType.SINGLE,
		                new YieldingWaitStrategy());
		        
		EventHandler<LongEvent> eventHandler = disruptorDemo.new LongEventHandler();
		disruptor.handleEventsWith(eventHandler);
		disruptor.start();
		
		/**
		 * 7.发布事件
		 * Disruptor 的事件发布过程是一个两阶段提交的过程：
			　　第一步：先从 RingBuffer 获取下一个可以写入的事件的序号；
			　　第二步：获取对应的事件对象，将数据写入事件对象；
			　　第三部：将事件提交到 RingBuffer;
			事件只有在提交之后才会通知 EventProcessor 进行处理
		 */
		// 发布事件；
		RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
		long sequence = ringBuffer.next();//请求下一个事件序号；
		    
		try {
		    LongEvent event = ringBuffer.get(sequence);//获取该序号对应的事件对象；
		    long data = event.getEventData();//获取要通过事件传递的业务数据；
		    event.set(data);
		} finally{
			//注意，最后的 ringBuffer.publish 方法必须包含在 finally 中以确保必须得到调用；
			//如果某个请求的 sequence 未被提交，将会堵塞后续的发布操作或者其它的 producer。
		    ringBuffer.publish(sequence);//发布事件；
		}
		
		
		/**
		 * 8.关闭Disroptor
		 */
		disruptor.shutdown();//关闭 disruptor，方法会堵塞，直至所有的事件都得到处理；
		executor.shutdown();//关闭 disruptor 使用的线程池；如果需要的话，必须手动关闭， disruptor 在 shutdown 时不会自动关闭；

	}

	/**
	 * 另外一种调用方式
	 */
	static class Translator implements EventTranslatorOneArg<LongEvent, Long>{
	    @Override
	    public void translateTo(LongEvent event, long sequence, Long data) {
	        event.set(data);
	    }    
	}
	    
	public static Translator TRANSLATOR = new Translator();
	    
	public static void publishEvent2(Disruptor<LongEvent> disruptor) {
	    // 发布事件；
	    RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();
	    long data = 123 ;//= getEventData();//获取要通过事件传递的业务数据；
	    ringBuffer.publishEvent(TRANSLATOR, data);
	}
	
}

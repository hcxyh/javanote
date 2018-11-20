package com.xyh.reactor.springboot.test;

/**
 * jdk9的包
import java.util.concurrent.Flow.Processor;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;
 */
import java.util.concurrent.TimeUnit;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import reactor.core.publisher.Flux;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * webFlux基础准备.
 *    copy from 晓风轻
 * @author xyh
 *
 */
public class WebFluxBase {
	
	/**
	 jdk9的响应式流
	 	就是reactive stream，也就是flow。其实和jdk8的stream没有一点关系。
	 	[Stream的底层实现 https://blog.csdn.net/u013898617/article/details/79146389 ]
	 	说白了就一个发布-订阅模式，一共只有4个接口，3个对象，非常简单清晰。写一个入门例子就可以掌握。
	 
	 */
	/**
	 * 带 process 的 flow demo
	 */

	/**
	 * Processor, 需要继承SubmissionPublisher并实现Processor接口
	 * 
	 * 输入源数据 integer, 过滤掉小于0的, 然后转换成字符串发布出去
	 */
	
	/**
	class  MyProcessor extends SubmissionPublisher<String> implements Processor<Integer, String> {

	  private Subscription subscription;  

	  @Override
	  public void onSubscribe(Subscription subscription) {
	    // 保存订阅关系, 需要用它来给发布者响应
	    this.subscription = subscription;
	    // 请求一个数据
	    this.subscription.request(1);
	  }

	  @Override
	  public void onNext(Integer item) {
	    // 接受到一个数据, 处理
	    System.out.println("处理器接受到数据: " + item);

	    // 过滤掉小于0的, 然后发布出去
	    if (item > 0) {
	      this.submit("转换后的数据:" + item);
	    }

	    // 处理完调用request再请求一个数据
	    this.subscription.request(1);

	    // 或者 已经达到了目标, 调用cancel告诉发布者不再接受数据了
	    // this.subscription.cancel();
	  }

	  @Override
	  public void onError(Throwable throwable) {
	    // 出现了异常(例如处理数据的时候产生了异常)
	    throwable.printStackTrace();

	    // 我们可以告诉发布者, 后面不接受数据了
	    this.subscription.cancel();
	  }

	  @Override
	  public void onComplete() {
	    // 全部数据处理完了(发布者关闭了)
	    System.out.println("处理器处理完了!");
	    // 关闭发布者
	    this.close();
	  }
	}

	public class FlowDemo2 {

	  public static void test2( ) throws Exception {
	    // 1. 定义发布者, 发布的数据类型是 Integer
	    // 直接使用jdk自带的SubmissionPublisher
	    SubmissionPublisher<Integer> publiser = new SubmissionPublisher<Integer>();

	    // 2. 定义处理器, 对数据进行过滤, 并转换为String类型
	    MyProcessor processor = new MyProcessor();

	    // 3. 发布者 和 处理器 建立订阅关系
	    publiser.subscribe(processor);

	    // 4. 定义最终订阅者, 消费 String 类型数据
	    Subscriber<String> subscriber = new Subscriber<String>() {

	      private Subscription subscription;

	      @Override
	      public void onSubscribe(Subscription subscription) {
	        // 保存订阅关系, 需要用它来给发布者响应
	        this.subscription = subscription;

	        // 请求一个数据
	        this.subscription.request(1);
	      }

	      @Override
	      public void onNext(String item) {
	        // 接受到一个数据, 处理
	        System.out.println("接受到数据: " + item);

	        // 处理完调用request再请求一个数据
	        this.subscription.request(1);

	        // 或者 已经达到了目标, 调用cancel告诉发布者不再接受数据了
	        // this.subscription.cancel();
	      }

	      @Override
	      public void onError(Throwable throwable) {
	        // 出现了异常(例如处理数据的时候产生了异常)
	        throwable.printStackTrace();

	        // 我们可以告诉发布者, 后面不接受数据了
	        this.subscription.cancel();
	      }

	      @Override
	      public void onComplete() {
	        // 全部数据处理完了(发布者关闭了)
	        System.out.println("处理完了!");
	      }

	    };

	    // 5. 处理器 和 最终订阅者 建立订阅关系
	    processor.subscribe(subscriber);

	    // 6. 生产数据, 并发布
	    // 这里忽略数据生产过程
	    publiser.submit(-111);
	    publiser.submit(111);

	    // 7. 结束后 关闭发布者
	    // 正式环境 应该放 finally 或者使用 try-resouce 确保关闭
	    publiser.close();

	    // 主线程延迟停止, 否则数据没有消费就退出
	    Thread.currentThread().join(1000);
	  }
	}
	
	/**
	背压
	背压依我的理解来说，是指订阅者能和发布者交互（通过代码里面的调用request和cancel方法交互），可以调节发布者发布数据的速率，
	解决把订阅者压垮的问题。关键在于上面例子里面的订阅关系Subscription这个接口，他有request和cancel 2个方法，
	用于通知发布者需要数据和通知发布者不再接受数据。
	我们重点理解背压在jdk9里面是如何实现的。关键在于发布者Publisher的实现类SubmissionPublisher的submit方法是block方法。
	订阅者会有一个缓冲池，默认为Flow.defaultBufferSize() = 256。当订阅者的缓冲池满了之后，
	发布者调用submit方法发布数据就会被阻塞，发布者就会停（慢）下来；订阅者消费了数据之后（调用Subscription.request方法），
	缓冲池有位置了，submit方法就会继续执行下去，就是通过这样的机制，实现了调节发布者发布数据的速率，消费得快，生成就快，消费得慢，发布者就会被阻塞，当然就会慢下来了。
	怎么样实现发布者和多个订阅者之间的阻塞和同步呢？使用的jdk7的Fork/Join的ManagedBlocker，有兴趣的请自己查找相关资料。

	reactor
	spring webflux是基于reactor来实现响应式的。那么reactor是什么呢？我是这样理解的
	reactor = jdk8的stream + jdk9的flow响应式流。理解了这句话，reactor就很容易掌握。
	reactor里面Flux和Mono就是stream，他的最终操作就是 subscribe/block 2种。
	reactor里面说的不订阅将什么也不会方法就是我们最开始学习的惰性求值
	
	*/
	
	public static class ReactorDemo {

		  public static void test3() {
		    // reactor = jdk8 stream + jdk9 reactive stream
		    // Mono 0-1个元素
		    // Flux 0-N个元素
		    String[] strs = { "1", "2", "3" };

		    // 2. 定义订阅者
		    Subscriber<Integer> subscriber = new Subscriber<Integer>() {

		      private Subscription subscription;

		      @Override
		      public void onSubscribe(Subscription subscription) {
		        // 保存订阅关系, 需要用它来给发布者响应
		        this.subscription = subscription;

		        // 请求一个数据
		        this.subscription.request(1);
		      }

		      @Override
		      public void onNext(Integer item) {
		        // 接受到一个数据, 处理
		        System.out.println("接受到数据: " + item);

		        try {
		          TimeUnit.SECONDS.sleep(3);
		        } catch (InterruptedException e) {
		          e.printStackTrace();
		        }

		        // 处理完调用request再请求一个数据
		        this.subscription.request(1);

		        // 或者 已经达到了目标, 调用cancel告诉发布者不再接受数据了
		        // this.subscription.cancel();
		      }

		      @Override
		      public void onError(Throwable throwable) {
		        // 出现了异常(例如处理数据的时候产生了异常)
		        throwable.printStackTrace();

		        // 我们可以告诉发布者, 后面不接受数据了
		        this.subscription.cancel();
		      }

		      @Override
		      public void onComplete() {
		        // 全部数据处理完了(发布者关闭了)
		        System.out.println("处理完了!");
		      }

		    };

		    // 这里就是jdk8的stream
		    Flux.fromArray(strs).map(s -> Integer.parseInt(s))
		    // 最终操作
		    // 这里就是jdk9的reactive stream
		    .subscribe(subscriber);
		  }
		}
	/**
	我们可以把jdk9里面flowdemo的订阅者代码原封不动的copy过来，直接就可以用在reactor的subscribe方法上。
	订阅就是相当于调用了stream的最终操作。有了 reactor = jdk8 stream + jdk9 reactive stream 概念后，
	在掌握了jdk8的stream和jkd9的flow之后，reactor也不难掌握
	
	*/
	
	
	
	/**
	 spring5的webflux
		上面的基础和原理掌握之后，学习webflux就水到渠成了！webflux的关键是自己编写的代码里面返回流（Flux/Mono），
		spring框架来负责处理订阅。 spring框架提供2种开发模式来编写响应式代码，使用mvc之前的注解模式和使用router function模式，都需要我们的代码返回流，
		spring的响应式数据库spring data jpa，如使用mongodb，也是返回流，订阅都需要交给框架，自己不能订阅。而编写响应式代码之前，
		我们还需要了解2个重要的概念，就是异步servlet和SSE。
	异步servlet
		学习异步servlet我们最重要的了解同步servlet阻塞了什么？为什么需要异步servlet？异步servlet能支持高吞吐量的原理是什么？
	servlet容器（如tomcat）里面，每处理一个请求会占用一个线程，同步servlet里面，业务代码处理多久，servlet容器的线程就会等（阻塞）多久，
	而servlet容器的线程是由上限的，当请求多了的时候servlet容器线程就会全部用完，就无法再处理请求（这个时候请求可能排队也可能丢弃，得看如何配置），就会限制了应用的吞吐量！
	而异步serlvet里面，servlet容器的线程不会傻等业务代码处理完毕，而是直接返回（继续处理其他请求），给业务代码一个回调函数（asyncContext.complete()），
	业务代码处理完了再通知我！这样就可以使用少量的线程处理更加高的请求，从而实现高吞吐量！
	我们看示例代码：
	 */
	/**
	 * Servlet implementation class AsyncServlet
	 */
	@WebServlet(asyncSupported = true, urlPatterns = { "/AsyncServlet" })
	public class AsyncServlet extends HttpServlet {
	  private static final long serialVersionUID = 1L;

	  /**
	   * @see HttpServlet#HttpServlet()
	   */
	  public AsyncServlet() {
	    super();
	  }

	  /**
	   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	   *      response)
	   */
	  protected void doGet(HttpServletRequest request,
	      HttpServletResponse response) throws ServletException, IOException {
	    long t1 = System.currentTimeMillis();

	    // 开启异步
	    AsyncContext asyncContext = request.startAsync();

	    // 执行业务代码
	    CompletableFuture.runAsync(() -> doSomeThing(asyncContext,
	        asyncContext.getRequest(), asyncContext.getResponse()));

	    System.out.println("async use:" + (System.currentTimeMillis() - t1));
	  }

	  private void doSomeThing(AsyncContext asyncContext,
	      ServletRequest servletRequest, ServletResponse servletResponse) {

	    // 模拟耗时操作
	    try {
	      TimeUnit.SECONDS.sleep(5);
	    } catch (InterruptedException e) {
	    }

	    //
	    try {
	      servletResponse.getWriter().append("done");
	    } catch (IOException e) {
	      e.printStackTrace();
	    }

	    // 业务代码处理完毕, 通知结束
	    asyncContext.complete();
	  }

	  /**
	   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	   *      response)
	   */
	  protected void doPost(HttpServletRequest request,
	      HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	  }
	  /**
	   大家可以运行上面代码，业务代码花了5秒，但servlet容器的线程几乎没有任何耗时。而如果是同步servlet的，线程就会傻等5秒，这5秒内这个线程只处理了这一个请求。
	   */
	}
	
	
	
	/**
	 
	 SSE（server-sent event）
		响应式流里面，可以多次返回数据（其实和响应式没有关系），使用的技术就是H5的SSE。我们学习技术，API的使用只是最初级也是最简单的，
		更加重要的是需要知其然并知其所以然，否则你只能死记硬背不用就忘！我们不满足在spring里面能实现sse效果，更加需要知道spring是如何做到的。
		其实SSE很简单，我们花一点点时间就可以掌握，我们在纯servlet环境里面实现。我们看代码，这里一个最简单的示例。
	关键是ContentType 是 "text/event-stream"，然后返回的数据有固定的要求格式即可。
	 */
	
	/**
	 * Servlet implementation class SSE
	 */
	@WebServlet("/SSE")
	public class SSE extends HttpServlet {
	  private static final long serialVersionUID = 1L;

	  /**
	   * @see HttpServlet#HttpServlet()
	   */
	  public SSE() {
	    super();
	  }

	  /**
	   * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	   *      response)
	   */
	  protected void doGet(HttpServletRequest request,
	      HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("text/event-stream");
	    response.setCharacterEncoding("utf-8");

	    for (int i = 0; i < 5; i++) {
	      // 指定事件标识
	      response.getWriter().write("event:me\n");
	      // 格式: data: + 数据 + 2个回车
	      response.getWriter().write("data:" + i + "\n\n");
	      response.getWriter().flush();

	      try {
	        TimeUnit.SECONDS.sleep(1);
	      } catch (InterruptedException e) {
	      }
	    }

	  }

	  /**
	   * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	   *      response)
	   */
	  protected void doPost(HttpServletRequest request,
	      HttpServletResponse response) throws ServletException, IOException {
	    doGet(request, response);
	  }
	}
	
	
	
	
	
	
}

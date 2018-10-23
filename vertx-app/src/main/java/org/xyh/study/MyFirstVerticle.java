package org.xyh.study;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class MyFirstVerticle extends AbstractVerticle {
	
	/**
	 这个实际上不是最优秀的应用，这个类继承AbstractVerticle，在Vert.x的世界里verticle就是组件，
	 通过继承AbstractVerticle类，获得vertx的入口。
	
	当verticle部署后会调用start方法，我们也能实现 stop 方法，在这个方法里回收资源，
	start方法接收Future对象的参数，可以告诉用户是执行完成还是报出错误，Vert.x是异步执行的，
	运行的时候不会等到start方法执行完成，所以 Future 参数是非常重要的，可以通知是否已经执行完成。

	在start方法里创建了一个HTTP 服务和一个请求处理器（handler），这个请求处理器使用lambda表达式，
	通过requestHandler方法，每次服务器收到请求，都会返回“Hello。。。”
	 */
	
	
  @Override
  public void start(Future<Void> fut) {
    vertx
        .createHttpServer()
        .requestHandler(r -> {
          r.response().end("<h1>Hello from my first " +
              "Vert.x 3 application</h1>");
        })
        .listen(8080, result -> {
          if (result.succeeded()) {
            fut.complete();
          } else {
            fut.fail(result.cause());
          }
        });
  }
}

package com.xyh.reactor.akka;

/*
    akka  --
    https://www.cnblogs.com/tiger-xc/category/990815.html
 */
public class AkkaNote1 {

    /*
    前一段时间一直沉浸在函数式编程模式里，主要目的之一是掌握一套安全可靠的并发程序编程方法（concurrent programming），
    最终通过开源项目FunDA实现了单机多核CPU上程序的并行运算。但是，
    虽然通过在终端实现并行运算能充分利用多核CPU的计算能力把数据处理运算分布到前台可以大大减轻后台服务器的压力，
    提高系统整体效率，对现今大数据普遍盛行的系统计算要求还是远远不足的，
    只有通过硬件平行拓展（scale-out）形成机群并在之上实现分布式运算才能正真符合新环境对软件程序的要求。
    那么，下一个阶段目标应该是分布式运算了。众所周知，Akka应该是目前最著名和通用的分布式软件开发工具了，
    加上是scala语言的开源项目。由于Akka已经是一个在现实中被大量使用的成熟软件工具，网上各方面的参考资料比较丰富，
    感觉应该是一个比较理想的选择。花了几天时间研究了一下Akka官方网站上的资料，先在这里把了解的情况在下面做个小结：

        Akka程序是由多个Actor组成的。它的工作原理是把一项大运算分割成许多小任务然后把这些任务托付给多个Actor去运算。
        Actor不单可以在当前JVM中运行，也可以跨JVM在任何机器上运行，这基本上就是Akka程序实现分布式运算的关键了。
        当然，这也有赖于Akka提供的包括监管、监视各种Actor角色，各式运算管理策略和方式包括容错机制、内置线程管理、远程运行管理（remoting）等，
        以及一套分布式的消息系统来协调、控制整体运算的安全进行。

    Actor是Akka系统中的最小运算单元。每个Actor只容许单一线程，这样来说Actor就是一种更细小单位的线程。
    Akka的编程模式和其内置的线程管理功能使用户能比较自然地实现多线程并发编程。
    Actor的主要功能就是在单一线程里运算维护它的内部状态，那么它的内部状态肯定是可变的（mutable state），
    但因为每个Actor都是独立的单一线程运算单元，加上运算是消息驱动的（message-driven），只容许线性流程，
    Actor之间运算结果互不影响，所以从Akka整体上来讲Actor又好像是纯函数不可变性的（pure immutable）。
    Actor的内部状态（internal state）与函数式编程不可变集合（immutable collection）的元素差不多，都是包嵌在一个类型内，
    即F[A] >>> Actor[A]从类型款式来讲很相像，那么我们可否对Actor进行函数组合（functional composition），
    然后实现函数式编程模式的Akka编程呢？应该是不可能的，因为我们无法对Actor的运算结果进行固定。
    一是我们无法防止Actor的运算产生副作用，再就是Actor的运算结果是无法预料的，例如它可能把结果发送给任何其它Actor，
    这样对同样的输入就可以产生不同的结果。我们可以把Actor视作不纯函数（impure function），
    对同样的输入可能会产生不同的输出结果，如此就无法把对Actor的编程归类为函数式编程了，
    但Actor编程的确是一种有别于其它编程模式、别具风格的编程模式，而且Akka还有一套领域特定语言DSL，是一种独立的编程模式，
    即Actor编程模式了。这是一种需要掌握的崭新编程模式。

    Akka程序具备了以下的优点：
        1、Responsive 快速响应
           以最快时间对用户请求进行回复（响应）
        2、Resilient 高容错性
           可以通过对Actor的：
           复制（replication)、
           封闭（containment）、
           分离（isolation）、
           托管（delegation）来应对解决Actor产生的任何程度的错误和异常
        3、Elastic 可伸缩性
           通过提升计算机配置的垂直扩展（scale-up）、添加网络中计算机数量的水平扩展（scale-out）等系统拓展能力
           实现在任何负载压力情况下的快速响应
        4、Message-driven 消息驱动
           - 异步通信（asynchronous communication）
           - 松散耦合（loosely coupled）
           - 位置透明的Actor定位方式
           - 负载均衡（load management）、流程控制（flow control）、back-pressure
    上面所述特点之一的消息驱动模式中提供了位置透明的Actor定位方式，可以简单的通过设定消息接收方地址来实现程序的分布式运算。
    这点倒是很有趣。
    除了普通功能的Actor之外，Akka还提供了几种具有特殊功能的Actor，
    包括：路由（routingActer）、有限状态机（FSMActor）、持久式（persistenceActor）。其中persistenceActor很有吸引力，
    它可以通过CQRS模式帮助实现新的数据库操作模式ES（Event-Sourcing）。
    CQRS模式的基本原理是对数据库的读和写进行分离操作，目的是提高大数据类型网络应用程序的响应。
    当然，从另一个方面来讲，Event-Sourcing作为一种新的数据库操作模式，应该能解决任何数据库应用软件所普遍面对的数据重演功能缺失，
    以及数据库使用压力等问题。
    初步打算下面的主攻方向是基于persistenceActor的ES模式数据库应用和基于Actor-http的Microservice工具库。
    当然，希望通过各种努力最终实现让那些不精通Akka的朋友们能方便的编写Actor模式的分布式应用程序。
    这可能会涉及到对Akka功能的再组合，搭建新的更高层次的抽象框架、提供API等。
    当然，这些还是需要对Akka进行详细的研究学习后才能有所定论。

     */


}

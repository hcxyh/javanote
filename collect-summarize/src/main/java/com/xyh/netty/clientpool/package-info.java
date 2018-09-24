/**
 * 
 */
/**
 * @author xyh
 *
 */
package com.xyh.netty.clientpool;

/**
DB连接池中,当某个线程获取到一个db connection后,在读取数据或者写数据时,
如果线程没有操作完,这个db connection一直被该线程独占着,直到线程执行完任务。
如果netty client的channel连接池设计也是使用这种独占的方式的话,有几个问题。
1、netty中channel的writeAndFlush方法,调用完后是不用等待返回结果的,
writeAndFlush一被调用,马上返回。对于这种情况,是完全没必要让线程独占一个channel的。
2、使用类似DB pool的方式,从池子里拿连接,用完后返回,这里的一进一出,需要考虑并发锁的问题。
另外,如果请求量很大的时候,连接会不够用,其他线程也只能等待其他线程释放连接。
因此不太建议使用上面的方式来设计netty channel连接池,channel独占的代价太大了。
可以使用Channel数组的形式, 复用netty的channel。当线程要需要Channel的时候,随机从数组选中一个Channel,
如果Channel还未建立,则创建一个。如果线程选中的Channel已经建立了,则复用这个Channel。


1、等待服务端的返回
由于 channel.writeAndFlush是异步的，必须有一种机制来让线程等待服务端返回结果。这里采用最原始的wait和notify方法。当writeAndFlush调用后，立刻让当前线程wait住，放置在callbackservice对象的等待列表中，当服务器端返回消息时，客户端的SocketClientHandler类中的channelRead方法会被执行，解析完数据后，从channel的attr属性中获取DATA_MAP_ATTRIBUTEKEY 这个key对应的map。并根据解析出来的seq从map中获取事先放置好的callbackservice对象，执行它的receiveMessage方法。将receiveBuf这个存放结果的缓存区对象赋值到callbackservice的result属性中。并调用callbackservice对象的notify方法，唤醒wait在callbackservice对象的线程，让其继续往下执行。
2、产生消息序列号
int seq = IntegerFactory.getInstance().incrementAndGet();
为了演示的方便，这里是产生单服务器全局唯一的序列号。如果请求量大的话，就算是AtomicInteger是CAS操作，也会产生很多的竞争。建议产生channel级别的唯一序列号，降低竞争。只要保证在一个channel内的消息的序列号是不重复的即可。



*/
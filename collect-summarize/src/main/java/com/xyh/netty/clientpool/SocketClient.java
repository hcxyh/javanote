package com.xyh.netty.clientpool;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.Channel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * netty自带的FixedChannelPool 其实具备了这个功能的
 * @author xyh
 *
 */

public class SocketClient {
    public static void main(String[] args) throws InterruptedException {
        //当所有线程都准备后,开闸,让所有线程并发的去获取netty的channel
        final CountDownLatch countDownLatchBegin = new CountDownLatch(1);

        //当所有线程都执行完任务后,释放主线程,让主线程继续执行下去
        final CountDownLatch countDownLatchEnd = new CountDownLatch(10);

        //netty channel池
        final NettyChannelPool nettyChannelPool = new NettyChannelPool();

        final Map<String, String> resultsMap = new HashMap<>();
        //使用10个线程,并发的去获取netty channel
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //先让线程block住
                        countDownLatchBegin.await();

                        Channel channel = null;
                        try {
                            channel = nettyChannelPool.syncGetChannel();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        //为每个线程建立一个callback,当消息返回的时候,在callback中获取结果
                        CallbackService callbackService = new CallbackService();
                        //给消息分配一个唯一的消息序列号
                        int seq = IntegerFactory.getInstance().incrementAndGet();
                        //利用Channel的attr方法,建立消息与callback的对应关系
                        ChannelUtils.putCallback2DataMap(channel,seq,callbackService);

                        synchronized (callbackService) {
                            UnpooledByteBufAllocator allocator = new UnpooledByteBufAllocator(false);
                            ByteBuf buffer = allocator.buffer(20);
                            buffer.writeInt(ChannelUtils.MESSAGE_LENGTH);

                            buffer.writeInt(seq);
                            String threadName = Thread.currentThread().getName();
                            buffer.writeBytes(threadName.getBytes());
                            buffer.writeBytes("body".getBytes());

                            //给netty 服务端发送消息,异步的,该方法会立刻返回
                            channel.writeAndFlush(buffer);

                            //等待返回结果
                            callbackService.wait();

                            //解析结果,这个result在callback中已经解析到了。
                            ByteBuf result = callbackService.result;
                            int length = result.readInt();
                            int seqFromServer = result.readInt();

                            byte[] head = new byte[8];
                            result.readBytes(head);
                            String headString = new String(head);

                            byte[] body = new byte[4];
                            result.readBytes(body);
                            String bodyString = new String(body);
                            resultsMap.put(threadName, seqFromServer + headString + bodyString);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    finally {
                        countDownLatchEnd.countDown();
                    }
                }
            }).start();
        }

        //开闸,让10个线程并发获取netty channel
        countDownLatchBegin.countDown();

        //等10个线程执行完后,打印最终结果
        countDownLatchEnd.await();
        System.out.println("resultMap="+resultsMap);
    }

    public static class CallbackService{
        public volatile ByteBuf result;
        public void receiveMessage(ByteBuf receiveBuf) throws Exception {
            synchronized (this) {
                result = receiveBuf;
                this.notify();
            }
        }
    }
}


package com.xyh.java.collection.queue;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

/**
 * 模拟在线订单生成之后,规定时间里未支付自动失效.
 * @author hcxyh  2018年8月9日
 *
 */
public class OrderDelay implements Delayed{
	
	private String orderId;
    private long timeout;  //过期时间
    
    public OrderDelay(String orderId, long timeout) {
        this.orderId = orderId;
        this.timeout = timeout + System.nanoTime();
    }
    public int compareTo(Delayed other) {
        if (other == this)
            return 0;
        OrderDelay t = (OrderDelay) other;
        long d = (getDelay(TimeUnit.NANOSECONDS) - t
                .getDelay(TimeUnit.NANOSECONDS));
        return (d == 0) ? 0 : ((d < 0) ? -1 : 1);
    }
    // 返回距离你自定义的超时时间还有多少
    public long getDelay(TimeUnit unit) {
        return unit.convert(timeout - System.nanoTime(), TimeUnit.NANOSECONDS);
    }
    public void print() {
        System.out.println(orderId+"编号的订单要删除啦。。。。");
    }
}

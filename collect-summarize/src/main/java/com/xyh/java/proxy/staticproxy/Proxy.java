package com.xyh.java.proxy.staticproxy;

/**
 * 代理类
 * @author hcxyh  2018年8月8日
 *
 */
public class Proxy implements Subject{
	
	private Subject realSubject;
    private long start;
    private long end;


    public Proxy(){
        //经典的代理模式是在构造函数中实例化具体主题类
        //realSubject = new RealSubject();
    }

    @Override
    public void request() {
        //进行功能增强1
        this.doBefore();

        //本例通过"虚拟代理"实现了延迟加载
        //即：只有真正执行操作时，才实例化对象
        if(null == realSubject){
            realSubject = new RealSubject();
        }
        //执行真实对象的操作
        realSubject.request();

        //进行功能增强2
        this.doAfter();
    }

    //记录执行起始时间
    private void doBefore(){
        start = System.nanoTime();
        System.out.println("Do something before!");

    }

    //记录执行结束时间
    private void doAfter(){
        end = System.nanoTime();
        System.out.println("Do something after!");
    }

    //获取计算耗时
    public long getCost(){
        return end - start;
    }
}

package com.xyh.spring.transaction.rw;

/**
* @ClassName: DynamicDataSourceHolder
* @author xueyh
* @date 2018年2月12日 下午3:17:59
*/
public final class DynamicDataSourceHolder {
	
	private static final ThreadLocal<DynamicDataSourceGlobal> holder = new ThreadLocal<DynamicDataSourceGlobal>();

    private DynamicDataSourceHolder() {
        //
    }

    public static void putDataSource(DynamicDataSourceGlobal dataSource){
        holder.set(dataSource);
    }

    public static DynamicDataSourceGlobal getDataSource(){
        return holder.get();
    }

    public static void clearDataSource() {
        holder.remove();
    }
}

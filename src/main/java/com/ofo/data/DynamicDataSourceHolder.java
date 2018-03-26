package com.ofo.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DynamicDataSourceHolder {

    private static final Logger log = LoggerFactory.getLogger(DynamicDataSourceHolder.class);

    //定义主数据源 write
    private static final String MASTER="master";

    //定义从数据源 read
    private static final String SLAVE="slave";

    //记录当前线程使用的数据源
    private static final ThreadLocal<String> holder = new ThreadLocal<String>();

    public static void setDataSource(String key){
        log.info("设置当前数据源为:{}",key);
        holder.set(key);
    }

    public static String getDataSource(){
        return holder.get();
    }

    public static void markMaster(){
        setDataSource(MASTER);
    }

    public static void markSlave(){
        setDataSource(SLAVE);
    }
}

package com.zust.yan.rpc.common.utils;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.LongAdder;

public class DefaultThreadFactory implements ThreadFactory {
    private LongAdder longAdder=new LongAdder();
    private String name;
    public DefaultThreadFactory(String name){
        this.name=name;
    }
    @Override
    public Thread newThread(Runnable r) {
        longAdder.increment();
        return new Thread(r,name+"Thread"+longAdder.longValue());
    }
}

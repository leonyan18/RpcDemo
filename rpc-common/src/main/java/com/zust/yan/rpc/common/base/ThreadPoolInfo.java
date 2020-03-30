package com.zust.yan.rpc.common.base;

import com.zust.yan.rpc.common.utils.DefaultThreadFactory;
import lombok.Data;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yan
 */
@Data
public class ThreadPoolInfo {
    private int coreSize = 10;
    private int maxSize = 20;
    private int queueLength = 5;
    private int keepAliveTime = 1000;

    public ThreadPoolInfo() {

    }

    public Executor getExecutor(String name) {
        return new ThreadPoolExecutor(coreSize, maxSize, keepAliveTime,
                TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(queueLength)
                , new DefaultThreadFactory(name));
    }
}
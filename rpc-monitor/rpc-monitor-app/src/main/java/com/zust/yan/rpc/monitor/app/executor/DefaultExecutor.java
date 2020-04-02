package com.zust.yan.rpc.monitor.app.executor;

import com.zust.yan.rpc.common.utils.DefaultThreadFactory;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author yan
 */
public class DefaultExecutor {
    private static Executor executor = new ThreadPoolExecutor(10, 20, 2000,
            TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(5)
            , new DefaultThreadFactory("DefaultExecutor"));

    public static void submit(Runnable runnable) {
        executor.execute(runnable);
    }
}

package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.utils.RpcFutureUtils;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author yan
 * 完成处理数据回调
 */
public class DefaultFuture {
    private volatile Response res;
    private volatile Request request;
    private Lock lock = new ReentrantLock();
    private Condition receive = lock.newCondition();

    public DefaultFuture(Channel channel, Request request) {
        this.request = request;
        RpcFutureUtils.putDefaultFuture(request.getRequestId(), this,request.isNormal());
        RpcFutureUtils.putChannel(request.getRequestId(), channel);
    }

    public DefaultFuture(Channel channel, Request request, CallBack callBack) {
        this.request = request;
        RpcFutureUtils.putDefaultFuture(request.getRequestId(), this,request.isNormal());
        RpcFutureUtils.putChannel(request.getRequestId(), channel);
        RpcFutureUtils.putCallBack(request.getRequestId(), callBack);
    }

    public static void handleMsg(Response response) {
        if (response.getType()==1){
            System.out.println("responseTest: "+response.getData());
        }

        DefaultFuture defaultFuture = RpcFutureUtils.getDefaultFuture(response.getRequestId());
        if (defaultFuture != null) {
            CallBack callBack = RpcFutureUtils.getCallBack(response.getRequestId());
            if (callBack != null) {
                callBack.execute(response);
            }
            defaultFuture.setRes(response);
            // 结果已经赋值到future里了 所以可以删除
            RpcFutureUtils.removeFuture(response.getRequestId());
            defaultFuture.wakeUpBlock();
        }
    }

    public void wakeUpBlock() {
        lock.lock();
        try {
            receive.signal();
        } finally {
            lock.unlock();
        }
    }

    public Response getRes() {
        return res;
    }

    public void setRes(Response res) {
        this.res = res;
    }

    public Response getResBlockInTime() {
        return getResBlockInTime(null);
    }

    public Response getResBlockInTime(Long milliseconds) {
        if (null == res) {
            lock.lock();
            try {
                // 判断停顿时间
                if (res == null) {
                    if (milliseconds == null) {
                        receive.await(RpcUtils.timeOut, TimeUnit.MILLISECONDS);
                    } else if (milliseconds > 0) {
                        receive.await(milliseconds, TimeUnit.MILLISECONDS);
                    } else {
                        receive.await();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        }
        return res;
    }

    public Response getResBlock() {
        return getResBlockInTime(0L);
    }
}

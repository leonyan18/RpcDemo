package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.utils.RpcUtils;
import lombok.Data;

import java.util.concurrent.atomic.LongAdder;

/**
 * @author yan
 */
@Data
public class Request {
    private static LongAdder adder = new LongAdder();

    static {
        // 初始化为当前时间戳
        adder.add(System.currentTimeMillis());
    }

    private Long requestId;
    private String fromAddress;
    private String toAddress;
    private Object data;
    private Long fromRequestId;
    private Long requestTime;
    private Long receiveTime;
    private Long handleEndTime;
    private Long handleStartTime;

    // todo 雪花算法优化
    public Request() {
        adder.increment();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }

    public Request(Object data) {
        this.data = data;
        adder.increment();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }
}

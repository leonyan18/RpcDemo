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
    private Integer type;
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
        type=1;
        adder.increment();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }

    public Request(Object data) {
        this.data = data;
        type=1;
        adder.increment();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }
    public static Request makeHeartBeat(){
        Request request=new Request();
        request.setType(0);
        request.setData("PING");
        return request;
    }
}

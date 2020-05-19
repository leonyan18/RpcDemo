package com.zust.yan.rpc.net.base;

import com.zust.yan.rpc.common.utils.RpcUtils;
import lombok.Data;

import java.io.Serializable;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author yan
 */
@Data
public class Request implements Serializable {
    private static LongAdder adder = new LongAdder();

    static {
        // 初始化为当前时间戳
        adder.add(System.currentTimeMillis());
    }

    /**
     * 1 普通
     * 0 心跳
     * 2 监控
     */
    private Integer type;
    private Long requestId;
    private Long seq;
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
        type = 1;
        adder.increment();
        seq = adder.longValue();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }

    public Request(Object data) {
        this.data = data;
        type = 1;
        adder.increment();
        seq = adder.longValue();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }

    public static Request makeHeartBeat() {
        Request request = new Request();
        request.setType(0);
        request.setData("PING");
        return request;
    }

    public Boolean isHeartBeat() {
        return type == 0;
    }

    public Boolean isNormal() {
        return type == 1;
    }


    public Request(Request request) {
        this.seq = request.seq;
        this.requestId = request.getRequestId();
        this.type = request.getType();
        this.data = request.getData();
        this.fromRequestId = request.getFromRequestId();
        this.fromAddress = request.getFromAddress();
        this.toAddress = request.getToAddress();
        this.requestTime = request.getRequestTime();
        this.receiveTime = request.getReceiveTime();
        this.handleEndTime = request.getHandleEndTime();
        this.handleStartTime = request.getHandleStartTime();
    }

}

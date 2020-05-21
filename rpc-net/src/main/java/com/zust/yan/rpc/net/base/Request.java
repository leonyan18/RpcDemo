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
    /**
     * 1 普通
     * 0 心跳
     * 2 监控
     */
    public static final int HEARTBEAT_TYPE = 0;
    public static final int NORMAL_TYPE = 1;
    public static final int MONITOR_TYPE = 2;
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

    static {
        // 初始化为当前时间戳
        adder.add(System.currentTimeMillis());
    }

    // todo 雪花算法优化
    public Request() {
        type = NORMAL_TYPE;
        adder.increment();
        seq = adder.longValue();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }

    public Request(Object data) {
        this.data = data;
        type = NORMAL_TYPE;
        adder.increment();
        seq = adder.longValue();
        requestId = adder.longValue() * 100000 + RpcUtils.getMachineCode();
    }

    public static Request makeHeartBeat() {
        Request request = new Request();
        request.setType(HEARTBEAT_TYPE);
        request.setData("PING");
        return request;
    }

    public Boolean isHeartBeat() {
        return type == HEARTBEAT_TYPE;
    }

    public Boolean isNormal() {
        return type == NORMAL_TYPE;
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

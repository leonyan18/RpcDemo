package com.zust.yan.rpc.net.base;

import lombok.Data;

import java.util.List;

@Data
public class Response {
    private Long requestId;
    private Object data;
    private Long toRequestId;
    private String fromAddress;
    private String toAddress;
    private Long requestTime;
    private Long receiveTime;
    private Long handleEndTime;
    private Long handleStartTime;
    public Response(){}

    public Response(Long requestId, Object data) {
        this.requestId = requestId;
        this.data = data;
    }
}

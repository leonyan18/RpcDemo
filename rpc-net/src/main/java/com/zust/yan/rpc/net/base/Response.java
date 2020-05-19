package com.zust.yan.rpc.net.base;

import lombok.Data;

import java.io.Serializable;

/**
 * @author yan
 */
@Data
public class Response implements Serializable {
    private Long requestId;
    private Integer type;
    private Object data;
    private Long fromRequestId;
    private String fromAddress;
    private String toAddress;
    private Long requestTime;
    private Long receiveTime;
    private Long handleEndTime;
    private Long handleStartTime;

    public Response() {
        type = 1;
    }

    public Response(Long requestId, Object data) {
        type = 1;
        this.requestId = requestId;
        this.data = data;
    }

    public static Response makeHeartBeat(Long requestId) {
        Response response = new Response(requestId, "PONG");
        response.setType(0);
        return response;
    }

    public Response(Response response) {
        this.requestId = response.getRequestId();
        this.type = response.getType();
        this.data = response.getData();
        this.fromRequestId = response.getFromRequestId();
        this.fromAddress = response.getFromAddress();
        this.toAddress = response.getToAddress();
        this.requestTime = response.getRequestTime();
        this.receiveTime = response.getReceiveTime();
        this.handleEndTime = response.getHandleEndTime();
        this.handleStartTime = response.getHandleStartTime();
    }
}

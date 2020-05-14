package com.zust.yan.rpc.net.base;

import lombok.Data;

import java.util.List;

@Data
public class Response {
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
    public Response(){}

    public Response(Long requestId, Object data) {
        this.requestId = requestId;
        this.data = data;
    }
    public static Response makeHeartBeat(Long requestId){
        Response response=new Response(requestId,"PONG");
        response.setType(0);
        return response;
    }
}

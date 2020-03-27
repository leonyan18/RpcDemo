package com.zust.yan.rpc.net.base;

import lombok.Data;

@Data
public class Response {
    private Long requestId;
    private Object data;
    public Response(){}

    public Response(Long requestId, Object data) {
        this.requestId = requestId;
        this.data = data;
    }
}

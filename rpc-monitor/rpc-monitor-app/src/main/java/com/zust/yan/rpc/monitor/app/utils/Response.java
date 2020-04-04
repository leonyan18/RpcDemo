package com.zust.yan.rpc.monitor.app.utils;

import lombok.Data;

import java.io.Serializable;

@Data
public class Response<T> implements Serializable {
    Integer status;
    String msg;
    T data;

    public Response(Integer status, String msg, T data) {
        this.status = status;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Response<T> yes(T data) {
        return new Response<>(200, "success", data);
    }

    public static Response no(String msg) {
        return new Response<>(400, msg, null);
    }

}

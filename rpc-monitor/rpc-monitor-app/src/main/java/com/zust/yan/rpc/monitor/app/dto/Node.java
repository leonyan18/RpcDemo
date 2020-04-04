package com.zust.yan.rpc.monitor.app.dto;

import com.zust.yan.rpc.monitor.app.entity.RequestData;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;

@Data
public class Node implements Serializable {
    Integer x;
    Integer y;
    String method;
    String clazz;
    String fromAddress;
    String toAddress;
    String name;
    Long requestId;
    Long fromRequestId;

    public Node() {
    }

    public Node(RequestData requestData) {
        BeanUtils.copyProperties(requestData, this);
        this.name = requestData.getClazz() + "::" + requestData.getMethod();
    }

}
package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;

/**
 * @author yan
 */
@Data
public class RpcData {
    private String ip;
    private Long averageHandleTime;
    private Long averageNetTime;
    private Long callTime;
    private String method;
    private String clazz;
}

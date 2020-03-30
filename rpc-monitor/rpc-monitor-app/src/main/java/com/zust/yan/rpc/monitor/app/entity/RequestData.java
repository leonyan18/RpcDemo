package com.zust.yan.rpc.monitor.app.entity;

import lombok.Data;

import java.util.Date;

/**
 * @author yan
 */
@Data
public class RequestData {
    private Long requestId;
    private Long toRequestId;
    private Date requestTime;
    private Date receiveTime;
    private Date handleEndTime;
    private Date handleStartTime;
    private String clazz;
    private String method;
    private String parameters;
}

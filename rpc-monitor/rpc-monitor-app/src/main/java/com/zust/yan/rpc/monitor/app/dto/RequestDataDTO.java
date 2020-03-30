package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;

import java.util.Date;

@Data
public class RequestDataDTO {
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

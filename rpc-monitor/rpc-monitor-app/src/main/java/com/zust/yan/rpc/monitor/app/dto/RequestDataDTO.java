package com.zust.yan.rpc.monitor.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.util.Date;

@Data
public class RequestDataDTO {
    @JsonSerialize(using = ToStringSerializer.class)
    private Long requestId;
    private Long fromRequestId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date requestTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date receiveTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleEndTime;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date handleStartTime;
    private Long spendTime;
    private Long handleTime;
    private String clazz;
    private String method;
    private String parameters;
    private String fromAddress;
    private String toAddress;

    public void makeTime() {
        spendTime = receiveTime.getTime() - requestTime.getTime();
        handleTime = handleEndTime.getTime() - handleStartTime.getTime();
    }
}

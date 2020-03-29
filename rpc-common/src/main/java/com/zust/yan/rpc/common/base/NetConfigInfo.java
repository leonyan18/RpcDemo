package com.zust.yan.rpc.common.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetConfigInfo {
    private int configId;
    private String host;
    private int port;
    private String password;
    private int timeOut;
    private int retryTimes;
}

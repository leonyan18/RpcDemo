package com.zust.yan.rpc.net.base;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NetConfigInfo {
    private String address;
    private int port;
    private int timeOut;
    private int retryTimes;
}

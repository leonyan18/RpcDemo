package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;

/**
 * @author yan
 */
@Data
public class NetConfigInfoDTO {
    private String host;
    private Integer port;
    private Long handleTime;
    private Long callCount;

    public String getNetAddress() {
        return host + ":" + port;
    }
}

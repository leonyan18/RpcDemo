package com.zust.yan.rpc.monitor.app.dto;

import lombok.Data;

import java.util.List;

/**
 * @author yan
 */
@Data
public class ServiceData {
    List<NetConfigInfoDTO> producerList;
    /**
     * 服务提供者数量
     */
    private Long producerCount;
    /**
     * 累计调用数量
     */
    private Long callCount;
    /**
     * 平均调用时间
     */
    private Long handleTime;

    private String serviceName;
}

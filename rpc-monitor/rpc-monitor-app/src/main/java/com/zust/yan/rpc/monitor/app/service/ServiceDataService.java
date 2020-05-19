package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.ServiceData;

import java.util.List;

public interface ServiceDataService {
    /**
     * 获取所有服务地址
     * @return 服务网络地址
     */
    List<ServiceData> getAllServiceData();
}

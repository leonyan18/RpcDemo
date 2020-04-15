package com.zust.yan.rpc.register.service;

import com.zust.yan.rpc.common.base.NetConfigInfo;

import java.util.List;
import java.util.Map;

public interface RegisterService {
    List<NetConfigInfo> getServiceNetInfo(String clazz);

    Map<String, List<NetConfigInfo>> getAllServiceNetInfo();

    Map<String, List<NetConfigInfo>> getNeededServiceNetInfo();

    void registerService(String host, int port, String clazz);

    void removeResistedService(String host, int port, String clazz);

    void sync(Boolean redo);
}

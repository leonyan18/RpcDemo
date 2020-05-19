package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.monitor.app.dto.ServiceData;
import com.zust.yan.rpc.monitor.app.mapper.RequestDataMapper;
import com.zust.yan.rpc.monitor.app.mapping.NetConfigInfoMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yan
 */
@Service
public class ServiceDataServiceImpl implements ServiceDataService {
    @Autowired
    RequestDataMapper requestDataMapper;

    @Autowired
    NetConfigInfoMapping netConfigInfoMapping;

    @Override
    public List<ServiceData> getAllServiceData() {
        List<ServiceData> serviceDataList = new ArrayList<>();
        Map<String, List<NetConfigInfo>> map = RpcUtils.getProviderNetInfoMap();
        for (String serviceName : map.keySet()) {
            ServiceData serviceData = new ServiceData();
            serviceData.setServiceName(serviceName);
            List<NetConfigInfo> netConfigInfos = map.get(serviceName);
            serviceData.setProducerList(netConfigInfoMapping.toDTOs(netConfigInfos));
            serviceData.setCallCount(countServiceCallTime(serviceName));
            serviceData.setProducerCount((long) netConfigInfos.size());
            serviceDataList.add(serviceData);
        }
        return serviceDataList;
    }

    public Long countServiceCallTime(String serviceName) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("serviceName", serviceName);
        return requestDataMapper.count(map);
    }
}

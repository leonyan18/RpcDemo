package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.monitor.app.dto.NetConfigInfoDTO;
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
            List<NetConfigInfoDTO> netConfigInfos = netConfigInfoMapping.toDTOs(map.get(serviceName));
            setNetInfosHandleTime(serviceName, netConfigInfos);
            serviceData.setProducerList(netConfigInfos);
            serviceData.setHandleTime(getHandleTime(serviceName, null));
            serviceData.setCallCount(countServiceCallTime(serviceName, null));
            serviceData.setProducerCount((long) netConfigInfos.size());
            serviceDataList.add(serviceData);
        }
        return serviceDataList;
    }

    public Long countServiceCallTime(String serviceName, String address) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("serviceName", serviceName);
        if (address != null) {
            map.put("toAddress", address);
        }
        return requestDataMapper.count(map);
    }

    public Long getHandleTime(String serviceName, String address) {
        Map<String, Object> map = new HashMap<>(3);
        map.put("serviceName", serviceName);
        if (address != null) {
            map.put("toAddress", address);
        }
        return requestDataMapper.getAverageHandleTime(map);
    }

    public void setNetInfosHandleTime(String serviceName, List<NetConfigInfoDTO> netConfigInfos) {
        for (NetConfigInfoDTO n : netConfigInfos) {
            n.setHandleTime(getHandleTime(serviceName, n.getNetAddress()));
            n.setCallCount(countServiceCallTime(serviceName, n.getNetAddress()));
        }
    }
}

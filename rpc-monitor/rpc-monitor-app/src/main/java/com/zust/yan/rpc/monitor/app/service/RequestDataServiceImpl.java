package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.dto.RpcData;
import com.zust.yan.rpc.monitor.app.mapper.RequestDataMapper;
import com.zust.yan.rpc.monitor.app.mapping.RequestDataMapping;
import com.zust.yan.rpc.monitor.app.utils.PageInfo;
import com.zust.yan.rpc.monitor.app.utils.Paging;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class RequestDataServiceImpl implements RequestDataService {
    @Autowired
    private RequestDataMapping requestDataMapping;
    @Autowired
    RequestDataMapper requestDataMapper;


    @Override
    public Paging<RequestDataDTO> pagingRequestData(int pageNo, int pageSize, Map<String, Object> query) {
        log.info("pagingRequestData" + pageNo + " " + pageSize);
        PageInfo pageInfo = new PageInfo(pageSize, pageNo);
        query.put("pos", pageInfo.getIndex());
        query.put("pageSize", pageInfo.getPageSize());
        long count = requestDataMapper.count(query);
        if (count == 0) {
            return Paging.emptyPaging();
        }
        List<RequestDataDTO> dataDTOS = requestDataMapping.toDTOs(requestDataMapper.pagingRequestData(query));
        // 计算时间
        dataDTOS.forEach(RequestDataDTO::makeTime);
        return new Paging<>(count, dataDTOS);
    }

    @Override
    public void insertRequestData(RequestDataDTO requestData) {
        log.info("insertRequestData" + requestData);
        Map<String, Object> query = new HashMap<>();
        query.put("requestId", requestData.getRequestId());
        // 会有两次次请求
        if (requestDataMapper.count(query) == 0) {
            requestDataMapper.insertRequestData(requestDataMapping.toEntity(requestData));
        } else {
            // 判断是否是已完成的请求
            if (requestData.getReceiveTime() == null) {
                return;
            }
            requestDataMapper.updateRequestData(requestDataMapping.toEntity(requestData));
        }
    }

    @Override
    public List<RpcData> getTopSpendTimeIp() {
        Map<String, Object> map = new HashMap<>();
        map.put("spend", true);
        return requestDataMapper.getTopIp(map);
    }

    @Override
    public List<RpcData> getTopSpendTimeMethod() {
        Map<String, Object> map = new HashMap<>();
        map.put("spend", true);
        return requestDataMapper.getTopMethod(map);
    }

    @Override
    public List<RpcData> getTopCallTimeIp() {
        Map<String, Object> map = new HashMap<>();
        map.put("call", true);
        return requestDataMapper.getTopIp(map);
    }

    @Override
    public List<RpcData> getTopCallTimeMethod() {
        Map<String, Object> map = new HashMap<>();
        map.put("call", true);
        return requestDataMapper.getTopMethod(map);
    }

}

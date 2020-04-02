package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.mapper.RequestDataMapper;
import com.zust.yan.rpc.monitor.app.mapping.RequestDataMapping;
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
    public List<RequestDataDTO> pagingRequestData(int pageNo, int pageSize) {
        log.info("pagingRequestData" + pageNo + " " + pageSize);
        Map<String, Object> query = new HashMap<>(3);
        if (pageNo != 0 && pageSize != 0) {
            query.put("pos", (pageNo - 1) * pageSize);
            query.put("pageSize", pageSize);
        } else {
            query.put("pos", 0);
            query.put("pageSize", 10);
        }
        return requestDataMapping.toDTOs(requestDataMapper.pagingRequestData(query));
    }

    @Override
    public void insertRequestData(RequestDataDTO requestData) {
        log.info("insertRequestData" + requestData);
        requestDataMapper.insertRequestData(requestDataMapping.toEntity(requestData));
    }
}

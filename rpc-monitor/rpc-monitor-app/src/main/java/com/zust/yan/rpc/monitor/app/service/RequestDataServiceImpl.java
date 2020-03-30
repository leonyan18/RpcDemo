package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.mapper.RequestDataMapper;
import com.zust.yan.rpc.monitor.app.mapping.RequestDataMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class RequestDataServiceImpl implements RequestDataService {
    @Autowired
    private RequestDataMapping requestDataMapping;
    @Autowired
    RequestDataMapper requestDataMapper;

    @Override
    public List<RequestDataDTO> pagingRequestData(int pageNo, int pageSize) {
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
}

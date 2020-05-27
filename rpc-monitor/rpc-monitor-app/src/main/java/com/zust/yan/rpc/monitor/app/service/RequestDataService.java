package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.dto.RpcData;
import com.zust.yan.rpc.monitor.app.utils.Paging;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Map;

/**
 * @author yan
 */
public interface RequestDataService {
    Paging<RequestDataDTO> pagingRequestData(int pageNo, int pageSize, Map<String, Object> query);

    void insertRequestData(RequestDataDTO requestData);

    List<RpcData> getTopSpendTimeIp(String interval);

    List<RpcData> getTopSpendTimeMethod(String interval);

    List<RpcData> getTopCallTimeIp(String interval);

    List<RpcData> getTopCallTimeMethod(String interval);
}


package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.entity.RequestData;

import java.util.List;
import java.util.Map;

/**
 * @author yan
 */
public interface RequestDataService {
    List<RequestDataDTO> pagingRequestData(int pageNo, int pageSize);
}

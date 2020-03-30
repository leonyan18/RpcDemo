package com.zust.yan.rpc.monitor.app.mapper;

import com.zust.yan.rpc.monitor.app.entity.RequestData;

import java.util.List;
import java.util.Map;

public interface RequestDataMapper {
    /**
     * @param query 查询参数
     * @return 返回列表
     */
    List<RequestData> pagingRequestData(Map<String,Object> query);
}

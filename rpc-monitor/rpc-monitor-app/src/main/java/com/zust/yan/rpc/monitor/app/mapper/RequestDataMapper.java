package com.zust.yan.rpc.monitor.app.mapper;

import com.zust.yan.rpc.monitor.app.dto.RpcData;
import com.zust.yan.rpc.monitor.app.entity.RequestData;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface RequestDataMapper {
    /**
     * @param query 查询参数
     * @return 返回列表
     */
    List<RequestData> pagingRequestData(Map<String, Object> query);

    void insertRequestData(RequestData requestData);

    void updateRequestData(RequestData requestData);

    long count(Map<String, Object> query);

    List<RpcData> getTopIp(Map<String, Object> query);

    List<RpcData> getTopMethod(Map<String, Object> query);

    RequestData getRequestData(@Param("id") Long id);

    List<RequestData> getRequestDataFromId(@Param("id") Long id);

    long getAverageHandleTime(Map<String, Object> query);
}

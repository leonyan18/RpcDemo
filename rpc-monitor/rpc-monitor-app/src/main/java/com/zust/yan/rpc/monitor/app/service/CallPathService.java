package com.zust.yan.rpc.monitor.app.service;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.dto.RpcData;
import com.zust.yan.rpc.monitor.app.dto.RpcPath;
import com.zust.yan.rpc.monitor.app.utils.Paging;

import java.util.List;
import java.util.Map;

/**
 * @author yan
 */
public interface CallPathService {

    RpcPath getCallPath(Long requestId);
}

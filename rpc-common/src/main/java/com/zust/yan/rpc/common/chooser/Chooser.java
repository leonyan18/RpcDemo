package com.zust.yan.rpc.common.chooser;

import com.zust.yan.rpc.common.base.NetConfigInfo;

import java.util.List;

public interface Chooser {
    /**
     * 选择合适的网络地址
     *
     * @param netConfigInfos 网络地址列表
     * @return 网络地址
     */
    NetConfigInfo chooseNetConfigInfo(List<NetConfigInfo> netConfigInfos);
}

package com.zust.yan.rpc.common.chooser;

import com.zust.yan.rpc.common.base.NetConfigInfo;

import java.util.List;

public interface Chooser {
    NetConfigInfo chooseNetConfigInfo(List<NetConfigInfo> netConfigInfos);
}

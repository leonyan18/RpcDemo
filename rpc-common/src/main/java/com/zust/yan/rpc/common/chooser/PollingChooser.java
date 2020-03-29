package com.zust.yan.rpc.common.chooser;

import com.zust.yan.rpc.common.base.NetConfigInfo;

import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author yan
 */
public class PollingChooser implements Chooser {
    private static LongAdder index = new LongAdder();

    @Override
    public NetConfigInfo chooseNetConfigInfo(List<NetConfigInfo> netConfigInfos) {
        int pos = (int) index.longValue();
        // 考虑并发其实影响也不大
        if (pos > Integer.MAX_VALUE - 10) {
            index.reset();
        }
        NetConfigInfo netConfigInfo = netConfigInfos.get(pos % netConfigInfos.size());
        index.increment();
        return netConfigInfo;
    }
}

package com.zust.yan.rpc.common.chooser;

import com.zust.yan.rpc.common.base.NetConfigInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author yan
 */
public class PriorityPollingChooser implements Chooser {
    private static LongAdder index = new LongAdder();

    @Override
    public NetConfigInfo chooseNetConfigInfo(List<NetConfigInfo> netConfigInfos) {
        if (netConfigInfos == null || netConfigInfos.isEmpty()) {
            return null;
        }
        if (netConfigInfos.size() == 1) {
            return netConfigInfos.get(0);
        }
        int pos = index.intValue();
        // 考虑并发其实影响也不大
        if (pos > Integer.MAX_VALUE - 10) {
            index.reset();
        }
        int maxv = 0;
        // 取得最大值 最小值0 与最大值的差值就是要遍历的次数
        for (NetConfigInfo info : netConfigInfos) {
            maxv = Math.max(info.getFailTimes().intValue(), maxv);
        }
        // 当前全部遍历次数
        int times = 0;
        List<Integer> timeList = new ArrayList<>();
        for (NetConfigInfo info : netConfigInfos) {
            // 差值次数
            times += maxv - info.getFailTimes().intValue();
            timeList.add(times);
        }
        // 对pos进行处理保证不超过上界，并进行二分查找
        pos = binarySearch(timeList, pos % times);
        NetConfigInfo netConfigInfo = netConfigInfos.get(pos % netConfigInfos.size());
        index.increment();
        return netConfigInfo;
    }

    private int binarySearch(List<Integer> timeList, int pos) {
        int high = timeList.size() - 1;
        int low = 0;
        int mid = 0;
        // 取大于等于pos的第一个元素
        while (low < high) {
            mid = (high + low) >> 1;
            //小于的话不能取直接排除
            if (timeList.get(mid) < pos) {
                low = mid + 1;
            } else if (timeList.get(mid) > pos) {
                high = mid;
            } else {
                break;
            }
        }
        return mid;
    }
}

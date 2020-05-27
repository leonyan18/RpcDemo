package com.zust.yan.rpc.common.chooser;

/**
 * @author yan
 */
public interface ChooserBuilder {

    /**
     * 生成一个新的chooser
     *
     * @param name 类名 如果是监控选择name为monitor
     * @return 返回选择器
     */
    Chooser getChooser(String name);
}

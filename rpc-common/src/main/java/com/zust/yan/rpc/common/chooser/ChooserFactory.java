package com.zust.yan.rpc.common.chooser;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yan
 */
public class ChooserFactory {
    public static final String POLLING = "polling";
    public static final String PRIORITY = "priority";
    private static volatile ChooserBuilder chooserBuilder;
    private static Map<String, Chooser> map = new ConcurrentHashMap<>();

    public static Chooser getChooser(String strategy, String name) {
        Chooser chooser;
        // 如果已经构造好了
        if ((chooser = map.get(name)) != null) {
            return chooser;
        }
        // 生成一个选择器
        // 针对特殊定制 如果有专门针对某个类的选择器时也可以使用
        if (chooserBuilder != null) {
            chooser = chooserBuilder.getChooser(name);
        }
        if (chooser == null) {
            switch (strategy) {
                case POLLING:
                    chooser = new PollingChooser();
                    break;
                case PRIORITY:
                    chooser = new PriorityPollingChooser();
                    break;
                default:
                    chooser = new PollingChooser();
            }
        }

        map.put(name, chooser);
        return chooser;
    }

    public static void setChooserBuilder(ChooserBuilder builder) {
        chooserBuilder = builder;
    }
}

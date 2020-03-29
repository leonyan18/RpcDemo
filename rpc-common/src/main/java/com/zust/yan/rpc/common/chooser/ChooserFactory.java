package com.zust.yan.rpc.common.chooser;

/**
 * @author yan
 */
public class ChooserFactory {
    public static Chooser getChooser(String strategy) {
        Chooser chooser;
        switch (strategy) {
            case "polling":
                chooser = new PollingChooser();
                break;
            default:
                chooser = new PollingChooser();
        }
        return chooser;
    }
}

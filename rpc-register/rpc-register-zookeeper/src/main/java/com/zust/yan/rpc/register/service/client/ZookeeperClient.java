package com.zust.yan.rpc.register.service.client;

import com.zust.yan.rpc.common.utils.RpcUtils;
import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;

/**
 * @author yan
 */
public class ZookeeperClient {
    private static volatile CuratorFramework client = null;

    public static CuratorFramework getZookeeperClient() {
        if (client == null) {
            synchronized (ZookeeperClient.class) {
                if (client == null) {
                    RetryPolicy retryPolicy = new RetryNTimes(3, 5000);
                    client = CuratorFrameworkFactory.builder()
                            .connectString(RpcUtils.getRegisterInfo().getNetAddress())
                            .sessionTimeoutMs(10000).retryPolicy(retryPolicy)
                            .namespace("workspace").build();
                    client.start();
                }
            }
        }
        return client;
    }
}

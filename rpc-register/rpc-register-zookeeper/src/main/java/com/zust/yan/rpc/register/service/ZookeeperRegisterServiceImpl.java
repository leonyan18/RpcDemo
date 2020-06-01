package com.zust.yan.rpc.register.service;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.service.client.ZookeeperClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.TreeCache;
import org.apache.curator.framework.recipes.cache.TreeCacheEvent;
import org.apache.curator.framework.recipes.cache.TreeCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author yan
 */
@Slf4j
public class ZookeeperRegisterServiceImpl implements RegisterService, TreeCacheListener {
    private static String PRE_PATH = "/rpc";
    private static String CONSUMERS = "/consumers";
    private final static String SEP = "/";
    private final static String FIELD_SEP = ":";
    private final static String PATH_SEP = "/";
    private final static String CLASS_SEP = ".";
    private static final Executor executor = RpcUtils.getExecutor("ZookeeperRegister");
    private TreeCache childrenCache;

    @Override
    public List<NetConfigInfo> getServiceNetInfo(String clazz) {
        CuratorFramework client = ZookeeperClient.getZookeeperClient();
        List<NetConfigInfo> netConfigInfoList = new ArrayList<>();
        String path = getConsumersPath(clazz);
        try {
            List<String> children = client.getChildren().forPath(path);
            return toNetConfigInfos(children);
        } catch (KeeperException.NoNodeException e) {
            // 不影响功能
        } catch (Exception e) {
            e.printStackTrace();
        }
        return netConfigInfoList;
    }

    @Override
    public Map<String, List<NetConfigInfo>> getAllServiceNetInfo() {
        Map<String, List<NetConfigInfo>> map = new HashMap<>(20);
        CuratorFramework client = ZookeeperClient.getZookeeperClient();
        try {
            List<String> children = client.getChildren().forPath(PRE_PATH);
            for (String clazz : children) {
                map.put(clazz, getServiceNetInfo(clazz));
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Map<String, List<NetConfigInfo>> getNeededServiceNetInfo() {
        return null;
    }

    @Override
    public void registerService(String host, int port, String clazz) {
        CuratorFramework client = ZookeeperClient.getZookeeperClient();
        try {
            client.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.EPHEMERAL)
                    .forPath(getConsumerPath(clazz, host, port));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeResistedService(String host, int port, String clazz) {
        CuratorFramework client = ZookeeperClient.getZookeeperClient();
        try {
            client.delete()
                    .deletingChildrenIfNeeded()
                    .forPath(getConsumerPath(clazz, host, port));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void sync(Boolean redo) {
        Map<String, List<NetConfigInfo>> allServiceNetInfos = getAllServiceNetInfo();
        if (redo) {
            RpcUtils.setProviderNetInfoMap(allServiceNetInfos);
        } else {
            for (String s : allServiceNetInfos.keySet()) {
                for (NetConfigInfo netConfigInfo : allServiceNetInfos.get(s)) {
                    RpcUtils.addProviderNetInfo(s, netConfigInfo);
                }
            }
        }
        if (childrenCache == null) {
            childrenCache = new TreeCache(ZookeeperClient.getZookeeperClient(), PRE_PATH);
            try {
                childrenCache.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
            childrenCache.getListenable().addListener(this, executor);
        }
    }

    private static String getConsumersPath(String clazz) {
        return PRE_PATH + PATH_SEP + clazz + CONSUMERS;
    }

    private static String getConsumerPath(String clazz, String host, int port) {
        return getConsumersPath(clazz) + PATH_SEP + host + FIELD_SEP + port;
    }

    private static List<NetConfigInfo> toNetConfigInfos(List<String> result) {
        List<NetConfigInfo> netConfigInfoList = new ArrayList<NetConfigInfo>(5);
        for (String address : result) {
            String[] fields = address.split(FIELD_SEP);
            NetConfigInfo netConfigInfo = NetConfigInfo.builder()
                    .host(fields[0])
                    .port(Integer.parseInt(fields[1]))
                    .build();
            netConfigInfoList.add(netConfigInfo);
        }
        return netConfigInfoList;
    }

    @Override
    public void childEvent(CuratorFramework client, TreeCacheEvent event) throws Exception {
        ChildData childData = event.getData();
        log.info("childEvent:" + event);
        if (childData != null && !event.getType().equals(TreeCacheEvent.Type.INITIALIZED)) {
            String[] path = childData.getPath().split(PATH_SEP);
            if (TreeCacheEvent.Type.NODE_ADDED.equals(event.getType())) {
                if (path.length == 5) {
                    String[] fields = path[4].split(FIELD_SEP);
                    NetConfigInfo netConfigInfo = NetConfigInfo.builder()
                            .host(fields[0])
                            .port(Integer.parseInt(fields[1]))
                            .build();
                    RpcUtils.addProviderNetInfo(path[2], netConfigInfo);
                }
            } else if (TreeCacheEvent.Type.NODE_REMOVED.equals(event.getType())) {

                if (path.length == 5) {
                    RpcUtils.removeServiceNetInfo(path[2], path[4]);
                }
            }
        }
    }
}

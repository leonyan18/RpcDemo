package com.zust.yan.rpc.register.service;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.register.factory.JedisSingleTonFactory;
import redis.clients.jedis.Jedis;

import java.util.*;

public class RedisRegisterService implements RegisterService {
    private final static String HOST = "HOST";
    private final static String PORT = "PORT";
    private final static String CLAZZ = "CLAZZ";
    private final static String PRE_FLAG = "RegisterService";
    private final static String SEP = "\n";

    @Override
    public List<NetConfigInfo> getServiceNetInfo(String clazz) {
        Jedis jedis = JedisSingleTonFactory.getJedis();
        return getNetConfigInfos(jedis.smembers(PRE_FLAG + clazz));
    }

    @Override
    public Map<String, List<NetConfigInfo>> getAllServiceNetInfo() {
        Jedis jedis = JedisSingleTonFactory.getJedis();
        Set<String> clazzs = jedis.smembers(PRE_FLAG + CLAZZ);
        Map<String, List<NetConfigInfo>> netConfigInfoMap = new HashMap<String, List<NetConfigInfo>>(30);
        for (String clazz : clazzs) {
            netConfigInfoMap.put(clazz, getServiceNetInfo(clazz));
        }
        return netConfigInfoMap;
    }

    @Override
    public void registerService(String host, int port, String clazz) {
        Jedis jedis = JedisSingleTonFactory.getJedis();
        jedis.sadd(PRE_FLAG + CLAZZ, clazz);
        jedis.sadd(PRE_FLAG + clazz, makeString(host, port));
    }

    @Override
    public void removeResistedService(String host, int port, String clazz) {
        Jedis jedis = JedisSingleTonFactory.getJedis();
        jedis.srem(PRE_FLAG + CLAZZ, clazz);
        jedis.srem(PRE_FLAG + clazz, makeString(host, port));
    }

    @Override
    public void sync(Boolean redo) {
        if (redo) {
            RpcUtils.clearServiceNetInfo();
        }
        Map<String, List<NetConfigInfo>> allServiceNetInfos = getAllServiceNetInfo();
        for (String s : allServiceNetInfos.keySet()) {
            for (NetConfigInfo netConfigInfo : allServiceNetInfos.get(s)) {
                System.out.println(s);
                System.out.println(netConfigInfo);
                RpcUtils.addProviderNetInfo(s, netConfigInfo);
            }
        }
    }

    private List<NetConfigInfo> getNetConfigInfos(Set<String> netConfigInfos) {
        List<NetConfigInfo> netConfigInfoList = new ArrayList<NetConfigInfo>(5);
        for (String info : netConfigInfos) {
            String[] fields = info.split(SEP);
            NetConfigInfo netConfigInfo = NetConfigInfo.builder()
                    .host(fields[0])
                    .port(Integer.parseInt(fields[1]))
                    .build();
            netConfigInfoList.add(netConfigInfo);
        }
        return netConfigInfoList;
    }

    private String makeString(Object... strings) {
        StringBuilder ans = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i != 0) {
                ans.append(SEP);
            }
            ans.append(strings[i]);
        }
        return ans.toString();
    }
}
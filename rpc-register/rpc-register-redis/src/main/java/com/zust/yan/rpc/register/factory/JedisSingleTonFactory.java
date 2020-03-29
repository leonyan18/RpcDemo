package com.zust.yan.rpc.register.factory;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.Map;

/**
 * @author yan
 */
public class JedisSingleTonFactory {
    private volatile static Jedis jedis;
    private volatile static JedisPool pool;
    private static final String MAX_IDLE = "maxIdle";
    private static final String MAX_TOTAL = "maxTotal";

    public static Jedis getJedis() {
        if (jedis == null || jedis.isConnected()) {
            synchronized (JedisSingleTonFactory.class) {
                if (jedis == null || jedis.isConnected()) {
                    jedis = initJedis();
                }
            }
        }
        return jedis;
    }

    private static Jedis initJedis() {
        NetConfigInfo netConfigInfo = RpcUtils.getRegisterInfo();
        Map<String, Object> redisParameter = RpcUtils.getJedisPoolConfig();
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle((Integer) redisParameter.get(MAX_IDLE));
        config.setMaxTotal((Integer) redisParameter.get(MAX_TOTAL));
        pool = new JedisPool(config, netConfigInfo.getHost(), netConfigInfo.getPort(), netConfigInfo.getTimeOut(), netConfigInfo.getPassword());
        return pool.getResource();
    }
}

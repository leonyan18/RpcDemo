package com.zust.yan.rpc.spring.demo.service;

import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import org.springframework.stereotype.Component;

/**
 * @author yan
 */
@Component
@RpcServiceProvider
public class SadServiceImpl implements Sad {
    @RpcServiceConsumer
    Normal normal;

    @Override
    public String sad(String word) {
        return normal.doSad(word);
    }
}

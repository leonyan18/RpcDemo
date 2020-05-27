package com.zust.yan.rpc.spring.demo.service;

import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import org.springframework.stereotype.Component;

/**
 * @author yan
 */
@Component
public class HappyServiceImpl implements Happy {
    @RpcServiceConsumer(sync = false)
    Sad sadService;

    @Override
    public String happy(String word) {
        return sadService.sad(word);
    }
}

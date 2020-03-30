package com.zust.yan.rpc.spring.demo.service;

import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Component;

/**
 * @author yan
 */
@Component
public class HappyServiceImpl implements Happy {
    @RpcServiceConsumer
    Sad sadService;

    public String happy(String word) {
        return sadService.sad(word);
    }
}

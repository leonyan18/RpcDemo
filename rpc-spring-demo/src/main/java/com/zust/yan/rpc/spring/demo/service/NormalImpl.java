package com.zust.yan.rpc.spring.demo.service;

import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import org.springframework.stereotype.Component;

@Component
@RpcServiceProvider
public class NormalImpl implements Normal {
    @Override
    public String doSad(String word) {
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return word;
    }
}

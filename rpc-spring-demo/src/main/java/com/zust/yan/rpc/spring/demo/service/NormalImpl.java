package com.zust.yan.rpc.spring.demo.service;

import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import org.springframework.stereotype.Component;

@Component
@RpcServiceProvider
public class NormalImpl implements Normal {
    public String doSad(String word) {
        return word;
    }
}

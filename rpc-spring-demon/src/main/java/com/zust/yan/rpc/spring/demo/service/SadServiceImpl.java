package com.zust.yan.rpc.spring.demo.service;

import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import org.springframework.stereotype.Component;

@Component
@RpcServiceProvider
public class SadServiceImpl implements Sad {
    public String sad(String word) {
        return "okkkkkkkk";
    }
}

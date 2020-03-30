package com.zust.yan.rpc.spring.demo.config;

import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans({@ComponentScan("com.zust.yan.rpc.spring.demo")})
public class RootConfig {

}

package com.zust.yan.rpc.spring.demo.config;

import com.zust.yan.rpc.spring.config.RpcRootConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScans({@ComponentScan("com.zust.yan.rpc.spring.demo")})
@Import(RpcRootConfig.class)
public class RootConfig {
}

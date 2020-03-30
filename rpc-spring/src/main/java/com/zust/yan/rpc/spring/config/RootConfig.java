package com.zust.yan.rpc.spring.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScans({@ComponentScan(basePackages = "com.zust.yan.rpc.spring.config"),
        @ComponentScan(basePackages = "com.zust.yan.rpc.spring.listener")})
public class RootConfig {
}

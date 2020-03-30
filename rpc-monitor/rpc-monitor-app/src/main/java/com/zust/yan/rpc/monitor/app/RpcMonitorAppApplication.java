package com.zust.yan.rpc.monitor.app;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.zust.yan.rpc.monitor.app.mapper")
@SpringBootApplication
public class RpcMonitorAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcMonitorAppApplication.class, args);
    }

}

package com.zust.yan.rpc.monitor.app;

import com.zust.yan.rpc.monitor.app.service.RequestDataService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "com.zust.yan.rpc.monitor.app.mapper")
@SpringBootApplication
public class RpcMonitorAppApplication {
    @Autowired
    RequestDataService requestDataService;

    public static void main(String[] args) {
        SpringApplication.run(RpcMonitorAppApplication.class, args);
    }

}

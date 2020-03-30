package com.zust.yan.rpc.monitor.app;

import com.zust.yan.rpc.monitor.app.service.RequestDataService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class RpcMonitorAppApplicationTests {
    @Autowired
    RequestDataService requestDataService;

    @Test
    void contextLoads() {
    }

    @Test
    void testRequestDataService() {
        System.out.println(requestDataService.pagingRequestData(1,10));
    }

}

package com.zust.yan.rpc.monitor.app;

import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.mapper.RequestDataMapper;
import com.zust.yan.rpc.monitor.app.service.CallPathService;
import com.zust.yan.rpc.monitor.app.service.RequestDataService;
import com.zust.yan.rpc.monitor.app.utils.Paging;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class RpcMonitorAppApplicationTests {
    @Autowired
    RequestDataService requestDataService;

    @Autowired
    CallPathService callPathService;

    @Autowired
    RequestDataMapper requestDataMapper;

    @Test
    void contextLoads() {
    }

    @Test
    void testRequestDataService() {
        Map<String, Object> map = new HashMap<>();
        map.put("call",0);
        System.out.println(requestDataMapper.getTopIp(map));
        System.out.println(requestDataMapper.getTopMethod(map));
        Paging<RequestDataDTO> requestDataDTOS = requestDataService.pagingRequestData(1, 1000, map);
        for (RequestDataDTO r : requestDataDTOS.getData()) {
            if (r.getRequestTime() != null && r.getReceiveTime() != null)
                System.out.println(r.getRequestTime().getTime() + "   " + r.getReceiveTime().getTime());
        }
    }
    @Test
    void testCallPath(){
        System.out.println(callPathService.getCallPath(158600663478910005L));
    }

}

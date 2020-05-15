package com.zust.yan.rpc.spring.demo;

import com.zust.yan.rpc.app.manage.ClientManager;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.DefaultFuture;
import com.zust.yan.rpc.net.monitor.utils.MonitorClientUtils;
import com.zust.yan.rpc.net.utils.RpcFutureUtils;
import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import com.zust.yan.rpc.spring.demo.config.RootConfig;
import com.zust.yan.rpc.spring.demo.service.Happy;
import com.zust.yan.rpc.spring.listener.ServiceExportListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.management.monitor.Monitor;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@Slf4j
public class RpcSpringTest {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    Happy happy;

    @Autowired
    ServiceExportListener listener;
    @Test
    public void testRpcSpring() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            System.out.println("result=="+happy.happy("testtest"+i));
        }
        List<DefaultFuture> defaultFutures=RpcFutureUtils.getFutures();
        for (DefaultFuture f:defaultFutures) {
            System.out.println("resultFuture=="+f.getResBlock().getData());
        }
        Thread.sleep(5000);
        listener.close();
        ClientManager.closeAll();
        MonitorClientUtils.closeAll();
    }
}

package com.zust.yan.rpc.spring.demo;

import com.zust.yan.rpc.app.manage.ClientManager;
import com.zust.yan.rpc.common.chooser.PollingChooser;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.DefaultFuture;
import com.zust.yan.rpc.net.monitor.utils.MonitorClientUtils;
import com.zust.yan.rpc.net.utils.RpcFutureUtils;
import com.zust.yan.rpc.spring.demo.config.RootConfig;
import com.zust.yan.rpc.spring.demo.service.Happy;
import com.zust.yan.rpc.spring.demo.service.Sad;
import com.zust.yan.rpc.spring.listener.ServiceExportListener;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
    Sad sad;

    @Autowired
    ServiceExportListener listener;

    @Test
    public void testRpcSpring() throws InterruptedException {
        RpcUtils.setChooserBuilder(name -> new PollingChooser());
        System.out.println("cpu核心数:" + Runtime.getRuntime().availableProcessors());
        long startTime = System.currentTimeMillis();
        long maxVal = 0;
        for (int i = 0; i < 1; i++) {
            long requestStartTime = System.currentTimeMillis();
            System.out.println("result==" + happy.happy("testtest" + i));
            maxVal = Math.max(maxVal, (System.currentTimeMillis() - requestStartTime));
        }
        System.out.println("建立连接并发送请求最长花费时间  " + maxVal);
        maxVal=0;
        for (int i = 0; i < 10; i++) {
            long requestStartTime = System.currentTimeMillis();
            System.out.println("result==" + happy.happy("testtest" + i));
            maxVal = Math.max(maxVal, (System.currentTimeMillis() - requestStartTime));
        }
        List<DefaultFuture> defaultFutures = RpcFutureUtils.getFutures();
        for (DefaultFuture f : defaultFutures) {
            System.out.println("resultId==" + f.getRequest().getRequestId());
            System.out.println("resultFuture==" + f.getResBlock());
        }
        System.out.println("请求最长花费时间  " + maxVal);
        System.out.println("花费时间  " + (System.currentTimeMillis() - startTime));
        Thread.sleep(50000);
        listener.close();
        ClientManager.closeAll();
        MonitorClientUtils.closeAll();
    }
}

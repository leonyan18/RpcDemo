package com.zust.yan.rpc.spring.demo;

import com.zust.yan.rpc.spring.annotation.RpcServiceConsumer;
import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import com.zust.yan.rpc.spring.demo.config.RootConfig;
import com.zust.yan.rpc.spring.demo.service.Happy;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.ILoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RootConfig.class)
@Slf4j
public class RpcSpringTest {
    @Autowired
    ApplicationContext applicationContext;
    @Autowired
    Happy happy;
    @Test
    public void testRpcSpring(){
        System.out.println("+++++++++++++++++++++++");
        System.out.println(applicationContext.getBeansWithAnnotation(RpcServiceProvider.class));
        System.out.println("+++++++++++++++++++++++");
        System.out.println(happy.happy("test"));
    }
}

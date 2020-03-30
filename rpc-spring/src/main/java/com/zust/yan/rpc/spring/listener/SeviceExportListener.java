package com.zust.yan.rpc.spring.listener;

import com.zust.yan.rpc.app.handler.DefaultServerMessageHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Server;
import com.zust.yan.rpc.register.service.RegisterService;
import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import com.zust.yan.rpc.spring.factory.RegisterServiceFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class SeviceExportListener implements ApplicationListener<ContextRefreshedEvent> {

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> objectMap = applicationContext.getBeansWithAnnotation(RpcServiceProvider.class);
        DefaultServerMessageHandler.addHandlers(objectMap);
        NetConfigInfo netConfigInfo = RpcUtils.getLocalServerNetInfo();
        Server server = new Server(netConfigInfo, DefaultServerMessageHandler::new);
        server.start();
        RegisterService registerService = RegisterServiceFactory.getRegisterService();
        // 注册服务
        objectMap.forEach((key, value) ->
            registerService.registerService(netConfigInfo.getHost(), netConfigInfo.getPort(),
                    value.getClass().getName()));
    }
}

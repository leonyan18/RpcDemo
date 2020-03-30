package com.zust.yan.rpc.spring.listener;

import com.zust.yan.rpc.app.handler.DefaultServerMessageHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Server;
import com.zust.yan.rpc.register.service.RegisterService;
import com.zust.yan.rpc.spring.annotation.RpcServiceProvider;
import com.zust.yan.rpc.spring.factory.RegisterServiceFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;
import java.util.Map;
import java.util.concurrent.Executor;

/**
 * @author yan
 */
@Component
@Slf4j
public class ServiceExportListener implements ApplicationListener<ContextRefreshedEvent> {
    Executor executor = RpcUtils.getExecutor("ServiceExport");

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        log.info("ServiceExportListener start");
        ApplicationContext applicationContext = event.getApplicationContext();
        Map<String, Object> objectMap = applicationContext.getBeansWithAnnotation(RpcServiceProvider.class);
        DefaultServerMessageHandler.addHandlers(objectMap);
        NetConfigInfo netConfigInfo = RpcUtils.getLocalServerNetInfo();
        Server server = new Server(netConfigInfo, DefaultServerMessageHandler::new);
        executor.execute(() -> server.start());
        RegisterService registerService = RegisterServiceFactory.getRegisterService();
        // 注册服务
        objectMap.forEach((key, value) -> {
                    for (Type t : value.getClass().getGenericInterfaces()) {
                        registerService.registerService(netConfigInfo.getHost(), netConfigInfo.getPort(),
                                t.getTypeName());
                    }

                }
        );
        // 同步服务
        registerService.sync(true);
        log.info("ServiceExportListener end");
    }
}

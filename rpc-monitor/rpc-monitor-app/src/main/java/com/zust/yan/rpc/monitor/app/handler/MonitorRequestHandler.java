package com.zust.yan.rpc.monitor.app.handler;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.monitor.app.dto.RequestDataDTO;
import com.zust.yan.rpc.monitor.app.executor.DefaultExecutor;
import com.zust.yan.rpc.monitor.app.mapping.RequestDataMapping;
import com.zust.yan.rpc.monitor.app.service.RequestDataService;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.RequestMethodInfo;
import com.zust.yan.rpc.net.monitor.server.MonitorServer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

/**
 * @author yan
 */
@Component
@Slf4j
public class MonitorRequestHandler implements ApplicationListener<ContextRefreshedEvent> {
    private BlockingQueue<Request> blockingQueue = new LinkedBlockingQueue<>();
    private MonitorServer monitorServer;
    @Autowired
    private RequestDataService requestDataService;
    @Autowired
    RequestDataMapping requestDataMapping;
    @Value("${rpc.monitor.port}")
    private int localPort;
    public void startListen() throws CertificateException, SSLException {
        NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(localPort)
                .build();
        monitorServer = new MonitorServer(RpcUtils.getLocalServerNetInfo(), blockingQueue);
        monitorServer.start();
        DefaultExecutor.submit(() -> {
            while (true) {
                try {
                    log.info("handle msg");
                    Request request = blockingQueue.take();
                    RequestDataDTO requestData = requestDataMapping.toDTO(request);
                    RequestMethodInfo requestMethodInfo = (RequestMethodInfo) request.getData();
                    requestData.setClazz(requestMethodInfo.getClassName());
                    requestData.setMethod(requestMethodInfo.getMethodName());
                    requestData.setParameters(Arrays.stream(requestMethodInfo.getParameterTypes())
                            .map(Class::getName)
                            .collect(Collectors.joining(",")));
                    requestDataService.insertRequestData(requestData);
                } catch (Exception e) {
                    e.printStackTrace();
                    log.info("blockingQueue.take() error", e.getMessage());
                }
            }
        });
    }


    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            startListen();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (SSLException e) {
            e.printStackTrace();
        }
    }
}

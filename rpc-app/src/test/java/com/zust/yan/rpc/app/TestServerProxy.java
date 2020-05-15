package com.zust.yan.rpc.app;

import com.zust.yan.rpc.app.factory.BeanProxyFactory;
import com.zust.yan.rpc.app.handler.DefaultServerMessageHandler;
import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.common.utils.RpcUtils;
import com.zust.yan.rpc.net.base.Server;
import com.zust.yan.rpc.register.service.RedisRegisterServiceImpl;
import com.zust.yan.rpc.register.service.RegisterService;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;

public class TestServerProxy {
    public static void main(String[] args) throws CertificateException, SSLException {
        NetConfigInfo monitorInfo = NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8886)
                .build();
        NetConfigInfo local = RpcUtils.getLocalServerNetInfo();
        RegisterService registerService = new RedisRegisterServiceImpl();
        registerService.registerService(local.getHost(), local.getPort(), Happy.class.getName());
        registerService.registerService(local.getHost(), local.getPort(), Sad.class.getName());
        RpcUtils.addMonitorInfo(monitorInfo);
        DefaultServerMessageHandler.addHandler((Happy) word -> "okkkkkkkk");
        Happy happy = BeanProxyFactory.createProxy(Happy.class,true);
        DefaultServerMessageHandler.addHandler((Sad) word -> happy.happy("dssd"));
        Server server = new Server(RpcUtils.getLocalServerNetInfo(), DefaultServerMessageHandler::new);
        registerService.sync(true);
        server.start();
    }
}

package com.zust.yan.rpc.net.monitor.server;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Request;
import com.zust.yan.rpc.net.base.Server;
import com.zust.yan.rpc.net.monitor.factory.DefaultServerMessageHandlerFactory;
import lombok.extern.slf4j.Slf4j;

import javax.net.ssl.SSLException;
import java.security.cert.CertificateException;
import java.util.concurrent.BlockingQueue;

/**
 * @author yan
 */
@Slf4j
public class MonitorServer {
    private Server server;

    public MonitorServer(NetConfigInfo info, BlockingQueue<Request> queue) {
        server = new Server(info, new DefaultServerMessageHandlerFactory(queue));
    }

    public void start() throws CertificateException, SSLException {
        server.start();
    }

    public void close() {
        server.close();
    }
}

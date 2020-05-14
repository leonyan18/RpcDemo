package com.zust.yan.rpc.net.utils;

import io.netty.handler.ssl.SslContext;

/**
 * @author yan
 */
public class RpcSslContextUtils {
    private static SslContext clientSslContext = null;
    private static SslContext serverSslContext = null;

    public static SslContext getClientSslContext() {
        return clientSslContext;
    }

    public static void setClientSslContext(SslContext clientSslContext) {
        RpcSslContextUtils.clientSslContext = clientSslContext;
    }

    public static SslContext getServerSslContext() {
        return serverSslContext;
    }

    public static void setServerSslContext(SslContext serverSslContext) {
        RpcSslContextUtils.serverSslContext = serverSslContext;
    }
}

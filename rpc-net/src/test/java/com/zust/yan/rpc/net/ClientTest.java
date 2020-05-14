package com.zust.yan.rpc.net;

import com.zust.yan.rpc.common.base.NetConfigInfo;
import com.zust.yan.rpc.net.base.Client;
import com.zust.yan.rpc.net.base.DefaultFuture;
import com.zust.yan.rpc.net.base.Request;

import javax.net.ssl.SSLException;

public class ClientTest {
    public static void main(String[] args) throws InterruptedException, SSLException {
        NetConfigInfo info = NetConfigInfo.builder()
                .host("127.0.0.1")
                .port(8888)
                .build();
        Client client = new Client(info);
        client.start();
        Bean bean = new Bean();
        bean.setA("dsa");
        bean.setB("Sda");
        DefaultFuture defaultFuture = client.send(new Request(bean));
        System.out.println(defaultFuture.getResBlock());
//        client.close();
    }
}

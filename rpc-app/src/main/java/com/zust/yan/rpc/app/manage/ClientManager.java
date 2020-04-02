package com.zust.yan.rpc.app.manage;

import com.zust.yan.rpc.net.base.Client;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yan
 */
public class ClientManager {
    private static List<Client> clients = new ArrayList<>();

    public static void addClient(Client client) {
        clients.add(client);
    }

    public static void closeAll() throws InterruptedException {
        for (Client c :clients) {
            if(c==null){
                continue;
            }
            if(c.isClosed()){
                continue;
            }
            c.close();
        }
    }
}

package com.rpc;

import com.rpc.client.RPCClient;
import com.rpc.server.HelloService;
import com.rpc.server.HelloServiceImpl;
import com.rpc.server.Server;
import com.rpc.server.ServiceCenter;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by lcc on 2018/7/6.
 */
public class ServerTest {

    public static void main(String[] args) throws IOException {
        new Thread(new Runnable() {
            public void run() {
                try {
                    Server serviceServer = new ServiceCenter(8078);
                    serviceServer.register(HelloService.class, HelloServiceImpl.class);
                    serviceServer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


}

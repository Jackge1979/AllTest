package com.rpc;

/**
 * Created by lcc on 2018/7/6.
 */

import com.rpc.client.RPCClient;
import com.rpc.server.HelloService;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;


public class ClientTest {
    public static void main(String[] args) throws IOException {
        HelloService service = RPCClient
                .getRemoteProxyObj(HelloService.class, new InetSocketAddress("localhost", 8078));

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true){
            String line = br.readLine();

            System.out.println(service.sayHi(line));
        }


    }


}

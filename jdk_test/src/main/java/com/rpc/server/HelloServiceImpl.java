package com.rpc.server;

import com.rpc.server.HelloService;

/**
 * Created by lcc on 2018/7/6.
 */
public class HelloServiceImpl implements HelloService {


    public String sayHi(String name) {
        return "Hi, " + name;
    }


}

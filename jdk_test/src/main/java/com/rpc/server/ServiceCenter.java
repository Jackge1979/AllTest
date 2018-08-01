package com.rpc.server;

/**
 * Created by lcc on 2018/7/6.
 */

import com.rpc.server.Server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class ServiceCenter implements Server {

    private  ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private  final HashMap<String, Class> serviceRegistry = new HashMap<String, Class>();


    private  boolean isRunning = false;


    private  int port;


    public ServiceCenter(int port) {
        this.port = port;
    }


    public void stop() {
        System.out.println("stop server");
        isRunning = false;
        executor.shutdown();
    }


    public void start() throws IOException {
        ServerSocket server = new ServerSocket();
        server.bind(new InetSocketAddress(port));
        System.out.println("start server");
        try {
            while (true) {
                // 1.监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行
                System.out.println("监听客户端的TCP连接，接到TCP连接后将其封装成task，由线程池执行");
                Socket client = server.accept();
                executor.execute(new ServiceTask(client,serviceRegistry));
            }
        } finally {
            server.close();
        }
    }


    public void register(Class serviceInterface, Class impl) {
        System.out.println(serviceInterface.getName().toString() +" 注册 "+impl.toString());
        serviceRegistry.put(serviceInterface.getName(), impl);
    }


    public boolean isRunning() {
        System.out.println( "正在运行" );
        return isRunning;
    }


    public int getPort() {
        return port;
    }





}


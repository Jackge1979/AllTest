package com.rpc.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.HashMap;

/**
 * Created by lcc on 2018/7/6.
 */
public  class ServiceTask implements Runnable {

    HashMap<String, Class>  serviceRegistry;
    Socket clent = null;

    public ServiceTask(Socket client,HashMap<String, Class>  serviceRegistry) {
        this.clent = client;
        this.serviceRegistry = serviceRegistry;
    }


    public void run() {
        ObjectInputStream input = null;
        ObjectOutputStream output = null;
        try {
            // 2.将客户端发送的码流反序列化成对象，反射调用服务实现者，获取执行结果
            input = new ObjectInputStream(clent.getInputStream());
            System.out.println("ServiceCenter=>"+input);
            String serviceName = input.readUTF();
            String methodName = input.readUTF();
            Class<?>[] parameterTypes = (Class<?>[]) input.readObject();
            Object[] arguments = (Object[]) input.readObject();
            Class serviceClass = serviceRegistry.get(serviceName);
            if (serviceClass == null) {
                throw new ClassNotFoundException(serviceName + " not found");
            }
            Method method = serviceClass.getMethod(methodName, parameterTypes);
            Object result = method.invoke(serviceClass.newInstance(), arguments);


            // 3.将执行结果反序列化，通过socket发送给客户端
            output = new ObjectOutputStream(clent.getOutputStream());
            output.writeObject(result);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (clent != null) {
                try {
                    clent.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }
}
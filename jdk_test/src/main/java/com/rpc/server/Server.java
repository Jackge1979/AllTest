package com.rpc.server;

/**
 * Created by lcc on 2018/7/6.
 */
import java.io.IOException;


public interface Server {
    public void stop();


    public void start() throws IOException;


    public void register(Class serviceInterface, Class impl);


    public boolean isRunning();


    public int getPort();
}

package com.com.Proxy.demo1;

/**
 * Created by lcc on 2018/9/18.
 */
//测试   （我就打了个电话去订票）
public class ProxyTest {
    public static void main(String[] args) {
        ICalc target = new CalcImpl();
        ICalc calcproxy =  (ICalc) CalcLoggingProxy.factory1(target);
        int result =  calcproxy.add(2, 4);
        System.out.println(result);
    }
}


package com.com.Proxy.demo1;

/**
 * Created by lcc on 2018/9/18.
 */
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

public class CalcLoggingProxy  implements InvocationHandler {
    //被代理对象
    private ICalc target;
    public CalcLoggingProxy(){}
    public CalcLoggingProxy(ICalc obj){
        target = obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args)throws Throwable {
        String methodName = method.getName();
        //日志
        System.out.println("invoke...before---> "+methodName+"---"+Arrays.asList(args));
        //执行方法
        Object  result = method.invoke(target,args);
        //日志
        System.out.println("invoke...after---->"+result);
        return result;
    }

    public static Object factory1(ICalc target)
    {
        //获取被代理对象的Class
        Class cls = target.getClass();
        //代理对象由哪一个类加载器负责
        ClassLoader loader = cls.getClassLoader();
        //代理对象的类型，即其中有哪些方法
        Class [] interfaces = new Class[]{ICalc.class};
        //当调用代理对象的方法时执行该代码--->先给代理对象赋值--->自动调用invoke();
        InvocationHandler handler = new CalcLoggingProxy(target);
        //返回代理对象的实例
        return Proxy.newProxyInstance(loader,interfaces,handler);
    }

}
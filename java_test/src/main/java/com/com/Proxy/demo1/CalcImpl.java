package com.com.Proxy.demo1;

/**
 * Created by lcc on 2018/9/18.
 */
public class CalcImpl implements ICalc {

    @Override
    public int add(int i,int j) {
        int result = i + j;
        return result;
    }
}
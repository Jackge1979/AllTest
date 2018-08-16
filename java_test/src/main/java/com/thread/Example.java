package com.thread;

/**
 * Created by lcc on 2018/8/13.
 */
public class Example {

    public static void main(String[] args){

        Counter counter = new Counter();

        Thread  threadA = new CounterThread(counter);

        Thread  threadB = new CounterThread(counter);

        threadA.start();
        threadB.start();


        Counter counterA = new Counter();
        Counter counterB = new Counter();
        Thread  threadAA = new CounterThread(counterA);
        Thread  threadBB = new CounterThread(counterB);
        threadAA.start();
        threadBB.start();

    }

}

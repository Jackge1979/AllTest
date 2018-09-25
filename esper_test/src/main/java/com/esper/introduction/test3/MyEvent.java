package com.esper.introduction.test3;

/**
 * Created by lcc on 2018/9/21.
 */
public class MyEvent {

    private int id;
    private String name;

    public MyEvent(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
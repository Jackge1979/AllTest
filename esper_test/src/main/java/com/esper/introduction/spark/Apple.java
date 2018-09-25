package com.esper.introduction.spark;

import java.io.Serializable;

/**
 * Created by lcc on 2018/9/21.
 */
public class  Apple  implements Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private int price;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }
}
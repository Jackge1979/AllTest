package com.secbro.drools.model;

/**
 * Created by lcc on 2018/9/27.
 */
public class Product {

    public static final String DIAMOND = "DIAMOND"; // 钻石

    public static final String GOLD = "GOLD"; // 黄金

    private String type;

    private int discount;

    public static String getDIAMOND() {
        return DIAMOND;
    }

    public static String getGOLD() {
        return GOLD;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}

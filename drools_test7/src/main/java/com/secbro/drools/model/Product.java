package com.secbro.drools.model;

import lombok.Data;

/**
 * Created by lcc on 2018/9/27.
 */
@Data
public class Product {
    public static final String DIAMOND = "DIAMOND"; // 钻石
    public static final String GOLD = "GOLD"; // 黄金
    private String type;
    private int discount;
}

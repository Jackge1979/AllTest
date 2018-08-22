package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by baisong on 17/8/21.
 */
@Data
public class HiveMeta implements Serializable {

    //数据库
    private String database;

    //表
    private String table;

    //列
    private String column;
}

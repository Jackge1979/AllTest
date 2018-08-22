package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description: 元数据请求返回实体
 *
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 上午11:19.
 * Update Date Time:
 */
@Data
public class MetaRes implements Serializable {
    /**
     * 数据库
     */
    private String database;

    /**
     * 表
     */
    private String table;

    /**
     * 列
     */
    private String column;
}

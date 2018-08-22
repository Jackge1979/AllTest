package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *  元数据字段信息
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 上午11:48.
 * Update Date Time:
 */
@Data
public class MetaColumn implements Serializable {
    /**
     * 字段名
     */
    private String name;
    /**
     * 字段类型
     */
    private String type;
    /**
     * 字段描述
     */
    private String comment;

    /** 字段顺序*/
    private String index;

    /** 是否是分区字段 0: 普通字段 1：分区字段*/
    private Integer isPartitionField;
    /**
     * 实体ID
     */
    private Integer entityId;
    /**
     * columnId
     */
    private Integer id;
}

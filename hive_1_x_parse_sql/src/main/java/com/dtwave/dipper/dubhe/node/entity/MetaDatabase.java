package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *  数据库元数据信息
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 下午2:32.
 * Update Date Time:
 */
@Data
public class MetaDatabase implements Serializable {
    /**
     * 拥有者类型
     */
    private String ownerType;
    /**
     * 拥有者名称
     */
    private String ownerName;
    /**
     * 库存储地址
     */
    private String dbLocationUri;
    /**
     * 库描述
     */
    private String desc;
    /**
     * 库名称
     */
    private String schemaName;
    /**
     * 库大小
     */
    private long totalSize;
    /**
     * gp,pg下的schema Name
     */
    private String secondSchemaName;

    /**
     * gp,pg下的schema ID
     */
    private Integer secondSchemaId;

    /**
     * gp,pg下的owner信息
     */
    private String secondSchemaOwner;
    /**
     * 外部数据库id
     */
    private Integer outerSchemaId;
    /**
     * 数据库id
     */
    private Integer schemaId;
}

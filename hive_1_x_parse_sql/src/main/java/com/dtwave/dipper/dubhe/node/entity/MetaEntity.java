package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Description:
 * 元数据表信息
 *
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 上午11:44.
 * Update Date Time:
 */
@Data
public class MetaEntity implements Serializable {
    private Integer id;
    private String schemaName;
    private String entityName;
    private String owner;
    private String createTime;
    private String tableType;
    private long totalSize;
    private String dataModifyTime;
    private String ddlModifyTime;
    private String isPartition;
    private String comment;
    private Integer outerSchemaId;
    private Integer schemaId;
    private Integer entityId;
    private Integer outerEntityId;
    private Integer sdId;
    /**
     * 最后表结构更新时间
     */
    private String lastModifyTime;
    /**
     * gp,pg下的schema Name
     */
    private String secondSchemaName;

    /**
     * gp,pg下的schema ID
     */
    private Integer secondSchemaId;
    /**
     * 字段信息
     */
    List<? extends MetaColumn> metaColumnList;
}

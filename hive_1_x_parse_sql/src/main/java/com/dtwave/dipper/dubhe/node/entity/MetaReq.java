package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *  元数据信息获取
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 上午10:43.
 * Update Date Time:
 */
@Data
public class MetaReq<T> extends RmdbReq<T> {
    /**
     * 多个数据库名称
     */
    private List<String> dbNameList;

    /**
     * 实体名称
     */
    private String entityName;
    /**
     * gp schemaName
     */
    private String secondSchemaName;

    /**
     * 实体ID
     */
    private Integer entityId;
    /**
     * 实体ID列表
     */
    private List<Integer> entityIdList;

    /**
     * 数据库id列表
     */
    private List<Integer> schemaIdList;

}

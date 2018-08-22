package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * Author: plzhao
 * Date: created on 2018-01-12 10:43:41
 **/
@Data
public class LineageDto {
    private String schemaName;
    private String sql;
    private String param;

    /**
     * 数据库类型
     */
    private String storageType;
}

package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * 字段血缘关系顶点
 */
@Data
public class Vertices{
    /**顶点数字ID*/
    Integer id;
    /**顶点类型：COLUMN*/
    String vertexType;
    /**顶点唯一标识ID：库.表.字段 例如：default.source.source_key*/
    String vertexId;
}

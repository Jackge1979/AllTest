package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.List;

/**
 * 字段血缘关系边
 */
@Data
public class Edge{
    /**边source顶点ID*/
    List<Integer> sources;
    /**边sink顶点ID*/
    List<Integer> targets;
    /**边类型： 例如PROJECTION*/
    String edgeType;
}
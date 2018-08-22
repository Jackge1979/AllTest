package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: plzhao
 * Date: created on 2018-01-12 10:43:41
 **/
@Data
public class LineageBackDto implements Serializable{
    private String sql;
    private String lineage;
}

package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.TreeSet;

/**
 * Author: plzhao
 * Date: created on 2018-01-12 10:31:20
 **/
@Data
public class LineageResultDto {
    private String sql;
    private TreeSet<String> input;
    private TreeSet<String> output;
}

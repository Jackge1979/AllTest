package com.sql.parse.entity.entity;

import java.util.TreeSet;

/**
 * Author: plzhao
 * Date: created on 2018-01-12 10:31:20
 **/
public class LineageResultDto {
    private String sql;
    private TreeSet<String> input;
    private TreeSet<String> output;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public TreeSet<String> getInput() {
        return input;
    }

    public void setInput(TreeSet<String> input) {
        this.input = input;
    }

    public TreeSet<String> getOutput() {
        return output;
    }

    public void setOutput(TreeSet<String> output) {
        this.output = output;
    }
}

package com.sql.parse.entity;

import java.util.TreeSet;

/**
 * Created by lcc on 2018/8/1.
 * 存储sql解析的输入输出表
 */
public class Tables {

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

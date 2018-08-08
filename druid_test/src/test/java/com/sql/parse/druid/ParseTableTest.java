package com.sql.parse.druid;

/**
 * Created by lcc on 2018/8/1.
 */
public class ParseTableTest {

    public static void main(String[] args){
        String sql = "create table t1 (id int);";
        ParseTable.parseSqlTable(sql,"MYSQL");
    }
}

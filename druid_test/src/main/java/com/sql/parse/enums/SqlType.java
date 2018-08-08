package com.sql.parse.enums;

/**
 * Created by lcc on 2018/8/1.
 *
 * sql语句的类型
 */
public enum SqlType {

    HIVE(Integer.valueOf(1), "Hive"),
    MYSQL(Integer.valueOf(2), "Mysql"),
    GREENPLUM(Integer.valueOf(3), "Greenplum"),
    PRESTO(Integer.valueOf(4), "Presto"),
    LIBRA(Integer.valueOf(6), "LibrA");

    private Integer storageType;
    private String storageTypeCn;

    private SqlType(Integer storageType, String storageTypeCn) {
        this.storageType = storageType;
        this.storageTypeCn = storageTypeCn;
    }

    public Integer getStorageType() {
        return this.storageType;
    }

    public String getStorageTypeCn() {
        return this.storageTypeCn;
    }


}
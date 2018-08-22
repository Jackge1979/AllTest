package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * Hive函数 查询与校验
 *
 *  根据schemaName查询function列表。
 *  根据schemaName.function查询函数是否存在。
 *
 * @author hulb
 * @date 2017/12/4 下午2:34
 */
@Data
public class HiveFunction extends MetaFunction {
    private Integer functionId;
    private String className;

    //private String createTime;
    //private String[] resourceUris;
}

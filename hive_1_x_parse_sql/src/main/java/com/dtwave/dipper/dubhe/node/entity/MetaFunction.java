package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *  函数元数据信息
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 下午2:44.
 * Update Date Time:
 */
@Data
public class MetaFunction implements Serializable {
    /**
     * 函数名
     */
    private String functionName;
    /**
     * 数据库名
     */
    private String schemaName;
}

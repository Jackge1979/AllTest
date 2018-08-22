package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.List;

/**
 * Description:
 *  rmdb请求信息
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/29 下午2:55.
 * Update Date Time:
 */
@Data
public class RmdbReq<T> extends ConnectReq {
    /**
     * 请求返回的数据类型
     */
    Class<T> clazz;
    /**
     * 执行sql脚本
     */
    private String executeSql;

    /**
     * 执行参数
     */
    private List<Object> paramList;
}

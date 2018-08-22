package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * 封装连接请求的参数类,为其他dto类的基类
 * Created by gaowenxiao on 17/7/18.
 */
@Data
public class ConnectReq implements Serializable {

    /** 链接地址 */
    private String connectUrl;

    /** 用户名 */
    private String userName;

    /** 密码 */
    protected String password;

    /*** 数据源类型 */
    private String dbType;

    /**
     * 数据库名称
     */
    private String dbName;
}

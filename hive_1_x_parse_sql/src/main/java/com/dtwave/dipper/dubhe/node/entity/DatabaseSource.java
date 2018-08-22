package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 *
 * @author zhangsensen
 * @date 2018/1/18
 */
@Data
public class DatabaseSource {

    /** hive server2 连接信息*/
    private String url;

    /** 用户名*/
    private String userName;

    /** 密码*/
    private String password;

    /** 队列*/
    private String jobQueueName;
}

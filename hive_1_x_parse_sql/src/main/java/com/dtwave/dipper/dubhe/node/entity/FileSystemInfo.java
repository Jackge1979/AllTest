package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.List;

/**
 * Created by zhangsensen on 2017/8/8.
 */
@Data
public class FileSystemInfo {
    //hdfs  hdfs://host:port 地址 单机模式下
    private String address;
    //node 服务信息 ha模式下
    private List<String> serverList;
}

package com.dtwave.dipper.dubhe.node.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

/**
 * Created by zhangsensen on 2017/8/10.
 *
 * 对外查询接口
 */
@Data
public class FileSystemDto {

    //起始资源
    private String src;

    //目的资源
    private String dest;

    //是否递归
    private boolean recursive;

    //起始资源服务
    private FSKey srcServer;

    //目的资源服务
    private JSONObject descServer;
}

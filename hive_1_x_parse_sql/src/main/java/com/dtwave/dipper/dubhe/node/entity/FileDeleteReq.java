package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * Author: plzhao
 * Date: created on 2018-01-11 19:27:59
 **/
@Data
public class FileDeleteReq {
    /**要删除的目标文件*/
    String dest;
    /**是否递归删除文件夹*/
    //boolean recursive;
    /**连接信息*/
    FSKey fskey;
}

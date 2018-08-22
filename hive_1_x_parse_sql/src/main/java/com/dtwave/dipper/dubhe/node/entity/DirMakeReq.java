package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * Author: plzhao
 * Date: created on 2018-01-11 20:03:18
 **/
@Data
public class DirMakeReq {
    //欲创建的文件夹
    String dest;
    //父文件夹不存在时，是否递归创建
    boolean recursive;
    //源文件系统连接信息
    FSKey fskey;
}

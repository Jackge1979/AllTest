package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * Author: plzhao
 * Date: created on 2018-01-11 19:32:42
 **/
@Data
public class FileCopyReq {
    //源文件
    String src;
    // 目标文件位置
    String dest;
    //源文件系统连接信息
    FSKey srcFskey;
    //目标文件系统连接信息
    FSKey destFskey;
    //是否覆盖
    boolean overwrite;
}

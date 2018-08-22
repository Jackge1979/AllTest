package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

/**
 * Author: plzhao
 * Date: created on 2018-01-11 19:19:39
 **/
@Data
public class FileRenameReq {
    //原始文件名
    String src;
    //新文件名
    String dest;
    //连接信息
    FSKey fskey;
}

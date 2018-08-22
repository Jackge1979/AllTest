package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: plzhao
 * Date: created on 2018-01-06 15:45:37
 **/
@Data
public class FileInfo implements Serializable {

    private String name;

    private String owner;

    private long len;

    private long modificationTime;

}

package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Yarn上指定队列的资源信息
 *
 * Created by baisong on 17/10/6.
 */
@Data
public class EngineDto implements Serializable {

    //标记服务是否可用
    private boolean isAvailable;

    //总共CPU和数
    private Integer totalCpu;

    //剩余CPU核数
    private Integer remainCpu;

    //总内存,单位Byte
    private Long totalMemory;

    //总内存,单位Byte
    private Long remainMemory;

    //运行的作业数目
    private Integer runTask;

    //等待的作业数目
    private Integer waitTask;

}

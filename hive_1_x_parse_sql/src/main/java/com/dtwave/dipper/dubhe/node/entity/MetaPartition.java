package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Description:
 *
 * @author: fangdi
 * Version: 1.0
 * Create Date Time: 2018/7/24 下午2:40.
 * Update Date Time:
 */
@Data
public class MetaPartition implements Serializable {
    /**
     * 分区名字
     */
    private String partitionName;
    /**
     * 分区大小
     */
    private long  totalSize;

    /**
     * 分区创建时间
     */
    private String createTime;
    /**
     * 表ID
     */
    private Integer entityId;
    /**
     * 分区ID
     */
    private Integer partitionId;
}

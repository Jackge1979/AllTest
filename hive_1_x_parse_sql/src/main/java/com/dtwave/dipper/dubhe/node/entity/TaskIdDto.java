package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Created by baisong on 17/8/23.
 */
@Data
public class TaskIdDto implements Serializable{

    //任务ID
    private String taskId;

    public TaskIdDto() {}

    public TaskIdDto(String taskId) {
        this.taskId = taskId;
    }
}

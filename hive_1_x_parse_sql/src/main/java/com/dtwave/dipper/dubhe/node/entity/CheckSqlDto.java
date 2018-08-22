package com.dtwave.dipper.dubhe.node.entity;

import com.dtwave.dipper.dubhe.common.constant.TaskType;
import lombok.Data;

/**
 * @author hulb
 * @date 2017/12/19 下午4:15
 */
@Data
public class CheckSqlDto {
    String source;
    String parameter;
    TaskType taskType;
}

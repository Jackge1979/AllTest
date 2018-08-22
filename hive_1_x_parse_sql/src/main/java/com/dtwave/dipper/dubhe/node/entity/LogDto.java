package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 返回日志
 *
 * Created by baisong on 17/8/18.
 */
@Data
public class LogDto implements Serializable {

    //读取当前的行号
    private int currentLine;

    //日志内容
    private String content;

    //返回结果ID
    private List<Integer> resultIdList;

    private Integer status;
}

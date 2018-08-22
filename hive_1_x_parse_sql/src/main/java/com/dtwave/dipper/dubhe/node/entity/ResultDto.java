package com.dtwave.dipper.dubhe.node.entity;

import com.alibaba.fastjson.JSONObject;
import lombok.Data;

import java.io.Serializable;

/**
 * 对SQL SELECT 返回的查询结果
 *
 * Created by baisong on 17/8/18.
 */
@Data
public class ResultDto implements Serializable {

    //查询结果ID
    private int resultId;

    //查询结果总行数
    private int totalSize;

    //结果内容, schema+data
    private JSONObject content;

}

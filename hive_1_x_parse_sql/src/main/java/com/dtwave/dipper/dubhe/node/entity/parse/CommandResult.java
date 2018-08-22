package com.dtwave.dipper.dubhe.node.entity.parse;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hulb
 * @date 2018/4/16 上午11:12
 */
@Data
public class CommandResult implements Serializable {
    String command;
    List<String> failedReasonList;

    public CommandResult(){
        failedReasonList = new ArrayList<>();
    }
}

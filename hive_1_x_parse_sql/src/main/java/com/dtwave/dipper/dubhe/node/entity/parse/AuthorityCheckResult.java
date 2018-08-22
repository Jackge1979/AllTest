package com.dtwave.dipper.dubhe.node.entity.parse;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hulb
 * @date 2018/4/16 上午10:40
 */
@Data
public class AuthorityCheckResult implements Serializable{
    /**整体的结果*/
    Boolean result;
    /**sql: failedReasonList */
    List<CommandResult> commandResultList;

    public AuthorityCheckResult() {
        result=true;
        commandResultList = new ArrayList<>();
    }

}



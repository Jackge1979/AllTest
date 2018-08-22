package com.dtwave.dipper.megrez.server.lineage;

import com.dtwave.dipper.dubhe.node.entity.parse.Entity;
import lombok.Data;

import java.util.LinkedList;
import java.util.List;

/**
 * 描述信息
 *
 * @author baisong
 * @date 18/4/11
 */
@Data
public class ParseResult {

    /**
     * 输入列表
     */
    private List<com.dtwave.dipper.dubhe.node.entity.parse.Entity> inputList;

    /**
     * 输出列表
     */
    private List<Entity> outputList;


    public ParseResult() {
        this.inputList = new LinkedList<>();
        this.outputList = new LinkedList<>();
    }
}

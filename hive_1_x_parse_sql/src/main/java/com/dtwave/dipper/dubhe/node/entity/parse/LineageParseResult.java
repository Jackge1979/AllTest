package com.dtwave.dipper.dubhe.node.entity.parse;

import lombok.Data;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * 描述信息
 *
 * @author baisong
 * @date 18/4/11
 */
@Data
public class LineageParseResult extends ParseResult implements Serializable{

    private Long userId;
    private Long tenantId;
    private Long workspaceId;
    private String schemaName;
    private String optType;
    private String command;
    private MegrezLineageDto megrezLineageDto;

    /**
     * 输入列表
     */
    private List<Entity> inputList;
    private List<String> fieldList;
    private List<String> functionList;
    /**
     * 输出列表
     */
    private List<Entity> outputList;
    public LineageParseResult() {
        this.inputList = new LinkedList<>();
        this.functionList = new LinkedList<>();
        this.outputList = new LinkedList<>();
        this.fieldList = new LinkedList<>();
    }

    @Override
    public String toString() {
        return "LineageParseResult{" +"\n"+
                "userId=" + userId +"\n"+
                ", tenantId=" + tenantId +"\n"+
                ", workspaceId=" + workspaceId +"\n"+
                ", schemaName='" + schemaName + '\'' +"\n"+
                ", optType='" + optType + '\'' +"\n"+
                ", command='" + command + '\'' +"\n"+
                ", megrezLineageDto=" + megrezLineageDto +"\n"+
                ", inputList=" + inputList +"\n"+
                ", fieldList=" + fieldList +"\n"+
                ", outputList=" + outputList +"\n"+
                '}';
    }
}

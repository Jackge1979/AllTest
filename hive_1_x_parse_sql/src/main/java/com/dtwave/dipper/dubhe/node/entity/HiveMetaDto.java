package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author hulb
 * @date 2017/12/19 下午4:15
 */
@Data
public class HiveMetaDto implements Serializable{
    Integer entityId;
    List<Integer> entityIdList;
    List<Integer> schemaIdList;
    List<String> schemaNameList;

    /** 执行语句 */
    private String executeSql;

    /** 数据库名称 */
    private String dataBaseName;

    /** 表名*/
    private String entityName;
    public HiveMetaDto(){
    }
    public HiveMetaDto(String dataBaseName,String entityName,String executeSql){
        this.dataBaseName = dataBaseName;
        this.entityName = entityName;
        this.executeSql = executeSql;
    }

}

package com.dtwave.dipper.dubhe.node.entity.parse;

import lombok.Data;

/**
 * 描述信息
 *
 * @author baisong
 * @date 18/4/11
 */
@Data
public class Entity {

    private String database;

    private String table;

    /**
     * 实体类型
     */
    private String type;

    private String writeType;


    public Entity(String database, String table, String type,String writeType) {
        this.database = database;
        this.table = table;
        this.type = type;
        this.writeType = writeType;
    }
}

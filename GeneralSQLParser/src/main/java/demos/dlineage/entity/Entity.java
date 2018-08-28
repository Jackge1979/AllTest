package demos.dlineage.entity;

import lombok.Data;

/**
 * Created by lcc on 2018/7/5.
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


    public Entity(String database, String table, String type, String writeType) {
        this.database = database;
        this.table = table;
        this.type = type;
        this.writeType = writeType;
    }
}

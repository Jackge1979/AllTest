package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.List;

/**
 * Hive 血缘Logger 分析结果类
 * @author hulb
 * @date 2017/12/19 下午4:15
 *
 *
 *  FIL(0)  FIL(1)
 *     \    /
 *     FIL(2)
 *
 * 顶点为 FIL(0), FIL(1), FIL(2)
 * 边为：([0 ,1] -> [2])
 *
 *
 * 完整示例1(一节点对应一节点)：
 * {"version":"1.0","user":"hulb","timestamp":170475521788,"duration":-168952350930951,"jobIds":[]
 * ,"engine":"mr","database":"default","hash":"91b81acff45df6400e032d050b2901fc"
 * ,"queryText":"insert into table sink select source_key,source_value from source"
 * ,"edges":[
 * {"sources":[2],"targets":[0],"edgeType":"PROJECTION"}
 * ,{"sources":[3],"targets":[1],"edgeType":"PROJECTION"}]
 * ,"vertices":[
 * {"id":0,"vertexType":"COLUMN","vertexId":"default.sink.sink_key"}
 * ,{"id":1,"vertexType":"COLUMN","vertexId":"default.sink.sink_value"}
 * ,{"id":2,"vertexType":"COLUMN","vertexId":"default.source.source_key"}
 * ,{"id":3,"vertexType":"COLUMN","vertexId":"default.source.source_value"}]}
 *
 *
 * 完整示例2（两节点对应一节点）：
 * {"version":"1.0","user":"hulb","timestamp":171915233297,"duration":-170392061000096,"jobIds":[]
 * ,"engine":"mr","database":"default","hash":"690e42f0d74fe8b858603ba575c0770c"
 * ,"queryText":"INSERT INTO TABLE sink SELECT source_key,source_value FROM source UNION SELECT source_1_key,source_1_value FROM source_1"
 * ,"edges":[
 * {"sources":[2,3],"targets":[0],"expression":"source_key","edgeType":"PROJECTION"}
 * ,{"sources":[4,5],"targets":[1],"expression":"source_value","edgeType":"PROJECTION"}]
 * ,"vertices":[
 * {"id":0,"vertexType":"COLUMN","vertexId":"default.sink.sink_key"}
 * ,{"id":1,"vertexType":"COLUMN","vertexId":"default.sink.sink_value"}
 * ,{"id":2,"vertexType":"COLUMN","vertexId":"default.source.source_key"}
 * ,{"id":3,"vertexType":"COLUMN","vertexId":"default.source_1.source_1_key"}
 * ,{"id":4,"vertexType":"COLUMN","vertexId":"default.source.source_value"}
 * ,{"id":5,"vertexType":"COLUMN","vertexId":"default.source_1.source_1_value"}]}
 *
 */
@Data
public class HiveLineageDto {
    /**版本*/
    String version;
    /**用户*/
    String user;
    /**时间戳*/
    Long timestamp;
    /**时间*/
    Long duration;
    /**jobID*/
    List<String> jobIds;
    /**引擎：mr 或spark*/
    String engine;
    /**所属数据库*/
    String database;
    /**hash值*/
    String hash;
    /**查询内容sql*/
    String queryText;
    /**顶点*/
    List<Edge> edges;
    /**边*/
    List<Vertices> vertices;
}






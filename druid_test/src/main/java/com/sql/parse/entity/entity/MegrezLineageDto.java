package com.sql.parse.entity.entity;


import java.io.Serializable;
import java.util.ArrayList;
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
 */
public class MegrezLineageDto implements Serializable {

    String user;
    /**所属数据库*/
    String database;
    /**查询内容sql*/
    String queryText;
    /**顶点*/
    List<MegrezLineageEdge> edges;

    public MegrezLineageDto(){
        edges = new ArrayList<MegrezLineageEdge>();
    }


    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getQueryText() {
        return queryText;
    }

    public void setQueryText(String queryText) {
        this.queryText = queryText;
    }

    public List<MegrezLineageEdge> getEdges() {
        return edges;
    }

    public void setEdges(List<MegrezLineageEdge> edges) {
        this.edges = edges;
    }
}





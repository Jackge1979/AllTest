package com.dtwave.dipper.dubhe.node.entity.parse;

import lombok.Data;

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
@Data
public class MegrezLineageDto implements Serializable {

    String user;
    /**所属数据库*/
    String database;
    /**查询内容sql*/
    String queryText;
    /**顶点*/
    List<MegrezLineageEdge> edges;

    public MegrezLineageDto(){
        edges = new ArrayList<>();
    }
}





package com.dtwave.dipper.megrez.server.lineage;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hulb
 * @date 2018/4/8 下午3:49
 */
@Data
public class MegrezLineageEdge implements Serializable {
    /**边source顶点ID*/
    List<String> sources;
    /**边sink顶点ID*/
    List<String> targets;

    public MegrezLineageEdge(){
        sources = new ArrayList<>();
        targets = new ArrayList<>();
    }
}

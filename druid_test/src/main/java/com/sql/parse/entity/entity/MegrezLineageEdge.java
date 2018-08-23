package com.sql.parse.entity.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hulb
 * @date 2018/4/8 下午3:49
 */
public class MegrezLineageEdge implements Serializable {
    /**边source顶点ID*/
    List<String> sources;
    /**边sink顶点ID*/
    List<String> targets;

    public MegrezLineageEdge(){
        sources = new ArrayList<String>();
        targets = new ArrayList<String>();
    }


    public List<String> getSources() {
        return sources;
    }

    public void setSources(List<String> sources) {
        this.sources = sources;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }
}

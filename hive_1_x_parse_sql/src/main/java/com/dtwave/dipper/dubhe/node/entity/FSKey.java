package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: plzhao
 * Date: created on 2018-01-09 10:00:41
 **/
@Data
public class FSKey implements Serializable{
    /**nameservice url或者defaultFS URL*/
    private String fsUrl;
    /**若为namenode为"HA"模式，此处为namenode集合*/
    private String[] nameNodes;

    public FSKey(){
        this.nameNodes = new String[0];
    }
    @Override
    public boolean equals(final Object obj) {
        if (obj == this) {
            return true;
        }
        else if (obj != null && obj.getClass() == FSKey.class) {
            final FSKey that = (FSKey) obj;
            if(nameNodes.length==2){
                return this.fsUrl.equals(that.fsUrl) && this.nameNodes[0].equals(that.nameNodes[0])
                        && this.nameNodes[1].equals(that.nameNodes[1]);
            }
            return this.fsUrl.equals(that.fsUrl);
        }
        else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int baseCode = 31* this.fsUrl.hashCode();
        if(nameNodes.length==2){
            baseCode+=17* nameNodes[0].hashCode()+17* nameNodes[1].hashCode();

        }
        return baseCode;
    }

    @Override
    public String toString() {
        StringBuilder builder=new StringBuilder();
        builder.append(fsUrl.startsWith("hdfs://")?"":"hdfs://")
                .append(fsUrl);
        if(nameNodes.length==2){
            builder.append("?nameNode1=")
                    .append(nameNodes[0])
                    .append("&nameNode2=")
                    .append(nameNodes[1]);
        }
        return builder.toString();
    }
}

package com.xml.to.obj.entity.xmls;


import lombok.Data;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
//@XStreamAlias("source")
public class Source {

//    @XStreamAsAttribute
    private String coordinate;
//    @XStreamAsAttribute
    private String column;
//    @XStreamAsAttribute
    private String id;
//    @XStreamAsAttribute
    private String parentId;
//    @XStreamAsAttribute
    private String parentName;

}

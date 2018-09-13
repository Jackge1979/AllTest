package com.xml.to.obj.entity.xmls;


import lombok.Data;

import java.util.List;

/**
 * Created by lcc on 2018/8/30.
 */
@Data
//@XStreamAlias("resultset")  // 对应relation
public class Resultset {

//    @XStreamAsAttribute
    private String name;
//    @XStreamAsAttribute
    private String id;
//    @XStreamAsAttribute
    private String type;
//    @XStreamAsAttribute
    private String coordinate;

//    @XStreamImplicit(itemFieldName = "column")
    private List<Column> column;
}

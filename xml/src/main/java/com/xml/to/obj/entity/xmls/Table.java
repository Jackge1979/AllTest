package com.xml.to.obj.entity.xmls;


import lombok.Data;

import java.util.List;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
//@XStreamAlias("table")
public class Table {

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

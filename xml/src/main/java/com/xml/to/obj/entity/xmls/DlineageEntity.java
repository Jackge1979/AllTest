package com.xml.to.obj.entity.xmls;


import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
//@XStreamAlias("DlineageEntity")//对应dlineage元素
public class DlineageEntity  implements Serializable{

    private static final long serialVersionUID = 1L;


//     对应relation
//    @XStreamImplicit(itemFieldName = "relation")
    private List<Relation> relation;

    // 对应table
//    @XStreamImplicit(itemFieldName = "table")
    private List<Table> table;

    // 对应table
//    @XStreamImplicit(itemFieldName = "resultset")
    private List<Resultset> resultset;

}

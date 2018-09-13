package com.xml.to.obj.entity.xmls;


import lombok.Data;

import java.util.List;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
//@XStreamAlias("relation")  // 对应relation
public class Relation {

    // 对应 id 属性
//    @XStreamAsAttribute
    private String id;
    // 对应 type 属性
//    @XStreamAsAttribute
    private String type;

    // 对应 target 子元素
//    @XStreamImplicit(itemFieldName = "target")
    private List<Target> target ;
    // 对应 source 子元素
//    @XStreamImplicit(itemFieldName = "source")
    private List<Source> source ;


}

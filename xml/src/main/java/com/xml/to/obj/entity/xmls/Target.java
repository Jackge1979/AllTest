package com.xml.to.obj.entity.xmls;


import lombok.Data;

/**
 * Created by lcc on 2018/8/27.
 */
@Data
//@XStreamAlias("target") // 对应 target
public class Target {

    // 对应 coordinate 属性  对应在sql的第几行第几个字符到第几个字符
//    @XStreamAsAttribute
    private String coordinate;
    // 对应 column 属性
//    @XStreamAsAttribute
    private String column;
    // 对应 id 属性
//    @XStreamAsAttribute
    private String id;
    // 对应 parentId 属性
//    @XStreamAsAttribute
    private String parentId;
    // 对应 parentName 属性
//    @XStreamAsAttribute
    private String parentName;

}

package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;

import java.util.List;

/**
 * @author hulb
 * @date 2017/12/1 上午10:41
 */
@Data
public class HiveMetaEntity extends MetaEntity {

    /** 字段信息*/
    List<HiveMetaColumn> hiveMetaColumnList;
}
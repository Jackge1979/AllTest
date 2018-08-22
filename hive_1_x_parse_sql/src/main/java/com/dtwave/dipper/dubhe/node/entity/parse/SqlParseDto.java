package com.dtwave.dipper.dubhe.node.entity.parse;

import lombok.Data;

import java.io.Serializable;

/**
 * Author: xuehuan
 * Date: created on 2018-04-08 10:43:41
 **/
@Data
public class SqlParseDto implements Serializable{

    private Long userId;
    private Long tenantId;
    private Long workspaceId;
    private String schemaName;
    private Boolean lineageFlag;

    /** 待解析的sql */
    private String sql;

    /** 当前所处的database */
    private String currentDatabase;
}

package com.dtwave.dipper.dubhe.node.entity;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Author: plzhao
 * Date: created on 2018-06-11 16:58:24
 **/
@Data
public class ConnectEntity {
    /**
     * 连接类型
     */
    private int connectType;
    /**
     * 连接的url
     */
    private String jdbcUrl;
    /**
     * 连接操作的数据库
     */
    private String database;
    /**
     * 用户名
     */
    private String userName;
    /**
     * password
     */
    private String password;
    /**
     * kerberos principal
     */
    private String principal;
    /**
     * kerberos keytab
     */
    private String keytab;
    /**
     * kerberos pricipal for hiveserver2
     */
    private String principal_hive2;

    /**
     * 创建数据库，获取链接时用的库名
     */
    private String connectSchema;


    private String fullUrlStr;

    /**
     *  是否创建数据库，如果为flase，不创建；如果为true，则创建库
     */
    private boolean isInitDB=false;

    /**
     * 计算引擎是否高可用
     */
    private Boolean isAddressHa;
    /**
     * hive高可用模式下的参数
     */
    private String engineParam;

    @Override
    public int hashCode() {
        StringBuilder stringBuilder = new StringBuilder("");
        stringBuilder.append(jdbcUrl).append(database).append(userName)
                .append(password).append(password).append(keytab)
                .append(principal_hive2).append(connectSchema);
        return connectType + stringBuilder.toString().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        ConnectEntity c = (ConnectEntity) obj;
        return !((this.connectType == c.connectType)
                && StringUtils.equals(this.jdbcUrl, c.getJdbcUrl())
                && StringUtils.equals(this.database, c.getDatabase())
                && StringUtils.equals(this.userName, c.getUserName())
                && StringUtils.equals(this.password, c.getPassword())
                && StringUtils.equals(this.principal, c.getPrincipal())
                && StringUtils.equals(this.keytab, c.getKeytab())
                && StringUtils.equals(this.principal_hive2, c.getPrincipal_hive2())
                && StringUtils.equals(this.connectSchema, c.getConnectSchema()));
    }
}

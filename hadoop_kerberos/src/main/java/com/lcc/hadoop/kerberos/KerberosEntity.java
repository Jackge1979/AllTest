package com.lcc.hadoop.kerberos;

import lombok.Data;

/**
 * @Author: chuanchuan.lcc
 * @CreateDate: 2018/11/23 AM10:49
 * @Version: 1.0
 * @Description: java类作用描述：
 */
@Data
public class KerberosEntity {
    /**
     * krb5 配置信息
     */
    private String krb5Conf;
    /**
     * 服务的keytab文件
     */
    private String keytab;

    /**
     * 服务的principal
     */
    private String principal;
    /**
     * 是否需要更新登陆
     */
    private boolean needRenewTicket;

    /**
     * 最大票据更新时间
     */
    private long maxRenewTicketLife;

}

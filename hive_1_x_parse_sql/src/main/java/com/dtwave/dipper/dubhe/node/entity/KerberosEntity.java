package com.dtwave.dipper.dubhe.node.entity;

import lombok.EqualsAndHashCode;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.conf.Configuration;


/**
 * Author: plzhao
 * Date: created on 2018-07-02 21:18:05
 **/
@EqualsAndHashCode
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

    /**
     * hadoop configuration
     */
    private Configuration conf;

    private KerberosEntity(){
        this.needRenewTicket = false;
        this.conf = null;
    }

    public static  class Builder {

        private KerberosEntity kerberosEntity;

        public Builder(){
           this.kerberosEntity = new KerberosEntity();
        }

        public  Builder withKrb5Conf(String krb5Conf){
            kerberosEntity.krb5Conf = krb5Conf;
            return this;
        }

        public  Builder withPrincipal(String principal){
            kerberosEntity.principal = principal;
            return this;
        }

        public  Builder withKeytab(String keytab){
            kerberosEntity.keytab = keytab;
            return this;
        }

        public Builder  needRenewTicket(boolean needRenewTicket){
            kerberosEntity.needRenewTicket = needRenewTicket;
            return this;
        }

        public Builder withMaxRenewTicketLife(long maxRenewTicketLife) {
            kerberosEntity.maxRenewTicketLife = maxRenewTicketLife;
            return this;
        }


        public Builder withConfiguration(Configuration conf){
            kerberosEntity.conf = conf;
            return this;
        }


        public KerberosEntity build(){
            // 校验
            if(StringUtils.isBlank(kerberosEntity.getPrincipal()) ||
                    StringUtils.isBlank(kerberosEntity.getKeytab())){
                throw new IllegalArgumentException("the keytab or principal cannot be null or empty");
            }

            if(kerberosEntity.needRenewTicket){
                kerberosEntity.maxRenewTicketLife = Math.max(10000L,kerberosEntity.getMaxRenewTicketLife());
            }
            return kerberosEntity;
        }
    }

    public String getKrb5Conf() {
        return krb5Conf;
    }

    public String getKeytab() {
        return keytab;
    }

    public String getPrincipal() {
        return principal;
    }

    public boolean isNeedRenewTicket() {
        return needRenewTicket;
    }

    public long getMaxRenewTicketLife() {
        return maxRenewTicketLife;
    }

    public Configuration getConf() {
        if(conf == null){
            return  new Configuration();
        }
        return conf;
    }

    public void setConf(Configuration conf) {
        this.conf = conf;
    }
}

package com.lcc.hadoop.kerberos;

import org.apache.hadoop.conf.Configuration;

import java.io.IOException;

/**
 * @Author: chuanchuan.lcc
 * @CreateDate: 2018/11/23 AM10:53
 * @Version: 1.0
 * @Description: java类作用描述：
 */
public class LoginTest {
    public static void main(String[] args) throws IOException {

        String krb5ConfPath = args[0];
        String principal = args[1];
        String keytab = args[2];

        System.out.println("参数：krb5ConfPath "+krb5ConfPath);
        System.out.println("参数：principal "+principal);
        System.out.println("参数：keytab "+keytab);


        UserLogginUtil.setKrb5Conf(krb5ConfPath);

        KerberosEntity kerberosEntity = new KerberosEntity();
        kerberosEntity.setKrb5Conf(krb5ConfPath);
        kerberosEntity.setPrincipal(principal);
        kerberosEntity.setKeytab(keytab);

        Configuration configuration = new Configuration();

        UserLogginUtil.login(kerberosEntity,configuration);

        System.out.println("登陆成功");
    }
}

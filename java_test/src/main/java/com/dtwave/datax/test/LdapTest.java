package com.dtwave.datax.test;

/**
 * Created by lcc on 2018/10/25.
 */
public class LdapTest {

    public static void instanceLDAP(String hive2Url,
                                                   String hiveUserName,
                                                   String hivePassword ) {
                    if (hiveUserName != null  && hivePassword != null ) {

                    }else  if(hive2Url.contains(";")){
                        // 说明用户名密码是写在url后面的
                        String[] urls = hive2Url.split(";");
                        if( urls.length == 3){
                            // 格式是只有一个用户名密码
                            hive2Url =  urls[0];
                            hiveUserName = urls[1].split("=")[1];
                            hivePassword =  urls[2].split("=")[1];
//                            LOG.info("提示！分割url得到的url：{},userName:{},Password:{}",hive2Url,hiveUserName,hivePassword);
//                            instance = new HiveTableConfHolder(hive2Url,hiveUserName,hivePassword);
                        }
                    }else  if(hive2Url.contains(";")){
                        // 说明用户名密码是写在url后面的
//                        LOG.error("LDAP方式连接hive用户名或者密码为空");
                    }


                }

    public static void main(String[] args){
        String hiveUrls = "jdbc:hive2://36.11.192.14:10000/tdh_dev;user=hive;password=roothive";
        LdapTest.instanceLDAP(hiveUrls,null,null);
    }



}

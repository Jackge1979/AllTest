package com.lcc.hadoop.kerberos;



import com.google.common.base.Preconditions;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.security.UserGroupInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;

/**
 * @Author: chuanchuan.lcc
 * @CreateDate: 2018/11/23 AM10:47
 * @Version: 1.0
 * @Description: java类作用描述：
 */
public class UserLogginUtil {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserLogginUtil.class);
    /**
     * 登陆的重试次数
     */
    private static final int MAX_RETRY_NUMS =3;

    /**
     * krb5 config key
     */

    private static final String JAVA_SECURITY_KRB5_CONF_KEY = "java.security.krb5.conf";

    /**
     * 以指定的"keytab"和"principal" 登录
     * @param principal  principal
     * @param keytab keytab
     * @return Conifiguration
     */
    public static Configuration init(String principal, String keytab) {
        Preconditions.checkArgument(StringUtils.isNotBlank(principal),"must specify the principal");
        Preconditions.checkArgument(StringUtils.isNotBlank(keytab),"must specify the keytab");
        Configuration conf = new org.apache.hadoop.conf.Configuration();
        conf.set("hadoop.security.authentication", "Kerberos");
        UserGroupInformation.setConfiguration(conf);
        int retryNums = 0;
        do{
            try {
                UserGroupInformation.loginUserFromKeytab(principal,keytab);
                break;
            } catch (IOException e) {
                ++retryNums;
                if(retryNums >= MAX_RETRY_NUMS){
                    throw new RuntimeException(String.format("failed to loggin in the given principal-%s," +
                                    "keytab-%s, has retried 3 times,caused by %s",
                            principal,keytab, ExceptionUtils.getStackTrace(e)));
                }else{
                    try{
                        if (UserGroupInformation.isLoginKeytabBased()) {
                            UserGroupInformation.getLoginUser().reloginFromKeytab();
                        } else {
                            UserGroupInformation.getLoginUser().reloginFromTicketCache();
                        }
                    }catch (Exception ex){
                        // 重试过程中，不再捕获异常
                    }
                }
            }

        }while (true);
        return conf;
    }


    public static void login(KerberosEntity kerberosEntity,Configuration conf) throws IOException{
        // 检查必须的参数信息
        Preconditions.checkArgument(StringUtils.isNotBlank(kerberosEntity.getKeytab()),"must specify the principal");
        Preconditions.checkArgument(StringUtils.isNotBlank(kerberosEntity.getPrincipal()),"must specify the keytab");
        Preconditions.checkArgument(StringUtils.isNotBlank(kerberosEntity.getKrb5Conf()),"must specify the krb5Conf");
        // 校验krb5Conf
        setKrb5Conf(kerberosEntity.getKrb5Conf());
        // 校验keytab
        File keytabConfigFile = new File(kerberosEntity.getKeytab());
        if(!keytabConfigFile.exists() || !keytabConfigFile.isFile()){
            throw new IOException(String.format("the specified hdfs-keytab file-%s does not exist, or is not a file",
                    kerberosEntity.getKeytab()));
        }
        // 设置配置信息，并以相应的principal和 principal登陆
        conf.set("hadoop.security.authentication", "kerberos");
        conf.set("hadoop.security.authorization", "true");
        UserGroupInformation.setConfiguration(conf);

        String keytab = keytabConfigFile.getAbsolutePath();
        try {
            UserGroupInformation.loginUserFromKeytab(kerberosEntity.getPrincipal(),keytab);
            // 解决"ticket"失效问题
            UserGroupInformation ugi = UserGroupInformation.getLoginUser();

            if(kerberosEntity.isNeedRenewTicket()) {
                Thread reNewTicketThread = new Thread(
                        ()->{
                            do{
                                try{
                                    Thread.sleep(kerberosEntity.getMaxRenewTicketLife());
                                    ugi.reloginFromKeytab();
                                }catch (InterruptedException  ex){
                                    Thread.currentThread().interrupt();
                                }catch (IOException e) {
                                    LOGGER.warn("unExcepted error occurs in accquiring the ticket of kerberos,{}",
                                            ExceptionUtils.getStackTrace(e));
                                    break;
                                }
                            } while (true);
                        }
                );
                reNewTicketThread.setDaemon(true);
                reNewTicketThread.start();

            }

        } catch (IOException e) {
            LOGGER.warn("failed to logIn in the principal-{} and keyTab-{},error:{}",
                    kerberosEntity.getPrincipal(),keytab, ExceptionUtils.getStackTrace(e));
            throw e;
        }
    }


    /**
     * 设置krb5文件
     *
     * @param krb5ConfPath  krb5ConfPath
     * @throws IOException  IOException
     */
    public static void setKrb5Conf(String krb5ConfPath) throws IOException{
        LOGGER.info("系统参数：krb5ConfPath：{}",krb5ConfPath);
        // 检查并设置krb5配置文件
        File krb5ConfFile = new File(krb5ConfPath);
        if(!krb5ConfFile.exists() || !krb5ConfFile.isFile()){
            LOGGER.info("系统参数加载失败，文件不存在或者不是文件");
            throw new IOException(String.format("the specified krb5 file-%s does not exist, or is not a file",
                    krb5ConfPath));
        }
        System.setProperty(JAVA_SECURITY_KRB5_CONF_KEY,krb5ConfPath);
        LOGGER.info("系统参数设置成功");
    }
}

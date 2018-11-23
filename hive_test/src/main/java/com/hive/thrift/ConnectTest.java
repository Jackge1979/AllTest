package com.hive.thrift;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.ql.session.SessionState;

/**
 * @Author: chuanchuan.lcc
 * @CreateDate: 2018/11/22 PM2:19
 * @Version: 1.0
 * @Description: java类作用描述：
 */
public class ConnectTest {

    public static final String LINEAGE_LOGGER_CLASSNAME = "org.apache.hadoop.hive.ql.hooks.LineageLogger";

    public static void main(String[] args) {
        try {
            HiveConf conf = new HiveConf();
            conf.set("hive.metastore.uris", args[0] );
            conf.set("hive.exec.post.hooks", LINEAGE_LOGGER_CLASSNAME);
            conf.set("hive.exec.dynamic.partition.mode", "nonstrict");
            SessionState.start(conf);
            System.out.println("建立连接hive metastore成功, uris: {}"+ args[0]  );
        } catch (Exception e) {
            System.out.println("建立连接hive metastore服务出错, 错误: "+ e);
        }
    }
}

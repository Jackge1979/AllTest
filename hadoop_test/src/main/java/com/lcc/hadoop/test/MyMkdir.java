package com.lcc.hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

/**
 * 1. 只支持FileSystem增删改查文件
 * 2. 不支持命令行
 * 3. 不支持spark读写文件
 * 4. 用户为空，不会走鉴权接口
 */
public class MyMkdir {
    public static void main(String[] args)throws Exception{
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://lcc:9000"),conf,"{\"isAllowAuth\":\"true\",\"tenantId\": 5,\"userId\": 23,\"checkHdfsUrl\":\"http://192.168.1.9:9018/api/v1/megrez/checkPrivilegesObject\"}" );
//        FileSystem fs = FileSystem.get(new URI("hdfs://lcc:9000"),conf );
        //测试创建一个文件夹,在HDFS上创建一个leitao文件夹,原根目录下使没有这个文件的
//        boolean flag = fs.mkdirs(new Path("/lcc/workspace/lcc2"));
        FileStatus[] aa = fs.listStatus(new Path("/lcc"));
        System.out.println(aa.length);
//        System.out.println(flag);
    }
}



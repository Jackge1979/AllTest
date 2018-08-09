package com.lcc.hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;


public class MyMkdir {
    public static void main(String[] args)throws Exception{
        Configuration conf = new Configuration();
        conf.set("hadoop.megrez.userId", "10");
        conf.set("hadoop.megrez.tenantId", "211110");
        conf.set("hadoop.megrez.workspaceId", "208");
        conf.set("hadoop.megrez.enable", "1");
        FileSystem fs = FileSystem.get(new URI("hdfs://lcc:9000"),conf,"1001");
        //测试创建一个文件夹,在HDFS上创建一个leitao文件夹,原根目录下使没有这个文件的
        boolean flag = fs.mkdirs(new Path("/leitao10"));
        System.out.println(flag);
    }
}

/**
 * 1. 只支持FileSystem增删改查文件
 * 2. 不支持命令行
 * 3. 不支持spark读写文件
 */

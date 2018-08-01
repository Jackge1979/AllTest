package com.lcc.hadoop.test;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import java.net.URI;

public class MyMkdir {
    public static void main(String[] args)throws Exception{
        FileSystem fs = FileSystem.get(new URI("hdfs://lcc:9000"),new Configuration(),"root");
        //测试创建一个文件夹,在HDFS上创建一个leitao文件夹,原根目录下使没有这个文件的
        boolean flag = fs.mkdirs(new Path("/leitao3"));
        System.out.println(flag);
    }
}

package com.spark.hdfs;

import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.VoidFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.List;

/**
 * 调用HdfsOperate类的方法把RDD数据保存到Hdfs上
 */
public class FeatureExtractor implements Serializable {
    private static Logger logger = LoggerFactory.getLogger(FeatureExtractor.class);

    public  static  void main(String[] args) throws Exception {
        SparkConf sparkConf = new SparkConf().setAppName("HDFSQuery").setMaster("local[2]");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        String hdfsPath = "hdfs://lcc:9000/lcc/student.txt"; //存放原始数据的文件
        //Spark可以读取单独的一个文件或整个目录
        JavaRDD<String> rddx = sc.textFile(hdfsPath).repartition(1);

        //写入hdfs文件位置
        String destinationPath = "hdfs://lcc:9000/lcc/student3.txt" ;
        //创建Hdfs文件，打开Hdfs输出流
        HdfsOperate.openHdfsFile(destinationPath);

        rddx.foreach(new VoidFunction<String>() {
            public void call(String s) throws Exception {

                System.out.println("s="+s);
                //写一行到Hdfs文件
                HdfsOperate.writeString(s);
            }
        });
        //关闭Hdfs输出流
        HdfsOperate.closeHdfsFile();

    }




}
package com.dtwave.megrez


import java.util.Base64

import com.alibaba.fastjson.{JSONArray, JSONObject}
import org.apache.spark.sql.CarbonSession._
import org.apache.spark.sql.{DataFrame, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

import scala.io.Source



/**
  * Created by hulb on 17/9/11.
  *
  *
  * spark-submit --class com.dtwave.megrez.MegrezApp --master yarn --deploy-mode client {megrez_v1.jar} ${taskType} ${param}
  */
object MegrezApp {

  def main(args: Array[String]): Unit = {

    val Array(taskType, param) = args

    val conf = new SparkConf()
    conf.setAppName("lcc")
    conf.setMaster("local")
    val sc = new SparkContext(conf)

    val storePath = "hdfs://co1:9000/Opt/CarbonStore"
    val spark:SparkSession = SparkSession.builder().config(sc.getConf).getOrCreateCarbonSession(storePath)

    val no_match_count = (column:String,regex:String ) => {
      try {
        if(column.matches(regex)) 0 else 1
      }catch {
        case e:Exception => 2
      }
    }

    spark.sqlContext.udf.register("no_match_count",no_match_count)



    /**
      * TODO 参数解析
      */
    taskType match {

      case "table_show" => tableShow(spark, param);
      case "table_count" => tableCount(spark, param);
      case "partition_count" => partitionCount(spark, param);


      //读取文件

      case "partition_drop" => doLifeCycle(spark, param);
      case "partition_drop_new" => readMysql(spark);


      /**
        * 245:dada_prd.dtw_bid_dwd_bas_email_d:age:8,1:dtos.test:age:1,1:dtos.test:age:2,1:dtos.test:age:3,1:dtos.test:age:5
        *
        * 字段统计进入字段统计入口再根据不同维度计算不同的统计值
        *spark-submit --master yarn --class com.dtwave.megrez.MegrezApp megrez-plugin-0.0.1-jar-with-dependencies.jar partition_drop /home/youboy/megrez/param
        */
      case "column_stat" => columnStat(spark, param);
    }

  }


  /**
    * 1.show tables
    */
  def tableShow(spark: SparkSession, tables: String): Unit = {
    println("==========start==========")
    val entityIdEntityNameMap = tables.split(",")
    for (entity <- entityIdEntityNameMap) {
      //val entityId = entity.split(":").apply(0)
      val entityName = entity.split(":").apply(1)
      try {
        val showDF = spark.sql(s"select * from $entityName limit 10")
        print("entityId" + "##@#" + entityName + "##@#")
        printJsonTable(showDF, spark)
      } catch {
        case e: Exception =>
      }
    }
    println("==========end==========")
  }


  /**
    * 2.Table count
    */
  def tableCount(spark: SparkSession, param: String): Unit = {
    val entityIdEntityNameMap = param.split(",")

    println("==========start==========")
    for (entity <- entityIdEntityNameMap) {
      val entityId = entity.split(":").apply(0)
      val entityName = entity.split(":").apply(1)
      try {
        val countDF = spark.sql(s"select count(1) from $entityName")
        val count = countDF.collect().apply(0).getLong(0)
        val data = Seq((entityId, count))
        println("entityId:" + entityId + "," + "entityName:" + entityName + "," + "count:" + count)
        spark.createDataFrame(data).toDF("entityId", "count").createOrReplaceTempView("nameCount")
      } catch {
        case _: Exception => println("error:" + entityName)
      }
    }
    println("==========end==========")

  }


  /**
    * 3.partition count
    */
  def partitionCount(spark: SparkSession, param: String): Unit = {

    val entityIdEntityNameMap = param.split(",")
    println("==========start==========")
    for (entity <- entityIdEntityNameMap) {
      val entityId = entity.split(":").apply(0)
      val entityName = entity.split(":").apply(1)
      val partitionId = entity.split(":").apply(2)
      val partitionName = entity.split(":").apply(3)

      val newPartitionName = doPartition(partitionName)
      var sql = ""
      try {
        sql = s"select count(1) from $entityName where $newPartitionName"
        val countDF = spark.sql(sql)
        val count = countDF.collect().apply(0).getLong(0)
        val data = Seq((entityId, count))
        println("entityId:" + entityId + "," + "entityName:" + entityName + "," + "partitionId:" + partitionId + "," + "partitionName:" + partitionName + "," + "count:" + count)
      } catch {
        case _: Exception => println("error:" + partitionName + " sql:" + sql)
      }
    }
    println("==========end==========")


  }

  /**
    * 4.删除生命周期
    *
    * @param tables
    * @param spark
    *
    *               二级分区：
    *              youboy.cds_tdm_tc_commodity_receive_sd:ds=20171120/tp=15d
    */
  def doLifeCycle(spark: SparkSession, tables: String): Unit = {

    var tableList = ""
    //读取文件
    if(tables.startsWith("/")){
      println("地址："+tables)
      Source.fromFile(tables).getLines().foreach(line  => tableList += line)
    }else{
      tableList = tables
    }

    /**
      *
      */



    println("==========start==========")
    val entityIdEntityNameMap = tableList.split(",")
    println("=== size"+entityIdEntityNameMap.size)
    for (entity <- entityIdEntityNameMap) {
      println("=== entity:"+entity)
      val entityName = entity.split(":").apply(0)
      val partition = entity.split(":").apply(1)
      // ds=20171120/tp=15d
      //val realPartition = doPartition(partition)
      val realPartition = dropPartition(partition)
      try {
        // alter table youboy.cds_tdm_tc_commodity_receive_sd drop partition (ds=20171120 and tp=15d)")
        spark.sql(s"alter table $entityName drop partition ($realPartition)")
        println(s"alter table $entityName drop partition ($realPartition)")
        println(entityName)
        //printJsonTable(showDF,spark)
      } catch {
        case _: Exception =>
      }
    }
    println("==========end==========")
  }


  def readMysql(spark:SparkSession):String={
    val lifecycleresult = spark.read.format("jdbc").option("url", "jdbc:mysql://10.176.66.49:3306/megrez").option("dbtable", "megrez.lifecycleresult").option("user", "hive").option("password", "youboy@2017_04_24").load()
    lifecycleresult.collect().map(row=>{
      val entityName = row.getString(1)
      val partition  = row.getString(2)
      val realPartition = dropPartition(partition)
      try {
        // alter table youboy.cds_tdm_tc_commodity_receive_sd drop partition (ds=20171120 and tp=15d)")
        println(s"start alter table $entityName drop partition ($realPartition)")
        spark.sql(s"alter table $entityName drop partition ($realPartition)")
        println(s"end alter table $entityName drop partition ($realPartition)")
        println(entityName)
        //printJsonTable(showDF,spark)
      } catch {
        case _: Exception =>
      }
    })
    ""
  }



  /**
    * 打印表结构
    *
    * @param df
    * @param spark
    */
  def printJsonTable(df: DataFrame, spark: SparkSession): Unit = {
    val result = df.take(10)
    val jsonObject = new JSONObject()
    val nameJsonArray = new JSONArray()
    val dataJsonArray = new JSONArray()
    val schema = df.schema
    val schemaSize = schema.size
    schema.map(sf => {
      val name = new JSONObject()
      name.put("dataKey", sf.name)
      name.put("title", sf.name)
      nameJsonArray.add(name)

    })
    var i = 0;
    while (i < result.length) {
      var j = 0
      val cell = new JSONObject()
      while (j < schemaSize) {
        cell.put(schema(j).name, result(i)(j))
        j = j + 1
      }
      dataJsonArray.add(cell)
      i = i + 1
    }
    jsonObject.fluentPut("columns", nameJsonArray).fluentPut("data", dataJsonArray)
    println(jsonObject.toJSONString)
  }


  /**
    * 处理二级分区
    * @param partitionName
    * @return
    */
  def doPartition(partitionName: String): String = {
    if (!partitionName.contains("/")) {
      return partitionName
    } else {
      var newPartition = ""
      val partitionList = partitionName.split("/")
      for (partition <- partitionList) {
        val partitionKey = partition.split("=").apply(0)
        val partitionValue = partition.split("=").apply(1)
        if ("".equals(newPartition)) {
          newPartition += partitionKey + "=" + s"'$partitionValue'"
        } else {
          newPartition += " and " + partitionKey + "=" + s"'$partitionValue'"
        }
      }
      return newPartition
    }
  }

  def dropPartition(partitionName: String): String = {
    if (!partitionName.contains("/")) {
      return partitionName
    } else {
      var newPartition = ""
      val partitionList = partitionName.split("/")
      for (partition <- partitionList) {
        val partitionKey = partition.split("=").apply(0)
        val partitionValue = partition.split("=").apply(1)
        if ("".equals(newPartition)) {
          newPartition += partitionKey + "=" + s"'$partitionValue'"
        } else {
          newPartition += "," + partitionKey + "=" + s"'$partitionValue'"
        }
      }
      return newPartition
    }
  }

  /**
  //是否唯一
        IS_UNIQUE(1, "distinctCount"),
        //是否为空
        IS_EMPTY(2, "nullCount"),
        //是否规范
        IS_NORM(3, "regexCount"),
        AVG(4, "avg"),
        MAX(5, "max"),
        MIN(6, "min"),
        SUM(7, "sum"),
        //方差
        VARIANCE(8, "pop");
    */
  /**
    * 字段统计入口类
    *
    * @param spark
    * @param base64tables 245:dada_prd.dtw_bid_dwd_bas_email_d:age:8,1:dtos.test:age:1,1:dtos.test:age:2,1:dtos.test:age:3,1:dtos.test:age:5
    */
  def columnStat(spark: SparkSession, base64tables: String): Unit = {
    println("==========start==========")

    /**
      * 这里防止{}的出现导致任务运行失败 添加了一个 base64编码
      * 在内部解码
      */
    val tables = new String(Base64.getDecoder.decode(base64tables))
    // val encode = new String(Base64.getEncoder.encode(decode.getBytes()))


    //这里使用#@##  不是,做分隔符 避免和 正则匹配处 冲突
    val entityIdEntityNameMap = tables.split("#@##")
    for (entity <- entityIdEntityNameMap) {
      val entityId = entity.split(":").apply(0)
      val entityName = entity.split(":").apply(1)
      val column = entity.split(":").apply(2)
      val statType = entity.split(":").apply(3)
      //TODO 这个ds由外部传入.且支持多级分区。多个分区
      val ds = entity.split(":").apply(4);
      statType match {
        case "1" => distinctStat(spark,entityId,entityName,column,"distinctCount",ds)
        case "2" => nullStat(spark,entityId,entityName,column,"nullCount",ds)
        case "3" => regexCount(spark,entityId,entityName,column,"regexCount",ds)
        case "4" => simpleStat(spark,entityId,entityName,column,"avg",ds)
        case "5" => simpleStat(spark,entityId,entityName,column,"max",ds)
        case "6" => simpleStat(spark,entityId,entityName,column,"min",ds)
        case "7" => simpleStat(spark,entityId,entityName,column,"sum",ds)
        case "8" => simpleStat(spark,entityId,entityName,column,"var_pop",ds)
      }
    }
    println("==========end==========")
  }

  def simpleStat(spark: SparkSession, entityId:String,entityName: String,column:String,statType:String,ds:String): Unit = {
    try {
      val showDF = spark.sql(s"select $statType($column) as value from $entityName where ds=$ds")
      val value = showDF.collect().apply(0).get(0)
      println("entityId" + "##@#" +entityId + "##@#" + entityName + "##@#"+column+"."+statType+ "##@#"+value)
    } catch {
      case _: Exception =>
    }
  }

  def distinctStat(spark: SparkSession, entityId:String,entityName: String,column:String,statType:String,ds:String): Unit = {
    try {
      val showDF = spark.sql(s"select count(distinct($column)) as value from $entityName where ds=$ds")
      val value = showDF.collect().apply(0).get(0)
      println("entityId" + "##@#" +entityId + "##@#" + entityName + "##@#"+column+"."+statType+ "##@#"+value)
    } catch {
      case _: Exception =>
    }
  }

  def nullStat(spark: SparkSession, entityId:String,entityName: String,column:String,statType:String,ds:String): Unit = {
    try {
      val value = spark.sql(s"select $column as value from $entityName where ds=$ds").filter("value is null").count
      println("entityId" + "##@#" +entityId + "##@#" + entityName + "##@#"+column+"."+statType+ "##@#"+value)
    } catch {
      case _: Exception =>
    }
  }

  /**
    * 计算没有匹配规则的值
    * @param spark
    * @param entityId
    * @param entityName
    * @param columnAndRegex
    * @param statType
    * @param ds
    */
  def regexCount(spark: SparkSession, entityId:String,entityName: String,columnAndRegex:String,statType:String,ds:String): Unit = {

    val column = columnAndRegex.split("##@#").apply(0)
    val regex = columnAndRegex.split("##@#").apply(1)
    //正则匹配成功则返回0 等于1 为没有匹配上的计算没有 匹配上的个数
    val value = spark.sql(
      s"""
         |select no_match_count($column,'$regex') as match from $entityName where ds=$ds
         """.stripMargin).filter("match = 1").count
    println("entityId" + "##@#" +entityId + "##@#" + entityName + "##@#"+column+"."+statType+ "##@#"+value)

  }


  /**
    * 合并分区服务
    */
  def mergePartition(spark: SparkSession, tables: String): Unit = {
    println("==========start==========")
    val entityIdEntityNameMap = tables.split(",")
    for (entity <- entityIdEntityNameMap) {
      val entityName = entity.split(":").apply(0)
      val partition = entity.split(":").apply(1)
      val realPartition = doPartition(partition)
      try {
        val mutiFilePartitionDf = spark.sql(s"select * from $entityName where $realPartition")
        if(mutiFilePartitionDf.rdd.partitions.length>10){
          mutiFilePartitionDf.coalesce(10).createOrReplaceTempView("mutiFilePartitionDf")

          //方式1:排除分区列 插入
          val cols = ""
          spark.sql(s"insert overwrite table $entityName partition($realPartition) select $cols from mutiFilePartitionDf")

          //TODO 方式2:直接插入动态分区
        }
      } catch {
        case _: Exception =>
      }
    }
    println("==========end==========")
  }


}


/**
  *

参数: 1:dtw.test_dqm_field:age:20171011,1:dtw.test_dqm_field:age:20171012

计算:


方差:

spark.sql("select var_pop(age) from dtw.test_dqm_field").show




spark.sql("select avg(age) from dtw.test_dqm_field").show
spark.sql("select sum(age) from dtw.test_dqm_field").show
spark.sql("select max(age) from dtw.test_dqm_field").show
spark.sql("select min(age) from dtw.test_dqm_field").show


字段唯一性：

spark.sql("select age as field,count(1) as count from dtw.test_dqm_field group by age").filter("count>1").count

是否为空：

spark.sql("select age as field from dtw.test_dqm_field where ds=20171011").filter("field is null").count

【正则匹配需要自己写一个udf  如果 匹配上就返回1 否则返回 0 根据是否有 0 判断是否有不符合规范的 做count】
是否符合格式：

spark.sql(“select  age ,udf(age) as uff from ddd where ds=2017 where uff=0”).count






scala> simpleStat(spark,"1","dtw.test_dqm_field","age","avg","20171011")
1##@#dtw.test_dqm_field##@#age.avg##@#3.0

scala> simpleStat(spark,"1","dtw.test_dqm_field","age","max","20171011")
1##@#dtw.test_dqm_field##@#age.max##@#4

scala> simpleStat(spark,"1","dtw.test_dqm_field","age","var_pop","20171011")
1##@#dtw.test_dqm_field##@#age.var_pop##@#1.0

scala> simpleStat(spark,"1","dtw.test_dqm_field","age","min","20171011")
1##@#dtw.test_dqm_field##@#age.min##@#2

scala> simpleStat(spark,"1","dtw.test_dqm_field","age","sum","20171011")
1##@#dtw.test_dqm_field##@#age.sum##@#6




  *
  *
  */
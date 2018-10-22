package com.lcc.hive.connect.test;

/**
 * Created by lcc on 2018/10/13.
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
//import org.apache.Hive.jdbc.HiveDriver;
//import org.apache.hadoop.hive.jdbc.HiveDriver;
//import com.facebook.fb303.FacebookService;
public class HiveJdbcTest1 {
//         private static String driverName = "org.apache.Hive.jdbc.HiveDriver";//Hive驱动名称
         private static String driverName = "org.apache.hadoop.hive.jdbc.HiveDriver";//Hive驱动名称
         private static String url = "jdbc:inceptor://10.0.120.162:10000/lcc_two";//连接Hive2服务的连接地址，Hive0.11.0以上版本提供了一个全新的服务：HiveServer2
         private static String user = "";//对HDFS有操作权限的用户
         private static String password = "";//在非安全模式下，指定一个用户运行查询，忽略密码
         private static String sql = "";
         private static ResultSet res;
         public static void main(String[] args) {
             try {
                 Class.forName(driverName);//加载HiveServer2驱动程序
                 Connection conn = DriverManager.getConnection(url, user, password);//根据URL连接指定的数据库
                 Statement stmt = conn.createStatement();
                 
                 //创建的表名
                 String tableName = "testHiveDriverTable";
                 

                 // 执行“show tables”操作
                 sql = "show tables '" + tableName + "'";
                 res = stmt.executeQuery(sql);
                 if (res.next()) {
                     System.out.println(res.getString(1));
                 }
                 
//                 // 执行“describe table”操作
//                 sql = "describe " + tableName;
//                 res = stmt.executeQuery(sql);
//                 while (res.next()) {
//                     System.out.println(res.getString(1) + "\t" + res.getString(2));
//                 }
//
//                 // 执行“load data into table”操作
//                 String filepath = "/home/hadoop/djt.txt";//Hive服务所在节点的本地文件路径
//                 sql = "load data local inpath '" + filepath + "' into table " + tableName;
//                 stmt.execute(sql);
//
//                 // 执行“select * query”操作
//                 sql = "select * from " + tableName;
//                 res = stmt.executeQuery(sql);
//                 while (res.next()) {
//                     System.out.println(res.getInt(1) + "\t" + res.getString(2));
//                 }
//
//                 // 执行“regular Hive query”操作，此查询会转换为MapReduce程序来处理
//                 sql = "select count(*) from " + tableName;
//                 res = stmt.executeQuery(sql);
//                 while (res.next()) {
//                     System.out.println(res.getString(1));
//                 }
                 conn.close();
                 conn = null;
             } catch (ClassNotFoundException e) {
                 e.printStackTrace();
                 System.exit(1);
             } catch (SQLException e) {
                 e.printStackTrace();
                 System.exit(1);
             }
         }
}
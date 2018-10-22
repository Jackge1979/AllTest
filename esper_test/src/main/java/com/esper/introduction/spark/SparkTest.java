package com.esper.introduction.spark;

import java.io.Serializable;
import java.util.*;

import com.espertech.esper.client.*;
import org.apache.spark.*;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.*;
import org.apache.spark.broadcast.Broadcast;
import org.apache.spark.streaming.*;
import org.apache.spark.streaming.api.java.*;
import scala.Tuple2;

/**
 * Created by lcc on 2018/9/25.
 */
public class SparkTest {

    public static void main(String[] args) throws  Exception {

        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("NetworkWordCount").set("spark.testing.memory",
                "2147480000");
        JavaStreamingContext jssc =  new JavaStreamingContext(conf, Durations.seconds(5));
        System.out.println(jssc);

        // Create a DStream that will connect to hostname:port, like
        // localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("lcc", 9999);

        JavaDStream<Apple> mapRDD = lines.map(new Function<String, Apple>() {
            @Override
            public Apple call(String s) throws Exception {
                String[] danci = s.split(" ");
                if( danci.length == 2 ){
                    Apple apple = new Apple();
                    apple.setId(Integer.parseInt(danci[0]));
                    apple.setPrice(Integer.parseInt(danci[1]));
                    return apple;
                }
                return null;
            }
        });

        String[]  arg = {"select avg(price) from com.esper.introduction.spark.Apple.win:length_batch(2)"};


        mapRDD.foreachRDD(new VoidFunction<JavaRDD<Apple>>() {
            @Override
            public void call(JavaRDD<Apple> appleJavaRDD) throws Exception {
                appleJavaRDD.foreach(new VoidFunction<Apple>() {
                    @Override
                    public void call(Apple apple) throws Exception {
                        if( apple != null ){
                            System.out.println("id: " + apple.getId());
                            System.out.println("proice: " + apple.getPrice());
                            EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
                            EPAdministrator admin = epService.getEPAdministrator();
                            String product = Apple.class.getName();
                            String epl = "select avg(price) from " + product + ".win:length_batch(4)";
                            EPStatement state = admin.createEPL(epl);
                            state.addListener(new AppleListener());
                            EPRuntime runtime = epService.getEPRuntime();
//                            EPRuntime runtime = ESperUtils.get(arg[0]);
                            System.out.print(runtime.toString());
                            runtime.sendEvent(apple);
                        }
                    }
                });
            }
        });

        jssc.start();
        //System.out.println(wordCounts.count());// Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate

    }

}

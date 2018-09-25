package com.esper.introduction.myself;

import com.esper.introduction.spark.Apple;
import com.esper.introduction.spark.ESperUtils;
import com.espertech.esper.client.EPRuntime;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.VoidFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;
import com.espertech.esper.client.*;

/**
 * Created by lcc on 2018/9/25.
 */
public class SparkTest {
    public static void main(String[] args) throws  Exception {


//        test1();
//        test2();
//        test3();
//        test4();
        test5();
    }


    /**
     * 求后一个事件ping次数  > 前一个事件的数据  说明数据是递增的 表示ping的次数增加，有可能发生死亡之ping攻击
     * @throws Exception
     */
    private static void test5()  throws Exception  {
        String[]  arg = {" select a.counts,a.timeStamp  from   pattern[every a=com.esper.introduction.myself.PingEntity   -> b=com.esper.introduction.myself.PingEntity  ] where a.counts < b.counts " };

        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
        EPAdministrator admin = epService.getEPAdministrator();
        String epl = arg[0];
        EPStatement state = admin.createEPL(epl);
        state.addListener(new AppleListener2());
        EPRuntime runtime = epService.getEPRuntime();



        int s = 0;
        for (int i=0;i< 10;i++){
            long timeStamp = System.currentTimeMillis()/1000+s;
//            System.out.println(10+","+timeStamp);
            PingEntity ping = new PingEntity();
            ping.setCounts(10);
            ping.setTimeStamp(timeStamp);
            runtime.sendEvent(ping);
            s++;
        }

        for (int i=0;i< 10;i++){
            long timeStamp = System.currentTimeMillis()/1000+s;
            int count = 10+i;
            PingEntity ping = new PingEntity();
//            System.out.println(count+","+timeStamp);
            ping.setCounts(count);
            ping.setTimeStamp(timeStamp);
            runtime.sendEvent(ping);
            s++;
        }

        for (int i=0;i< 10;i++){
            long timeStamp = System.currentTimeMillis()/1000+s;
//            System.out.println(10+","+timeStamp);
            PingEntity ping = new PingEntity();
            ping.setCounts(10);
            ping.setTimeStamp(timeStamp);
            runtime.sendEvent(ping);
            s++;
        }



    }


    /**
     * 求后一个事件ping次数  > 前一个事件的数据  说明数据是递增的 表示ping的次数增加，有可能发生死亡之ping攻击
     * @throws Exception
     */
    private static void test4()  throws Exception  {
        String[]  arg = {" select a.counts,a.timeStamp  from   pattern[every a=com.esper.introduction.myself.PingEntity   -> b=com.esper.introduction.myself.PingEntity  ] where a.counts < b.counts " };
        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("NetworkWordCount").set("spark.testing.memory",
                "2147480000");
        JavaStreamingContext jssc =  new JavaStreamingContext(conf, Durations.seconds(5));
        System.out.println(jssc);

        // Create a DStream that will connect to hostname:port, like
        // localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("lcc", 9999);
        JavaDStream<PingEntity> mapRDD = lines.map(new Function<String, PingEntity>() {
            @Override
            public PingEntity call(String s) throws Exception {
                String[] danci = s.split(",");
                PingEntity apple = new PingEntity();
                apple.setCounts(Integer.parseInt(danci[0].trim()));
                apple.setTimeStamp(Long.parseLong(danci[1].trim()));
                EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
                EPAdministrator admin = epService.getEPAdministrator();
                String epl =arg[0];
                EPStatement state = admin.createEPL(epl);
                if( danci.length == 2 ){
                    state.addListener(new AppleListener2());
                    EPRuntime runtime = epService.getEPRuntime();
//                            System.out.println(runtime.toString());
                    runtime.sendEvent(apple);


                }
                return null;
            }
        });

        mapRDD.foreachRDD(new VoidFunction<JavaRDD<PingEntity>>() {
            @Override
            public void call(JavaRDD<PingEntity> appleJavaRDD) throws Exception {
                appleJavaRDD.foreach(new VoidFunction<PingEntity>() {
                    @Override
                    public void call(PingEntity apple) throws Exception {
                    }
                });
            }
        });

        jssc.start();
        //System.out.println(wordCounts.count());// Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }


    private static void test3()  throws Exception  {
        String[]  arg = {"select count(*), sum(counts)   from com.esper.introduction.myself.PingEntity(counts > 10) "};
        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("NetworkWordCount").set("spark.testing.memory",
                "2147480000");
        JavaStreamingContext jssc =  new JavaStreamingContext(conf, Durations.seconds(5));
        System.out.println(jssc);

        // Create a DStream that will connect to hostname:port, like
        // localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("lcc", 9999);
        JavaDStream<PingEntity> mapRDD = lines.map(new Function<String, PingEntity>() {
            @Override
            public PingEntity call(String s) throws Exception {
                String[] danci = s.split(",");
                if( danci.length == 2 ){
                    PingEntity apple = new PingEntity();
                    apple.setCounts(Integer.parseInt(danci[0].trim()));
                    apple.setTimeStamp(Long.parseLong(danci[1].trim()));
                    EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
                    EPAdministrator admin = epService.getEPAdministrator();
                    String product = PingEntity.class.getName();
                    String epl =arg[0];
                    EPStatement state = admin.createEPL(epl);
                    state.addListener(new AppleListener());
                    EPRuntime runtime = epService.getEPRuntime();
//                            System.out.println(runtime.toString());
                    runtime.sendEvent(apple);

                    return apple;
                }
                return null;
            }
        });

        mapRDD.foreachRDD(new VoidFunction<JavaRDD<PingEntity>>() {
            @Override
            public void call(JavaRDD<PingEntity> appleJavaRDD) throws Exception {
                appleJavaRDD.foreach(new VoidFunction<PingEntity>() {
                    @Override
                    public void call(PingEntity apple) throws Exception {
                    }
                });
            }
        });

        jssc.start();
        //System.out.println(wordCounts.count());// Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }


    /**
     * 每个数据打印好多遍
     *
     * @throws Exception
     */
    private static void test2()  throws Exception  {
        String[]  arg = {"select *  from com.esper.introduction.myself.PingEntity(counts > 10) "};
        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("NetworkWordCount").set("spark.testing.memory",
                "2147480000");
        JavaStreamingContext jssc =  new JavaStreamingContext(conf, Durations.seconds(5));
        System.out.println(jssc);

        // Create a DStream that will connect to hostname:port, like
        // localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("lcc", 9999);
        JavaDStream<PingEntity> mapRDD = lines.map(new Function<String, PingEntity>() {
            @Override
            public PingEntity call(String s) throws Exception {
                String[] danci = s.split(",");
                if( danci.length == 2 ){
                    PingEntity apple = new PingEntity();
                    apple.setCounts(Integer.parseInt(danci[0].trim()));
                    apple.setTimeStamp(Long.parseLong(danci[1].trim()));
                    EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
                    EPAdministrator admin = epService.getEPAdministrator();
                    String product = PingEntity.class.getName();
                    String epl =arg[0];
                    EPStatement state = admin.createEPL(epl);
                    state.addListener(new AppleListener2());
                    EPRuntime runtime = epService.getEPRuntime();
//                            System.out.println(runtime.toString());
                    runtime.sendEvent(apple);

                    return apple;
                }
                return null;
            }
        });

        mapRDD.foreachRDD(new VoidFunction<JavaRDD<PingEntity>>() {
            @Override
            public void call(JavaRDD<PingEntity> appleJavaRDD) throws Exception {
                appleJavaRDD.foreach(new VoidFunction<PingEntity>() {
                    @Override
                    public void call(PingEntity apple) throws Exception {
                    }
                });
            }
        });

        jssc.start();
        //System.out.println(wordCounts.count());// Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate
    }


    private static void test1()  throws Exception  {
        String[]  arg = {"select count(*), sum(counts) from com.esper.introduction.myself.PingEntity"};
        testbase(arg );
    }


    private static void testbase(String[] arg) throws Exception {

        SparkConf conf = new SparkConf().setMaster("local[4]").setAppName("NetworkWordCount").set("spark.testing.memory",
                "2147480000");
        JavaStreamingContext jssc =  new JavaStreamingContext(conf, Durations.seconds(5));
        System.out.println(jssc);

        // Create a DStream that will connect to hostname:port, like
        // localhost:9999
        JavaReceiverInputDStream<String> lines = jssc.socketTextStream("lcc", 9999);
        JavaDStream<PingEntity> mapRDD = lines.map(new Function<String, PingEntity>() {
            @Override
            public PingEntity call(String s) throws Exception {
                String[] danci = s.split(",");
                if( danci.length == 2 ){
                    PingEntity apple = new PingEntity();
                    apple.setCounts(Integer.parseInt(danci[0].trim()));
                    apple.setTimeStamp(Long.parseLong(danci[1].trim()));
                    EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
                    EPAdministrator admin = epService.getEPAdministrator();
                    String product = PingEntity.class.getName();
                    String epl =arg[0];
                    EPStatement state = admin.createEPL(epl);
                    state.addListener(new AppleListener());
                    EPRuntime runtime = epService.getEPRuntime();
//                            System.out.println(runtime.toString());
                    runtime.sendEvent(apple);

                    return apple;
                }
                return null;
            }
        });

        mapRDD.foreachRDD(new VoidFunction<JavaRDD<PingEntity>>() {
            @Override
            public void call(JavaRDD<PingEntity> appleJavaRDD) throws Exception {
                appleJavaRDD.foreach(new VoidFunction<PingEntity>() {
                    @Override
                    public void call(PingEntity apple) throws Exception {
                    }
                });
            }
        });

        jssc.start();
        //System.out.println(wordCounts.count());// Start the computation
        jssc.awaitTermination();   // Wait for the computation to terminate

    }


}

package com.lcc.hadoop.test;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.util.ThreadUtil;

import java.net.URI;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by lcc on 2018/10/19.
 */
public class SchemaTest{
    /** 缓存鉴权需要的文件系统清理线程的刷新时间，每隔离N秒重新获取一个文件系统 */
    private static final   long AUTHOR_TIME_CLEAN_INTERVAL = 10l;
    /** 每隔离N秒重新获取一个文件系统 */
    private static final   long AUTHOR_TIME_INTERVAL = 60*1l;


    /** 缓存鉴权需要的文件系统 */
    private static Map<String,FileSystemTuple<FileSystem,Long>> authFileSystemCache = new ConcurrentHashMap<String, FileSystemTuple<FileSystem, Long>>();


    /** 单线程，定期清理过时的鉴权文件系统fileAuthSystem */
    ScheduledExecutorService authFileSystemCacheClean =  newDaemonSingleThreadScheduledExecutor("filesystem-cleaner");

    private  SchemaTest(){

        /** 定期清理过时的权限鉴定文件系统 */
        authFileSystemCacheClean.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    for (Iterator<Map.Entry<String,FileSystemTuple<FileSystem,Long>> > it = authFileSystemCache.entrySet().iterator(); it.hasNext();){
                        Map.Entry<String,FileSystemTuple<FileSystem,Long>> item = it.next();
                        FileSystemTuple<FileSystem, Long> pair = item.getValue();
                        FileSystem fileSystem = pair.getFileSystem();
                        long oldTime = pair.getTime();
                        long newTime = System.currentTimeMillis() / 1000;
                        long interval = newTime - oldTime;
                        if( AUTHOR_TIME_INTERVAL < interval ){
                            // 一定要关闭
                            fileSystem.close();
                            it.remove();
                        }
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        }, 0l, AUTHOR_TIME_CLEAN_INTERVAL, TimeUnit.SECONDS);
    }



    public static void main(String[] args)throws Exception {
        Configuration conf = new Configuration();
        FileSystem newFs = FileSystem.get(new URI("hdfs://lcc:9000"), conf, "{\"isAllowAuth\":\"true\",\"tenantId\": 5,\"userId\": 23,\"checkHdfsUrl\":\"http://192.168.1.9:9018/api/v1/megrez/checkPrivilegesObject\"}");
        long time = System.currentTimeMillis() / 1000;
        FileSystemTuple<FileSystem, Long> newPair = new FileSystemTuple<>(newFs,time);
        authFileSystemCache.put("aa",newPair);
        for (Iterator<Map.Entry<String,FileSystemTuple<FileSystem,Long>> > it = authFileSystemCache.entrySet().iterator(); it.hasNext();){
            Map.Entry<String,FileSystemTuple<FileSystem,Long>> item = it.next();
            FileSystemTuple<FileSystem, Long> pair = item.getValue();
            FileSystem fileSystem = pair.getFileSystem();
            long oldTime = pair.getTime();
            long newTime = System.currentTimeMillis() / 1000;
            long interval = newTime - oldTime;

                fileSystem.close();
                it.remove();
        }

        System.out.println("aa");
    }




    /**
     * Wrapper over ScheduledThreadPoolExecutor.
     * 在ScheduledThreadPoolExecutor包装器。
     */
    public static ScheduledExecutorService newDaemonSingleThreadScheduledExecutor(String threadName)  {
        ThreadFactory threadFactory = new ThreadFactoryBuilder().setDaemon(true).setNameFormat(threadName).build();
        ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory);
        // By default, a cancelled task is not automatically removed from the work queue until its delay
        // elapses. We have to enable it manually.
        executor.setRemoveOnCancelPolicy(true);
        return executor;
    }

}

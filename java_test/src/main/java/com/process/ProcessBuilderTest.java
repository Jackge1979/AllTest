package com.process;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lcc on 2018/9/14.
 */
public class ProcessBuilderTest {


    public static void main(String[] args){

//        test1();
//        test2();
        test3();

    }


    private static void test3() {

        List<String> command = new ArrayList<>();
        command.add("java");
        command.add("-cp");
        command.add(".:/Users/lcc/IdeaProjects/AllTest/druid_test/target/druid_test-1.0-SNAPSHOT.jar");
        command.add("com.sql.parse.self.MainTest");
        command.add("-args");
        command.add("aaaa");
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream in=process.getInputStream();
            byte[] re=new byte[1024];
            while (in.read(re)!= -1) {
                System.out.println("==>"+new String(re));
            }
            in.close();
            if(process.isAlive()){
                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private static void test2() {

        try {
            // 会初始化
            Class clazz2 = Class.forName("com.sql.parse.druid.ParseFunction");
            Method method = clazz2.getMethod("parseSqlTable", String.class);
            method.invoke("show databases","MYSQL");
        }catch (Exception e){
            e.printStackTrace();
        }

    }






    private static void test1() {
        
        List<String> command = new ArrayList<>();
        command.add("/bin/ls");
        command.add("/Users/lcc/IdeaProjects/AllTest/");
        try {
            ProcessBuilder builder = new ProcessBuilder(command);
            builder.redirectErrorStream(true);
            Process process = builder.start();
            InputStream in=process.getInputStream();
            byte[] re=new byte[1024];
            while (in.read(re)!= -1) {
                System.out.println("==>"+new String(re));
            }
            in.close();
            if(process.isAlive()){
                process.waitFor();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


}

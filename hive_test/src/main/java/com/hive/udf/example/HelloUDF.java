package com.hive.udf.example;

import org.apache.hadoop.hive.ql.exec.UDF;
import org.apache.hadoop.io.Text;

/**
 * Created by lcc on 2018/8/17.
 */
public class HelloUDF extends UDF {

    public Text evaluate(Text body){

        return new Text("hello"+body);
    }

    public static void main(String[] args){
        HelloUDF udf = new HelloUDF();
        Text result = udf.evaluate(new Text("张三"));
        System.out.println(result.toString());
    }

}

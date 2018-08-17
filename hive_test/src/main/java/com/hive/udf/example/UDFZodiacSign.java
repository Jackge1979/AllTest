package com.hive.udf.example;

import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDF;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lcc on 2018/8/16.
 */
@Description(name="zodiac",value=" UDFZodiacSign function")
public class UDFZodiacSign  extends UDF {

    private SimpleDateFormat df;

    public UDFZodiacSign(SimpleDateFormat df) {
        this.df = new SimpleDateFormat("MM-dd-yyy");
    }

    public String evaluate(String body){
        Date date = null;
        try{
            date = df.parse(body);
        }catch (Exception e){
            return null;
        }
        return this.evaluate(date.getMonth()+1,date.getDate());
    }

    public String evaluate(Integer month,Integer day){
        if(month ==1){
            if(day < 20){
                return "Capricron";
            }else {
                return "Aquarius";
            }
        }
        if(month == 2 ){
            if(day < 19){
                return "Aquarius";
            }else {
                return "Pisces";
            }
        }

        return null;
    }

}

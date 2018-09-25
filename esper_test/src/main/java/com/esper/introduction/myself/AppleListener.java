package com.esper.introduction.myself;

/**
 * Created by lcc on 2018/9/21.
 */

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public  class AppleListener implements UpdateListener
{

    public void update(EventBean[] newEvents, EventBean[] oldEvents)
    {

        long name = (long) newEvents[0].get("count(*)");
        int age = (int) newEvents[0].get("sum(counts)");
//        long age = (long) newEvents[0].get("timeStamp");
        System.out.println("Name:"+name+", Age:"+age );
//        System.out.println("Name:"+name );

//        if (newEvents != null)
//        {
//            Double avg = (Double) newEvents[0].get("avg(counts)");
//            System.out.println("Apple's average price is " + avg);
//        }
    }

}
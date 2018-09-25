package com.esper.introduction.myself;

/**
 * Created by lcc on 2018/9/21.
 */

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public  class AppleListener2 implements UpdateListener
{

    public void update(EventBean[] newEvents, EventBean[] oldEvents)
    {

        int name = (int) newEvents[0].get("a.counts");
        long age = (long) newEvents[0].get("a.timeStamp");
        System.out.println("counts:"+name+", timeStamp:"+age );
//        System.out.println("Name:"+name );

//        if (newEvents != null)
//        {
//            Double avg = (Double) newEvents[0].get("avg(counts)");
//            System.out.println("Apple's average price is " + avg);
//        }
    }

}
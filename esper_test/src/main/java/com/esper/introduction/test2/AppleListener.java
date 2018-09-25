package com.esper.introduction.test2;

/**
 * Created by lcc on 2018/9/21.
 */

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public  class AppleListener implements UpdateListener
{

    public void update(EventBean[] newEvents, EventBean[] oldEvents)
    {
        if (newEvents != null)
        {
            Double avg = (Double) newEvents[0].get("avg(price)");
            System.out.println("Apple's average price is " + avg);
        }
    }

}
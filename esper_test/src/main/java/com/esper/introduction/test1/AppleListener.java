package com.esper.introduction.test1;

import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

class AppleListener implements UpdateListener
{
    public void update(EventBean[] newEvents, EventBean[] oldEvents)
    {
        if (newEvents != null)
        {
            Long count = (Long) newEvents[0].get("count(1)");
            System.out.println(count);
        }
    }
}

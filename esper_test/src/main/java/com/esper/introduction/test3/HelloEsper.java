package com.esper.introduction.test3;

import com.espertech.esper.client.Configuration;
import com.espertech.esper.client.EPAdministrator;
import com.espertech.esper.client.EPRuntime;
import com.espertech.esper.client.EPServiceProvider;
import com.espertech.esper.client.EPServiceProviderManager;
import com.espertech.esper.client.EPStatement;
import com.espertech.esper.client.EventBean;
import com.espertech.esper.client.UpdateListener;

public class HelloEsper {

    public static void main(String[] args) {

        /* 设置配置信息 */
        Configuration config = new Configuration();
        config.addEventType("myEvent", MyEvent.class); //添加事件类型定义

        /* 创建引擎实例 */
        EPServiceProvider provider = EPServiceProviderManager.getDefaultProvider(config);

        /* 创建statement的管理接口实例 */
        EPAdministrator admin = provider.getEPAdministrator();
        //admin.createEPL("create schema myEvent as com.esper.test.helloesper.MyEvent");
        EPStatement statement = admin.createEPL("select id, name from myEvent"); //创建EPL查询语句实例，功能：查询所有进入的myEvent事件
        statement.addListener(new UpdateListener() { //为statement实例添加监听
            @Override
            public void update(EventBean[] newEvents, EventBean[] oldEvents) {
                for(EventBean eb : newEvents) {
                    System.out.println("id:"+eb.get("id") + " name:"+eb.get("name"));
                }
            }
        });

        /* 引擎实例运行接口，负责为引擎实例接收数据并发送给引擎处理 */
        EPRuntime er = provider.getEPRuntime();
        er.sendEvent(new MyEvent(1,"aaa"));  //发送事件
    }

}
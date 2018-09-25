package com.esper.introduction.spark;

import com.espertech.esper.client.*;

/**
 * Created by lcc on 2018/9/25.
 */
public class ESperUtils {

    public static  EPRuntime get(String sql){

        EPServiceProvider epService = EPServiceProviderManager.getDefaultProvider();
        EPAdministrator admin = epService.getEPAdministrator();
        String product = Apple.class.getName();
//        String epl = "select avg(price) from " + product + ".win:length_batch(4)";
        EPStatement state = admin.createEPL(sql);
        state.addListener(new AppleListener());
        EPRuntime runtime = epService.getEPRuntime();
        return runtime;
    }
}

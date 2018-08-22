package com.dtwave.dipper.megrez.server.lineage;



import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author hulb
 * @date 2018/4/18 下午8:36
 */
public class FunctionState {

    private static final List<String> FUNCTION_LIST = new ArrayList<>();
    private static final Map<String, List<String>> sqlFunctionMap = new HashMap<>();
    private static final Map<String, LineageParseResult> sqlResultMap = new HashMap<>();


    public static void setCommandFunction(String command, String function) {
        if (sqlFunctionMap.containsKey(command)) {
            sqlFunctionMap.get(command).add(function);
        } else {
            List<String> list = new ArrayList<>();
            list.add(function);
            sqlFunctionMap.put(command, list);
        }
    }

    public static List<String> getCommandFunction(String command) {
        if (sqlFunctionMap.containsKey(command)) {
            return sqlFunctionMap.get(command);
        } else {
            return null;
        }
    }


    public static LineageParseResult getCommandResult(String command) {
        if (sqlResultMap.containsKey(command)) {
            return sqlResultMap.get(command);
        } else {
            return null;
        }
    }


    public static void setCommandResult(String command, LineageParseResult lineageParseResult) {
        if (!sqlResultMap.containsKey(command)) {
            sqlResultMap.put(command, lineageParseResult);
        }
    }

    /**
     * Singleton Session object per thread.
     **/
    private static ThreadLocal<FunctionStates> fst = new ThreadLocal<FunctionStates>() {
        @Override
        protected FunctionStates initialValue() {
            return new FunctionStates();
        }
    };

    public static FunctionState get() {
        return fst.get().state;
    }

    public static void add(String fun) {
        FUNCTION_LIST.add(fun);
    }

    public static void clear() {
        //FUNCTION_LIST = new ArrayList<>();
    }

//    public void SessionState start() {
//        FunctionState ss = new FunctionState();
//        fst.get().attach(ss);
//    }

    private static class FunctionStates {
        private FunctionState state;

        private void attach(FunctionState state) {
            this.state = state;
        }
    }

}

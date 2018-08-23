package com.dtwave.dipper.megrez.server.lineage;

import org.apache.hadoop.hive.conf.HiveConf;
import org.apache.hadoop.hive.metastore.api.Schema;
import org.apache.hadoop.hive.ql.Context;
import org.apache.hadoop.hive.ql.Driver;
import org.apache.hadoop.hive.ql.QueryPlan;
import org.apache.hadoop.hive.ql.hooks.DtwaveLineageLogger;
import org.apache.hadoop.hive.ql.hooks.HookContext;
import org.apache.hadoop.hive.ql.hooks.LineageInfo;
import org.apache.hadoop.hive.ql.lib.Node;
import org.apache.hadoop.hive.ql.lib.NodeProcessor;
import org.apache.hadoop.hive.ql.lib.NodeProcessorCtx;
import org.apache.hadoop.hive.ql.metadata.Hive;
import org.apache.hadoop.hive.ql.optimizer.lineage.ExprProcCtx;
import org.apache.hadoop.hive.ql.optimizer.lineage.LineageCtx;
import org.apache.hadoop.hive.ql.parse.*;
import org.apache.hadoop.hive.ql.plan.HiveOperation;
import org.apache.hadoop.hive.ql.session.SessionState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spark_project.jetty.util.security.Credential;
import com.dtwave.dipper.megrez.server.lineage.Entity;
import org.apache.hadoop.hive.ql.hooks.Entity.Type;

import java.util.List;
import java.util.Stack;

/**
 * Created by lcc on 2018/8/21.
 */
public class HiveParserServiceImpl  implements NodeProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(HiveParserServiceImpl.class);
    private static final String QUERY_ID = "query_id";
    private static final String QUERY_FLAG = "QUERY";

    private Context sqlContext;
    private ParseDriver parseDriver;
//    private QueryState queryState;
    HiveConf conf ;


    public HiveParserServiceImpl(String metaStoreUrls) {
        try {
            conf = new HiveConf();
            conf.setVar(HiveConf.ConfVars.METASTOREURIS,metaStoreUrls);
            conf.setVar(HiveConf.ConfVars.POSTEXECHOOKS, "org.apache.hadoop.hive.ql.hooks.DtwaveLineageLogger");
            conf.setVar(HiveConf.ConfVars.DYNAMICPARTITIONINGMODE, "nonstrict");
            SessionState.start(conf);
            LOGGER.info("建立连接hive metastore成功, uris: {}", metaStoreUrls);
            this.sqlContext = new Context(conf);
            this.parseDriver = new ParseDriver();
//            this.queryState = new QueryState(conf);
        } catch (Exception e) {
            LOGGER.error("建立连接hive metastore服务出错, 错误: ", e);
        }
    }


    @Override
    public Object process(Node nd, Stack<Node> stack, NodeProcessorCtx procCtx, Object... nodeOutputs) throws SemanticException {
        ExprProcCtx epc = (ExprProcCtx) procCtx;
        LineageCtx lc = epc.getLineageCtx();
        LineageCtx.Index index = lc.getIndex();
        return null;
    }

    private String combine(String command,String currentDatabase){
        return Credential.MD5.MD5.digest(command+currentDatabase);
    }



    public LineageParseResult parse(String command,String currentDatabase,Boolean lineageFlag) throws Exception {
      /*  if(FunctionState.getCommandResult(combine(command,currentDatabase))!=null){
            return FunctionState.getCommandResult(combine(command,currentDatabase));
        }*/
        SessionState.get().setCurrentDatabase(currentDatabase);
        LOGGER.info("要设置的数据库:{}  \t 当前数据库 ： ",currentDatabase,SessionState.get().getCurrentDatabase());
        Hive.get().reloadFunctions();
        ASTNode tree = parseDriver.parse(command, sqlContext,false);
        tree = ParseUtils.findRootNonNullToken(tree);
        BaseSemanticAnalyzer sem = SemanticAnalyzerFactory.get(conf, tree);
        /**
         * （在Hive原有Driver代码中：sem.analyze(tree, ctx); 构建成功血缘依赖。）
         *
         * 在这次analyse会把tree 翻译成index lineage info
         *
         * Optimizer 的optimize方法。遍历transformations 对pctx进行转换
         * transformations  中有一个Transform 的实现：Generator（负责从plan中生成表和字段的血缘关系）
         * 涉及到的关键类为：org.apache.hadoop.hive.ql.optimizer.lineage.Generator
         * 在这个类的transform中 添加了字段依赖关系。
         *
         * 最终添加到在SessionState的ls的depMap的Index内。
         * 血缘解析时 根据这个Index 构建字段血缘关系。
         */
        SessionState.get().initTxnMgr(conf);
        sem.analyze(tree, sqlContext);
        String optTypeName = SessionState.get().getHiveOperation().getOperationName().toString();
//        String optTypeName = queryState.getHiveOperation().getOperationName().toString();
        LOGGER.info("输入表: {}", sem.getInputs());
        LOGGER.info("输出表: {}", sem.getOutputs());
//        List<String> functionList = FunctionListThreadLocal.threadLocal.get();
//        functionList.stream().forEach(functionName -> {
//            LOGGER.info("解析出functionName:{}",functionName);
//        });
        MegrezLineageDto megrezLineageDto = null;
        List<String> fieldList = null;
        /**
         * 如果开启了字段血缘解析功能
         */
        if(optTypeName.equals(QUERY_FLAG) && lineageFlag){
//            Schema schema = HiveParseUtil.getSchema(sem, SessionState.get().getConf());
            QueryPlan queryPlan = new QueryPlan(command, sem, System.currentTimeMillis(),
                    QUERY_ID, optTypeName);
            HookContext hookContext = new HookContext(queryPlan, conf, sqlContext.getPathToCS(), SessionState.get().getUserName(),
                    SessionState.get().getUserIpAddress(), QUERY_ID);
            hookContext.setHookType(HookContext.HookType.POST_EXEC_HOOK);
            DtwaveLineageLogger dtwaveLineageLogger = new DtwaveLineageLogger();
            dtwaveLineageLogger.run(hookContext);
            String lineageString = null;
            try {
                megrezLineageDto = HiveLineageInfo.parseLineageString(lineageString);
                fieldList = HiveLineageInfo.parseColumn(lineageString);
            }catch (Exception e){
                LOGGER.error("血缘解析异常:{}",e);
            }
        }
        //构造输出
        LineageParseResult result = new LineageParseResult();
        result.setCommand(command);
        result.setOptType(optTypeName);
        result.setMegrezLineageDto(megrezLineageDto);
        result.setFieldList(fieldList);
        sem.getInputs().forEach(e -> {
            if (e.getTyp().equals(Type.TABLE)) {
                result.getInputList()
                        //输入表的type定义为null
                        .add(new Entity(e.getTable().getDbName()
                                , e.getTable().getTableName()
                                , e.getType().name(),null));
            }
        });

        sem.getOutputs().forEach(e -> {
            if (e.getTyp().equals(Type.TABLE) || e.getTyp().equals(Type.PARTITION)) {
                result.getOutputList()
                        .add(new Entity(e.getTable().getDbName()
                                , e.getTable().getTableName(),e.getType().name()
                                , e.getWriteType().name()));
            }
        });
        SessionState.get().close();
        LOGGER.info(result.toString());
        FunctionState.setCommandResult(combine(command,currentDatabase),result);
        return result;
    }

    /**
     * 解析SQL 得到输入、输出字段级别血缘
     *
     * @param command 待解析的SQL
     * @return
     * @throws Exception
     */
    public ParseResult parseLineage(String command,String currentDatabase) throws Exception {
        return null;
    }



    public static void main(String[] args) throws Exception {
        HiveParserServiceImpl service = new HiveParserServiceImpl("thrift://localhost:9083");
        LOGGER.info("result-1: {}", service.parse("insert into myuser select * from sink","lccdb",true));

    }


}

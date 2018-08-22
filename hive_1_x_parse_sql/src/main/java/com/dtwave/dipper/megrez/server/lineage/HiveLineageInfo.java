package com.dtwave.dipper.megrez.server.lineage;

/**
 * 血缘解析类
 *
 * @author hulb
 * @date 2018/1/5 下午3:11
 */

import com.alibaba.fastjson.JSONObject;
import com.dtwave.common.util.Base64Util;
import com.dtwave.common.util.RegexUtil;
import com.dtwave.dipper.dubhe.node.entity.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.lib.*;
import org.apache.hadoop.hive.ql.parse.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HiveLineageInfo implements NodeProcessor {

    private static final Logger logger = LoggerFactory.getLogger(HiveLineageInfo.class);
    /**
     * Stores input tables in sql.
     */
    TreeSet<String> inputTableList = new TreeSet();

    /**
     * Stores output tables in sql.
     */
    TreeSet<String> OutputTableList = new TreeSet();

    /**
     * @return java.util.TreeSet
     */
    public TreeSet<String> getInputTableList() {
        return inputTableList;
    }

    /**
     * @return java.util.TreeSet
     */
    public TreeSet<String> getOutputTableList() {
        return OutputTableList;
    }

    /**
     * Implements the process method for the NodeProcessor interface.
     */
    @Override
    public Object process(Node nd, Stack stack, NodeProcessorCtx procCtx, Object... nodeOutputs)
            throws SemanticException {
        ASTNode pt = (ASTNode) nd;

        switch (pt.getToken().getType()) {

            case HiveParser.TOK_CREATETABLE:
                OutputTableList.add(BaseSemanticAnalyzer.getUnescapedName((ASTNode) pt.getChild(0)));
                break;
            case HiveParser.TOK_TAB:
                OutputTableList.add(BaseSemanticAnalyzer.getUnescapedName((ASTNode) pt.getChild(0)));
                break;

            case HiveParser.TOK_TABREF:
                ASTNode tabTree = (ASTNode) pt.getChild(0);
                String table_name = (tabTree.getChildCount() == 1)
                        ? BaseSemanticAnalyzer.getUnescapedName((ASTNode) tabTree.getChild(0))
                        : BaseSemanticAnalyzer.getUnescapedName((ASTNode) tabTree.getChild(0)) + "." + tabTree.getChild(1);
                inputTableList.add(table_name);
                break;
        }
        return null;
    }

    /**
     * parses given queryExist and gets the lineage info.
     *
     * @param query
     * @throws ParseException
     */
    public void getLineageInfo(String query) throws ParseException, SemanticException {

        //Get the AST tree
        ParseDriver pd = new ParseDriver();
        ASTNode tree = pd.parse(query);

        while ((tree.getToken() == null) && (tree.getChildCount() > 0)) {
            tree = (ASTNode) tree.getChild(0);
        }

        //initialize Event Processor and dispatcher.
        inputTableList.clear();
        OutputTableList.clear();

        // create a walker which walks the tree in a DFS manner while
        // maintaining
        // the operator stack. The dispatcher
        // generates the plan from the operator tree
        Map<Rule, NodeProcessor> rules = new LinkedHashMap<Rule, NodeProcessor>();

        // The dispatcher fires the processor corresponding to the closest
        // matching
        // rule and passes the context along
        Dispatcher disp = new DefaultRuleDispatcher(this, rules, null);
        GraphWalker ogw = new DefaultGraphWalker(disp);

        // Create a list of topop nodes
        ArrayList topNodes = new ArrayList();
        topNodes.add(tree);
        ogw.startWalking(topNodes, null);
    }

    /**
     * 解析表级别血缘返回结果
     * @param sql
     * @return
     */
    public static LineageResultDto lineage(String sql){
        LineageResultDto lineageResultDto = new LineageResultDto();
        lineageResultDto.setSql(sql);
        HiveLineageInfo hiveLineageInfo = new HiveLineageInfo();
        try {
            hiveLineageInfo.getLineageInfo(sql);
            lineageResultDto.setInput(hiveLineageInfo.getInputTableList());
            lineageResultDto.setOutput(hiveLineageInfo.getOutputTableList());
            return lineageResultDto;
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SemanticException e) {
            e.printStackTrace();
        }
        return lineageResultDto;
    }

    /**
     * 解析字段级别血缘返回结果
     * @param sql
     * @return
     */
    public static LineageResultDto columnLineage(String sql){
       return  null;
    }

    /**
     * 获得正真的执行sql
     * 解码source， 去除注释， 参数替换
     *
     * @param source   任务资源
     * @param parameter 参数
     * @return
     */
    public static String getRealSql(String source, String parameter) {
        String result = "";
        try {
            //解码source
            source = Base64Util.decoder(source);
            //去除注释
            source = removeAnnotation(source);
            //参数替换
            result = replaceParams(source, parameter);
            //去除tab健
            result = result.replaceAll("\t", " ");
        } catch (Exception e) {
            logger.warn("任务资源、参数解码替换失败,  source: {}, parameter: {}, exception: {}"
                    , source, parameter, e);
        }

        return result;
    }

    /**
     * sql 去除注释
     *
     * @param source
     * @return
     */
    private static String removeAnnotation(String source) {
        String[] splitArray = source.split("\n");

        StringBuilder result = new StringBuilder();
        //包含 "--" 的为注释，去除
        for (String str : splitArray) {
            if (!str.trim().startsWith("--")) {
                result.append(str).append(" ");
            }
        }

        return result.toString();
    }

    /**
     * 参数替换
     *
     * @param source
     * @param parameter
     * @return
     * @throws Exception
     */
    private static String replaceParams(String source, String parameter) throws Exception {
        //资源名称替换
        String result = source;
        //变量名称替换
        Map<String, String> paramsMap = buildParams(parameter);
        //代码中的参数标识
        List<String> params = RegexUtil.regexSelectList(source, "\\$\\{[\\w-_\\.]+(.\\w+)?\\}");
        for (String name : params) {
            if (null == paramsMap) {
                logger.info("变量替换出错, 参数传入为空");
            }
            String replaceParam = paramsMap.get(name.substring(2, name.length() - 1));
            if (null == replaceParam) {
                logger.info("变量替换出错, 变量" + name.substring(2, name.length() - 1) + "不存在");
            }
            result = result.replace(name, replaceParam);
        }
        return result;
    }



    /**
     * 构造需要的参数
     *
     * @param params
     * @return
     * @throws Exception
     */
    private static Map<String, String> buildParams(String params) throws Exception {
        if (null == params || params.equals("")) {
            return null;
        }

        params = Base64Util.decoder(params);
        Map<String, String> paramsMap = new HashMap<>();
        JSONObject.parseObject(params).forEach((k, v) -> paramsMap.put(k, String.valueOf(v)));
        return paramsMap;
    }

    /**
     * 按照分号切分SQL
     *
     * 需要处理 select '中国;你好' 或者 select "中国;你好" 中的分号
     *
     * @param source 输入的source
     * @return 替换后的SQL
     */
    public static List<String> split(String source) {
        String REPLACEMENT = "@@@@@@@@@@";

        List<String> resultList = new ArrayList<>();
        if (StringUtils.isEmpty(source)) {
            return resultList;
        }

        String regex = "(\'|\")[^\'\"]*;[^\'\"\n]*(\'|\")";
        //替换掉''及""号中的所有;号 替换为 replacement
        StringBuilder builder = new StringBuilder();
        int start = 0;
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(source);
        while (m.find()) {
            builder.append(source.substring(start, m.start()))
                    .append(m.group().replaceAll(";", REPLACEMENT));
            start = m.end();
        }
        builder.append(source.substring(start));
        String[] items = builder.toString().split(";");
        for (int i = 0; i < items.length; i++) {
            if (!items[i].trim().isEmpty()) {
                resultList.add(items[i].trim().replaceAll(REPLACEMENT, ";"));
            }
        }
        return resultList;
    }


    /**
     * 将Hive原生的ID型字段对应关系直接转换为实际的字符串字段血缘对应关系
     *
     * @param lineage   Hive原生血缘关系结构
     */
    public static MegrezLineageDto parseLineageString(String lineage) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(lineage);
        if(jsonObject==null){
            return null;
        }
        HiveLineageDto hiveLineageDto = JSONObject.parseObject(jsonObject.toJSONString(), HiveLineageDto.class);
        MegrezLineageDto megrezLineageDto = new MegrezLineageDto();
        parseLineage(hiveLineageDto, megrezLineageDto);
        return megrezLineageDto;
    }

    /**
     * 将Hive原生的ID型字段对应关系直接转换为实际的字符串字段血缘对应关系
     *
     * @param hiveLineageDto   Hive原生血缘关系结构
     * @param megrezLineageDto 自定义字段血缘关系结构
     */
    public static void parseLineage(HiveLineageDto hiveLineageDto,
                                    MegrezLineageDto megrezLineageDto) {
        List<Edge> edges = hiveLineageDto.getEdges();
        Map<Integer, Vertices> verticesMap = new HashMap<>();
        megrezLineageDto.setEdges(new ArrayList<>());
        megrezLineageDto.setDatabase(hiveLineageDto.getDatabase());
        megrezLineageDto.setUser(hiveLineageDto.getUser());
        megrezLineageDto.setQueryText(hiveLineageDto.getQueryText());
        hiveLineageDto.getVertices().forEach(vertices -> {
            verticesMap.put(vertices.getId(), vertices);
        });
        edges.forEach(edge -> {
            MegrezLineageEdge megrezLineageEdge = new MegrezLineageEdge();
            megrezLineageEdge.setSources(new ArrayList<>());
            megrezLineageEdge.setTargets(new ArrayList<>());
            edge.getSources().forEach(sourceId -> {
                megrezLineageEdge.getSources().add(verticesMap.get(sourceId).getVertexId());
            });
            edge.getTargets().forEach(targetId -> {
                megrezLineageEdge.getTargets().add(verticesMap.get(targetId).getVertexId());
            });
            megrezLineageDto.getEdges().add(megrezLineageEdge);
        });
    }

    /**
     * 将所有列转换成String List
     * @param lineageString
     * @return
     */
    public static List<String> parseColumn(String lineageString) {
        JSONObject jsonObject = (JSONObject) JSONObject.parse(lineageString);
        HiveLineageDto hiveLineageDto = JSONObject.parseObject(jsonObject.toJSONString(), HiveLineageDto.class);
        List<Vertices> vertices = hiveLineageDto.getVertices();
        List<String> columnList = new ArrayList<>();
        vertices.forEach(vertices1 -> {
            if(vertices1.getVertexType().equals("COLUMN")){
                columnList.add(vertices1.getVertexId());
            }
        });
        return columnList;
    }
}



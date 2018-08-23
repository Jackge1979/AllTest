package com.sql.parse.stored.procedure;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by lcc on 2018/8/23.
 */
public class StoreProcedure {





    public  void commonParse(String sql, String mysql) {

        // 正则 必须以 空格开头，紧接着中间字符串一次到多个，紧接着没有空格，紧接着是左括号，中间一些东西，右括号
        List<String> matchedKeys = RegexUtil.regexSelectList(sql, "\\s{1}\\S+\\s{0}\\((.*?)\\)");
        for(String functionString:matchedKeys){
            String functionName = functionString.split("\\(")[0];
            String functionParmms = functionString.split("\\(")[1];
            String params = functionParmms.substring(0,functionParmms.length()-1);

            GreenplumEntity greenplumEntity =  getStoreFunctionInDb( functionName);
            String  storeDesc = greenplumEntity.getProsrc();

            storeDesc = store;
            storeDesc = removeAnnotation(storeDesc);

            System.out.println("storeDesc:"+storeDesc);

            storeDesc = getFromatSql(storeDesc);
            System.out.println("storeDesc:"+storeDesc);

            String[] sqls = storeDesc.split("\\;");
            for(String storeSqls : sqls){
                System.out.println("storeSqls:" + storeSqls);
                if( storeSqls.trim().toUpperCase().startsWith("SELECT") ||
                        storeSqls.trim().toUpperCase().startsWith("DELETE") ||
                        storeSqls.trim().toUpperCase().startsWith("DROP") ||
                        storeSqls.trim().toUpperCase().startsWith("INSERT")
                        ) {

                    System.out.println("storeSqls2=====>:" + storeSqls);
                    CommonSelfParseTable ss = new CommonSelfParseTable();
                    ss.parsTable(storeSqls);

                }

            }

        }

        System.out.println(matchedKeys);
    }



    public  void parseSqlTable(String sql, String mysql) {

        // 正则 必须以 空格开头，紧接着中间字符串一次到多个，紧接着没有空格，紧接着是左括号，中间一些东西，右括号
        List<String> matchedKeys = RegexUtil.regexSelectList(sql, "\\s{1}\\S+\\s{0}\\((.*?)\\)");
        for(String functionString:matchedKeys){
            String functionName = functionString.split("\\(")[0];
            String functionParmms = functionString.split("\\(")[1];
            String params = functionParmms.substring(0,functionParmms.length()-1);
            GreenplumEntity greenplumEntity =  getStoreFunctionInDb( functionName);
            String  storeDesc = greenplumEntity.getProsrc();
            storeDesc = store;
            storeDesc = removeAnnotation(storeDesc);
            storeDesc = getFromatSql(storeDesc);
            String[] sqls = storeDesc.split("\\;");
            for(String storeSqls : sqls){
                System.out.println("语句=====>:" + storeSqls);
               if( storeSqls.trim().toUpperCase().startsWith("SELECT") ||
//                       storeSqls.trim().toUpperCase().startsWith("DELETE") ||
                       storeSqls.trim().toUpperCase().startsWith("DROP") ||
                       storeSqls.trim().toUpperCase().startsWith("INSERT")
                       ) {
                   System.out.println("语句=====>:" + storeSqls);

                   try {
                       String dbType = JdbcConstants.POSTGRESQL;
                       List<SQLStatement> stmtList = SQLUtils.parseStatements(storeSqls, dbType);
                       TreeSet<String> inputTreeSet = new TreeSet<String>();
                       TreeSet<String> outputTreeSet = new TreeSet<String>();
                       //解析出的独立语句的个数
                       System.out.println("size is:" + stmtList.size());
                       for (int i = 0; i < stmtList.size(); i++) {

                           SQLStatement stmt = stmtList.get(i);

                           PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
                           stmt.accept(visitor);
                           //获取表名称
                           Map<TableStat.Name, TableStat> tabmap = visitor.getTables();
                           makeInAndOut(tabmap, inputTreeSet, outputTreeSet);
                       }
                   } catch (com.alibaba.druid.sql.parser.ParserException e) {
                       continue;
                   }

               }

            }

        }

        System.out.println(matchedKeys);
    }


    private  void  makeInAndOut(Map<TableStat.Name, TableStat> tabmap,TreeSet<String> inputTreeSet,
                                      TreeSet<String> outputTreeSet ){
        for (Iterator iterator = tabmap.keySet().iterator(); iterator.hasNext(); ) {
            TableStat.Name name = (TableStat.Name) iterator.next();
            TableStat tableStat = tabmap.get(name);
            String tableName = name.toString();
            if(tableStat.toString().equals("Insert")){
                inputTreeSet.add(tableName);
            }
            if(tableStat.toString().equals("Select")){
                outputTreeSet.add(tableName);
            }
            if(tableStat.toString().equals("Drop")){
                outputTreeSet.add(tableName);
            }
            if(tableStat.toString().equals("Update")){
                outputTreeSet.add(tableName);
            }
            if(tableStat.toString().equals("Alter")){
                outputTreeSet.add(tableName);
            }
        }
        System.out.println("输入===>"+inputTreeSet.size() +"内容："+inputTreeSet);
        System.out.println("输出===>"+outputTreeSet.size() +"内容："+outputTreeSet);
    }


    /**
     * 获取数据库中的存储过程
     *
     * @param functionName
     */
    private  GreenplumEntity getStoreFunctionInDb(String functionName) {
        GreenplumEntity greenplumEntity =  new GreenplumEntity();
        try {
            String url = "jdbc:postgresql://47.92.38.137:8006/lcc_gp";
            String username = "lbq_test";
            String password = "lbq_test@dtwave";
            Class.forName("org.postgresql.Driver");
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlssss = "select * from pg_proc where proname='prc_test';";
            PreparedStatement pre = conn.prepareStatement(sqlssss);
            ResultSet rs = pre.executeQuery();
            while(rs.next()){
                System.out.println("===============》"+rs.getString("proname"));
                System.out.println("===============》"+rs.getString("proargnames"));
                System.out.println("===============》"+rs.getString("prosrc"));

                String proname = rs.getString("proname");
                String proargnames = rs.getString("proargnames");
                String prosrc = rs.getString("prosrc");

                greenplumEntity.setProname(proname);
                greenplumEntity.setProargnames(proargnames);
                greenplumEntity.setProsrc(prosrc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return greenplumEntity;

    }


    public static String getFromatSql(String sql) {
        // 去除字符串中的空格、回车、换行符、制表符
        // 去除分号，主要是sql最后末尾
        sql = sql.replaceAll("`", "");
        sql = sql.replaceAll("\\s", " ");
        sql = sql.replaceAll("\\t", " ");
        sql = sql.replaceAll("\\r", " ");
        sql = sql.replaceAll("\\n", " ");
        // 多个空格转换成一个空格
        sql = sql.replaceAll(" +", " ");
        // 将逗号两边的空格转换成一个逗号紧挨着的
        sql = sql.replaceAll(", ", ",");
        sql = sql.replaceAll(" ,", ",");
        sql = sql.replaceAll(" as ", " ");
        // 多个空格转换成一个空格
        sql = sql.replaceAll(" +", " ");
        return sql;
    }



    /**
     * sql 去除注释
     *
     * @param source  要分析的sql
     * @return        返回未注释的一行sql
     */
    public static String removeAnnotation(String source) {
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





    public final static String  store = "CREATE OR REPLACE FUNCTION report.proc_southern_fund_staff_trade_detail(v_period_id character)\n" +
            "RETURNS numeric AS \n" +
            "$BODY$\n" +
            "          \n" +
            "  --call by ds job\n" +
            "  --Author: zak\n" +
            "  --Creation: 20141126\n" +
            "  --Discription:向南方基金提供其员工及亲属的成交明细\n" +
            "  --            业务接口人：经纪业务总部 姚亮\n" +
            "  --            处理优先级：低\n" +
            "  --            时间要求：每月前三个交易日跑上月数据\n" +
            "  --            数据用途：按接口要求生成南方基金员工及亲属的成交数据,后续由ds job生成excel\n" +
            "  --Modification: 20141126 初始化\n" +
            "  --              20141201 改成每月前三个交易日跑上月数据\n" +
            "  declare v_program_name varchar(50);\n" +
            "  ln_line          integer;\n" +
            "  v_return         integer;\n" +
            "  v_next_tradedate integer;\n" +
            "  v_last_month_begin_tradedate integer;\n" +
            "  v_last_month_end_tradedate   integer;\n" +
            "BEGIN\n" +
            "  v_return       := 0;\n" +
            "  v_program_name := 'report.proc_southern_fund_staff_trade_detail';\n" +
            "\n" +
            "  ln_line := 1;\n" +
            "  if (dw_pub.get_month_begin_tradedate(v_period_id)<>v_period_id and \n" +
            "      dw_pub.get_month_begin_tradedate(v_period_id)<>dw_pub.get_last_n_tradedate(v_period_id, -1) AND      \n" +
            "      dw_pub.get_month_begin_tradedate(v_period_id)<>dw_pub.get_last_n_tradedate(v_period_id, -2)) then \n" +
            "     return 0;\n" +
            "  end if;\n" +
            "\n" +
            "  ln_line := 2;\n" +
            "  v_last_month_begin_tradedate := dw_pub.get_month_begin_tradedate(dw_pub.get_last_month_end_date(v_period_id));\n" +
            "  v_last_month_end_tradedate := dw_pub.get_month_end_tradedate(dw_pub.get_last_month_end_date(v_period_id));\n" +
            "\n" +
            "  ln_line := 10;\n" +
            "  delete from report.southern_fund_staff_trade_detail\n" +
            "   where init_date between 1 and 2;\n" +
            "   \n" +
            "  ln_line := 20;\n" +
            "  insert into report.southern_fund_staff_trade_detail\n" +
            "    (stock_account,\n" +
            "     market_name,\n" +
            "     stock_code,\n" +
            "     stock_name,\n" +
            "     stock_type,\n" +
            "     entrust_bs,\n" +
            "     business_flag,\n" +
            "     business_price,\n" +
            "     business_amount,\n" +
            "     business_balance,\n" +
            "     init_date,\n" +
            "     client_id,\n" +
            "     created_busi_date,\n" +
            "     last_modified_busi_date,\n" +
            "     creation_date,\n" +
            "     created_by,\n" +
            "     last_modification_date,\n" +
            "     last_modified_by)\n" +
            "    select a.stock_account,\n" +
            "           b.source_name as market_name,\n" +
            "           a.stock_code,\n" +
            "           a.stock_name,\n" +
            "           e.source_name as stock_type,\n" +
            "           case\n" +
            "             when a.entrust_bs = '1' then\n" +
            "              '买入'\n" +
            "             when a.entrust_bs = '2' then\n" +
            "              '卖出'\n" +
            "             else\n" +
            "              '其他'\n" +
            "           end as entrust_bs,\n" +
            "           coalesce(case\n" +
            "             when j.special_digest_flag is not null and\n" +
            "                  j.special_digest_flag = 1 then\n" +
            "              j.source_digest_name\n" +
            "             else\n" +
            "              i.source_digest_name\n" +
            "           end,'') as business_flag,\n" +
            "           a.business_price,\n" +
            "           a.business_amount,\n" +
            "           a.business_balance,\n" +
            "           a.init_date,\n" +
            "           a.client_id,\n" +
            "           v_period_id created_busi_date,\n" +
            "           v_period_id last_modified_busi_date,\n" +
            "           now() creation_date,\n" +
            "           v_program_name created_by,\n" +
            "           now() last_modification_date,\n" +
            "           v_program_name last_modified_by\n" +
            "      from his_ht_hs08_hsasset.deliver       a\n" +
            "      join dw_dim.market_map                 b\n" +
            "           on trim(a.exchange_type) = trim(b.source_code)\n" +
            "              and b.source_system_id = 1\n" +
            "      join dw_dim.security_type_map          e\n" +
            "           on trim(a.stock_type) = trim(e.source_code)\n" +
            "          and e.source_system_id = 1\n" +
            "      left join dw_dim.trade_direction_map h on trim(a.entrust_bs) =\n" +
            "                                                trim(h.source_code)\n" +
            "                                            and h.source_system_id =\n" +
            "                                                1\n" +
            "      left join dw_dim.digest_map i on trim(a.business_flag) =\n" +
            "                                       trim(i.source_digest_code)\n" +
            "                                   and i.source_system_id =\n" +
            "                                       1\n" +
            "                                   and i.special_digest_flag <> 1\n" +
            "      left join dw_dim.digest_map j on trim(a.business_flag) =\n" +
            "                                       trim(j.source_digest_code)\n" +
            "                                   and h.trade_direction_id =\n" +
            "                                       j.trade_direction_id\n" +
            "                                   and j.source_system_id =\n" +
            "                                       1\n" +
            "                                   and j.special_digest_flag = 1\n" +
            "     where a.business_price <> 0\n" +
            "       and exists (select null from dw_mnu.southern_fund_staff_list   d,\n" +
            "           dw_dim.hs_client_dim_unreconsiled f \n" +
            "         where (trim(d.id_no) = trim(f.idno) or trim(d.id_no)=trim(f.adjust_idno)) \n" +
            "       and trim(d.client_name) = trim(f.client_name)\n" +
            "       and f.source_system_id = 1\n" +
            "       and a.init_date between d.data_begin_date and d.data_end_date\n" +
            "       and d.valid_flag = 1\n" +
            "       and f.source_client_id = a.client_id)        \n" +
            "       and a.dc_business_date between v_last_month_begin_tradedate and v_last_month_end_tradedate;\n" +
            "    \n" +
            "     return v_return;\n" +
            "EXCEPTION\n" +
            "  when others then\n" +
            "    perform dw_pub.program_error(v_program_name || '[' || ln_line || ']',\n" +
            "                                 '程序' || v_program_name || '运行错误. ' ||\n" +
            "                                 SQLERRM,\n" +
            "                                 5,\n" +
            "                                 '',\n" +
            "                                 '',\n" +
            "                                 'greenplum db');\n" +
            "    return 1;\n" +
            "end; \n" +
            "$BODY$\n" +
            "LANGUAGE plpgsql VOLATILE;\n" +
            "\n" +
            "\n" +
            "\n";

}

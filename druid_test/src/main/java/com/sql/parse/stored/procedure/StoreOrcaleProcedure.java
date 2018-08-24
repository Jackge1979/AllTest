package com.sql.parse.stored.procedure;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.oracle.visitor.OracleSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.sql.*;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

/**
 * Created by lcc on 2018/8/23.
 */
public class StoreOrcaleProcedure {



    public  void parseSqlTable(String sql, String mysql) {

        // 正则 必须以 空格开头，紧接着中间字符串一次到多个，紧接着没有空格，紧接着是左括号，中间一些东西，右括号
        List<String> matchedKeys = RegexUtil.regexSelectList(sql, "\\s{1}\\S+\\s{0}\\((.*?)\\)");
        for(String functionString:matchedKeys){
            String functionName = functionString.split("\\(")[0];
            String functionParmms = functionString.split("\\(")[1];
            String params = functionParmms.substring(0,functionParmms.length()-1);
            String  storeSql =  getStoreFunctionInDb( functionName);
            if(storeSql == null ){
                continue;
            }
            String  storeDesc = storeSql;
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
                       String dbType = JdbcConstants.ORACLE;
                       List<SQLStatement> stmtList = SQLUtils.parseStatements(storeSqls, dbType);
                       TreeSet<String> inputTreeSet = new TreeSet<String>();
                       TreeSet<String> outputTreeSet = new TreeSet<String>();
                       //解析出的独立语句的个数
                       System.out.println("size is:" + stmtList.size());
                       for (int i = 0; i < stmtList.size(); i++) {

                           SQLStatement stmt = stmtList.get(i);

//                           PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
                           OracleSchemaStatVisitor visitor = new OracleSchemaStatVisitor();
                           
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
    private  String getStoreFunctionInDb(String functionName) {
        String storeSql = "";
        try {
            String url = "jdbc:oracle:thin:@47.97.11.138:1522:xe";
            String username = "system";
            String password = "oracle";
            Class.forName("oracle.jdbc.driver.OracleDriver");
            Connection conn = DriverManager.getConnection(url, username, password);
            String sqlssss = "select * from all_source where OWNER='ZYT' and name='P_TEST' ORDER BY line ASC";
            PreparedStatement ps = conn.prepareStatement(sqlssss);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                System.out.println("===============》"+rs.getString("TEXT"));
                String text = rs.getString("TEXT");
                storeSql = storeSql +  text;
            }

            System.out.println("orcale的存储过程"+storeSql);
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return storeSql;

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





    public final static String  store = "create PROCEDURE PROC_UPDATE_COMPOSITERATING AS\n" +
            "  CUR_MONTH NUMBER;\n" +
            "  CURSOR CUR IS\n" +
            "    SELECT -1 FROM DUAL UNION SELECT -2 FROM DUAL UNION SELECT -3 FROM DUAL UNION SELECT -6 FROM DUAL;\n" +
            "BEGIN\n" +
            "  EXECUTE IMMEDIATE 'DELETE FROM RPT_COMPOSITERATING';\n" +
            "  OPEN CUR;\n" +
            "  LOOP\n" +
            "    FETCH CUR\n" +
            "      INTO CUR_MONTH;\n" +
            "    EXIT WHEN CUR%NOTFOUND;\n" +
            "\n" +
            "    INSERT INTO RPT_COMPOSITERATING\n" +
            "      (ID,\n" +
            "       SECUCODE,\n" +
            "       TRADINGCODE,\n" +
            "       SECUABBR,\n" +
            "       STATISTICDATE,\n" +
            "       RATINGAGENCYNUM,\n" +
            "       BUYAGENCYNUM,\n" +
            "       INCREASEAGENCYNUM,\n" +
            "       NEUTRALAGENCYNUM,\n" +
            "       REDUCEAGENCYNUM,\n" +
            "       SELLAGENCYNUM,\n" +
            "       HIGHESTPRICE,\n" +
            "       LOWESTPRICE,\n" +
            "       AVGPRICE,\n" +
            "       MONTH,\n" +
            "       ENTRYTIME,\n" +
            "       UPDATETIME,\n" +
            "       GROUNDTIME,\n" +
            "       UPDATEID,\n" +
            "       RESOURCEID,\n" +
            "       RECORDID,\n" +
            "       PUBDATE)\n" +
            "      SELECT SEQ_ID.NEXTVAL,\n" +
            "             A.SECUCODE,\n" +
            "             A.TRADINGCODE,\n" +
            "             A.SECUABBR,\n" +
            "             TRUNC(SYSDATE) STATISTICDATE,\n" +
            "             A.RATINGAGENCYNUM,\n" +
            "             NVL(A.BUYAGENCYNUM, 0) AS BUYAGENCYNUM,\n" +
            "             NVL(A.INCREASEAGENCYNUM, 0) AS NCREASEAGENCYNUM,\n" +
            "             NVL(A.NEUTRALAGENCYNUM, 0) AS NEUTRALAGENCYNUM,\n" +
            "             NVL(A.REDUCEAGENCYNUM, 0) AS REDUCEAGENCYNUM,\n" +
            "             NVL(A.SELLAGENCYNUM, 0) AS SELLAGENCYNUM,\n" +
            "             NVL(B.HIGHESTPRICE, 0) AS HIGHESTPRICE,\n" +
            "             NVL(B.LOWESTPRICE, 0) AS LOWESTPRICE,\n" +
            "             NVL(B.AVGPRICE, 0) AS AVGPRICE,\n" +
            "             CUR_MONTH*-1 AS MONTH,\n" +
            "             SYSDATE,\n" +
            "             SYSDATE,\n" +
            "             SYSDATE,\n" +
            "             SEQ_ID.NEXTVAL,\n" +
            "             SEQ_ID.NEXTVAL,\n" +
            "             SEQ_ID.NEXTVAL,\n" +
            "             PUBDATE\n" +
            "        FROM (SELECT SECUCODE,\n" +
            "                     TRADINGCODE,\n" +
            "                     SECUABBR,\n" +
            "                     SUM(C) RATINGAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 1, C)) BUYAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 2, C)) INCREASEAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 3, C)) NEUTRALAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 4, C)) REDUCEAGENCYNUM,\n" +
            "                     SUM(DECODE(INVRATINGCODE, 5, C)) SELLAGENCYNUM\n" +
            "                FROM (SELECT a.SECUCODE,\n" +
            "                             a.TRADINGCODE,\n" +
            "                             b.SECUABBR,\n" +
            "                             INVRATINGCODE,\n" +
            "                             COUNT(*) C\n" +
            "                        FROM TEXT_FORECASTRATING a\n" +
            "                        JOIN pub_securitiesmain b ON a.secucode = b.secucode\n" +
            "                       WHERE TRUNC(PUBDATE) >= ADD_MONTHS(TRUNC(SYSDATE), CUR_MONTH)\n" +
            "                         AND a.SECUCODE != 0\n" +
            "                         AND INVRATINGCODE IN (1, 2, 3, 4, 5)\n" +
            "                       GROUP BY a.SECUCODE,\n" +
            "                                a.TRADINGCODE,\n" +
            "                                b.SECUABBR,\n" +
            "                                INVRATINGCODE)\n" +
            "               GROUP BY SECUCODE, TRADINGCODE, SECUABBR) A,\n" +
            "             (SELECT SECUCODE,MAX(T.PUBDATE) AS PUBDATE,\n" +
            "                     MAX(T.INDEXVAL) HIGHESTPRICE,\n" +
            "                     MIN(T.INDEXVAL) LOWESTPRICE,\n" +
            "                     TRUNC(AVG(T.INDEXVAL), 2) AVGPRICE\n" +
            "                FROM TEXT_PERFORMANCEFORECAST T\n" +
            "               WHERE T.PUBDATE > ADD_MONTHS(TRUNC(SYSDATE), CUR_MONTH)\n" +
            "                 AND T.INDEXCODE = 1190\n" +
            "               GROUP BY SECUCODE) B\n" +
            "       WHERE A.SECUCODE = B.SECUCODE(+);\n" +
            "\n" +
            "  END LOOP;\n" +
            "  CLOSE CUR;\n" +
            "  COMMIT;\n" +
            "END PROC_UPDATE_COMPOSITERATING;\n";

}

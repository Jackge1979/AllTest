package com.sql.parse.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.visitor.MySqlSchemaStatVisitor;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.TableStat;
import com.alibaba.druid.util.JdbcConstants;

import java.util.*;

/**
 * Created by lcc on 2018/8/1.
 * <p>
 * 只是解析出sql语句中的输入输出表，不解析字段
 */
public class ParseTable {

    private static  TreeSet<String> inputTreeSet = new TreeSet<String>() ;
    private static  TreeSet<String> outputTreeSet = new TreeSet<String>() ;


    public static void parseSqlTable(String realSql, String sqlType) {
        switch (sqlType) {
            case "HIVE":
                doHive(realSql);
                break;
            case "MYSQL":
                doMySQL(realSql);
                break;
            case "GREENPLUM":

                break;
            case "POSTGRES":

                break;
            case "LIBRA":

                break;
            default:
                break;
        }

    }

    private static void doHive(String realSql) {
    }

    private static void doMySQL(String realSql) {

        String dbType = JdbcConstants.MYSQL;
        String result = SQLUtils.format(realSql, dbType);
        List<SQLStatement> stmtList = SQLUtils.parseStatements(realSql, dbType);

        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);

//            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            MySqlSchemaStatVisitor visitor = new MySqlSchemaStatVisitor();
            stmt.accept(visitor);
//            Map<String, String> aliasmap = visitor.getAliasMap();
//            for (Iterator iterator = aliasmap.keySet().iterator(); iterator.hasNext(); ) {
//                String key = iterator.next().toString();
//                System.out.println("[ALIAS]" + key + " - " + aliasmap.get(key));
//            }
//            Set<TableStat.Column> groupby_col = visitor.getGroupByColumns();
//            //
//            for (Iterator iterator = groupby_col.iterator(); iterator.hasNext(); ) {
//                TableStat.Column column = (TableStat.Column) iterator.next();
//                System.out.println("[GROUP]" + column.toString());
//            }
            //获取表名称
            System.out.println("table names:");
            Map<TableStat.Name, TableStat> tabmap = visitor.getTables();
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
                System.out.println(name.toString() + " - " + tabmap.get(name).toString());

            }

        }

    }
}



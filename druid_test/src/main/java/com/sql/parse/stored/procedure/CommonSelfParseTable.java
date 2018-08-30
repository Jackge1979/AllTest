package com.sql.parse.stored.procedure;



import com.sql.parse.entity.entity.LineageResultDto;

import java.util.*;


/**
 * Created by lcc on 2018/7/26.
 *
 * 普通的sql解析器，只解析输入输出表
 */
public class CommonSelfParseTable {

    Set<String> newInputSet = new HashSet<String>();
    Set<String> newOnputSet = new HashSet<String>();


    public List<LineageResultDto> makeLineResult(List<String> sqlList, String realSql) {
        List<LineageResultDto> lineageResultDtoList = new ArrayList<LineageResultDto>();
        for (String sql : sqlList) {
            CommonSelfParseTable comSql = new CommonSelfParseTable();
            comSql.parsTable(realSql);

            LineageResultDto lineageResultDto = new LineageResultDto();
            lineageResultDto.setSql(sql);
            lineageResultDto.setInput(  comSql.hashSetToTreeset(comSql.getNewInputSet()) );
            lineageResultDto.setOutput( comSql.hashSetToTreeset(comSql.getNewOnputSet()) );

            lineageResultDtoList.add(lineageResultDto);

            newInputSet.clear();
            newOnputSet.clear();
        }
        return lineageResultDtoList;
    }


    public void parsTable(String sql) {
        sql = repacePGSqlSpecialFunction(sql);
        // 去除字符串中的空格、回车、换行符、制表符
        // 去除分号，主要是sql最后末尾
        sql = sql.replaceAll(";", " ");
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
        sql = sql.replaceAll("\\(", " ( ");
        sql = sql.replaceAll("\\)", " ) ");
        // 多个空格转换成一个空格
        sql = sql.replaceAll(" +", " ");

        Set<String> inputSet = new HashSet<>();
        Set<String> outSet = new HashSet<>();
        String[] newWord = sql.split(" ");

        for (int i = 0; i < newWord.length; i++) {
            String currentWord = newWord[i].trim().toLowerCase();
            String nextWord = null;
            if(i + 1 < newWord.length){
                nextWord = newWord[i+1].trim().toLowerCase();
            }
            String next2Word = null;
            if(i + 2 < newWord.length){
                next2Word = newWord[i+2].trim().toLowerCase();
            }

            if (currentWord.equals("") || currentWord.equals(" ")) {
                continue;
            }

            // 处理insert into 表名
            if (currentWord.equals("insert") && i + 1 < newWord.length && nextWord.equals("into") && i + 2 <= newWord.length) {
                String outputTable = newWord[i + 2];
                outSet.add(outputTable);
            }

            // 处理  insert overwrite table dwd_run1_smzj_step2  表名
            if (currentWord.equals("insert") && i + 1 < newWord.length && nextWord.equals("overwrite") && i + 2 <= newWord.length
                    && next2Word.equals("table") && i + 3 <= newWord.length) {
                String outputTable = newWord[i + 3];
                outSet.add(outputTable);
            }


            // 处理insert into 表名
            if ( currentWord.equals("create") && i + 1 < newWord.length && nextWord.equals("table") && i + 2 <= newWord.length) {
                // 处理 create table if not exists demo_users的情况
                if( next2Word.equals("if") && i + 5 < newWord.length ){
                    String outputTable = newWord[i + 5];
                    outSet.add(outputTable);
                }else{
                    String outputTable = newWord[i + 2];
                    outSet.add(outputTable);
                }
            }


            // 处理insert into 表名
            if (currentWord.equals("alter") && i + 1 < newWord.length && nextWord.equals("table") && i + 2 <= newWord.length) {
                String outputTable = next2Word;
                outSet.add(outputTable);
            }

            // 处理insert into 表名
            if (currentWord.equals("drop") && i + 1 < newWord.length && nextWord.equals("table") && i + 2 <= newWord.length) {
                // 处理 create table if not exists demo_users的情况
                if(newWord[i + 2].equals("if") && i + 4 <= newWord.length ){
                    String outputTable = newWord[i + 4];
                    outSet.add(outputTable);
                }else{
                    String outputTable = newWord[i + 2];
                    outSet.add(outputTable);
                }
            }

            // 处理insert into 表名
            if (currentWord.equals("delete") && i + 1 < newWord.length && nextWord.equals("table") && i + 2 <= newWord.length) {
                String outputTable = newWord[i + 2];
                outSet.add(outputTable);
            }

            if (currentWord.equals("from") && i + 1 <= newWord.length) {
                // 如果from后面是括号，说明后面是一个子查询,那么不能添加join后面的数据为表
                if ((i + 1) <= newWord.length && (nextWord.startsWith("(") || nextWord.startsWith("select"))) {
                    continue;
                }

                String inputTable = newWord[i + 1];
                inputSet.add(inputTable);

                // 如果from后面是连续的多个表，而且有别名，如 from empe1,emp e2,eml as ss
                // 拷贝from后的数组
                String[] fromArray = Arrays.copyOfRange(newWord, i + 1, newWord.length);
                if (fromArray.length > 0) {
                    for (int fr = 0; fr < fromArray.length; fr++) {
                        if ((fr + 1) < fromArray.length && fromArray[fr + 1].contains(",") && !fromArray[fr + 1].startsWith("(")) {
                            if (fromArray[fr + 1].split(",").length > 1) {
                                inputTable = fromArray[fr + 1].split(",")[1];
                                inputSet.add(inputTable);
                            }
                        } else {
                            break;
                        }
                    }
                }
            }

            // join的处理
            if (currentWord.equals("join") && i + 1 <= newWord.length) {
                // 如果join后面是括号，说明后面是一个子查询,那么不能添加join后面的数据为表
                if ((i + 1) <= newWord.length && nextWord.startsWith("(")) {
                    continue;
                }
                String inputTable = newWord[i + 1];
                inputSet.add(inputTable);
            }

            // update的处理 update后面直接是table
            if (currentWord.equals("update") && i + 1 <= newWord.length) {
                // 如果join后面是括号，说明后面是一个子查询,那么不能添加join后面的数据为表
                if ((i + 1) <= newWord.length && nextWord.startsWith("(")) {
                    continue;
                }
                String outputTable = newWord[i + 1];
                outSet.add(outputTable);
            }

        }


        newInputSet = setToSplit(inputSet);
        newOnputSet = setToSplit(outSet);


    }


    /**
     * 将Set集合转换成Treeset，因为不能强转
     *
     * @param oldSet
     * @return
     */
    public TreeSet<String> hashSetToTreeset(Set<String> oldSet) {
        TreeSet<String> treeSet = new TreeSet<String>();
        for (String tables : oldSet) {
            treeSet.add(tables);
        }
        return treeSet;
    }


    /**
     * set集合中有些数据是 一个内容 goods,life  要分割开来
     *
     * @param oldSet
     * @return
     */
    private Set<String> setToSplit(Set<String> oldSet) {
        Set<String> newSet = new HashSet<String>();
        for (String tableStr : oldSet) {
            if (tableStr != null || !tableStr.equals("")) {
                if (tableStr.split(",").length > 1) {
                    String[] tables = tableStr.split(",");
                    for (int i = 0; i < tables.length; i++) {
                        newSet.add(tables[i]);
                    }
                } else {
                    newSet.add(tableStr);
                }
            }
        }
        return newSet;
    }

    /**
     * 排除一些函数里面带有from的函数
     * 替换greenplu sql语句中一些特殊的函数，比如
     * trim(both 'x' from 'xgreenplumxx')
     * substring('greenplum' from 6 for 3)
     * 因为这里使用这两个还是函数，会导致hive解析SQL语法报错，所以这里替换成
     * select rim(both 'x' from 'xgreenplumxx') from life  => select "no"  from life
     *
     * @param sql
     * @return
     */
    private String repacePGSqlSpecialFunction(String sql) {

        String[] partten = new String[]{
                // 函数名(字符  from 字符 )
                "substring\\\\([\\\\s\\\\S]*from[\\\\s\\\\S]*\\\\)",
                "trim\\\\([\\\\s\\\\S]*from[\\\\s\\\\S]*\\\\)",
                "overlay\\\\([\\\\s\\\\S]*from[\\\\s\\\\S]*\\\\)",
                "extract\\\\([\\\\s\\\\S]*from[\\\\s\\\\S]*\\\\)"
        };

        for (int i = 0; i < partten.length; i++) {
            sql = sql.replaceAll(partten[i], "\"no\"");
        }

        return sql;
    }

    public Set<String> getNewInputSet() {
        return newInputSet;
    }

    public void setNewInputSet(Set<String> newInputSet) {
        this.newInputSet = newInputSet;
    }

    public Set<String> getNewOnputSet() {
        return newOnputSet;
    }

    public void setNewOnputSet(Set<String> newOnputSet) {
        this.newOnputSet = newOnputSet;
    }


}
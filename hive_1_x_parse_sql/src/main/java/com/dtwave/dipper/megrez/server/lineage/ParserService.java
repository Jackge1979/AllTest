package com.dtwave.dipper.megrez.server.lineage;



/**
 * 描述信息
 *
 * @author baisong
 * @date 18/4/11
 */
public interface ParserService {



    /**
     * 解析SQL, 得到输入、输出
     *
     * @param command 待解析的SQL
     */
    LineageParseResult parse(String command, String currentDatabase, Boolean lineageFlag) throws Exception;

    /**
     * 解析SQL 得到输入、输出字段级别血缘
     * @param command 待解析的SQL
     * @return
     * @throws Exception
     */
    ParseResult parseLineage(String command, String currentDatabase) throws Exception;

}

package com.sql.parse.stored.procedure;

import com.sql.parse.druid.ParseTable;

/**
 * Created by lcc on 2018/8/23.
 */
public class StoreProcedureTest {



    public static void main(String[] args){
        String sql = "select report.branch_client_asset_struc('aa1',1,200) join report.branch_client_asset_struc('aa1',1,200);";
        StoreProcedure storeProcedure = new StoreProcedure();
//        storeProcedure.commonParse(sql,"GREENPLUM");
        storeProcedure.parseSqlTable(sql,"GREENPLUM");
    }
}

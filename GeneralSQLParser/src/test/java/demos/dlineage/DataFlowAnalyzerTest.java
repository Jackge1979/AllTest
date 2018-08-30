package demos.dlineage;

import demos.dlineage.entity.ConnectEntity;
import gudusoft.gsqlparser.EDbVendor;
import org.junit.Test;


/**
 * Created by lcc on 2018/8/27.
 */
public class DataFlowAnalyzerTest {



    private static String ORACLE_SQL   = "create PROCEDURE PROC_UPDATE_COMPOSITERATING AS\n" +
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



    private String GREENPLUM_PRODUCER = "CREATE OR REPLACE FUNCTION report.proc_southern_fund_staff_trade_detail(v_period_id character)\n" +
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
            "   where init_date between v_last_month_begin_tradedate and v_last_month_end_tradedate;\n" +
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
            "LANGUAGE plpgsql VOLATILE;";


    private String GREENPLUM_PRODUCER2 = "CREATE OR REPLACE FUNCTION dw_mine.real_clt_account_avg_rate_daily(v_period_id character)\n"+
            "RETURNS numeric AS \n"+
            "$BODY$\n"+
            "                 \n"+
            "  --real_clt_account_avg_rate_daily by wuhui\n"+
            "\n"+
            "  declare ln_linenum int4;\n"+
            "  v_program_name varchar(50);\n"+
            "  v_table_name   varchar(80);\n"+
            "  v_last_30_trd_date integer;\n"+
            "\tv_sql varchar(10000);\n"+
            "  v_return integer;\n"+
            "  \n"+
            "\n"+
            "BEGIN\n"+
            "  v_return := 0;\n"+
            "  v_program_name          := 'dw_mine.real_clt_account_avg_rate_daily';\n"+
            "\n"+
            "  ln_linenum     := 10;\n"+
            "select last_30_trd_date into v_last_30_trd_date\n"+
            "from (\n"+
            "select period_id,lag(period_id,30) over(order by period_id) last_30_trd_date\n"+
            "from dw_dim.period_dim t\n"+
            "where t.period_level='D'\n"+
            "and t.period_id=t.latest_trade_begin_date\n"+
            "and t.period_id between cast(to_char(to_date(v_period_id,'YYYYMMDD')-60,'YYYYMMDD') as integer) and v_period_id\n"+
            ") t\n"+
            "where period_id=v_period_id;\n"+
            "  \n"+
            "delete from  dw_mine.real_clt_account_avg_rate_pre where period_id=v_last_30_trd_date;\n"+
            "\n"+
            "v_sql=$a$\n"+
            "insert into dw_mine.real_clt_account_avg_rate_pre\n"+
            "select a.dc_business_date as period_id,a.client_id,case when a.business_flag in(4001,4701,4703) then 1 else 2 end as bs_flag,\n"+
            "\tsum((b.max_asset_price1-a.business_price)/a.business_price) buy_1_avg_rate,\n"+
            "\tsum((b.max_asset_price5-a.business_price)/a.business_price) buy_5_avg_rate,\n"+
            "\tsum((b.max_asset_price10-a.business_price)/a.business_price) buy_10_avg_rate,\n"+
            "\tsum((b.max_asset_price30-a.business_price)/a.business_price) buy_30_avg_rate,\n"+
            "\tsum(-(b.min_asset_price1-a.business_price)/a.business_price) sell_1_avg_rate,\n"+
            "\tsum(-(b.min_asset_price5-a.business_price)/a.business_price) sell_5_avg_rate,\n"+
            "\tsum(-(b.min_asset_price10-a.business_price)/a.business_price) sell_10_avg_rate,\n"+
            "\tsum(-(b.min_asset_price30-a.business_price)/a.business_price) sell_30_avg_rate,\n"+
            "\tcount(1) trd_times\n"+
            "from his_ht_hs08_hshis.his_deliver a,\n"+
            "(\n"+
            "select b.period_id,c.exchange_type,c.stock_code,\n"+
            "\tmax(case when c.dc_business_date<=b.period_id_aft1 then c.asset_price else 0 end) max_asset_price1,\n"+
            "\tmax(case when c.dc_business_date<=b.period_id_aft5 then c.asset_price else 0 end) max_asset_price5,\n"+
            "\tmax(case when c.dc_business_date<=b.period_id_aft10 then c.asset_price else 0 end) max_asset_price10,\n"+
            "\tmax(case when c.dc_business_date<=b.period_id_aft30 then c.asset_price else 0 end) max_asset_price30,\n"+
            "\tmin(case when c.dc_business_date<=b.period_id_aft1 then c.asset_price else 999999 end) min_asset_price1,\n"+
            "\tmin(case when c.dc_business_date<=b.period_id_aft5 then c.asset_price else 999999 end) min_asset_price5,\n"+
            "\tmin(case when c.dc_business_date<=b.period_id_aft10 then c.asset_price else 999999 end) min_asset_price10,\n"+
            "\tmin(case when c.dc_business_date<=b.period_id_aft30 then c.asset_price else 999999 end) min_asset_price30\n"+
            "from (\n"+
            "\t\t\tselect a.period_id,\n"+
            "\t\t\t\t\t\tlead(period_id,1) over(order by period_id) period_id_aft1,\n"+
            "\t\t\t\t\t\tlead(period_id,5) over(order by period_id) period_id_aft5,\n"+
            "\t\t\t\t\t\tlead(period_id,10) over(order by period_id) period_id_aft10,\n"+
            "\t\t\t\t\t\tlead(period_id,30) over(order by period_id) period_id_aft30\n"+
            "\t\t\tfrom dw_dim.period_dim a\n"+
            "\t\t\twhere a.period_level='D'\n"+
            "\t\t\tand a.period_id=a.latest_trade_begin_date\n"+
            "\t\t\tand a.period_id between $a$||v_last_30_trd_date||$a$ and cast(to_char(to_date($a$||v_last_30_trd_date||$a$,'YYYYMMDD')+70,'YYYYMMDD') as integer)\n"+
            "\t\t\t) b,\n"+
            "\this_ht_hs08_hsuser.price c,src_ht_hs08_hsuser.stkcode d\n"+
            "where b.period_id=$a$||v_last_30_trd_date||$a$\n"+
            "and c.dc_business_date between b.period_id and b.period_id_aft30\n"+
            "and c.exchange_type in('1','2')\n"+
            "and c.money_type='0'\n"+
            "and c.exchange_type=d.exchange_type and c.stock_code=d.stock_code and d.stock_type in('0','c')\n"+
            "group by b.period_id,c.exchange_type,c.stock_code\n"+
            ") b\n"+
            "where a.dc_business_date=$a$||v_last_30_trd_date||$a$\n"+
            "and a.exchange_type in('1','2')\n"+
            "and (a.business_flag in(4001,4002) or a.business_flag between 4701 and 4704)\n"+
            "and a.stock_type in('0','c')\n"+
            "and b.period_id=a.dc_business_date\n"+
            "and a.exchange_type=b.exchange_type\n"+
            "and a.stock_code=b.stock_code\n"+
            "group by a.dc_business_date,a.client_id,case when a.business_flag in(4001,4701,4703) then 1 else 2 end\n"+
            "$a$;\n"+
            "execute v_sql;\n"+
            " return v_return;\n"+
            "\n"+
            "exception\n"+
            "  when others then\n"+
            "    perform dw_pub.program_error(v_program_name || '[' || ln_linenum || ']',\n"+
            "                                 '归' || v_table_name || '历史错误. ' || SQLERRM,\n"+
            "                                 5,\n"+
            "                                 '',\n"+
            "                                 '',\n"+
            "                                 'greenplum db');\n"+
            "    return 1;\n"+
            "END \n"+
            "$BODY$\n"+
            "LANGUAGE plpgsql VOLATILE;";

    /**
     * 测试点：无血缘测试  select 包含 resultset
     */
    @Test
    public  void test1() {
        String sqls = "select name,age from  b;";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);
    }

    /**
     * 测试点：简单血缘测试
     */
    @Test
    public  void test2() {
        String sqls = "insert into tablea (names,ages) select * from  b;";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);
    }


    /**
     * 测试点：无血缘测试  drop <dlineage/>
     */
    @Test
    public   void test3() {
        String sqls = "drop table  b;";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);
    }

    /**
     * 测试点：简单血缘测试 先插入后查询
     */
    @Test
    public  void test4() {
        String sqls = "insert into tablea select name,age from  b; select name,age from  tablea;";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);
    }

    /**
     * 测试点：简单血缘测试 先插入后查询，做提取多条SQL处理
     */
    @Test
    public  void test5() {
        String sqls = "insert into tablea select name,age from  b; select name,age from  tablea;";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);

    }


    /**
     * 测试点：简单血缘测试 先插入后查询，做提取多条SQL处理 存储过程
     */
    @Test
    public  void test6() {
        String dbName = "lcc_gp";
//        allTest(PRODUCER_SQL,EDbVendor.dbvoracle,dbName);
    }


    /**
     * 测试点：只有insert  <dlineage/>
     */
    @Test
    public  void test7() {
        String sqls = "insert into tablea values(1,2);";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);

    }


    /**
     * 测试点：简单血缘测试,不写列名
     */
    @Test
    public  void test8() {
        String sqls = "insert into table_a select * from table_b;";
        String dbName = "lcc_gp";
//        allTest(sqls,EDbVendor.dbvgreenplum,dbName);
    }

    /**
     * 测试点：简单血缘测试,不写列名 greenplum
     */
    @Test
    public  void test9() {
        String sqls = "insert into public.user    select * from public.user2;";
        String dbName = "lcc_gp";
        String url = "jdbc:postgresql://47.92.38.137:8006/lcc_gp";
        String username = "lbq_test";
        String password = "lbq_test@dtwave";
        ConnectEntity connectEntity = new ConnectEntity();
        connectEntity.setDbName(dbName);
        connectEntity.setUrl(url);
        connectEntity.setUserName(username);
        connectEntity.setPassword(password);

        allTest(sqls,EDbVendor.dbvgreenplum,connectEntity);
    }

    /**
     * 测试点： greenplum 存储过程
     */
    @Test
    public  void test10() {
        String dbName = "lcc_gp";
        String url = "jdbc:postgresql://47.92.38.137:8006/lcc_gp";
        String username = "lbq_test";
        String password = "lbq_test@dtwave";
        ConnectEntity connectEntity = new ConnectEntity();
        connectEntity.setDbName(dbName);
        connectEntity.setUrl(url);
        connectEntity.setUserName(username);
        connectEntity.setPassword(password);

        allTest(GREENPLUM_PRODUCER2,EDbVendor.dbvpostgresql,connectEntity);
    }




    /**
     * 测试点：oracle 存储过程
     */
    @Test
    public  void test11() {
        String dbName = "lcc_gp";
        String url = "jdbc:postgresql://47.92.38.137:8006/lcc_gp";
        String username = "lbq_test";
        String password = "lbq_test@dtwave";
        ConnectEntity connectEntity = new ConnectEntity();
        connectEntity.setDbName(dbName);
        connectEntity.setUrl(url);
        connectEntity.setUserName(username);
        connectEntity.setPassword(password);

        allTest(OracleSQLS.ORACLE_SQL1,EDbVendor.dbvoracle,connectEntity);
    }

    /**
     * 测试点：完整的oracle测试
     */
    @Test
    public  void test12() {
        String sql = "call PROC_UPDATE_COMPOSITERATING()";
        String url = "jdbc:oracle:thin:@47.97.11.138:1522:xe";
        String username = "system";
        String password = "oracle";
        String dbName = "ZYT";
        ConnectEntity connectEntity = new ConnectEntity();
        connectEntity.setDbName(dbName);
        connectEntity.setUrl(url);
        connectEntity.setUserName(username);
        connectEntity.setPassword(password);

        ParseLineage  parseLineage = new ParseLineage();
        parseLineage.getStoreFunctionInDb(sql,EDbVendor.dbvoracle,connectEntity);
    }

    /**
     * 测试点：完整的oracle测试 不使用call调用
     */
    @Test
    public  void test13() {
        String sql = " declare\n" +
                "       uName varchar(40);\n" +
                "       Age int;\n" +
                "    begin\n" +
                "       uName:='1';\n" +
                "       Age:=234;\n" +
                "       PROC_UPDATE_COMPOSITERATING(uName,Age);\n" +
                "       DBMS_OUTPUT.PUT_LINE(uName||'   '||Age);\n" +
                "    END;\n" +
                "    exit;";
        String url = "jdbc:oracle:thin:@47.97.11.138:1522:xe";
        String username = "system";
        String password = "oracle";
        String dbName = "ZYT";
        ConnectEntity connectEntity = new ConnectEntity();
        connectEntity.setDbName(dbName);
        connectEntity.setUrl(url);
        connectEntity.setUserName(username);
        connectEntity.setPassword(password);

        ParseLineage  parseLineage = new ParseLineage();
        parseLineage.getStoreFunctionInDb(sql,EDbVendor.dbvoracle,connectEntity);
    }







    public   void  allTest(String sqls,EDbVendor dbType,ConnectEntity connectEntity) {
        ParseLineage  parseLineage = new ParseLineage();
        parseLineage.parseSqlsLineage(sqls,dbType,  connectEntity);
    }







}
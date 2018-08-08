package com.sql.parse.druid;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.postgresql.visitor.PGSchemaStatVisitor;
import com.alibaba.druid.stat.*;
import com.alibaba.druid.stat.TableStat.*;
import com.alibaba.druid.util.JdbcConstants;

import java.util.*;


public class Testparse {

    public static void main(String[] args) {

//        String sql= ""
//                + "insert into tar select * from boss_table bo, ("
//                + "select a.f1, ff from emp_table a "
//                + "inner join log_table b "
//                + "on a.f2 = b.f3"
//                + ") f "
//                + "where bo.f4 = f.f5 "
//                + "group by bo.f6 , f.f7 having count(bo.f8) > 0 "
//                + "order by bo.f9, f.f10;"
//                + "select func(f) from test1; "
//                + "";



        List<String>  sqls = new ArrayList<String>();

        sqls.add(" insert into lcc_test (name)  select cbrt(27.0) from life ");
        sqls.add(" insert into lcc_test (name)  select ceil(-42.8) from life ");
        sqls.add( " insert into lcc_test (name)  select ceiling(-95.3) from life ");
        sqls.add( " insert into lcc_test (name)  select degrees(0.5) from life ");
        sqls.add( " insert into lcc_test (name)  select exp(1.0) from life ");
        sqls.add( " insert into lcc_test (name)  select floor(-42.8) from life ");
        sqls.add( " insert into lcc_test (name)  select ln(2.0) from life ");
        sqls.add( " insert into lcc_test (name)  select log(100.0) from life ");
        sqls.add( " insert into lcc_test (name)  select log(2.0, 64.0) from life ");
        sqls.add( " insert into lcc_test (name)  select mod(9,4) from life ");
        sqls.add( " insert into lcc_test (name)  select pi() from life ");
        sqls.add( " insert into lcc_test (name)  select power(9.0, 3.0) from life ");
        sqls.add( " insert into lcc_test (name)  select power(9.0, 3.0) from life ");
        sqls.add( " insert into lcc_test (name)  select radians(45.0) from life ");
        sqls.add( " insert into lcc_test (name)  select random() from life ");
        sqls.add( " insert into lcc_test (name)  select round(42.4) from life ");
        sqls.add( " insert into lcc_test (name)  select round(42.4382, 2) from life ");
        sqls.add( " insert into lcc_test (name)  select setseed(0.54823) from life ");
        sqls.add( " insert into lcc_test (name)  select sign(-8.4) from life ");
        sqls.add( " insert into lcc_test (name)  select sqrt(2.0) from life ");
        sqls.add( " insert into lcc_test (name)  select trunc(42.8) from life ");
        sqls.add( " insert into lcc_test (name)  select trunc(42.4382, 2) from life ");
        sqls.add( " insert into lcc_test (name)  select width_bucket(5.35, 0.024, 10.06, 5) from life ");
        sqls.add( " insert into lcc_test (name)  select width_bucket(5.35, 0.024, 10.06, 5) from life ");


        // 字符串函数

        sqls.add( " insert into lcc_test (name)  select 'Value: ' || 42 from life ");
        sqls.add( " insert into lcc_test (name)  select bit_length('jose') from life ");
        sqls.add( " insert into lcc_test (name)  select char_length('jose') from life ");
        sqls.add( " insert into lcc_test (name)  select lower('TOM') from life ");
        sqls.add( " insert into lcc_test (name)  select octet_length('jose') from life ");
        sqls.add( " insert into lcc_test (name)  select overlay('Txxxxas' placing 'hom' from 2 for 4) from life ");
        sqls.add( " insert into lcc_test (name)  select position('om' in 'Thomas') from life ");
        sqls.add( " insert into lcc_test (name)  select substring('Thomas' from 2 for 3) from life ");
        sqls.add( " insert into lcc_test (name)  select substring('Thomas' from '...$') from life ");
        sqls.add( " insert into lcc_test (name)  select substring('Thomas' from '%#\"o_a#\"_' for '#') from life ");
        sqls.add( " insert into lcc_test (name)  select trim(both 'x' from 'xTomxx') from life ");
        sqls.add( " insert into lcc_test (name)  select upper('tom') from life ");


        sqls.add( " insert into lcc_test (name)  select btrim('xyxtrimyyx', 'xy') from life ");
        sqls.add( " insert into lcc_test (name)  select chr(65) from life ");
        sqls.add( " insert into lcc_test (name)  select convert('text_in_utf8', 'UTF8', 'LATIN1') from life ");
        sqls.add( " insert into lcc_test (name)  select convert_from('text_in_utf8', 'UTF8') from life ");
        sqls.add( " insert into lcc_test (name)  select convert_to('some text', 'UTF8') from life ");
        sqls.add( " insert into lcc_test (name)  select decode('MTIzAAE=', 'base64') from life ");
        sqls.add( " insert into lcc_test (name)  select encode(E'123\\000\\001', 'base64') from life ");
        sqls.add( " insert into lcc_test (name)  select initcap('hi THOMAS') from life ");
        sqls.add( " insert into lcc_test (name)  select length('jose') from life ");
        sqls.add( " insert into lcc_test (name)  select length('jose', 'UTF8') from life ");
        sqls.add( " insert into lcc_test (name)  select lpad('hi', 5, 'xy') from life ");
        sqls.add( " insert into lcc_test (name)  select ltrim('zzzytrim', 'xyz') from life ");
        sqls.add( " insert into lcc_test (name)  select  md5('abc') from life ");
        sqls.add( " insert into lcc_test (name)  select pg_client_encoding() from life ");
        sqls.add( " insert into lcc_test (name)  select quote_ident('Foo bar') from life ");
        sqls.add( " insert into lcc_test (name)  select  quote_literal('O\'Reilly') from life ");
        sqls.add( " insert into lcc_test (name)  select quote_literal(42.5) from life ");
        sqls.add( " insert into lcc_test (name)  select regexp_matches('foobarbequebaz', '(bar)(beque)') from life ");
        sqls.add( " insert into lcc_test (name)  select  regexp_replace('Thomas', '.[mN]a.', 'M') from life ");
        sqls.add( " insert into lcc_test (name)  select  regexp_split_to_array('hello world', E'\\s+') from life ");
        sqls.add( " insert into lcc_test (name)  select  regexp_split_to_table('hello world', E'\\s+') from life ");
        sqls.add( " insert into lcc_test (name)  select  repeat('Pg', 4) from life ");
        sqls.add( " insert into lcc_test (name)  select  replace('abcdefabcdef', 'cd', 'XX') from life ");
        sqls.add( " insert into lcc_test (name)  select  rpad('hi', 5, 'xy') from life ");
        sqls.add( " insert into lcc_test (name)  select  rtrim('trimxxxx', 'x') from life ");
        sqls.add( " insert into lcc_test (name)  select  split_part('abc~@~def~@~ghi', '~@~', 2) from life ");
        sqls.add( " insert into lcc_test (name)  select  strpos('high', 'ig') from life ");
        sqls.add( " insert into lcc_test (name)  select  substr('alphabet', 3, 2) from life ");
        sqls.add( " insert into lcc_test (name)  select  to_ascii('Karel') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_hex(2147483647) from life ");
        sqls.add( " insert into lcc_test (name)  select  translate('12345', '14', 'ax') from life ");


        // 二进制字符串
        // 太特殊了 java.lang.ClassCastException: com.alibaba.druid.sql.ast.expr.SQLBinaryOpExpr
//        sqls.add( " insert into lcc_test (name)  select  E'\\\\Post'::bytea || E'\\047gres\\000'::bytea from life ");
        sqls.add( " insert into lcc_test (name)  select  get_bit(E'Th\\000omas'::bytea, 45) from life ");
        sqls.add( " insert into lcc_test (name)  select  get_byte(E'Th\\000omas'::bytea, 4) from life ");
        sqls.add( " insert into lcc_test (name)  select  octet_length(E'jo\\000se'::bytea) from life ");
        sqls.add( " insert into lcc_test (name)  select  position(E'\\000om'::bytea in E'Th\\000omas'::bytea) from life ");
        sqls.add( " insert into lcc_test (name)  select  set_bit(E'Th\\000omas'::bytea, 45, 0) from life ");
        sqls.add( " insert into lcc_test (name)  select  set_byte(E'Th\\000omas'::bytea, 4, 64) from life ");
        sqls.add( " insert into lcc_test (name)  select  substring(E'Th\\000omas'::bytea from 2 for 3) from life ");
        sqls.add( " insert into lcc_test (name)  select  trim(E'\\000'::bytea from E'\\000Tom\\000'::bytea) from life ");

        sqls.add( " insert into lcc_test (name)  select  btrim(E'\\000trim\\000'::bytea, E'\\000'::bytea) from life ");
        sqls.add( " insert into lcc_test (name)  select  decode(E'123\\000456', 'escape') from life ");
        sqls.add( " insert into lcc_test (name)  select  encode(E'123\\000456'::bytea, 'escape') from life ");
        sqls.add( " insert into lcc_test (name)  select  length(E'jo\\000se'::bytea) from life ");
        sqls.add( " insert into lcc_test (name)  select  md5(E'Th\\000omas'::bytea) from life ");


        // 格式化函数
        sqls.add( " insert into lcc_test (name)  select  to_char(current_timestamp, 'HH12:MI:SS') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_char(interval '15h 2m 12s', 'HH24:MI:SS') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_char(125, '999') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_char(125.8::real, '999D9') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_char(-125.8, '999D99S') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_date('05 Dec 2000', 'DD Mon YYYY') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_number('12,454.8-', '99G999D9S') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_timestamp('05 Dec 2000', 'DD Mon YYYY') from life ");
        sqls.add( " insert into lcc_test (name)  select  to_timestamp(200120400) from life ");


        // 日期时间操作
        sqls.add( " insert into lcc_test (name)  select  date('2001-09-28') + integer '7' from life ");
        sqls.add( " insert into lcc_test (name)  select  date('2001-09-28') + interval '1hour' from life ");
        sqls.add( " insert into lcc_test (name)  select  date('2001-09-28') + time '03:00' from life ");
        sqls.add( " insert into lcc_test (name)  select  interval '1 day' + interval '1 hour' from life ");
        sqls.add( " insert into lcc_test (name)  select  timestamp '2001-09-28 01:00' + interval '23 hours' from life ");
        sqls.add( " insert into lcc_test (name)  select  time '01:00' + interval '3 hours' - interval '23 hours' from life ");
        sqls.add( " insert into lcc_test (name)  select  date '2001-10-01' - date '2001-09-28' from life ");
        sqls.add( " insert into lcc_test (name)  select  date '2001-10-01' - integer '7' from life ");
        sqls.add( " insert into lcc_test (name)  select  date '2001-09-28' - interval '1 hour' from life ");
        sqls.add( " insert into lcc_test (name)  select  time '05:00' - time '03:00' from life ");
        sqls.add( " insert into lcc_test (name)  select  time '05:00' - interval '2 hours' from life ");
        sqls.add( " insert into lcc_test (name)  select  timestamp '2001-09-28 23:00' - interval '23 hours' from life ");
        sqls.add( " insert into lcc_test (name)  select  interval '1 day' - interval '1 hour' from life ");
        sqls.add( " insert into lcc_test (name)  select  timestamp '2001-09-29 03:00' - timestamp '2001-09-27 12:00'from life ");
        sqls.add( " insert into lcc_test (name)  select  900 * interval '1 second' from life ");
        sqls.add( " insert into lcc_test (name)  select  21 * interval '1 day' from life ");
        sqls.add( " insert into lcc_test (name)  select  double precision '3.5' * interval '1 hour' from life ");
        sqls.add( " insert into lcc_test (name)  select  interval '1 hour' / double precision '1.5' from life ");





        for(int k=0;k<sqls.size();k++) {

            String sql = sqls.get(k);

            System.out.println("=============>"+sql); // 缺省大写格式
//            sql = repacePGSqlSpecialFunction(sql);

            System.out.println("=去除函数=========>"+sql);

//            doHive(sql);

            doDruid(sql);


        }
    }

    private static void doDruid(String sql) {


        String dbType = JdbcConstants.POSTGRESQL;

        //格式化输出
        String result = SQLUtils.format(sql, dbType);
        System.out.println(result); // 缺省大写格式
        List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

        //解析出的独立语句的个数
        System.out.println("size is:" + stmtList.size());
        for (int i = 0; i < stmtList.size(); i++) {

            SQLStatement stmt = stmtList.get(i);

            PGSchemaStatVisitor visitor = new PGSchemaStatVisitor();
            stmt.accept(visitor);
            Map<String, String> aliasmap = visitor.getAliasMap();
            for (Iterator iterator = aliasmap.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next().toString();
                System.out.println("[ALIAS]" + key + " - " + aliasmap.get(key));
            }
            Set<Column> groupby_col = visitor.getGroupByColumns();
            //
            for (Iterator iterator = groupby_col.iterator(); iterator.hasNext(); ) {
                Column column = (Column) iterator.next();
                System.out.println("[GROUP]" + column.toString());
            }
            //获取表名称
            System.out.println("table names:");
            Map<Name, TableStat> tabmap = visitor.getTables();
            for (Iterator iterator = tabmap.keySet().iterator(); iterator.hasNext(); ) {
                Name name = (Name) iterator.next();
                System.out.println(name.toString() + " - " + tabmap.get(name).toString());
            }
            //System.out.println("Tables : " + visitor.getCurrentTable());
            //获取操作方法名称,依赖于表名称
            System.out.println("Manipulation : " + visitor.getTables());
            //获取字段名称
            System.out.println("fields : " + visitor.getColumns());
        }

    }






}
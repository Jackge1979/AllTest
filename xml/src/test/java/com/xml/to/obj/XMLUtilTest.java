package com.xml.to.obj;

import com.xml.to.obj.entity.xmls.DlineageEntity;
import org.junit.Test;

/**
 * Created by lcc on 2018/9/2.
 */
public class XMLUtilTest {
    @Test
    public void convertToXml() throws Exception {
    }

    @Test
    public void convertToXml1() throws Exception {
    }

    @Test
    public void convertXmlStrToObject() throws Exception {


        String result  =
                "<dlineage>\n" +
                "   <relation id=\"36\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[3,1],[3,10]\" column=\"period_id\" id=\"36\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[16,8],[16,19]\" column=\"v_period_id\" id=\"2\" parent_id=\"2\" parent_name=\"src_ht_zlfin_mobile.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"38\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[5,1],[5,5]\" column=\"name\" id=\"38\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[16,39],[16,45]\" column=\"name\" id=\"3\" parent_id=\"2\" parent_name=\"src_ht_zlfin_mobile.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"39\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[6,1],[6,10]\" column=\"stockcode\" id=\"39\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[16,46],[16,57]\" column=\"stockcode\" id=\"4\" parent_id=\"2\" parent_name=\"src_ht_zlfin_mobile.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"40\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[7,1],[7,9]\" column=\"marketno\" id=\"40\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[16,58],[16,68]\" column=\"marketno\" id=\"5\" parent_id=\"2\" parent_name=\"src_ht_zlfin_mobile.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"41\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[8,1],[8,7]\" column=\"remark\" id=\"41\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[16,69],[16,77]\" column=\"remark\" id=\"6\" parent_id=\"2\" parent_name=\"src_ht_zlfin_mobile.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"42\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[9,1],[9,7]\" column=\"optype\" id=\"42\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[16,78],[16,86]\" column=\"optype\" id=\"7\" parent_id=\"2\" parent_name=\"src_ht_zlfin_mobile.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"43\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[10,1],[10,9]\" column=\"name_opp\" id=\"43\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[17,1],[17,7]\" column=\"name\" id=\"14\" parent_id=\"3\" parent_name=\"src_ht_risk.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"44\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[11,1],[11,14]\" column=\"stockcode_opp\" id=\"44\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[17,8],[17,19]\" column=\"stockcode\" id=\"15\" parent_id=\"3\" parent_name=\"src_ht_risk.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"45\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[12,1],[12,13]\" column=\"marketno_opp\" id=\"45\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[17,20],[17,30]\" column=\"marketno\" id=\"16\" parent_id=\"3\" parent_name=\"src_ht_risk.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"46\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[13,1],[13,11]\" column=\"remark_opp\" id=\"46\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[17,31],[17,39]\" column=\"remark\" id=\"17\" parent_id=\"3\" parent_name=\"src_ht_risk.tifstockpool\"/>\n" +
                "   </relation>\n" +
                "   <relation id=\"47\" type=\"dataflow\">\n" +
                "      <target coordinate=\"[14,1],[14,11]\" column=\"optype_opp\" id=\"47\" parent_id=\"1\" parent_name=\"report.tifstockpool_check_detail\"/>\n" +
                "      <source coordinate=\"[17,40],[17,48]\" column=\"optype\" id=\"18\" parent_id=\"3\" parent_name=\"src_ht_risk.tifstockpool\"/>\n" +
                "   </relation>" +
                "<table name=\"report.tifstockpool_check_detail\" id=\"1\" type=\"table\" coordinate=\"[1,14],[1,46]\">\n" +
                "      <column name=\"period_id\" id=\"36\" coordinate=\"[3,1],[3,10]\"/>\n" +
                "      <column name=\"system_name\" id=\"37\" coordinate=\"[4,1],[4,12]\"/>\n" +
                "      <column name=\"name\" id=\"38\" coordinate=\"[5,1],[5,5]\"/>\n" +
                "      <column name=\"stockcode\" id=\"39\" coordinate=\"[6,1],[6,10]\"/>\n" +
                "      <column name=\"marketno\" id=\"40\" coordinate=\"[7,1],[7,9]\"/>\n" +
                "      <column name=\"remark\" id=\"41\" coordinate=\"[8,1],[8,7]\"/>\n" +
                "      <column name=\"optype\" id=\"42\" coordinate=\"[9,1],[9,7]\"/>\n" +
                "      <column name=\"name_opp\" id=\"43\" coordinate=\"[10,1],[10,9]\"/>\n" +
                "      <column name=\"stockcode_opp\" id=\"44\" coordinate=\"[11,1],[11,14]\"/>\n" +
                "      <column name=\"marketno_opp\" id=\"45\" coordinate=\"[12,1],[12,13]\"/>\n" +
                "      <column name=\"remark_opp\" id=\"46\" coordinate=\"[13,1],[13,11]\"/>\n" +
                "      <column name=\"optype_opp\" id=\"47\" coordinate=\"[14,1],[14,11]\"/>\n" +
                "   </table>\n" +
                "   <table name=\"src_ht_zlfin_mobile.tifstockpool\" id=\"2\" type=\"table\" coordinate=\"[18,7],[18,41]\" alias=\"a\">\n" +
                "      <column name=\"stockcode\" id=\"1\" coordinate=\"[20,4],[20,15]\"/>\n" +
                "      <column name=\"v_period_id\" id=\"2\" coordinate=\"[16,8],[16,19]\"/>\n" +
                "      <column name=\"name\" id=\"3\" coordinate=\"[16,39],[16,45]\"/>\n" +
                "      <column name=\"stockcode\" id=\"4\" coordinate=\"[16,46],[16,57]\"/>\n" +
                "      <column name=\"marketno\" id=\"5\" coordinate=\"[16,58],[16,68]\"/>\n" +
                "      <column name=\"remark\" id=\"6\" coordinate=\"[16,69],[16,77]\"/>\n" +
                "      <column name=\"optype\" id=\"7\" coordinate=\"[16,78],[16,86]\"/>\n" +
                "      <column name=\"name\" id=\"8\" coordinate=\"[21,7],[21,13]\"/>\n" +
                "      <column name=\"stockcode\" id=\"9\" coordinate=\"[22,4],[22,15]\"/>\n" +
                "      <column name=\"marketno\" id=\"10\" coordinate=\"[23,4],[23,14]\"/>\n" +
                "      <column name=\"remark\" id=\"11\" coordinate=\"[24,4],[24,12]\"/>\n" +
                "      <column name=\"optype\" id=\"12\" coordinate=\"[25,4],[25,12]\"/>\n" +
                "   </table>\n" +
                "   <table name=\"src_ht_risk.tifstockpool\" id=\"3\" type=\"table\" coordinate=\"[19,11],[19,37]\" alias=\"b\">\n" +
                "      <column name=\"stockcode\" id=\"13\" coordinate=\"[20,16],[20,27]\"/>\n" +
                "      <column name=\"name\" id=\"14\" coordinate=\"[17,1],[17,7]\"/>\n" +
                "      <column name=\"stockcode\" id=\"15\" coordinate=\"[17,8],[17,19]\"/>\n" +
                "      <column name=\"marketno\" id=\"16\" coordinate=\"[17,20],[17,30]\"/>\n" +
                "      <column name=\"remark\" id=\"17\" coordinate=\"[17,31],[17,39]\"/>\n" +
                "      <column name=\"optype\" id=\"18\" coordinate=\"[17,40],[17,48]\"/>\n" +
                "      <column name=\"name\" id=\"19\" coordinate=\"[21,15],[21,21]\"/>\n" +
                "      <column name=\"stockcode\" id=\"20\" coordinate=\"[22,17],[22,28]\"/>\n" +
                "      <column name=\"marketno\" id=\"21\" coordinate=\"[23,16],[23,26]\"/>\n" +
                "      <column name=\"remark\" id=\"22\" coordinate=\"[24,14],[24,22]\"/>\n" +
                "      <column name=\"optype\" id=\"23\" coordinate=\"[25,14],[25,22]\"/>\n" +
                "   </table>\n" +
                "</dlineage>";

        Object obj = XMLUtil.convertXmlStrToObject(DlineageEntity.class, result);

        System.out.println(obj);
    }

    @Test
    public void convertXmlFileToObject() throws Exception {
    }

}
package demos.dlineage;

import com.thoughtworks.xstream.XStream;
import demos.dlineage.entity.Entity;
import demos.dlineage.entity.LineageParseResult;
import demos.dlineage.entity.MegrezLineageDto;
import demos.dlineage.entity.MegrezLineageEdge;
import demos.dlineage.entity.xmls.*;
import gudusoft.gsqlparser.EDbVendor;
import gudusoft.gsqlparser.TGSqlParser;
import gudusoft.gsqlparser.TStatementList;
import lombok.Synchronized;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.*;

/**
 * Created by lcc on 2018/8/27.
 */
public class ParseLineage {

    private static final Logger logger = LoggerFactory.getLogger(ParseLineage.class);


    public  void getStoreFunctionInDb(String sql,EDbVendor dbType,ConnectEntity connectEntity) {

        // 正则 必须以 空格开头，紧接着中间字符串一次到多个，紧接着没有空格，紧接着是左括号，中间一些东西，右括号
        List<String> matchedKeys = RegexUtil.regexSelectList(sql, "\\s{1}\\S+\\s{0}\\((.*?)\\)");
        for (String functionString : matchedKeys) {
            String functionName = functionString.split("\\(")[0];
            String functionParmms = functionString.split("\\(")[1];
            String params = functionParmms.substring(0, functionParmms.length() - 1);

            String sqlInDb = "";
            if(dbType == EDbVendor.dbvgreenplum){
                sqlInDb = GreenplumUtils.getStoreFunctionInDb(functionName,  connectEntity);
            }else if(dbType == EDbVendor.dbvoracle){
                sqlInDb = OracleUtils.getStoreFunctionInDb(functionName,  connectEntity);
            }
            parseSqlsLineage( sqlInDb, dbType, connectEntity);
        }
    }

    /**
     * sql 去除注释
     *
     * @param source
     * @return
     */
    private String removeAnnotation(String source) {
        String[] lines = source.split("\n");
        StringBuilder resultBuilder = new StringBuilder();

        //包含 "--" 的为注释，去除
        for (String line : lines) {
            if (StringUtils.isNoneEmpty(line) && !line.trim().startsWith("--")) {
                resultBuilder.append(line).append("\n");
            }
        }
        return resultBuilder.toString();
    }




    public  void parseSqlsLineage(String sqlInDb,EDbVendor dbType,ConnectEntity connectEntity) {
        sqlInDb = removeAnnotation(sqlInDb);
        TGSqlParser sqlParser = new TGSqlParser(dbType);
        sqlParser.sqltext = sqlInDb;
        sqlParser.parse();

        TStatementList stList = sqlParser.sqlstatements;
        logger.info("共有条"+stList.size()+"sql语句!");
        for(int i=0; i<stList.size(); i++){
            String sql = stList.get(i).toString();
            logger.info("=====》SQL:\r\n"+sql);
            parseLineage(sql,dbType, connectEntity);
        }

    }

    public  void  parseLineage(String sql,EDbVendor dbType,ConnectEntity connectEntity) {
        DataFlowAnalyzer dataFlowAnalyzer = new DataFlowAnalyzer( sql,dbType,true );
        StringBuffer errorBuffer = new StringBuffer( );
        String result = dataFlowAnalyzer.generateDataFlow( errorBuffer );
        if ( result != null )
        {
            logger.info( "解析结果===>\r\n"+result );
            // 没有血缘信息的
            if(result.equals("<dlineage/>")){
                return;
            }
            transformXMLtoObject(result,sql, dbType,  connectEntity);
        }
    }



    /**
     * 解析xml,构造血缘对象
     *
     * <dlineage>
     * 	 <relation id="3" type="dataflow">
     * 	 <target coordinate="[1,27],[1,31]" column="NAME" id="5" parent_id="1" parent_name="TABLEA"/>
     * 	 <source coordinate="[1,27],[1,31]" column="NAME" id="1" parent_id="2" parent_name="B"/>
     * 	 </relation>
     * 	 <relation id="4" type="dataflow">
     * 	 <target coordinate="[1,32],[1,35]" column="AGE" id="6" parent_id="1" parent_name="TABLEA"/>
     * 	 <source coordinate="[1,32],[1,35]" column="AGE" id="2" parent_id="2" parent_name="B"/>
     * 	 </relation>
     * 	 <table name="TABLEA" id="1" type="table" coordinate="[1,13],[1,19]">
     * 	 <column name="NAME" id="5" coordinate="[1,27],[1,31]"/>
     * 	 <column name="AGE" id="6" coordinate="[1,32],[1,35]"/>
     * 	 </table>
     * 	 <table name="B" id="2" type="table" coordinate="[1,42],[1,43]">
     * 	 <column name="NAME" id="1" coordinate="[1,27],[1,31]"/>
     * 	 <column name="AGE" id="2" coordinate="[1,32],[1,35]"/>
     * 	 </table>
     * </dlineage>
     */
    public   void transformXMLtoObject(String result,String sql ,EDbVendor dbType,ConnectEntity connectEntity) {
        result = result.replaceAll("dlineage","DlineageEntity");
        result = result.replaceAll("parent_id","parentId");
        result = result.replaceAll("parent_name","parentName");

        XStream xStream = new XStream();
        xStream.autodetectAnnotations(true);
        xStream.processAnnotations(DlineageEntity.class);
        DlineageEntity dlineageEntity = (DlineageEntity) xStream.fromXML(result);

        logger.info( "===> \r\n"+dlineageEntity.toString());
        transformtoMySelfLineage(dlineageEntity, sql, dbType,  connectEntity);


    }

    /**
     * 补全SQL
     *
     * @param dlineageEntity
     * @param sql
     * @param dbType
     */
    private void completionSql(DlineageEntity dlineageEntity, String sql, EDbVendor dbType,ConnectEntity connectEntity) {
        List<Table> tables = dlineageEntity.getTable();

        List<String> tableIds = new ArrayList<>();
        for( demos.dlineage.entity.xmls.Table table : tables){
            tableIds.add(table.getId());
        }

        for( demos.dlineage.entity.xmls.Table table : tables){
            String tableName = table.getName();
            String tableCoordinate = table.getCoordinate();
            tableCoordinate = tableCoordinate.replaceAll("\\[","");
            tableCoordinate = tableCoordinate.replaceAll("\\]","");
            String[] tableIndex = tableCoordinate.split(",");

            List<Column> columns = table.getColumn();
            for( demos.dlineage.entity.xmls.Column column : columns){
                String columnName = column.getName();
                String columnId = column.getId();
                if(columnName.equals("*")){

                    String columnsInDb = getTableNames(tableName,dbType,  connectEntity);
                    String columnCoordinate = column.getCoordinate();
                    columnCoordinate = columnCoordinate.replaceAll("\\[","");
                    columnCoordinate = columnCoordinate.replaceAll("\\]","");
                    String[] columnIndex = columnCoordinate.split(",");
                    int columnIndexInt = Integer.parseInt(columnIndex[1]);
                    String indexString = sql.substring(columnIndexInt-1, columnIndexInt);
                    StringBuilder sb = new StringBuilder(sql);//构造一个StringBuilder对象
                    if(tableIds.contains(columnId)){
                        columnIndexInt = columnIndexInt-1;
                        columnsInDb = " "+columnsInDb+" ";
                        sb.replace(columnIndexInt,columnIndexInt+1,columnsInDb);
                    }else{
                        int tableIndexInt = Integer.parseInt(tableIndex[3]);
                        columnsInDb = " ("+columnsInDb+") ";
                        sb.insert(tableIndexInt, columnsInDb);
                    }
                    sql = sb.toString();
                    System.out.println("替换*号后的sql:"+sql);
                    parseLineage(sql,dbType,connectEntity);
                }
            }
        }
    }


    /**
     * 转换成自己需要的血缘格式
     *
     * @param dlineageEntity
     */
    private  void transformtoMySelfLineage(DlineageEntity dlineageEntity,String sql,EDbVendor  dbType,ConnectEntity connectEntity) {

        completionSql( dlineageEntity, sql,  dbType,  connectEntity);
        String dbName = connectEntity.getDbName();

        LineageParseResult lineageParseResult = new LineageParseResult();
        MegrezLineageDto megrezLineageDto = new MegrezLineageDto();
        Set<String> inputSet = new HashSet<>();
        Set<String> outputSet = new HashSet<>();
        List<String> fieldList = new ArrayList<>();
        List<demos.dlineage.entity.xmls.Relation> relations = dlineageEntity.getRelation();
        List<MegrezLineageEdge> edges = new ArrayList<>();
        if(relations != null){
            for (demos.dlineage.entity.xmls.Relation relation: relations){
                List<Target> targets = relation.getTarget();
                List<Source> sources = relation.getSource();

                MegrezLineageEdge edge = new MegrezLineageEdge();
                /**边source顶点ID*/
                List<String> sourcesList = new ArrayList<>();
                /**边sink顶点ID*/
                List<String> targetsList = new ArrayList<>();
                for(Source source : sources){
                    String inputColumn = source.getColumn();
                    String inPutTableName = source.getParentName();
                    inputSet.add(inPutTableName);
                    sourcesList.add(dbName+"."+inPutTableName+"."+inputColumn);
                    fieldList.add(dbName+"."+inPutTableName+"."+inputColumn);
                }
                for(Target target : targets){
                    String targetColumn = target.getColumn();
                    String outPutTableName = target.getParentName();
                    outputSet.add(outPutTableName);
                    targetsList.add(dbName+"."+outPutTableName+"."+targetColumn);
                    fieldList.add(dbName+"."+outPutTableName+"."+targetColumn);
                }
                edge.setSources(sourcesList);
                edge.setTargets(targetsList);
                edges.add(edge);
            }
            megrezLineageDto.setEdges(edges);
            lineageParseResult.setMegrezLineageDto(megrezLineageDto);
            lineageParseResult.setFieldList(fieldList);
            lineageParseResult.setInputList(hashSetToList(inputSet,dbName));
            lineageParseResult.setOutputList(hashSetToList(outputSet,dbName));
            logger.info(lineageParseResult.toString());
        }




    }


    /**
     * 将Set集合转换成list，因为不能强转
     *
     * @param oldSet
     * @return
     */
    public List<Entity> hashSetToList(Set<String> oldSet,String dbName)  {
        List<Entity>  list = new ArrayList<>();
        for(String table : oldSet ){
            Entity entity = new Entity(dbName,table,null,null);
            list.add(entity);
        }
        return list;
    }



    public  String getTableNames(String TableName, EDbVendor dbType,ConnectEntity connectEntity) {
        String columnsInDb = "";
        if(dbType == EDbVendor.dbvgreenplum){
            columnsInDb = GreenplumUtils.getTableNames(TableName,  connectEntity);
        }else if(dbType == EDbVendor.dbvoracle){
            columnsInDb = OracleUtils.getTableNames(TableName,  connectEntity);
        }
        return columnsInDb;

    }
}

package demos.dlineage.Connection;

import demos.dlineage.entity.ConnectEntity;
import demos.dlineage.ParseLineage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;

/**
 * Created by lcc on 2018/8/27.
 */
public class OracleUtils {

    private static final Logger logger = LoggerFactory.getLogger(ParseLineage.class);

    private static String DRIVE_NAME = "oracle.jdbc.driver.OracleDriver";




    public static Connection getConnect(String url,String userName ,String password){
        try{
            Class.forName(DRIVE_NAME);
            Connection conn = DriverManager.getConnection(url, userName, password);
            logger.info("提示！oracle数据库连接成功");
            return  conn;
        }catch (Exception e){
            e.printStackTrace();
        }
        return  null;
    }

    public static String getTableNames(String TableName,ConnectEntity connectEntity ) {
        String columnsInDb = "";
        try {
            String url = connectEntity.getUrl();
            String userName = connectEntity.getUserName();
            String password = connectEntity.getPassword();
            Connection conn  = getConnect(url, userName, password);
            String sql = "select * from "+TableName+" limit 0";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData() ;
            int colcount = rsmd.getColumnCount();
            for (int i = 1; i <= colcount; i++)
            {
                columnsInDb = columnsInDb+","+rsmd.getColumnName(i);
            }
            columnsInDb = columnsInDb.substring(1,columnsInDb.length());
            ps.close();
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return columnsInDb;
    }


    /**
     * 获取数据库中的存储过程
     *
     * @param functionName
     */
    public static String getStoreFunctionInDb(String functionName,ConnectEntity connectEntity) {
        String storeSql = "";
        try {
            String url = connectEntity.getUrl();
            String userName = connectEntity.getUserName();
            String password = connectEntity.getPassword();
            String dbName = connectEntity.getDbName();
            Connection conn  = getConnect(url, userName, password);
            String sqlssss = "select * from all_source where OWNER='"+dbName.toUpperCase()+"' and name='"+functionName.trim().toUpperCase()+"' ORDER BY line ASC";
            PreparedStatement ps = conn.prepareStatement(sqlssss);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                String text = rs.getString("TEXT");
                storeSql = storeSql +  text;
            }

            logger.info("提示！orcale的存储过程："+storeSql);
            ps.close();
            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return storeSql;
    }


}

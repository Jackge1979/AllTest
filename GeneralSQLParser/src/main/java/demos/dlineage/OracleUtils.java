package demos.dlineage;

import java.sql.*;

/**
 * Created by lcc on 2018/8/27.
 */
public class OracleUtils {


    private static String DRIVE_NAME = "oracle.jdbc.driver.OracleDriver";




    public static Connection getConnect(String url,String userName ,String password){

        try{
            Class.forName(DRIVE_NAME);
            Connection conn = DriverManager.getConnection(url, userName, password);
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
            Connection conn = DriverManager.getConnection(url, userName, password);
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


}

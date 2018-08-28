package demos.dlineage.Connection;

import demos.dlineage.entity.ConnectEntity;

import java.sql.*;

/**
 * Created by lcc on 2018/8/28.
 */
public class GreenplumUtils {


    private static String DRIVE_NAME = "org.postgresql.Driver";




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
            Connection conn  = getConnect(url, userName, password);
            String sqlssss = "select * from pg_proc where proname='"+functionName+"';";
            PreparedStatement pre = conn.prepareStatement(sqlssss);
            ResultSet rs = pre.executeQuery();
            while(rs.next()){
//                String proname = rs.getString("proname");
//                String proargnames = rs.getString("proargnames");
                 storeSql = rs.getString("prosrc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return storeSql;

    }


}

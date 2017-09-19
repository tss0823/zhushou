package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import org.slf4j.Logger;

import java.sql.*;

/**
 * Created by shan on 2017/9/8.
 */
public class JdbcUtils {
    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");
    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(JdbcUtils.class);
    public static void execute(DbConfigure dbConfigure, String sql) {
        bisLog.info("sql="+sql);
        try {
            Class.forName(dbConfigure.getDriver());
            // 获取数据库连接
            Connection conn = DriverManager.getConnection(dbConfigure.getUrl(), dbConfigure.getUser(), dbConfigure.getPassword());

            // 使用Connection来创建一个Statment对象
            Statement stmt = conn.createStatement();

            // 执行DDL,创建数据表
            int result = stmt.executeUpdate(sql);
            bisLog.info("result="+result);

        } catch (Exception e) {
            log.error("sql="+sql);
            throw new RuntimeException("execute sql failed!",e);
        }
    }

    public static void query() {
        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // 1加载驱动(根据不同的数据库，给DriverManager添加不同的驱动)
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");

            // 2连接DB
            con = DriverManager.getConnection("jdbc:odbc:myDB", "li", "1234");

            // 3执行SQL
            stmt = con.createStatement();

            // 4获得结果集
            rs = stmt.executeQuery("SELECT * FROM employee");

            // 5对获取的数据进行处理
            while (rs.next()) {
                System.out.println("编号:" + rs.getString("no") + "\t" + "姓名:"
                        + rs.getString("name") + "\t" + "性别:"
                        + rs.getString("sex") + "\t" + "工资：" + rs.getString(4));
            }

        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            // 6关闭连接
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
                if (con != null) {
                    con.close();
                    con = null;
                }
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}

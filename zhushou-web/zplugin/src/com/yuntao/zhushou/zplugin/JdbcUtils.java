package com.yuntao.zhushou.zplugin;

import com.yuntao.zhushou.model.domain.codeBuild.DbConfigure;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;

import java.sql.*;

/**
 * Created by shan on 2017/9/8.
 */
public class JdbcUtils {
    private final static Logger bisLog = org.slf4j.LoggerFactory.getLogger("bis");
    protected final static Logger log = org.slf4j.LoggerFactory.getLogger(JdbcUtils.class);
    public static void execute(DbConfigure dbConfigure, String sql) {
        bisLog.info("param sql="+sql);
        try {
            Class.forName(dbConfigure.getDriver());
            // 获取数据库连接
            Connection conn = DriverManager.getConnection(dbConfigure.getUrl(), dbConfigure.getUser(), dbConfigure.getPassword());

            // 使用Connection来创建一个Statment对象
            Statement stmt = conn.createStatement();

            // 执行DDL,创建数据表
            String[] split = sql.split(";");
            if(split.length > 1){
                for (int i = 0; i < split.length -1; i++) {
                    String s = split[i];
                    if (StringUtils.isNotEmpty(s)) {
                        stmt.addBatch(s);
                        bisLog.info("sql="+s);
                    }
                }
                stmt.executeBatch();
            }else{
                int result = stmt.executeUpdate(sql);
                bisLog.info("result="+result);
            }

        } catch (Exception e) {
            log.error("sql="+sql);
            throw new RuntimeException("execute sql failed! ",e);
        }
    }

    public static void main(String[] args) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            // 获取数据库连接
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/test1", "root", "123456");

            // 使用Connection来创建一个Statment对象
            Statement stmt = conn.createStatement();

            // 执行DDL,创建数据表)

            String sql = "DROP TABLE IF EXISTS `user`;" +
                    "CREATE TABLE `user` (" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',\n" +
                    "  `gmtCreate` datetime DEFAULT NULL COMMENT '创建时间',\n" +
                    "  `gmtModify` datetime DEFAULT NULL COMMENT '修改时间',\n" +
                    "  `delState` tinyint(4) DEFAULT NULL COMMENT '删除状态（0：已删除；1：未删除）',\n" +
                    "  `accountNo` varchar(20) DEFAULT NULL COMMENT '账号',\n" +
                    "  `pwd` varchar(30) DEFAULT NULL COMMENT '密码',\n" +
                    "  `nickname` varchar(10) DEFAULT NULL COMMENT '昵称',\n" +
                    "  `type` int(4) DEFAULT NULL COMMENT '类型',\n" +
                    "  `status` int(4) DEFAULT NULL COMMENT '状态',\n" +
                    "  `mobile` varchar(20) DEFAULT NULL COMMENT '手机',\n" +
                    "  `email` varchar(50) DEFAULT NULL COMMENT '邮件',\n" +
                    "  `sex` int(4) DEFAULT NULL COMMENT '性别',\n" +
                    "  `birthday` varchar(30) DEFAULT NULL COMMENT '生日',\n" +
                    "  `message` varchar(500) DEFAULT NULL COMMENT '宣言',\n" +
                    "  `avatar` varchar(500) DEFAULT NULL COMMENT '头像',\n" +
                    "  `desc` varchar(2000) DEFAULT NULL COMMENT '描述',\n" +
                    "  `payPwd` varchar(20) DEFAULT NULL COMMENT '支付密码',\n" +
                    "  `address` varchar(500) DEFAULT NULL COMMENT '用户地址',\n" +
                    "  `userName` varchar(30) DEFAULT NULL COMMENT '用户姓名',\n" +
                    "  `role` varchar(1000) DEFAULT NULL COMMENT '角色',\n" +
                    "  PRIMARY KEY (`id`)\n" +
                    ") ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COMMENT='用户';";
            String[] split = sql.split(";");
            stmt.addBatch(split[0]);
            stmt.addBatch(split[1]);
            stmt.executeBatch();
//            bisLog.info("result="+result);
        } catch (Exception e) {
            e.printStackTrace();
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

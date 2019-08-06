package com.bd.booksystem.dao.Imp;


import com.bd.booksystem.constants.Constants;
import com.bd.booksystem.util.ConfigManager;

import java.sql.*;

public class BaseDaoImp {
    /**
     * @Description: getConn
     * @Param: []
     * @return: java.sql.Connection
     * @Author: Mr.zhang
     * @Date: 2019-08-05
     */
    public Connection getConn() {
        String driver = ConfigManager.getValue(Constants.MYSQL_DRIVER);
        String url = ConfigManager.getValue(Constants.MYSQL_URL);
        String user = ConfigManager.getValue(Constants.MYSQL_USER);
        String password = ConfigManager.getValue(Constants.MYSQL_PASSWORD);
        Connection conn = null;
        try {
            // 加载驱动
            Class.forName(driver);
            // 获取数据库连接,返回的conn代表了java和数据库的连接
            conn = DriverManager.getConnection(url, user, password);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return conn;
    }

    /**
     * @Description: close
     * @Param: [conn, ps, rs]
     * @return: void
     * @Author: Mr.zhang
     * @Date: 2019-08-05
     */
    public void close(Connection conn, PreparedStatement ps, ResultSet rs) {
        try {
            if (conn != null) conn.close();
            if (ps != null) ps.close();
            if (rs != null) rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}

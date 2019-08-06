package com.bd.booksystem.dao.Imp;

import com.bd.booksystem.dao.StudentDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @program: bookSystem
 * @description: implement studentDao
 * @author: Mr.zhang
 * @create: 2019-08-05 21:07
 **/
public class StudentDaoImp extends BaseDaoImp implements StudentDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    /**
    * @Description: getBySidAndPwd 
    * @Param: [sid, pwd] 
    * @return: boolean 
    * @Author: Mr.zhang
    * @Date: 2019-08-05 
    */
    @Override
    public boolean getBySidAndPwd(int sid, String pwd) {
        boolean flag = false;
        // 获取数据库连接
        conn = this.getConn();
        String sql = "select * from student where sid=? and spwd=?";
        try {

            ps = conn.prepareStatement(sql);
            ps.setInt(1, sid);
            ps.setString(2, pwd);
            // 执行查询
            rs = ps.executeQuery();
            // 判断rs中是否包含数据库中取出的数据
            flag = rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
        return flag;
    }
    
    /** 
    * @Description: getEmailBySid
    * @Param: [sid] 
    * @return: java.lang.String 
    * @Author: Mr.zhang
    * @Date: 2019-08-06 
    */
    @Override
    public String getEmailBySid(int sid) {
        // 获取连接
        conn = this.getConn();
        String sql = "select email from student where sid=?";
        String email = "";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, sid);
            rs = ps.executeQuery();
            while (rs.next()) {
                email = rs.getString("email");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }

        return email;
    }

    @Override
    public void updatePasswordBySid(int sid, String password) {
        conn = this.getConn();
        String sql = "update student set spwd=? where sid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, password);
            ps.setInt(2, sid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
    }

}

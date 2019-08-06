package com.bd.booksystem.util;

import com.bd.booksystem.dao.Imp.BaseDaoImp;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

/**
 * @program: bookSystem
 * @description: mock data into database
 * @author: Mr.zhang
 * @create: 2019-08-05 19:43
 **/
public class MockData {
    public static void main(String[] args) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        // 获取连接
        BaseDaoImp baseDaoImp = new BaseDaoImp();
        conn = baseDaoImp.getConn();
        String[] names = {"张三", "李四", "王五", "赵柳", "孙琪", "周就", "郑和", "上官泡"};
        int[] ages = {13, 12, 18, 23, 56, 78, 30};
        String[] sexs = {"男", "女", "未知"};
        try {
            Random random = new Random();
            for (int i = 1; i < 30; i++) {
                int namesRandom = random.nextInt(names.length);
                int ageRandom = random.nextInt(ages.length);
                int sexRandom = random.nextInt(sexs.length);
                String sql = "insert into student values(?,?,?,?,?,?)";
                ps = conn.prepareStatement(sql);
                ps.setInt(1, i);
                ps.setString(2, names[namesRandom]);
                ps.setString(3, "123456");
                ps.setString(4, sexs[sexRandom]);
                ps.setInt(5, ages[ageRandom]);
                ps.setString(6, "test@qq.com");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            baseDaoImp.close(conn, ps, rs);
        }
    }
}

package com.bd.booksystem.dao.Imp;

import com.bd.booksystem.dao.BookDao;
import com.bd.booksystem.model.Book;

import javax.mail.internet.InternetAddress;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

/**
 * @program: bookSystem
 * @description: use BookDaoImp to communication with book database
 * @author: Mr.zhang
 * @create: 2019-08-06 21:36
 **/
public class BookDaoImp extends BaseDaoImp implements BookDao {
    Connection conn = null;
    PreparedStatement ps = null;
    ResultSet rs = null;

    @Override
    public void addBook(Book book) {
        conn = this.getConn();
        String sql = "insert into book(bid,bname,category,status) values(?,?,?,?)";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, book.getBid());
            ps.setString(2, book.getBname());
            ps.setString(3, book.getCategory());
            ps.setInt(4, book.getStatus());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
    }

    @Override
    public LinkedList<Book> getBookByStatus(int status) {
        LinkedList<Book> bookList = new LinkedList<Book>();
        conn = this.getConn();
        String sql = "select * from book where status=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, status);
            rs = ps.executeQuery();
            while (rs.next()) {
                int bid = rs.getInt("bid");
                String bname = rs.getString("bname");
                String category = rs.getString("category");
                int st = rs.getInt("status");
                bookList.add(new Book(bid, bname, category, st));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return bookList;
    }

    public static void main(String[] args) {
        LinkedList<Book> list = new BookDaoImp().getBookByStatus(3);
        for (Book book : list) {
            System.out.println(book);
        }
        Integer num = new Integer(1);
        System.out.println(num.toString());
    }
}

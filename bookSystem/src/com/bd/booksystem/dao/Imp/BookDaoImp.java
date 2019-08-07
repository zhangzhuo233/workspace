package com.bd.booksystem.dao.Imp;

import com.bd.booksystem.dao.BookDao;
import com.bd.booksystem.model.Book;
import com.bd.booksystem.model.Student;

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
        } finally {
            this.close(conn, ps, rs);
        }

        return bookList;
    }

    @Override
    public Book getBookByBid(int bid) {
        Book book = null;
        conn = this.getConn();
        String sql = "select * from book where bid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bid);
            rs = ps.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("bid");
                String bname = rs.getString("bname");
                String category = rs.getString("category");
                book = new Book(id, bname, category);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }

        return book;
    }

    @Override
    public void updateBook(String bname, String category, int bid) {
        Book book = null;
        conn = this.getConn();
        String sql = "update book set bname=?,category=? where bid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setString(1, bname);
            ps.setString(2, category);
            ps.setInt(3, bid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
    }

    @Override
    public Student getStudentByBid(int bid) {
        Student stu = null;
        conn = this.getConn();
        String sql = "select st.* from book b join  student st where b.bid=? and b.sid=st.sid and b.status=0";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bid);
            rs = ps.executeQuery();
            while (rs.next()) {
                int sid = rs.getInt("sid");
                String sname = rs.getString("sname");
                String sex = rs.getString("ssex");
                int age = rs.getInt("sage");
                String email = rs.getString("email");

                stu = new Student(sid, sname, sex, age, email);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
        return stu;
    }

    @Override
    public void deleteBookByBid(int bid) {
        conn = this.getConn();
        String sql = "delete from book where bid=?";
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
    }

    @Override
    public void borrowAndGivebackBookByBid(int bid, int status, int sid) {
        conn = this.getConn();
        String sql = "update book set sid=?,status=? where bid=?";
        try {
            ps= conn.prepareStatement(sql);
            ps.setInt(1, sid);
            ps.setInt(2, status);
            ps.setInt(3, bid);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
    }

    @Override
    public int getStatusByBid(int bid) {
        conn = this.getConn();
        String sql = "select * from book where bid=?";
        // 2代表没有查到
        int status = 2;
        try {
            ps = conn.prepareStatement(sql);
            ps.setInt(1, bid);
            rs = ps.executeQuery();
            while (rs.next()) {
                status = rs.getInt("status");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            this.close(conn, ps, rs);
        }
        return status;
    }

    public static void main(String[] args) {
        // LinkedList<Book> list = new BookDaoImp().getBookByStatus(3);
        // for (Book book : list) {
        //     System.out.println(book);
        // }
        // System.out.println(new BookDaoImp().getBookByBid(1));
        // System.out.println(new BookDaoImp().getStudentByBid(1));
        // new BookDaoImp().deleteBookByBid(7);
        // new BookDaoImp().borrowAndGivebackBookByBid(8, 1, 6);
        System.out.println(new BookDaoImp().getStatusByBid(1));
    }
}

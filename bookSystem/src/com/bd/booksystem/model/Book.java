package com.bd.booksystem.model;

/**
 * @program: bookSystem
 * @description: use javaBean to save book information
 * @author: Mr.zhang
 * @create: 2019-08-06 21:31
 **/
public class Book {
    // 数据库表中所存储的字段一一陈列
    private int bid;
    private String bname;
    private String category;
    private int sid;
    private int status;

    public Book(int bid, String bname, String category, int status) {
        this.bid = bid;
        this.bname = bname;
        this.category = category;
        this.status = status;
    }

    public Book(int bid, String bname, String category) {
        this.bid = bid;
        this.bname = bname;
        this.category = category;
    }

    @Override
    public String toString() {
        return "Book{" +
                "bid=" + bid +
                ", bname='" + bname + '\'' +
                ", category='" + category + '\'' +
                ", sid=" + sid +
                ", status=" + status +
                '}';
    }

    public int getBid() {
        return bid;
    }
    public String getBname() {
        return bname;
    }
    public String getCategory() {
        return category;
    }
    public int getStatus() {
        return status;
    }

}

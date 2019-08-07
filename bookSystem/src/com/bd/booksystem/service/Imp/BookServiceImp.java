package com.bd.booksystem.service.Imp;

import com.bd.booksystem.dao.BookDao;
import com.bd.booksystem.dao.Imp.BookDaoImp;
import com.bd.booksystem.model.Book;
import com.bd.booksystem.model.Student;
import com.bd.booksystem.service.BookService;

import java.util.LinkedList;

/**
 * @program: bookSystem
 * @description: implement BookService
 * @author: Mr.zhang
 * @create: 2019-08-06 21:47
 **/
public class BookServiceImp implements BookService {
    BookDao bookDao = new BookDaoImp();
    @Override
    public void addBook(Book book) {
        bookDao.addBook(book);
    }

    @Override
    public LinkedList<Book> getBookByStatus(int status) {
        return bookDao.getBookByStatus(status);
    }

    @Override
    public Book getBookByBid(int bid) {
        return bookDao.getBookByBid(bid);
    }

    @Override
    public void updateBook(String bname, String category, int bid) {
        bookDao.updateBook(bname, category, bid);
    }

    @Override
    public Student getStudentByBid(int bid) {
        return bookDao.getStudentByBid(bid);
    }

    @Override
    public void deleteBookByBid(int bid) {
        bookDao.deleteBookByBid(bid);
    }

    @Override
    public void borrowAndGivebackBookByBid(int bid, int status, int sid) {
        bookDao.borrowAndGivebackBookByBid(bid, status, sid);
    }

    @Override
    public int getStatusByBid(int bid) {
        return bookDao.getStatusByBid(bid);
    }
}

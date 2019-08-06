package com.bd.booksystem.service.Imp;

import com.bd.booksystem.dao.BookDao;
import com.bd.booksystem.dao.Imp.BookDaoImp;
import com.bd.booksystem.model.Book;
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
}

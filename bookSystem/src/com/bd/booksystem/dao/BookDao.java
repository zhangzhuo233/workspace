package com.bd.booksystem.dao;

import com.bd.booksystem.model.Book;

import java.util.LinkedList;

public interface BookDao {
    public void addBook(Book book);
    public LinkedList<Book> getBookByStatus(int status);
}

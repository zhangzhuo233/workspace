package com.bd.booksystem.service;

import com.bd.booksystem.model.Book;

import java.util.LinkedList;

public interface BookService {
    public void addBook(Book book);
    public LinkedList<Book> getBookByStatus(int status);
}

package com.bd.booksystem.service;

import com.bd.booksystem.model.Book;
import com.bd.booksystem.model.Student;

import java.util.LinkedList;

public interface BookService {
    public void addBook(Book book);
    public LinkedList<Book> getBookByStatus(int status);
    public Book getBookByBid(int bid);
    public void updateBook(String bname, String category, int bid);
    public Student getStudentByBid(int bid);
    public void deleteBookByBid(int bid);
    public void borrowAndGivebackBookByBid(int bid, int status, int sid);
    public int getStatusByBid(int bid);
}

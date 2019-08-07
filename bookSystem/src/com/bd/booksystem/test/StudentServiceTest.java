package com.bd.booksystem.test;

import com.bd.booksystem.model.Book;
import com.bd.booksystem.model.Student;
import com.bd.booksystem.service.BookService;
import com.bd.booksystem.service.Imp.BookServiceImp;
import com.bd.booksystem.service.Imp.StudentServiceImp;
import com.bd.booksystem.service.StudentService;
import com.bd.booksystem.util.SendMessage;

import java.util.LinkedList;
import java.util.Scanner;

/**
 * @program: bookSystem
 * @description: bookSystem login test
 * @author: Mr.zhang
 * @create: 2019-08-05 21:42
 **/
public class StudentServiceTest {
    public static void main(String[] args) {
        StudentService studentService = new StudentServiceImp();
        BookService bookService = new BookServiceImp();
        Scanner scanner = new Scanner(System.in);
        System.out.println("输入账号");
        int sid = scanner.nextInt();
        System.out.println("输入密码");
        String pwd = scanner.next();
        // 验证用户是否存在
        boolean flag = studentService.foundBySidAndPwd(sid, pwd);
        if (flag) {
            System.out.println("登录成功!");
            while(true) {
                System.out.println("请输入你的操作:\n1.新增图书\n2.查询可借阅图书\n3.查询已借阅图书\n" +
                        "4.查询所有图书\n5.修改图书\n6.查询借阅人信息\n7.删除图书\n8.借阅图书\n9.归还图书\n10.退出\n请输入编号");
                int operation = scanner.nextInt();
                switch (operation) {
                    case 1:
                        System.out.println("1.录入图书编号");
                        int bid = scanner.nextInt();
                        System.out.println("2.录入图书名称");
                        String bname = scanner.next();
                        System.out.println("3.录入图书摘要");
                        String category = scanner.next();
                        System.out.println("4.录入该图书借阅状态(0代表已全部借出)");
                        int status = scanner.nextInt();
                        Book book = new Book(bid, bname, category, status);
                        bookService.addBook(book);
                        System.out.println("该图书添加成功!");
                        break;
                    case 2:
                        LinkedList<Book> list = bookService.getBookByStatus(1);
                        for (Book tmp : list) {
                            System.out.println(tmp);
                        }
                        break;
                    case 3:
                        list = bookService.getBookByStatus(0);
                        for (Book tmp : list) {
                            System.out.println(tmp);
                        }
                        break;
                    case 4:
                        list = bookService.getBookByStatus(0);
                        LinkedList<Book> list1 = bookService.getBookByStatus(1);
                        for (Book tmp : list) {
                            System.out.println(tmp);
                        }
                        for (Book tmp : list1) {
                            System.out.println(tmp);
                        }
                        break;
                    case 5:
                        System.out.println("请输入要修改图书的编号:");
                        bid = scanner.nextInt();
                        System.out.println("图书的原始信息:");
                        // 输出原始信息
                        System.out.println(bookService.getBookByBid(bid));
                        System.out.println("请输入修改后的图书名:");
                        bname = scanner.next();
                        System.out.println("请输入修改后的图书摘要:");
                        category = scanner.next();
                        // 进行修改
                        bookService.updateBook(bname, category, bid);
                        System.out.println("修改成功!");
                        break;
                    case 6:
                        System.out.println("请输入图书编号:");
                        bid = scanner.nextInt();
                        // 查询借阅人信息
                        Student stu = bookService.getStudentByBid(bid);
                        System.out.println(stu);
                        break;
                    case 7:
                        System.out.println("请输入要删除图书的编号:");
                        bid = scanner.nextInt();
                        if (null == bookService.getBookByBid(bid)) {
                            System.out.println("查无此书!");
                        } else {
                            System.out.println("删除中...");
                            bookService.deleteBookByBid(bid);
                            System.out.println("已删除!");
                        }
                        break;
                    case 8:
                        System.out.println("请输入要借阅图书的编号:");
                        bid = scanner.nextInt();
                        if (0 == bookService.getStatusByBid(bid)) {
                            System.out.println("此书已借出!");
                        } else {
                            System.out.println("借阅中...");
                            bookService.borrowAndGivebackBookByBid(bid, 0, sid);
                            System.out.println("已借阅!");
                        }
                        break;
                    case 9:
                        System.out.println("请输入要归还图书的编号:");
                        bid = scanner.nextInt();
                        if (0 == bookService.getStatusByBid(bid)) {
                            System.out.println("归还中...");
                            bookService.borrowAndGivebackBookByBid(bid, 1, 0);
                            System.out.println("已归还!");
                        } else {
                            System.out.println("此书已归还!或者查无此书!");
                        }
                        break;
                    case 10:
                        System.exit(0);
                        break;
                    default:
                        break;
                }
            }
        } else {
            System.out.println("登录失败!用户名or密码不正确!是否找回密码：1.找回2.退出");
            // 验证邮箱功能
            int num = scanner.nextInt();
            switch (num) {
                case 1:
                    String email = studentService.getEmailById(sid);
                    System.out.println("系统将发送验证码...");
                    String verfCode = new SendMessage().sendToEmail(email);
                    System.out.println("验证码已发送...");
                    System.out.println("请输入验证码:");
                    String code = scanner.next();
                    if (code.equals(verfCode)) {
                        System.out.println("验证码正确");
                        System.out.println("请输入密码:");
                        String password = scanner.next();
                        System.out.println("请再次输入密码:");
                        String verfPassword = scanner.next();
                        if (password.equals(verfPassword)) {
                            studentService.updatePwdBySid(sid, password);
                            System.out.println("密码修改成功...");
                        } else {
                            System.out.println("两次密码不一致...");
                        }
                    } else {
                        System.out.println("验证码错误");
                    }
                    break;
                case 2:
                    System.out.println("期待下次登录!");
                    break;
                default:
                    break;
            }
        }
    }
}

package com.bd.booksystem.test;

import com.bd.booksystem.dao.Imp.BaseDaoImp;

/**
 * @program: bookSystem
 * @description: BaseDaoImp.getConnTest
 * @author: Mr.zhang
 * @create: 2019-08-05 19:01
 **/
public class ConnectionTest {
    public static void main(String[] args) {
        System.out.println(new BaseDaoImp().getConn());
    }
}

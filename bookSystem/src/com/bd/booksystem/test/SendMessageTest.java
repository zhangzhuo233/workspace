package com.bd.booksystem.test;

import com.bd.booksystem.util.SendMessage;

/**
 * @program: bookSystem
 * @description: util/SendMessage class Test
 * @author: Mr.zhang
 * @create: 2019-08-06 18:10
 **/
public class SendMessageTest {
    public static void main(String[] args) {
        new SendMessage().sendToEmail("1148581966@qq.com");
    }
}

package com.bd.booksystem.test;

import com.bd.booksystem.constants.Constants;
import com.bd.booksystem.util.ConfigManager;


public class ConfigManagerTest {
    public static void main(String[] args) {
        System.out.println(ConfigManager.getValue(Constants.MYSQL_URL));
    }
}

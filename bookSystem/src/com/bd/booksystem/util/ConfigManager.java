package com.bd.booksystem.util;

import com.bd.booksystem.constants.Constants;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigManager {
    /** 
    * @Description: getValue 
    * @Param: [key] 
    * @return: java.lang.String 
    * @Author: Mr.zhang
    * @Date: 2019-08-06
    */
    public static String getValue(String key) {
        // 创建properties对象
        Properties properties = new Properties();
        String value = null;
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(Constants.DATABASES_PROPERTIES);
            properties.load(inputStream);
            value = properties.getProperty(key);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (null != inputStream) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return value;
    }
}

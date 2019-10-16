package com.bd.sparkproject.util;

import com.bd.sparkproject.conf.ConfigurationManager;
import com.bd.sparkproject.constant.Constants;

/**
 * @program: smp-spark-project
 * @description: 日志打印系统
 * @author: Mr.zhang
 * @create: 2019-10-16 01:30
 **/
public class LogUtils {
    public static <T> void LogPrint(String o, T log) {
        if (ConfigurationManager.getBoolean(Constants.DEBUG)) {
            System.out.println(o + ": " + log);
        }
    }
}

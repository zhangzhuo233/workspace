package com.bd.sparkproject;

import com.alibaba.fastjson.JSONArray;

/**
 * @program: smp-spark-project
 * @description: fastjson测试类
 * @author: Mr.zhang
 * @create: 2019-10-13 19:47
 **/
public class FastjsonTest {
    public static void main(String[] args) {
        String json = "[{'学生':'张三', '班级':'一班', '年级':'大一', '科目':'高数', '成绩':90}, {'学生':'李四', '班级':'二班', '年级':'大一', '科目':'高数', '成绩':80}]";
        JSONArray jsonArray = JSONArray.parseArray(json);
        System.out.println(jsonArray);
        System.out.println(jsonArray.getJSONObject(0).getString("年级"));

        // String str = "hah|aa|bb|cc";
        // String[] strings = str.split("\\|");
        // for (String tmp : strings) {
        //     System.out.println(tmp);
        // }
        // Object a = null;
        // String res = (a!=null ? "str1" : "str2" + "|");
        // System.out.println(res);
        // System.out.println(res.endsWith("|"));
    }
}

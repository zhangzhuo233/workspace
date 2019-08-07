package com.bd.booksystem.model;

/**
 * @program: bookSystem
 * @description: use javabean to save student information
 * @author: Mr.zhang
 * @create: 2019-08-07 12:27
 **/
public class Student {
    private int sid;
    private String sname;
    private String ssex;
    private int age;
    private String email;

    public Student(int sid, String sname, String ssex, int age, String email) {
        this.sid = sid;
        this.sname = sname;
        this.ssex = ssex;
        this.age = age;
        this.email = email;
    }

    @Override
    public String toString() {
        return "Student{" +
                "sid=" + sid +
                ", sname='" + sname + '\'' +
                ", ssex='" + ssex + '\'' +
                ", age=" + age +
                ", email='" + email + '\'' +
                '}';
    }

    public int getSid() {
        return sid;
    }

    public String getSname() {
        return sname;
    }

    public String getSsex() {
        return ssex;
    }

    public int getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }
}

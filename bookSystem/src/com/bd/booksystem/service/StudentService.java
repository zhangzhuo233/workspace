package com.bd.booksystem.service;

/**
 * @program: bookSystem
 * @description: student service
 * @author: Mr.zhang
 * @create: 2019-08-05 21:35
 **/
public interface StudentService {
    public boolean foundBySidAndPwd(int sid, String pwd);
    public String getEmailById(int sid);
    public void updatePwdBySid(int sid, String password);
}

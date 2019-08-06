package com.bd.booksystem.dao;

public interface StudentDao {
    public boolean getBySidAndPwd(int sid, String pwd);
    public String getEmailBySid(int sid);
    public void updatePasswordBySid(int sid, String password);
}


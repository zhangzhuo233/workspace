package com.bd.booksystem.service.Imp;

import com.bd.booksystem.dao.Imp.StudentDaoImp;
import com.bd.booksystem.dao.StudentDao;
import com.bd.booksystem.service.StudentService;

/**
 * @program: bookSystem
 * @description: implement studentService
 * @author: Mr.zhang
 * @create: 2019-08-05 21:37
 **/
public class StudentServiceImp implements StudentService {
    StudentDao studentDao = new StudentDaoImp();
    /** 
    * @Description: foundBySidAndPwd 
    * @Param: [sid, pwd] 
    * @return: boolean 
    * @Author: Mr.zhang
    * @Date: 2019-08-05 
    */
    @Override
    public boolean foundBySidAndPwd(int sid, String pwd) {
        return studentDao.getBySidAndPwd(sid, pwd);
    }

    @Override
    public String getEmailById(int sid) {
        return studentDao.getEmailBySid(sid);
    }

    @Override
    public void updatePwdBySid(int sid, String password) {
        studentDao.updatePasswordBySid(sid, password);
    }
}

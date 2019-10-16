package com.bd.sparkproject.dao.factory;

import com.bd.sparkproject.dao.ISessionAggrStatDAO;
import com.bd.sparkproject.dao.ITaskDAO;
import com.bd.sparkproject.dao.impl.SessionAggrStatDAOImp;
import com.bd.sparkproject.dao.impl.TaskDAOImpl;

/**
 * @program: smp-spark-project
 * @description: DAO工厂类
 * @author: Mr.zhang
 * @create: 2019-10-13 19:01
 **/
public class DAOFactory {
    public static ITaskDAO getTaskDAO() {
        return new TaskDAOImpl();
    }
    public static ISessionAggrStatDAO getSessionAggrStatDAO() {
        return new SessionAggrStatDAOImp();
    }
}

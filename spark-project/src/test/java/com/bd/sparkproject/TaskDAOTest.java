package com.bd.sparkproject;

import com.bd.sparkproject.dao.ITaskDAO;
import com.bd.sparkproject.dao.factory.DAOFactory;
import com.bd.sparkproject.domain.Task;

/**
 * @program: smp-spark-project
 * @description: 任务管理DAO测试类
 * @author: Mr.zhang
 * @create: 2019-10-13 19:07
 **/
public class TaskDAOTest {
    public static void main(String[] args) {
        ITaskDAO taskDAO = DAOFactory.getTaskDAO();
        Task task = taskDAO.findById(1);
        System.out.println(task.getTaskName());
    }
}

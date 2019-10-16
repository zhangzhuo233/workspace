package com.bd.sparkproject.dao.impl;

import com.bd.sparkproject.dao.ITaskDAO;
import com.bd.sparkproject.domain.Task;
import com.bd.sparkproject.jdbc.JDBCHelper;

import java.sql.ResultSet;

/**
 * @program: smp-spark-project
 * @description: 任务管理DAO实现类
 * @author: Mr.zhang
 * @create: 2019-10-13 18:46
 **/
public class TaskDAOImpl implements ITaskDAO {
    @Override
    public Task findById(long taskid) {
        final Task task = new Task();

        String sql = "select * from task where task_id=?";
        Object[] params = new Object[]{taskid};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeQuery(sql, params, new JDBCHelper.QueryCallback() {
            @Override
            public void process(ResultSet rs) throws Exception {
                if (rs.next()) {
                    long taskid = rs.getLong(1);
                    String taskName = rs.getString(2);
                    String createTime = rs.getString(3);
                    String startTime = rs.getString(4);
                    String finishTime = rs.getString(5);
                    String taskType = rs.getString(6);
                    String taskStatus = rs.getString(7);
                    String taskParam = rs.getString(8);

                    task.setTaskid(taskid);
                    task.setTaskName(taskName);
                    task.setCreateTime(createTime);
                    task.setStartTime(startTime);
                    task.setFinishTime(finishTime);
                    task.setTaskType(taskType);
                    task.setTaskStatus(taskStatus);
                    task.setTaskParam(taskParam);
                }
            }
        });

        return task;
    }
}

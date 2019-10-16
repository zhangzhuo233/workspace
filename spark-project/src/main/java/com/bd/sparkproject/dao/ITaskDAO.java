package com.bd.sparkproject.dao;

import com.bd.sparkproject.domain.Task;

/**
 * @program: smp-spark-project
 * @description: 任务管理DAO接口
 * @author: Mr.zhang
 * @create: 2019-10-13 18:08
 **/
public interface ITaskDAO {
        /**
         * 根据主键查询任务
         * @param taskid 主键
         * @return 任务
         */
        Task findById(long taskid);
}

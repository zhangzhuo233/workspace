package com.bd.sparkproject.dao;

import com.bd.sparkproject.domain.SessionAggrStat;

/**
 * @program: smp-spark-project
 * @description: session聚合统计模块DAO接口
 * @author: Mr.zhang
 * @create: 2019-10-16 01:21
 **/
public interface ISessionAggrStatDAO {
    void insert(SessionAggrStat sessionAggrStat);
}

package com.bd.sparkproject.dao.impl;

import com.bd.sparkproject.dao.ISessionAggrStatDAO;
import com.bd.sparkproject.domain.SessionAggrStat;
import com.bd.sparkproject.jdbc.JDBCHelper;

/**
 * @program: smp-spark-project
 * @description: session聚合统计DAO实现类
 * @author: Mr.zhang
 * @create: 2019-10-16 01:22
 **/
public class SessionAggrStatDAOImp implements ISessionAggrStatDAO {
    /**
     * 插入session聚合统计结果
     *
     * @param sessionAggrStat
     */
    @Override
    public void insert(SessionAggrStat sessionAggrStat) {
        String sql = "insert into session_aggr_stat "
                + "values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{sessionAggrStat.getTaskid(),
                sessionAggrStat.getSession_count(),
                sessionAggrStat.getVisit_length_1s_3s_ratio(),
                sessionAggrStat.getVisit_length_4s_6s_ratio(),
                sessionAggrStat.getVisit_length_7s_9s_ratio(),
                sessionAggrStat.getVisit_length_10s_30s_ratio(),
                sessionAggrStat.getVisit_length_30s_60s_ratio(),
                sessionAggrStat.getVisit_length_1m_3m_ratio(),
                sessionAggrStat.getVisit_length_3m_10m_ratio(),
                sessionAggrStat.getVisit_length_10m_30m_ratio(),
                sessionAggrStat.getVisit_length_30m_ratio(),
                sessionAggrStat.getStep_length_1_3_ratio(),
                sessionAggrStat.getStep_length_4_6_ratio(),
                sessionAggrStat.getStep_length_7_9_ratio(),
                sessionAggrStat.getStep_length_10_30_ratio(),
                sessionAggrStat.getStep_length_30_60_ratio(),
                sessionAggrStat.getStep_length_60_ratio()};

        JDBCHelper jdbcHelper = JDBCHelper.getInstance();
        jdbcHelper.executeUpdate(sql, params);
    }
}

package com.bd.sparkproject.dao.impl;

import com.bd.sparkproject.dao.ISessionRandomExtractDAO;
import com.bd.sparkproject.domain.SessionRandomExtract;
import com.bd.sparkproject.jdbc.JDBCHelper;

/**
 * @program: smp-spark-project
 * @description: ISessionExtractDAO实现类
 * @author: Mr.zhang
 * @create: 2019-10-17 22:56
 **/
public class SessionRandomExtarctDAOImp implements ISessionRandomExtractDAO {

    @Override
    public void insert(SessionRandomExtract sessionRandomExtract) {
        String sql = "insert into session_random_extract values(?,?,?,?,?)";

        Object[] params = new Object[]{sessionRandomExtract.getTaskid(),
                sessionRandomExtract.getSessionid(),
                sessionRandomExtract.getStartTime(),
                sessionRandomExtract.getSearchKeywords(),
                sessionRandomExtract.getClickCategoryIds()};
        JDBCHelper instance = JDBCHelper.getInstance();
        instance.executeUpdate(sql, params);
    }
}

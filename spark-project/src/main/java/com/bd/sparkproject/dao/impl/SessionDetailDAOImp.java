package com.bd.sparkproject.dao.impl;

import com.bd.sparkproject.dao.ISessionDetail;
import com.bd.sparkproject.domain.SessionDetail;
import com.bd.sparkproject.jdbc.JDBCHelper;

/**
 * @program: smp-spark-project
 * @description: ISessionDetail实现类
 * @author: Mr.zhang
 * @create: 2019-10-17 23:13
 **/
public class SessionDetailDAOImp implements ISessionDetail {
    @Override
    public void insert(SessionDetail sessionDetail) {
        String sql = "insert into session_detail values(?,?,?,?,?,?,?,?,?,?,?,?)";

        Object[] params = new Object[]{sessionDetail.getTaskid(),
                sessionDetail.getUserid(),
                sessionDetail.getSessionid(),
                sessionDetail.getPageid(),
                sessionDetail.getActionTime(),
                sessionDetail.getSearchKeyword(),
                sessionDetail.getClickCategoryId(),
                sessionDetail.getClickProductId(),
                sessionDetail.getOrderCategoryIds(),
                sessionDetail.getOrderProductIds(),
                sessionDetail.getPayCategoryIds(),
                sessionDetail.getPayProductIds()};

        JDBCHelper instance = JDBCHelper.getInstance();
        instance.executeUpdate(sql, params);

    }
}

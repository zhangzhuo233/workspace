package com.bd.sparkproject.spark.session;

import com.bd.sparkproject.constant.Constants;
import com.bd.sparkproject.util.StringUtils;
import org.apache.spark.AccumulatorParam;

/**
 * @program: smp-spark-project
 * @description:
 * @author: Mr.zhang
 * @create: 2019-10-15 22:49
 **/
public class SessionAggrStatAccumulator implements AccumulatorParam<String> {
    /**
     * 数据均初始化为0
     *
     * @param v
     * @return
     */
    @Override
    public String zero(String v) {
        return Constants.SESSION_COUNT + "=0|"
                + Constants.TIME_PERIOD_1s_3s + "=0|"
                + Constants.TIME_PERIOD_4s_6s + "=0|"
                + Constants.TIME_PERIOD_7s_9s + "=0|"
                + Constants.TIME_PERIOD_10s_30s + "=0|"
                + Constants.TIME_PERIOD_30s_60s + "=0|"
                + Constants.TIME_PERIOD_1m_3m + "=0|"
                + Constants.TIME_PERIOD_3m_10m + "=0|"
                + Constants.TIME_PERIOD_10m_30m + "=0|"
                + Constants.TIME_PERIOD_30m + "=0|"
                + Constants.STEP_PERIOD_1_3 + "=0|"
                + Constants.STEP_PERIOD_4_6 + "=0|"
                + Constants.STEP_PERIOD_7_9 + "=0|"
                + Constants.STEP_PERIOD_10_30 + "=0|"
                + Constants.STEP_PERIOD_30_60 + "=0|"
                + Constants.STEP_PERIOD_60 + "=0";
    }

    /**
     * 在v1的字符串中找到字段v2，将v2对应的值累加1
     *
     * @param v1
     * @param v2
     * @return
     */
    @Override
    public String addAccumulator(String v1, String v2) {
        return add(v1, v2);
    }

    @Override
    public String addInPlace(String v1, String v2) {
        return add(v1, v2);
    }

    /**
     * session统计 计算逻辑
     * 连接串v1中 找到v2对应的值 累加1
     *
     * @param v1
     * @param v2
     * @return 新的v1
     */
    private String add(String v1, String v2) {
        if (StringUtils.isEmpty(v1)) {
            return v2;
        }
        String oldValue = StringUtils.getFieldFromConcatString(v1, "\\|", v2);
        if (oldValue != null) {
            int newValue = Integer.valueOf(oldValue) + 1;
            return StringUtils.setFieldInConcatString(v1, "\\|",
                    v2, String.valueOf(newValue));
        }
        // oldValue为null，工具类中的实现代表字段没有找到，故返回源字符串
        return v1;
    }
}

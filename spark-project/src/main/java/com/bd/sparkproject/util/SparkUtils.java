package com.bd.sparkproject.util;

import com.alibaba.fastjson.JSONObject;
import com.bd.sparkproject.MockData;
import com.bd.sparkproject.conf.ConfigurationManager;
import com.bd.sparkproject.constant.Constants;
import org.apache.spark.SparkContext;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.DataFrame;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.sql.hive.HiveContext;

/**
 * @program: smp-spark-project
 * @description: SparkUtils
 * @author: Mr.zhang
 * @create: 2019-10-13 20:50
 **/
public class SparkUtils {

    /**
     * 获取SQLContext
     * 如果是在本地测试环境的话，那么就生成SQLContext对象
     * 如果是在生产环境运行的话，那么就生成HiveContext对象
     *
     * @param sc SparkContext
     * @return SQLContext
     */
    public static SQLContext getSQLContext(SparkContext sc) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if (local) {
            return new SQLContext(sc);
        } else {
            return new HiveContext(sc);
        }
    }

    /**
     * 生成模拟数据
     * 如果spark.local配置设置为true，则生成模拟数据；否则不生成
     * @param sc
     * @param sqlContext
     */
    public static void mockData(JavaSparkContext sc, SQLContext sqlContext) {
        boolean local = ConfigurationManager.getBoolean(Constants.SPARK_LOCAL);
        if (local) {
            MockData.mock(sc, sqlContext);
        }
    }

    public static JavaRDD<Row> getActionRDDByDateRange(SQLContext sqlContext, JSONObject taskParam) {
        String startDate = ParamUtils.getParam(taskParam, Constants.PARAM_START_DATE);
        String endDate = ParamUtils.getParam(taskParam, Constants.PARAM_END_DATE);

        String sql = "select * from user_visit_action"
                + " where date>='" + startDate + "' "
                + "and date<='" + endDate + "'";
        DataFrame actionDF = sqlContext.sql(sql);
        // 此处根据数据量进行重分区操作
        // 比如需要1000个task去执行
        // 就是
        // return actionDF.javaRDD().repartition(1000);
        return actionDF.javaRDD();
    }
}

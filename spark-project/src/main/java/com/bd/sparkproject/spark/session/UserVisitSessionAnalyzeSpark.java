package com.bd.sparkproject.spark.session;

import com.alibaba.fastjson.JSONObject;
import com.bd.sparkproject.constant.Constants;
import com.bd.sparkproject.dao.ISessionAggrStatDAO;
import com.bd.sparkproject.dao.ITaskDAO;
import com.bd.sparkproject.dao.factory.DAOFactory;
import com.bd.sparkproject.domain.SessionAggrStat;
import com.bd.sparkproject.domain.Task;
import com.bd.sparkproject.util.*;
import org.apache.spark.Accumulator;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.api.java.function.PairFunction;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SQLContext;
import org.apache.spark.storage.StorageLevel;
import scala.Tuple2;

import java.util.Date;
import java.util.Iterator;

import static com.bd.sparkproject.util.SparkUtils.getSQLContext;

/**
 * @program: smp-spark-project
 * @description: 用户访问session分析Spark作业
 * @author: Mr.zhang
 * @create: 2019-10-13 20:00
 **/
public class UserVisitSessionAnalyzeSpark {
    public static void main(String[] args) {
        // 构建上下文
        SparkConf conf = new SparkConf()
                .setAppName(Constants.SPARK_APP_NAME_SESSION)
                .setMaster("local");

        JavaSparkContext sc = new JavaSparkContext(conf);
        SQLContext sqlContext = getSQLContext(sc.sc());

        // 生成模拟测试数据
        SparkUtils.mockData(sc, sqlContext);

        // 查询指定的任务id
        Long taskId = ParamUtils.getTaskIdFromArgs(args, Constants.SPARK_LOCAL_TASKID_SESSION);
        LogUtils.LogPrint("taskid", taskId);

        // 创建task DAO组件
        ITaskDAO taskDAO = DAOFactory.getTaskDAO();
        //通过taskId获取任务实例
        Task task = taskDAO.findById(taskId);
        LogUtils.LogPrint("task", task);
        if (task == null) {
            System.out.println(new Date() + ": can not find this task with id["
                    + taskId + "]");
            return;
        }
        // 通过task获取任务参数
        JSONObject taskParam = JSONObject.parseObject(task.getTaskParam());
        LogUtils.LogPrint("taskParam", taskParam);

        /**
         * 一.进行session粒度聚合步骤：
         * 1.从user_visit_action表中，查询出来指定日期范围内的行为数据->actionRDD
         * 2.actionRDD -> sessionId为key的sessionid2actionRDD
         *      sessionid2actionRDD为公用PairRDD
         *      （1）与通过筛选的sessionid进行join，获取通过筛选的session的明细数据
         *       (2)将这个RDD，直接传入aggregateBySession方法，进行session聚合统计
         * 3.所以对sessionid2ActionRDD进行持久化操作
         * 4.对sessionid2ActionRDD做聚合操作
         */
        // 1.actionRDD
        JavaRDD<Row> actionRDD = SparkUtils.getActionRDDByDateRange(sqlContext, taskParam);
        LogUtils.LogPrint("actionRDD count", actionRDD.count());
        LogUtils.LogPrint("actionRDD", actionRDD.take(1));
        // 2.actionRDD -> sessionid2actionRDD 用来聚合（聚合操作中加入访问时长步长的统计）
        JavaPairRDD<String, Row> sessionid2actionRDD = getSessionid2ActionRDD(actionRDD);
        LogUtils.LogPrint("sessionid2actionRDD", sessionid2actionRDD.take(1));
        // 3.持久化sessionid2ActionRDD
        // 总共12级选择
        // 如果是persist(StorageLevel.MEMORY_ONLY())，纯内存，无序列化，那么就可以用cache()方法来替代
        // StorageLevel.MEMORY_ONLY_SER()，第二选择
        // StorageLevel.MEMORY_AND_DISK()，第三选择
        // StorageLevel.MEMORY_AND_DISK_SER()，第四选择
        // StorageLevel.DISK_ONLY()，第五选择
        // 如果内存充足，要使用双副本高可靠机制
        // 选择后缀带_2的策略
        // StorageLevel.MEMORY_ONLY_2()
        sessionid2actionRDD = sessionid2actionRDD.cache();
        // 4.聚合操作
        // <sessionid, partAggrInfo+userInfo)>
        JavaPairRDD<String, String> sessionid2FullAggrInfoRDD = aggregateBySession(
                sc, sqlContext, sessionid2actionRDD);
        /**
         * 二.按使用者给定的筛选条件对session粒度聚合数据进行过滤
         * 步骤：
         *      1.编写统一的accumulater算子
         *      用来统计访问时长，访问步长各个范围的数据
         *      2.在过滤器中过滤出了符合条件的session
         *      顺便在这个过程也计算出了符合筛选条件的session的访问时长和访问步长
         */
        // 1.编写统一的accumulater算子
        Accumulator<String> sessionAggrStatAccumulator = sc.accumulator(
                "",
                new SessionAggrStatAccumulator());
        // 2.在过滤器中过滤出了符合条件的session
        JavaPairRDD<String, String> sessionid2FullAggrInfoRDDAfterFilter = filterSessionAndAggrStat(
                sessionid2FullAggrInfoRDD,
                taskParam,
                sessionAggrStatAccumulator);

        sessionid2FullAggrInfoRDDAfterFilter = sessionid2FullAggrInfoRDDAfterFilter.persist(StorageLevel.MEMORY_ONLY());
        /**
         * !!!!一定要有action操作，并且必须啊在calculateAndPersistAggrStat之前!!!!
         * 缺这一步在调试过程中数据全tm为空!!
         */
        sessionid2FullAggrInfoRDDAfterFilter.count();
        LogUtils.LogPrint("sessionAggrStatAccumulator.value", sessionAggrStatAccumulator.value());
        /**
         * 将计算结果持久化存储
         */
        calculateAndPersistAggrStat(sessionAggrStatAccumulator.value(), task.getTaskid());

        sc.close();
    }

    private static void calculateAndPersistAggrStat(String value, long taskid) {
        // 从Accumulator统计串中获取值
        long session_count = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.SESSION_COUNT));

        long visit_length_1s_3s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_1s_3s));
        long visit_length_4s_6s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_4s_6s));
        long visit_length_7s_9s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_7s_9s));
        long visit_length_10s_30s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_10s_30s));
        long visit_length_30s_60s = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_30s_60s));
        long visit_length_1m_3m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_1m_3m));
        long visit_length_3m_10m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_3m_10m));
        long visit_length_10m_30m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_10m_30m));
        long visit_length_30m = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.TIME_PERIOD_30m));

        long step_length_1_3 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_1_3));
        long step_length_4_6 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_4_6));
        long step_length_7_9 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_7_9));
        long step_length_10_30 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_10_30));
        long step_length_30_60 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_30_60));
        long step_length_60 = Long.valueOf(StringUtils.getFieldFromConcatString(
                value, "\\|", Constants.STEP_PERIOD_60));

        // 计算各个访问时长和访问步长的范围
        double visit_length_1s_3s_ratio = NumberUtils.formatDouble(
                (double) visit_length_1s_3s / (double) session_count, 2);
        double visit_length_4s_6s_ratio = NumberUtils.formatDouble(
                (double) visit_length_4s_6s / (double) session_count, 2);
        double visit_length_7s_9s_ratio = NumberUtils.formatDouble(
                (double) visit_length_7s_9s / (double) session_count, 2);
        double visit_length_10s_30s_ratio = NumberUtils.formatDouble(
                (double) visit_length_10s_30s / (double) session_count, 2);
        double visit_length_30s_60s_ratio = NumberUtils.formatDouble(
                (double) visit_length_30s_60s / (double) session_count, 2);
        double visit_length_1m_3m_ratio = NumberUtils.formatDouble(
                (double) visit_length_1m_3m / (double) session_count, 2);
        double visit_length_3m_10m_ratio = NumberUtils.formatDouble(
                (double) visit_length_3m_10m / (double) session_count, 2);
        double visit_length_10m_30m_ratio = NumberUtils.formatDouble(
                (double) visit_length_10m_30m / (double) session_count, 2);
        double visit_length_30m_ratio = NumberUtils.formatDouble(
                (double) visit_length_30m / (double) session_count, 2);

        double step_length_1_3_ratio = NumberUtils.formatDouble(
                (double) step_length_1_3 / (double) session_count, 2);
        double step_length_4_6_ratio = NumberUtils.formatDouble(
                (double) step_length_4_6 / (double) session_count, 2);
        double step_length_7_9_ratio = NumberUtils.formatDouble(
                (double) step_length_7_9 / (double) session_count, 2);
        double step_length_10_30_ratio = NumberUtils.formatDouble(
                (double) step_length_10_30 / (double) session_count, 2);
        double step_length_30_60_ratio = NumberUtils.formatDouble(
                (double) step_length_30_60 / (double) session_count, 2);
        double step_length_60_ratio = NumberUtils.formatDouble(
                (double) step_length_60 / (double) session_count, 2);

        SessionAggrStat sessionAggrStat = new SessionAggrStat();
        sessionAggrStat.setTaskid(taskid);
        sessionAggrStat.setSession_count(session_count);
        sessionAggrStat.setVisit_length_1s_3s_ratio(visit_length_1s_3s_ratio);
        sessionAggrStat.setVisit_length_4s_6s_ratio(visit_length_4s_6s_ratio);
        sessionAggrStat.setVisit_length_7s_9s_ratio(visit_length_7s_9s_ratio);
        sessionAggrStat.setVisit_length_10s_30s_ratio(visit_length_10s_30s_ratio);
        sessionAggrStat.setVisit_length_30s_60s_ratio(visit_length_30s_60s_ratio);
        sessionAggrStat.setVisit_length_1m_3m_ratio(visit_length_1m_3m_ratio);
        sessionAggrStat.setVisit_length_3m_10m_ratio(visit_length_3m_10m_ratio);
        sessionAggrStat.setVisit_length_10m_30m_ratio(visit_length_10m_30m_ratio);
        sessionAggrStat.setVisit_length_30m_ratio(visit_length_30m_ratio);
        sessionAggrStat.setStep_length_1_3_ratio(step_length_1_3_ratio);
        sessionAggrStat.setStep_length_4_6_ratio(step_length_4_6_ratio);
        sessionAggrStat.setStep_length_7_9_ratio(step_length_7_9_ratio);
        sessionAggrStat.setStep_length_10_30_ratio(step_length_10_30_ratio);
        sessionAggrStat.setStep_length_30_60_ratio(step_length_30_60_ratio);
        sessionAggrStat.setStep_length_60_ratio(step_length_60_ratio);

        // 调用对应的DAO层插入统计结果
        ISessionAggrStatDAO sessionAggrStatDAO = DAOFactory.getSessionAggrStatDAO();
        sessionAggrStatDAO.insert(sessionAggrStat);
    }

    /**
     * 求sessionid2actionRDD
     *
     * @param actionRDD
     * @return
     */
    private static JavaPairRDD<String, Row> getSessionid2ActionRDD(JavaRDD<Row> actionRDD) {
        return actionRDD.mapToPair(new PairFunction<Row, String, Row>() {
            @Override
            public Tuple2<String, Row> call(Row row) throws Exception {
                return new Tuple2<String, Row>(row.getString(2), row);
            }
        });
        // 有分区时使用
        // return actionRDD.mapPartitionsToPair(new PairFlatMapFunction<Iterator<Row>, String, Row>() {
        //     @Override
        //     public Iterable<Tuple2<String, Row>> call(Iterator<Row> rowIterator) throws Exception {
        //         List<Tuple2<String, Row>> list = new ArrayList<Tuple2<String, Row>>();
        //         while (rowIterator.hasNext()) {
        //             Row row = rowIterator.next();
        //             list.add(new Tuple2<String, Row>(row.getString(2), row))
        //         }
        //         return list;
        //     }
        // });
    }

    /**
     * 求sessionid2FullAggrInfoRDD
     *
     * @param sc
     * @param sqlContext
     * @param sessionid2actionRDD
     * @return
     */
    private static JavaPairRDD<String, String> aggregateBySession(JavaSparkContext sc, SQLContext sqlContext, JavaPairRDD<String, Row> sessionid2actionRDD) {
        /**
         * 1.将用户行为按照session粒度分组
         *   groupByKey sessionid
         * 2.聚合&获取userid2PartAggrInfoRDD：将搜索词、点击品类、用户信息、时长步长聚合(此处用到小技巧方便后面直接用userid和用户信息表聚合)
         *   sessionid2actionRDD -> userid2PartAggractionRDD（聚合&包含时长步长）
         * 3.通过userInfo表得到userInfoRDD -> userid2InfoRDD
         * 4.userid2InfoRDD与userid2PartAggractionRDD进行join得到userid2FullInfoRDD
         * 5.将join后的数据拼接成<sessionid,fullAggrInfo>
         */
        // 1
        JavaPairRDD<String, Iterable<Row>> session2actionRDDAfterGroup = sessionid2actionRDD.groupByKey();
        // 2
        JavaPairRDD<Long, String> userid2PartAggrInfoRDD = session2actionRDDAfterGroup.mapToPair(
                new PairFunction<Tuple2<String, Iterable<Row>>, Long, String>() {
                    @Override
                    public Tuple2<Long, String> call(Tuple2<String, Iterable<Row>> tuple) throws Exception {
                        String sessionid = tuple._1;
                        Iterator<Row> iterator = tuple._2.iterator();

                        StringBuffer searchKeywordsBuffer = new StringBuffer("");
                        StringBuffer clickCategoryIdsBuffer = new StringBuffer("");

                        //userid
                        Long userid = null;
                        // session起始时间定义
                        Date startTime = null;
                        Date endTime = null;
                        // 访问步长
                        int stepLength = 0;

                        // 遍历session的所有访问行为
                        while (iterator.hasNext()) {
                            // 提取每个访问行为的searchKeyword、clickCategoryId
                            Row row = iterator.next();

                            if (userid == null) {
                                userid = row.getLong(1);
                            }
                            String searchKeyword = row.getString(5);
                            Long clickCategoryId = row.getLong(6);

                            // （1）不为null（2）之前的str不含该值
                            // 判空
                            if (StringUtils.isNotEmpty(searchKeyword)) {
                                if (!searchKeywordsBuffer.toString().contains(searchKeyword)) {
                                    searchKeywordsBuffer.append(searchKeyword + ",");
                                }
                            }
                            if (clickCategoryId != null) {
                                if (!clickCategoryIdsBuffer.toString().contains(
                                        String.valueOf(clickCategoryId))) {
                                    clickCategoryIdsBuffer.append(clickCategoryId + ",");
                                }
                            }
                            // 对比得到最终的session的真实起始时间
                            Date actinTime = DateUtils.parseTime(row.getString(4));
                            if (startTime == null) {
                                startTime = actinTime;
                            }
                            if (endTime == null) {
                                endTime = actinTime;
                            }
                            if (actinTime.before(startTime)) {
                                startTime = actinTime;
                            }
                            if (actinTime.after(endTime)) {
                                endTime = actinTime;
                            }
                            // 计算访问步长
                            stepLength++;
                        }
                        String searchKeywords = StringUtils.trimComma(searchKeywordsBuffer.toString());
                        String clickCategoryIds = StringUtils.trimComma(clickCategoryIdsBuffer.toString());

                        // 计算访问时长(单位:s)
                        Long visitLength = (endTime.getTime() - startTime.getTime()) / 1000;
                        // 将<sessionid, actionInfo> -> <userid, sessionid+actionInfo>，方便与userInfo做join
                        String partAggrInfo = Constants.FIELD_SESSION_ID + "=" + sessionid + "|"
                                + Constants.FIELD_SEARCH_KEYWORDS + "=" + searchKeywords + "|"
                                + Constants.FIELD_CLICK_CATEGORY_IDS + "=" + clickCategoryIds + "|"
                                + Constants.FIELD_VISIT_LENGTH + "=" + visitLength + "|"
                                + Constants.FIELD_STEP_LENGTH + "=" + stepLength + "|"
                                + Constants.FIELD_START_TIME + "=" + DateUtils.formatTime(startTime);

                        return new Tuple2<Long, String>(userid, partAggrInfo);
                    }
                });
        // 3.得到userInfoRDD
        String sql = "select * from user_info";
        JavaRDD<Row> userInfoRDD = sqlContext.sql(sql).javaRDD();
        // 4.userInfoRDD -> userid2InfoRDD
        JavaPairRDD<Long, Row> userid2InfoRDD = userInfoRDD.mapToPair(new PairFunction<Row, Long, Row>() {
            @Override
            public Tuple2<Long, Row> call(Row row) throws Exception {
                return new Tuple2<Long, Row>(row.getLong(0), row);
            }
        });
        // 5.join
        JavaPairRDD<Long, Tuple2<String, Row>> userid2FullInfoRDD = userid2PartAggrInfoRDD.join(userid2InfoRDD);
        // 6.<userid, (partAggrInfo,userInfo)>
        //      -> sessionid2fullAggrInfoRDD
        JavaPairRDD<String, String> sessionid2FullAggrInfoRDD = userid2FullInfoRDD.mapToPair(new PairFunction<Tuple2<Long, Tuple2<String, Row>>, String, String>() {
            @Override
            public Tuple2<String, String> call(Tuple2<Long, Tuple2<String, Row>> tuple) throws Exception {
                String partAggrInfo = tuple._2._1;
                Row userInfoRow = tuple._2._2;

                String sessionid = StringUtils.getFieldFromConcatString(
                        partAggrInfo, "\\|", Constants.FIELD_SESSION_ID);
                int age = userInfoRow.getInt(3);
                String professional = userInfoRow.getString(4);
                String city = userInfoRow.getString(5);
                String sex = userInfoRow.getString(6);

                String fullAggrInfo = partAggrInfo + "|"
                        + Constants.FIELD_AGE + "=" + age + "|"
                        + Constants.FIELD_PROFESSIONAL + "=" + professional + "|"
                        + Constants.FIELD_CITY + "=" + city + "|"
                        + Constants.FIELD_SEX + "=" + sex;
                return new Tuple2<String, String>(sessionid, fullAggrInfo);
            }
        });
        return sessionid2FullAggrInfoRDD;
    }

    private static JavaPairRDD<String, String> filterSessionAndAggrStat(
            JavaPairRDD<String, String> sessionid2FullAggrInfoRDD,
            final JSONObject taskParam,
            final Accumulator<String> sessionAggrStatAccumulator) {
        /**
         * 1. 筛选参数拼接成串，方面后续使用ValidUtils类&性能优化
         * 2. 根据筛选参数进行过滤
         *      (1）获取聚合数据
         *     （2）层层筛选
         *     （3）调用accumulateer对各要求访问时长范围累加，各要求步长范围累加
         */
        // 1.
        String startAge = ParamUtils.getParam(taskParam, Constants.PARAM_START_AGE);
        String endAge = ParamUtils.getParam(taskParam, Constants.PARAM_END_AGE);
        String professionals = ParamUtils.getParam(taskParam, Constants.PARAM_PROFESSIONALS);
        String cities = ParamUtils.getParam(taskParam, Constants.PARAM_CITIES);
        String sex = ParamUtils.getParam(taskParam, Constants.PARAM_SEX);
        String keywords = ParamUtils.getParam(taskParam, Constants.PARAM_KEYWORDS);
        String categoryIds = ParamUtils.getParam(taskParam, Constants.PARAM_CATEGORY_IDS);

        String _paramter = (startAge != null ? Constants.PARAM_START_AGE + "=" + startAge + "|" : "")
                + (endAge != null ? Constants.PARAM_END_AGE + "=" + endAge + "|" : "")
                + (professionals != null ? Constants.PARAM_PROFESSIONALS + "=" + professionals + "|" : "")
                + (cities != null ? Constants.PARAM_CITIES + "=" + cities + "|" : "")
                + (sex != null ? Constants.PARAM_SEX + "=" + sex + "|" : "")
                + (keywords != null ? Constants.PARAM_KEYWORDS + "=" + keywords + "|" : "")
                + (categoryIds != null ? Constants.PARAM_CATEGORY_IDS + "=" + categoryIds : "");

        if (_paramter.endsWith("\\|")) {
            _paramter = _paramter.substring(0, _paramter.length() - 1);
        }
        final String parameter = _paramter;
        // 2.
        JavaPairRDD<String, String> filter = sessionid2FullAggrInfoRDD.filter(
                new Function<Tuple2<String, String>, Boolean>() {
            @Override
            public Boolean call(Tuple2<String, String> tuple) throws Exception {
                // 获取聚合数据
                String aggrInfo = tuple._2;
                // 依次各条件过滤
                // 年龄范围过滤
                if (!ValidUtils.between(aggrInfo, Constants.FIELD_AGE,
                        parameter, Constants.PARAM_START_AGE, Constants.PARAM_END_AGE)) {
                    return false;
                }
                // 职业范围过滤（professionals）
                if (!ValidUtils.in(aggrInfo, Constants.FIELD_PROFESSIONAL,
                        parameter, Constants.PARAM_PROFESSIONALS)) {
                    return false;
                }

                // 按照城市范围进行过滤（cities）
                if (!ValidUtils.in(aggrInfo, Constants.FIELD_CITY,
                        parameter, Constants.PARAM_CITIES)) {
                    return false;
                }

                // 按照性别进行过滤
                if (!ValidUtils.equal(aggrInfo, Constants.FIELD_SEX,
                        parameter, Constants.PARAM_SEX)) {
                    return false;
                }
                // 搜索关键词任何一个匹配
                if (!ValidUtils.in(aggrInfo, Constants.FIELD_SEARCH_KEYWORDS,
                        parameter, Constants.PARAM_KEYWORDS)) {
                    return false;
                }

                // 按照点击品类id进行过滤
                if (!ValidUtils.in(aggrInfo, Constants.FIELD_CLICK_CATEGORY_IDS,
                        parameter, Constants.PARAM_CATEGORY_IDS)) {
                    return false;
                }

                // 走到这一步，就是要保留的session
                sessionAggrStatAccumulator.add(Constants.SESSION_COUNT);
                // 计算访问时长&步长的范围，然后进行累加
                long visitLength = Long.valueOf(StringUtils.getFieldFromConcatString(
                        aggrInfo, "\\|", Constants.FIELD_VISIT_LENGTH));
                long stepLength = Long.valueOf(StringUtils.getFieldFromConcatString(
                        aggrInfo, "\\|", Constants.FIELD_STEP_LENGTH
                ));

                calculateVisitLength(visitLength);
                calculateStepLength(stepLength);

                return true;
            }

            /**
             * 计算访问时长范围
             * @param visitLength
             */
            private void calculateVisitLength(long visitLength) {
                if (visitLength >= 1 && visitLength <= 3) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_1s_3s);
                } else if (visitLength >= 4 && visitLength <= 6) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_4s_6s);
                } else if (visitLength >= 7 && visitLength <= 9) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_7s_9s);
                } else if (visitLength >= 10 && visitLength <= 30) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_10s_30s);
                } else if (visitLength > 30 && visitLength <= 60) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_30s_60s);
                } else if (visitLength > 60 && visitLength <= 180) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_1m_3m);
                } else if (visitLength > 180 && visitLength <= 600) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_3m_10m);
                } else if (visitLength > 600 && visitLength <= 1800) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_10m_30m);
                } else if (visitLength > 1800) {
                    sessionAggrStatAccumulator.add(Constants.TIME_PERIOD_30m);
                }
                // LogUtils.LogPrint("===visit list===", sessionAggrStatAccumulator.value());
            }

            /**
             * 计算访问步长范围
             * @param stepLength
             */
            private void calculateStepLength(long stepLength) {
                LogUtils.LogPrint("visitLength", stepLength);
                if (stepLength >= 1 && stepLength <= 3) {
                    sessionAggrStatAccumulator.add(Constants.STEP_PERIOD_1_3);
                } else if (stepLength >= 4 && stepLength <= 6) {
                    sessionAggrStatAccumulator.add(Constants.STEP_PERIOD_4_6);
                } else if (stepLength >= 7 && stepLength <= 9) {
                    sessionAggrStatAccumulator.add(Constants.STEP_PERIOD_7_9);
                } else if (stepLength >= 10 && stepLength <= 30) {
                    sessionAggrStatAccumulator.add(Constants.STEP_PERIOD_10_30);
                } else if (stepLength > 30 && stepLength <= 60) {
                    sessionAggrStatAccumulator.add(Constants.STEP_PERIOD_30_60);
                } else if (stepLength > 60) {
                    sessionAggrStatAccumulator.add(Constants.STEP_PERIOD_60);
                }
                // LogUtils.LogPrint("===step list===", sessionAggrStatAccumulator.value());
            }
        });
        return filter;
    }
}

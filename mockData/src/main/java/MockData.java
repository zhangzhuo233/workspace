import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.RowFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

/**
 * @program: mockData
 * @description: 为有猫漫画app造数据
 * @author: Mr.zhang
 * @create: 2019-11-26 10:21
 **/
public class MockData {
    public static void mockData(JavaSparkContext sc) {
        List<Row> rows = new ArrayList<Row>();
        //ac
        String[] acs = new String[]{"havecatstat", "gamecenterstat", "gamecenterstat", "h5activitystat", "tinygamestat"};
        String[] events = new String[]{"EVENT_PV", "EVENT_VIEW", "EVENT_CLICK", "EVENT_DURATION", "EVENT_COLLECT",
                "EVENT_COMIC_READ", "EVENT_COMIC_PAY", "EVENT_COMIC_CHARGE", "EVENT_SHARE"};
        String[] pages = new String[]{"漫画阅读器页", "主页", "Today页", "漫画合集页", "单本漫画预览页", "漫画详情页", "viewType1_0_0",
                "发现页", "发现页更多二级页", "漫画分类页", "排行榜页", "每日更新页", "心跳专区页", "具体排行榜页", "排行榜二级页",
                "我的订阅", "浏览历史",
                "我的页", "个人主页", "个人信息编辑页", "漫画充值页面",
                "社区总页", "社区推荐页", "社区详情页", "图文详情页", "社区关注页", "社区最新页", "个人专辑页", "投票详情页", "web页面", "图文沉浸页", "社区帖子发布总入口", "图文发布页", "图片选择页"};
        String[] poss = new String[]{"viewType5_0_0", "viewType3_0_x", "头部_1_0", "图文帖_0_0", "投票帖_0_0", "链接帖_0_0", "搜索热词_0_6"};
        Integer[] types = new Integer[]{0, 1};
        Random random = new Random();
        //imeisha1...
        for (int i = 0; i < 100000; i++) {
            String ac = acs[random.nextInt(5)];
            String imeisha1 = UUID.randomUUID().toString().replace("-", "") + "sha1";
            String imeisha2 = UUID.randomUUID().toString().replace("-", "") + "sha2";
            String imeimd5 = UUID.randomUUID().toString().replace("-", "") + "md5";
            String macmd5 = UUID.randomUUID().toString().replace("-", "") + "macmd5";
            String fuid = String.valueOf(random.nextInt(100000000));
            String dt = "[" + String.valueOf(random.nextInt(30)) + "/11/2019:12:18:36]";
            String ts = DateUtil.dateTimeToTimestamp(dt);
            String event = events[random.nextInt(9)];
            String ua = "Xiaomi|Mi-4c|NRD90M|24|libra";
            String cid = "meng_100_1_android";
            String cientversion = "1190";
            String page = "{\"name\":\"" + pages[random.nextInt(34)] + "\",\"id\":\"" + fuid + "\"}";
            String pageref = "com.miui.home";
            String frompage1 = "[" + page + "]";
            String frompage2 = "[" + page + "," + page + "]";
            String frompage3 = "[" + page + "," + page + page + "]";
            String[] frompages = new String[]{frompage1, frompage2, frompage3};
            String frompage = frompages[random.nextInt(3)];
            String pos = poss[random.nextInt(7)];
            String itemdata = "{\"pos\":\"" + pos + "\",\"contentId\":\"" + fuid + "\",\"isAd\":0,\"isAutoRec\":0,\"rid\":\"" + fuid + "\",\"contentType\":\"comic\"}";
            String poschain = "[{\"pos\":\"" + pos + "\",\"isAd\":0,\"isAutoRec\":0,\"rid\":\"-1\"},{\"pos\":\"other_0_0\",\"isAd\":0,\"isAutoRec\":0,\"rid\":\"-1\"}]";
            String downloadInfo = "";
            String fromapp = "havecat";
            String eventParam = "";
            Integer time = random.nextInt(100);
            Integer type = types[random.nextInt(2)];
            String duration = "{\"time\":" + time + ",\"type\":" + type + "}";
            String extra = "";
            String serverTime = "";
            String sid = "";
            String lang = "zh";
            String region = "CN";
            String devicetype = "0";
            String unionId = "unionId";
            String os = "MIUI|9";
            String trackid = "trackid";
            String accounttype = "-1";
            String sessionid = UUID.randomUUID().toString().replace("-", "");
            String localtime = ts;
            String carrier = "cmcc";
            String timezone = "null";
            String android = "5.1.1";
            String network = "MOBILE_4G";
            String dindex = "17";
            String searchInfo = "";
            String isAutoRec = "";
            String clientip = "117.136.94.249";
            String serverip = "10.162.31.46";
            String video = "";
            String oaid = "";

            // mock OdsGamecenterV3App
            Row row = RowFactory.create(ac + "\t" + imeisha1 + "\t" + imeisha2 + "\t" + imeimd5 + "\t" + macmd5 + "\t" + fuid + "\t" + dt + "\t" + ts + "\t" + event + "\t" + ua + "\t" + cid + "\t" + cientversion + "\t" +
                    page + "\t" + pageref + "\t" + frompage + "\t" + itemdata + "\t" + poschain + "\t" + downloadInfo + "\t" + fromapp + "\t" + eventParam + "\t" + duration + "\t" + extra + "\t" + serverTime + "\t" +
                    sid + "\t" + lang + "\t" + region + "\t" + devicetype + "\t" + unionId + "\t" + os + "\t" + trackid + "\t" + accounttype + "\t" + sessionid + "\t" + localtime + "\t" + carrier + "\t" + timezone + "\t" +
                    android + "\t" + network + "\t" + dindex + "\t" + searchInfo + "\t" + isAutoRec + "\t" + clientip + "\t" + serverip + "\t" + video + "\t" + oaid);
            rows.add(row);
        }
        JavaRDD<Row> parallelize = sc.parallelize(rows);
        parallelize.saveAsTextFile("output/test.data");
        System.out.println("已造数据量:" + parallelize.count());
        System.out.println("数据举例:" + parallelize.take(1));
    }

    public static void main(String[] args) {
        SparkConf sparkConf = new SparkConf().setAppName("havecat mockData").setMaster("local");
        JavaSparkContext sc = new JavaSparkContext(sparkConf);

        mockData(sc);
    }
}

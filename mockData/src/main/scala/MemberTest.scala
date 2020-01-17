import com.alibaba.fastjson.JSON
import org.apache.commons.lang.StringUtils
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
 * author: zhangzhuo7
 * createDate: 2019/12/26
 * purpose: 
 */
object MemberTest {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
        val session: SparkSession = SparkSession.builder()
            .master("local[2]")
            .appName("test")
            .config(conf)
            .getOrCreate()
        val sc = session.sparkContext
        //val list = List(("dog", "20191111"), ("dog", "20191112"), ("dog", "20191113"), ("cat", "20191111"), ("cat", "20191112"), ("cat", "20191113"))
        //val tmp = sc.parallelize(list).
        //    map(x => (x._1, x._2.toLong)).reduceByKey(math.max(_, _))
        //tmp.foreach(x => println(x._1, "::", x._2))

        val list = List(("dog", "1", "20191111"), ("dog", "1","20191111"), ("dog", "1", "20191113"), ("cat", "1", "20191111"), ("cat", "1", "20191112"), ("cat", "1", "20191113"))
        val tmp = sc.parallelize(list).map(x => ((x._1, x._2), x._3))
        tmp.foreach(x => println(x._1, x._2))
        //val str1 = "[{\"name\":\"漫画阅读器页\",\"id\":\"90620333\"},{\"name\":\"发现页_男\"},{\"name\":\"漫画详情页\",\"id\":\"90611830\"},{\"name\":\"漫画阅读器页\",\"id\":\"90611830\"},{\"name\":\"漫画阅读器页\",\"id\":\"90611830\"},{\"name\":\"web页面\",\"id\":\"https://m.youmao.g.mi.com/youmao.html#/task\"},{\"name\":\"web页面\",\"id\":\"https://m.youmao.g.mi.com/youmao.html#/task\"}]"
        //val str2 = "[{\"name\":\"漫画阅读器页\",\"id\":\"90620333\"},{\"name\":\"发现页_男\"}]"
        //val str3 = " {\"time\":1362,\"type\":1} "
        //val tmp1 = JSON.parseArray(str1).getJSONObject(JSON.parseArray(str1).size() - 1).getOrDefault("name", "").toString
        //val tmp2 = JSON.parseArray(str2).getJSONObject(JSON.parseArray(str2).size() - 1).getOrDefault("name", "").toString
        //val tmp3 = JSON.parseObject(str3).getString("type")
        //val bres = StringUtils.equals("1", JSON.parseObject(str3).getString("type"))
        //val time = JSON.parseObject(str3).getString("time").toLong
        //println("res ::", tmp1, "::", tmp2)
        //println("tmp3::", tmp3, "bres::", bres, "time::", time)

    }
}

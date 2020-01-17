/**
 * author: zhangzhuo7
 * createDate: 2019/11/26
 * purpose: 
 */

import java.text.SimpleDateFormat
import java.util.{Calendar, Date, Locale}

import org.joda.time.format.DateTimeFormat

/**
 * Created by 赵利民 on 2018/3/23.
 */
object DateUtil {

    val dateTimeFormat = DateTimeFormat.forPattern("dd/MMM/yyyy:HH:mm:SS +0800").withLocale(Locale.US)

    def getDateTime(usDateTime: String): String = {
        var dt = ""
        try {
            val reg = """^\[(.*)\]$""".r
            dt = usDateTime match {
                case reg(x) => dateTimeFormat.parseDateTime(x).toString("yyyy-MM-dd HH:mm:ss")
                case _ => ""
            }
        } catch {
            case e: Exception => {
                dt = null
            }
        }
        dt
    }

    // 获得前一天日期
    def getYesterday(today: String): String = {
        val sdf = new SimpleDateFormat("yyyyMMdd")
        val today1 = sdf.parse(today) //字符串转为 Date 类型
        val calendar = Calendar.getInstance()
        calendar.setTime(today1) //设置为指定日期
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - 1)
        val res = sdf.format(calendar.getTime())
        res
    }


    // 获得前 n 天的日期
    def getNDaysAgo(today: String, n: Int): String = {
        val sdf = new SimpleDateFormat("yyyyMMdd")
        val today1 = sdf.parse(today) //字符串转为 Date 类型
        val calendar = Calendar.getInstance()
        calendar.setTime(today1) //设置为指定日期
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - n)
        val res = sdf.format(calendar.getTime())
        res
    }


    // 获得后一天日期
    def getTomorrow(today: String): String = {
        val sdf = new SimpleDateFormat("yyyyMMdd")
        val today1 = sdf.parse(today) //字符串转为 Date 类型
        val calendar = Calendar.getInstance()
        calendar.setTime(today1) //设置为指定日期
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) + 1)
        val res = sdf.format(calendar.getTime())
        res
    }

    //获得去年今天的日期
    def getTodayAtLastYear(today: String): String = {
        // 考虑闰年
        var res = ""
        var year = today.substring(0, 4).toInt
        var month = today.substring(4, 6).toInt
        val day = today.substring(6, 8).toInt


        if (year % 4 == 0 && today.substring(4, 8) == "0229") { //今年是闰年
            year = year - 1
            res = year.toString + "0228"
        } else if ((year - 1) % 4 == 0 && today.substring(4, 8) == "0228") { //去年是闰年
            year = year - 1
            res = year.toString + "0229"
        } else { //正常情况
            year = year - 1
            res = year.toString + today.substring(4, 8)
        }
        res
    }


    //将13位时间戳转化成日期  1490803200000->20170330
    def timestampToDate(timeStamp1: String) = {
        var sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        var date: String = sdf.format(new Date((timeStamp1.toLong)))
        date
    }

    //将10位时间戳转化为日期
    def getUnixStringDate(ts: Long): String = {
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        val str: String = sdf.format(ts * 1000)
        str
    }

    // 将13位时间戳转化成日期  1497406627000->20170330 12:21:34
    def timestampToDateWithSecond(timeStamp1: String) = {
        var sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss")
        var date: String = sdf.format(new Date((timeStamp1.toLong)))
        date
    }

    // 将13位时间戳转化成日期  1497406627000->2017-03-30 12:21:34
    def timestampToDateWithSecond2(timeStamp1: String) = {
        var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var date: String = sdf.format(new Date((timeStamp1.toLong)))
        date
    }

    /** 将时间转成毫秒格式 */
    def dateToStamp(date: String): String = {
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        val c = Calendar.getInstance
        c.setTime(sdf.parse(date))
        c.getTimeInMillis.toString
    }

    /** 将时间转成毫秒格式 */
    def dateToStampLong(date: String): Long = {
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        val c = Calendar.getInstance
        c.setTime(sdf.parse(date))
        c.getTimeInMillis
    }

    // 日期转化为13位时间戳.dt:20181115
    def dateToTimestamp(dt1: String): String = {
        val df: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd HH:mm:ss")
        val dt: Date = df.parse(dt1 + " 00:00:00")
        val ts = dt.getTime().toString
        ts
    }
    // 日期时间转换为13位时间戳[18/11/2019:12:18:36]
    def dateTimeToTimestamp(dt1: String) = {
        val df: SimpleDateFormat = new SimpleDateFormat("[dd/MM/yyyy:HH:mm:ss]")
        val dt = df.parse(dt1)
        val ts = dt.getTime.toString
        ts
    }

    // 带时分秒的时间转化为13位时间戳.dt:2018-11-15 22:10:23
    def dateToTimestamp2(dt1: String) = {
        val df: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val dt: Date = df.parse(dt1)
        val ts = dt.getTime()
        ts
    }

    //得到当前日期
    def getNowDate(): String = {
        var now: Date = new Date()
        var sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        var nowDate = sdf.format(now)
        nowDate
    }

    //得到当前日期(yyyy-MM-dd HH:mm:ss)
    def getNowDateWithSecond(): String = {
        var now: Date = new Date()
        var sdf: SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        var nowDate = sdf.format(now)
        nowDate
    }


    // 20171012转化为year=2017/month=10/day=12
    def dateToPath(dt: String) = {
        val year = dt.substring(0, 4)
        val month = dt.substring(4, 6)
        val day = dt.substring(6, 8)
        val inpath1 = "year=" + year + "/month=" + month + "/day=" + day
        inpath1
    }


    //20170101->2017-01-01
    def dateAddLine(dt: String) = {
        val year = dt.substring(0, 4)
        val month = dt.substring(4, 6)
        val day = dt.substring(6, 8)
        year + "-" + month + "-" + day
    }


    //2017-01-01 -> 20170101
    def dateDeleteLine(dt: String) = {
        dt.replace("-", "")
    }

    // 通过某一天的日期得到当周第一天日期.ps:采用英系计数法，周日为一周第一天
    def getFirstDayOfWeek(dt: String): String = {
        val sdf = new SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateAddLine(dt))
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        val n: Int = calendar.get(Calendar.DAY_OF_WEEK)
        getNDaysAgo(dt, n - 1)
    }


    // 通过某一天的日期得到当周第七天日期.ps:采用英系计数法，周日为一周第一天
    def getLastDayOfWeek(dt: String): String = {
        val sdf = new SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateAddLine(dt))
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        val n: Int = calendar.get(Calendar.DAY_OF_WEEK)
        getNDaysAgo(dt, n - 7)
    }

    // 通过某一天的日期得到当周属于年度第几周
    def getWeekIndexOfYear(dt: String): String = {
        val sdf = new SimpleDateFormat("yyyy-MM-dd")
        val date = sdf.parse(dateAddLine(dt))
        val calendar = Calendar.getInstance()
        calendar.setTime(date)
        val index1: Int = calendar.get(Calendar.WEEK_OF_YEAR)
        var index = index1 + ""
        if (index1 < 10) index = "0" + index1
        index
    }

    // 得到指定日期的月初日期
    def getMonthStart(dt: String): String = {
        val ym = dt.toInt / 100
        ym + "01"
    }


    // 得到指定日期的月末日期
    def getMonthEnd(dt: String): String = {
        val calendar: Calendar = Calendar.getInstance()
        val sdf: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        val y = dt.toInt / 10000
        val m = dt.toInt % 10000 / 100
        val d = dt.toInt % 100
        calendar.set(y, m, 1)
        calendar.add(Calendar.DATE, -1)
        val days = calendar.get(Calendar.DAY_OF_MONTH).toString
        val ym = dt.toInt / 100
        ym + days
    }

    // 得到上个月的月初日期
    def getLastMonthStart(dt: String): String = {
        val firstDay = dt.toInt / 100 + "01"
        val yes = getYesterday(firstDay)
        val start = getMonthStart(yes)
        start
    }

    // 得到上个月的月末日期
    def getLastMonthEnd(dt: String): String = {
        val firstDay = dt.toInt / 100 + "01"
        val yes = getYesterday(firstDay)
        yes
    }


    // 得到本季度开始日期. 输入:20180606, 返回:20180401
    def getQuarterStart(dt: String): String = {
        val y = dt.substring(0, 4)
        val m = dt.substring(4, 6).toInt
        var res = ""
        if (m >= 1 && m <= 3) res = y + "0101"
        if (m >= 4 && m <= 6) res = y + "0401"
        if (m >= 7 && m <= 9) res = y + "0701"
        if (m >= 10 && m <= 12) res = y + "1001"
        res
    }

    // 得到本季度开始日期. 输入:20180606, 返回:20180630
    def getQuarterEnd(dt: String): String = {
        val y = dt.substring(0, 4)
        val m = dt.substring(4, 6).toInt
        var res = ""
        if (m >= 1 && m <= 3) res = y + "0331"
        if (m >= 4 && m <= 6) res = y + "0630"
        if (m >= 7 && m <= 9) res = y + "0930"
        if (m >= 10 && m <= 12) res = y + "1231"
        res
    }


    // 得到某日期所属的季度
    def getQuarterIndex(dt: String) = {
        val y = dt.substring(0, 4)
        val m = dt.substring(4, 6).toInt
        var res = ""
        if (m >= 1 && m <= 3) res = y + "-Q1"
        if (m >= 4 && m <= 6) res = y + "-Q2"
        if (m >= 7 && m <= 9) res = y + "-Q3"
        if (m >= 10 && m <= 12) res = y + "-Q4"
        res
    }

    // 得到指定时间n分钟间隔的时间。负数表示前n分钟，正数表示后n分钟
    def getNMinutesTime(time1: String, n: Int): String = {
        val df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val time: Date = df.parse(time1)
        val cal: Calendar = Calendar.getInstance()
        cal.setTime(time)
        cal.add(Calendar.MINUTE, n)
        df.format(cal.getTime)
    }

    //获取某段时间内的所有日期
    def getAllDate(begindate: String, enddate: String): Array[String] = {
        var res = List[String]()
        var cur_date: String = begindate
        while (cur_date <= enddate) {
            res ++= List(cur_date)
            cur_date = getNDaysAgo(cur_date, -1)
        }
        res.toArray
    }

    //两个时间的差值,相差几天,取绝对值 |date1 -date2|
    def dateDistance(date1: String, date2: String): Long = {
        val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        math.abs(dateFormat.parse(date1).getTime - dateFormat.parse(date2).getTime) / (1000 * 3600 * 24)
    }

    // 两个时间的差值,相差几天,date1 -date2
    def dateDistance2(date1: String, date2: String): Long = {
        val dateFormat: SimpleDateFormat = new SimpleDateFormat("yyyyMMdd")
        (dateFormat.parse(date1).getTime - dateFormat.parse(date2).getTime) / (1000 * 3600 * 24)
    }

    def main(args: Array[String]): Unit = {
        // println(DateUtil.getYesterday(DateUtil.getNowDate()))
        //println(DateUtil.getNowDateWithSecond().substring(11,19).compareTo("00:20:00"))
        println(DateUtil.dateToStamp("20190604"))
        // println(timestampToDateWithSecond("1543993224000"))
        //println(getNMinutesTime("2018-12-05 23:00:00",5))
        //println(dateToTimestamp2("2018-12-20 12:34:45"))
        //dateToTimestamp("20181115")
        //println("20180506".substring(4,8))
        /*
        var sdt = "20180101"
        val edt = "20180111"
        while(sdt <= edt) {
          println(sdt)
          sdt = DateUtil.getNDaysAgo(sdt, -1)
        }
        */
        //println(dateDeleteLine("2018-05-06"))
        /*
        for(n <- Set(1,2,3,4,5,6,7,14,30)) {
          val dt = DateUtil.getNDaysAgo("20180427", n)
          println(dt)
        }
        */

        /*
        var index="20180201"
        while(index<="20180210") {
          println(index)
          index = DateUtil.getTomorrow(index)
        }
        */

    }
}
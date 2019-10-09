package com.bd.mvnSparkTest

import java.io.PrintWriter

import org.apache.spark.{SparkConf, SparkContext}

object WordCount {
    def main(args: Array[String]): Unit = {
        val conf = new SparkConf()
            // Set a name for your application.
            // will show in 4040 page
            .setAppName("Word Count Demo")
            // The master URL to connect to, such as "local" to run locally with one thread,
            // "local[4]" to run locally with 4 cores,
            // or "spark://master:7077" to run on a Spark standalone cluster.
            .setMaster("local[2]")
        val context = SparkContext.getOrCreate(conf)
        // 通过SparkContext读取外部文件形成RDD
        val text = context.textFile("data/wc.data")
        // 调用RDD的Transformations算子(API),完成计算规则or业务需求
//        val outLine1 = text.flatMap(line => line.split(" "))
//        println(outLine1)
        val outLine = text.flatMap(line => line.split(" "))
            .map(word => (word, 1))
            .reduceByKey((a, b) => a+b)
        // 调用RDD的action算子，将结果打印or输出到文件
        // doc: https://blog.csdn.net/yizheyouye/article/details/49183265
        val writer = new PrintWriter("data/res.data")
        try {
            outLine.collect.foreach(x => writer.println(x))
        } finally {
            writer.close
        }

    }
}

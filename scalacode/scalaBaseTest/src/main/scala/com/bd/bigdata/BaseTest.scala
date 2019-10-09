package com.bd.bigdata

object BaseTest {
    def main(args: Array[String]): Unit = {
        val baseTest = new Base
        baseTest.age = 9
        baseTest.pName = "wangsheng"
        println(baseTest, baseTest.pName, baseTest.age)
    }
}

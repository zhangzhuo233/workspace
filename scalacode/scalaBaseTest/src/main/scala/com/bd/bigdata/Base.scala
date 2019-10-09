package com.bd.bigdata

class Base {
    // 定义属性
     var pName:String = ""
    // val pAge:Int = 10
    private var privAge = 10

    def age = privAge

    def age_=(newAge: Int): Unit = {
        if (newAge > privAge) {
            privAge = newAge
        }
    }

    // 返回值
    def outTest(name: String, age: Int) = {
        if (age > 18) {
            print(s"$name is $age")
            name
        } else {
            printf("%s is %d\n", name, age)
            age
        }
    }

    // 返回值
    def Fibonacci(n: Int): Int = {
        if (n < 2) {
            1
        } else {
            Fibonacci(n - 2) + Fibonacci(n - 1)
        }
    }

    // 匿名函数
    val res = (a: Int, b: Int) => println(a + b)

    // 默认参数
    def parmTest(name: String = "leo"): Unit = {
        println(s"Hi.I'm $name")
    }

    // 变长参数
    def parmsTest(name: String*): Unit = {
        name.foreach(x => println(x))
    }

    // 参数混用，演示带名参数
    def parmsUse(name: String, firstName: String = "zhang", lastName: String = "san"): Unit = {
        println(s"$name,$firstName,$lastName")
    }

    // for to
    def forTest(): Unit = {
        val end = 10
        for (i <- 1 to end)
            print(i)
    }

    // for until
    def forTest1() = {
        val end = 10
        for (i <- 1 until end)
            print(i)
    }

    // for String
    def forTest2() = {
        for (c <- "abcdef")
            print(c)
        val str = "ghjkl"
        for (c <- str)
            print(c)
    }

    import scala.util.control.Breaks._

    // 中断 return & 工具类break
    def interrupt(): Unit = {
        val n = 10
        for (i <- 1 to n) {
            if (i == 6) break;
            print(i)
        }
    }

    def interrupt1(): Unit = {
        val n = 10
        for (i <- 1 to n) {
            if (i == 6) return;
            print(i)
        }
    }

    // for循环结合if“守卫”
    def forifTest(): Unit = {
        val n = 10;
        for (i <- 1 to n if i % 2 == 0)
            print(i)
    }

    // 元组
    def tupleTest(): Unit = {
        val a = ("baidu", (1, "url"), (2, "www.baidu.com"))
        // println(a.productArity)
        // println(a.productElement(1))
        for (i <- 0 until a.productArity)
            println(a.productElement(i))
    }
}

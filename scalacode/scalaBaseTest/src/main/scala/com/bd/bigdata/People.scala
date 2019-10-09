package com.bd.bigdata

// 伴生类
class People(var name:String, var age:Int) {
    private var address = "BeiJing"
    def sayHello = println("name:"+ name + " age:" + age + " eyeNum:" + People.eyeNum)
}

// 伴生对象
object People {
    private var eyeNum = 1
    def getEyeNum = eyeNum
    def getAddr = println(new People("zhangsan", 18).address)

    // apply使用(若创建对象不想用new,可在伴生对象中实现apply方法)
    def apply(name: String, age: Int): People = new People(name, age)
    def main(args: Array[String]): Unit = {
        new People("lisi", 19).sayHello
        getAddr

        People("zhangsan", 20)
    }
}

// 对比
//object Person {
//    // new People("zhangsan", 18).address
//}

// 继承
abstract class Person {
    // scala中的属性必须赋初始值
    var name = ""
    var age = 0
    // 抽象方法
    def think():Unit

    // 非抽象方法
    def eat() = {
        println(s"$name eatting")
    }
    def sport(ball:String): Unit = {
        println(s"$name is playing $ball")
    }
}
class SuperPerson extends Person {
    override def think(): Unit = {
        println("思我所思")
    }

    override def eat(): Unit = {
        println("精神食粮")
    }
}
object SuperPerson {
    def apply(): SuperPerson = new SuperPerson()
    def main(args: Array[String]): Unit = {
        SuperPerson().eat()
        SuperPerson().think()
    }
}



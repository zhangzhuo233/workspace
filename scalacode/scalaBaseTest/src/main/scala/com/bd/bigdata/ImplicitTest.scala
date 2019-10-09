package com.bd.bigdata

import scala.util.Try

class ImplicitTest {

}

class SpecialPerson(var name:String)
class Older(var name:String)
class Student {
    def write(context: String) = println("I am writing " + context)
}

object ImplicitTest{
    // 隐式转换
    implicit def old2Special(older: Older): SpecialPerson = {
        new SpecialPerson(older.name)
    }
    def buyTicket(specialPerson: SpecialPerson): Unit = {
        println(specialPerson.name + " buy ticket")
    }
    implicit val student = new Student
    // 隐式参数
    def write(implicit student: Student) = {
        student.write("333")
    }
    def main(args: Array[String]): Unit = {
        var old:Older = new Older("zhangsan")
        buyTicket(old)
        write
    }
    val ff = buyTicket _
    
}

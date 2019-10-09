package com.bd.bigdata

class TraitTest {

}
trait Equal {
    def isEqual(x: Any): Boolean
    def isNotEqual(x: Any): Boolean = !isEqual(x)
}
trait Alarm {
    def alarm(): Unit
}
class Point(xc: Int, yc: Int) extends Equal with Alarm {
    var x: Int = xc
    var y: Int = yc
    override def isEqual(obj: Any): Boolean =
        obj.isInstanceOf[Point] &&
            obj.asInstanceOf[Point].x == x &&
            obj.asInstanceOf[Point].y == y
    override def alarm(): Unit = println("ding~~")
}
object Point {
    def apply(xc: Int, yc: Int): Point = new Point(xc, yc)
    def main(args: Array[String]): Unit = {
        val p1 = Point(1, 3)
        val p2 = Point(1, 4)
        val p3 = Point(1, 3)
        println(p1.isEqual(p2))
        println(p1.isEqual(p3))
        p1.alarm()
    }
}
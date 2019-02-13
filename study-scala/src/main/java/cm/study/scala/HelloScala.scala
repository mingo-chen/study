package cm.study.scala

import cm.study.scala.web.People

object HelloScala {
  def main(args: Array[String]): Unit = {
    println("Hello, Scala!!!")

    val address =
      s"""|安徽省
        |安庆市
      """.stripMargin

    var cm = People("cm", 30, address, 90)
    var ljx = People("ljx", 26, "gz", 98)

    cm + 10
    println("--> " + cm)
    println("--> " + cm.createTime)
    println("--> " + cm.name)
    cm.name
//    println("--> " + score)

  }
}
package cm.study.scala

import java.util


class Table {
  var table = new util.ArrayList[util.ArrayList[String]]() // trs
  var trs = new util.ArrayList[String]() // tds

//  table.add(trs)

  def |(td: String): Table = {
    trs.add(td)
    this
  }

  def ||(td: String): Table = {
    table.add(trs)
    trs = new util.ArrayList[String]()
    trs.add(td)
    this
  }

  override def toString: String = {
    val html = new StringBuilder()
    html.append("<table>")

    val iter = table.iterator()
    while(iter.hasNext) {
      val line = iter.next()
      html.append("\n\t<tr>")

      val tdIter = line.iterator()
      while(tdIter.hasNext) {
        val text = tdIter.next()
        html.append("<td>").append(text).append("</td>")
      }

      html.append("</tr>\n")
    }

    html.append("\n\t<tr>")

    val tdIter = trs.iterator()
    while(tdIter.hasNext) {
      val text = tdIter.next()
      html.append("<td>").append(text).append("</td>")
    }

    html.append("</tr>\n")

//    for(tr <- table.iterator()) {
//      html.append("<tr>")
//      for(td <- tr) {
////        var text = (String) td
//        println("--> " + td)
//
//        html.append("<td>").append("").append("</td>")
//      }
//      html.append("</tr>")
//    }

    html.append("</table>")

    html.toString()
  }
}

object Table {
  def apply(): Table = new Table()

  def main(args: Array[String]): Unit = {
    val html = Table() | "Java" | "Scala" || "C#" | "C++" | "C" || "VB"
    print( html)
  }
}

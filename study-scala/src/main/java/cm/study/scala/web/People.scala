package cm.study.scala.web

import java.util.Date

import scala.beans.BeanProperty

class People(@BeanProperty var name: String, age: Int, address: String, var score: Int) {

  @BeanProperty
  val createTime = new Date()

  def +(delta: Int): Unit = {
    this.score = this.score + delta
  }

  override def toString() = {
    val sb = new StringBuilder();
    sb.append("name=").append(name).append(", ")
        .append("age=").append(age).append(", ")
        .append("address=").append(address).append(", ")
        .append("score=").append(score)

    sb.toString()
  }

  println("People has create, time: " + this.createTime + ", name: " + this.name)
}

object People {
  def apply(name: String): People = new People(name, 0, "", 0)

  def apply(name: String, age: Int): People = new People(name, age, "", 0)

  def apply(name: String, age: Int, address: String): People = new People(name, age, address, 0)

  def apply(name: String, age: Int, address: String, score: Int): People = new People(name, age, address, score)
}
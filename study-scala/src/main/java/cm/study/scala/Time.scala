package cm.study.scala

class Time(val hour: Int, val minute: Int) {
  if(hour <0 || hour > 23) {
    throw new IllegalArgumentException("hour must between 0~23, but get: " + hour)
  }

  if(minute < 0 || minute > 59) {
    throw new IllegalArgumentException("minute must between 0~59, but get: " + minute)
  }

  def before(other: Time): Boolean = {
    return (this.hour * 60 + this.minute) < (other.hour * 60 + other.minute)
  }

  override def toString: String = {
    return hour + ":" + minute
  }
}

object Time {

  def compare(time1: Time, time2: Time): Boolean = {
    return time1.before(time2)
  }

  def main(args: Array[String]): Unit = {
    val t1 = new Time(1, 57)
    println("--> " + t1)

    val t2 = new Time(4, 12)
    println("--> " + t2)

    println("--> " + Time.compare(t1, t2))
  }
}
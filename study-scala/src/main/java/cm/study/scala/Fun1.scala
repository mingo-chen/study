package cm.study.scala

import java.util


object Fun1 {
  def values(fun: (Int) => Int, low: Int, high: Int): util.List[(Int, Int)] = {
    val result = new util.ArrayList[(Int, Int)]()

    for(x <- Range(low, high+1)) {
      println("--> " + x)
      result.add((x, fun(x)))
    }

    result
  }

  def maxValue(values: Array[Int]): Int = {
    values.reduceLeft((a, b) => if(a > b) a else b)
  }

  def jie_chen(limit: Int): Int = {
    1 to limit reduceLeft((a, b) => a * b)
  }

  def largest(fun: (Int) => Int, inputs: Seq[Int]): Int = {
    val maxBase = inputs.reduceLeft((a, b) => if(fun(a) > fun(b)) a else b)

    fun(maxBase)
  }

  def largestAt(fun: (Int) => Int, inputs: Seq[Int]): Int = {
    val maxBase = inputs.reduceLeft((a, b) => if(fun(a) > fun(b)) a else b)

    maxBase
  }

  def main(args: Array[String]): Unit = {
    val list = Fun1.values(x => x * x, -3, 3)
    println("--> " + list)

    val max = Fun1.maxValue(Array(11, 3, 5, 9))
    println("max: " + max)

    val limit = 5
    val rt2 = Fun1.jie_chen(limit)
    println("jiechen("+ limit +")= " + rt2)

    val max2 = Fun1.largest(x => 10 * x - x * x, 1 to 10)
    val maxAt = Fun1.largestAt(x => 10 * x - x * x, 1 to 10)
    println("max2: " + max2 + ", at: " +  maxAt)
  }
}
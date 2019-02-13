package cm.study.scala

object Fun2 {

  def unless(condition: => Boolean)(block: => Unit) {
    if(!condition) {
      block
    }
  }

  def corresponds_with_curry(): Unit = {
    val a1 = Array("cm", "ljx", "china")
    val a2 = Array(2, 3, 5)

    val rt = a1.corresponds(a2)((text, length) => text.length == length)
    println("--> " + rt)
  }

  def corresponds_with_uncurry[A, B](seq1: Seq[A], seq2: Seq[B], fun: (A, B)=> Boolean): Boolean = {
    var rt = true
    val minLen = Math.min(seq1.length, seq2.length)
    for(idx <- Range(0, minLen)) {
      val e1 = seq1(idx)
      val e2 = seq2(idx)

      if(!fun(e1, e2)) {
        rt = false
      }
    }

    rt
  }

  def main(args: Array[String]): Unit = {
    unless(3 > 10) {
      println("--> undo!")
    }

    corresponds_with_curry()

    val a1 = Array("cm", "ljx", "china")
    val a2 = Array(2, 4, 5)
    val fun = (text: String, length: Int) => text.length == length

    val rt = corresponds_with_uncurry(a1, a2, fun)
    println("--> " + rt)
  }

}

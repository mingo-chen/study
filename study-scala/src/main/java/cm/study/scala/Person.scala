package cm.study.scala

class Person(fullname: String) {

  def firstName(): String= {
    fullname.split(" ")(0)
  }

  def lastName(): String= {
    fullname.split(" ")(1)
  }
}

object Person {
  def apply(fullname: String): Person = new Person(fullname)

  def unapply(person: Person): Option[String] = {
    return Option(person.firstName + " " + person.lastName)
  }

  def main(args: Array[String]): Unit = {
    val peter = Person("tom boom")
    println("--> " + peter.firstName())
    println("--> " + peter.lastName())

    val fullname = Person.unapply(peter)
    println("--> " + fullname.get)
  }
}

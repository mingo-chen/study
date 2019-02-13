package cm.study.scala

class BankAccount(val balance: Int) {

  def deposit(): Int = {
    return balance
  }

  def withdraw(): Int = {
    return balance + 10
  }
}

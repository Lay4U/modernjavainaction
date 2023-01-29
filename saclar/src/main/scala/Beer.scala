import scala.collection.IterableOnce.iterableOnceExtensionMethods
import scala.io.Source

object Beer {
  def main(args: Array[String]): Unit = {
    var n : Int = 2
    while( n <= 6) {
      println(s"Hello ${n} bottles of beer")
      n += 1
    }

    2 to 6 foreach { n => println(s"Hello ${n} bottles of beer")}

    val authorsToAge = Map("Raoul" -> 23, "Mario" -> 40, "Alan" -> 53)

    val authors = List("Raoul", "Mario", "Alan")
    val numbers = Set(1, 1, 2 ,3 ,5 ,8)

    val numbers2 = Set(2, 5, 3);

    val fileLines = Source.fromFile("data.txt").getLines.toList
    val linesLongUpper = fileLines.filter(l => l.length() > 10)
      .map(l => l.toUpperCase())

    val linesLongUpper2 = fileLines filter (_.length() > 10) map(_.toUpperCase())


    val raoul = ("Raoul", "+ 44 123 4567")
    val alan = ("Alan", "+44 123 4568")

    val book = (2018, "Modern Java in Action", "Manning")
    val numbersTuple = (42, 1337, 0, 3, 14)


    def getCarInsuranceName(person: Option[Person], minAge: Int) =
      person.filter(_.getAge() >= minAge)
        .flatMap(_.getCar)
        .flatMap(_.getInsurace)
        .map(_.getName)
        .getOrElse("Unknown")
  }

  def isJavaMentioned(tweet: String) : Boolean = tweet.contains("Java")
  def isSHortTweet(tweet: String) : Boolean = tweet.length() < 20

  val tweets = List(
    "I love the new features in Java 8",
    "How's it going?",
    "An SQL query walks into a bar, walks up to two tables and asks...",
  )
  tweets.filter(isJavaMentioned).foreach(println)
  tweets.filter(isSHortTweet).foreach(println)

  val isLongTweet : String => Boolean = (tweet : String) => tweet.length() > 20


  var count = 0
  val inc = () => count += 1
  inc()
  println(count)
  inc()
  println(count)

  def multiply(x: Int, y: Int) = x * y

  val r = multiply(2, 10)

  def multiplyCurry(x: Int)(y: Int) = x * y

  var r = multiplyCurry(2)(10)


  class Hello {
    def sayThankYou(): Unit = {
      println("Thank you for your purchase!")
    }


  }

  val h = new Hello()
  h.sayThankYou()

  class Student(var name: String, var id: Int)

  val s = new Student("Raoul", 1)
  println(s)
  s.id = 1337
  println(s.id)

  trait Sized {
    var size: Int = 0
    def isEmpty() = size == 0
  }

  class Empty extends Sized

  println(new Empty().isEmpty())

  class Box
  val b1 = new Box() with Sized
  println(b1.isEmpty())
  val b2 = new Box()
//  b2.isEmpty()


}







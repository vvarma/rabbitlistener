package sample

/**
 * Created by vinay.varma on 10/19/14.
 */
object Hi extends App {
  println("Hello SBT")
  val now = new org.joda.time.DateTime()
  println("Hi SBT, the time is " + now.toString("hh:mm aa"))

//  val listener = new RabbitListener("localhost", "europa-statebus", "guest", "guest")
//  listener.listen()
}

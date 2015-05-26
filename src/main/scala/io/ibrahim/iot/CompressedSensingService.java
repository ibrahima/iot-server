package io.ibrahim.iot

import akka.actor.Actor
import spray.routing._
import spray.http._
import MediaTypes._

// we don't implement our route structure directly in the service actor because
// we want to be able to test it independently, without having to spin up an actor
class CompressedSensingActor extends Actor with CompressedSensingService {

  // the HttpService trait defines only one abstract member, which
  // connects the services environment to the enclosing actor or test
  def actorRefFactory = context

  // this actor only runs our route, but you could add
  // other things here, like request stream processing
  // or timeout handling
  def receive = runRoute(myRoute)
}


// this trait defines our service behavior independently from the service actor
trait CompressedSensingService extends HttpService {

  val myRoute =
    path("") {
      get {
        respondWithMediaType(`application/json`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            """{"message": "Hi"}"""
          }
        }
      }
    }
}

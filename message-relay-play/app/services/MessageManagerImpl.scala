package services

import java.io.File
import java.time.{ZoneId, ZonedDateTime}

import com.google.inject.{Inject, Provider}
import javax.inject.Singleton
import play.api.{Application, Logger, Play}
import play.api.libs.json.Json
import play.libs.ws.WSClient

import scala.collection.mutable.ListBuffer
import scala.concurrent.{ExecutionContext, duration}
import scala.concurrent.duration.Duration
import scala.util.{Failure, Success}

@Singleton
class MessageManagerImpl @Inject()(appProvider: Provider[Application], ws: WSClient)(implicit ec: ExecutionContext) extends MessageManager {

  val logger: Logger = Logger.apply(this.getClass)

  val myServiceName: String = sys.env("MY_SERVICE_NAME")
  val messageFile = new File(appProvider.get().path, "/public/messages.html")

  val messages: ListBuffer[String] = ListBuffer()

  def send(sendTo: String, message: String): Unit = {
    val url = s"http://$sendTo/message"

    val jsobj = Json.obj("sender" -> myServiceName, "message" -> message)
    ws
      .asScala()
      .url(url)
      .withRequestTimeout(Duration(5, duration.SECONDS))
      .post(jsobj)
      .onComplete {
        case Success(value) => None
        case Failure(exception) => logger.error("Failed to message: " + url)
      }
  }

  def write(sender: String, message: String): Unit = {
    logger.info("received message from " + sender)

    val time = ZonedDateTime.now().withZoneSameInstant(ZoneId.of("UTC"))
    messages.append(s"$time $sender wrote: $message<br>")

    //val bw = new BufferedWriter(new FileWriter(messageFile, true))
    //bw.write(s"$time $sender wrote: $message<br>")
    //bw.close()

  }

  def getMessages(): ListBuffer[String] = {
    messages
  }

}

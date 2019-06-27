package controllers

import javax.inject._
import play.api.libs.json._
import play.api.Logger
import play.api.mvc._
import services.{MessageManager, MessageManagerImpl}

import scala.concurrent.ExecutionContext


/**
 * This controller creates an `Action` to handle HTTP requests to the
 * application's home page.
 */
@Singleton
class HomeController @Inject()(cc: ControllerComponents, messageManager: MessageManager)(implicit ec: ExecutionContext) extends AbstractController(cc) {

  val logger: Logger = Logger.apply(this.getClass)

  val hostname: String = sys.env("MY_SERVICE_NAME")

  def index = Action {
    Ok(views.html.index())
  }

  def send() = Action { implicit request: Request[AnyContent] =>
    val body = request.body.asJson.get
    logger.info(request.body.asJson.toString)
    val sendto = (body \ "sendto").as[JsArray].value.map(a => a.as[String]).flatMap(a => if (a.isEmpty) None else Some(a)) ++ Seq(hostname)
    val message = (body \ "message").as[String]

    logger.info("Sending message to " + sendto.reduce( (a,b) => s"$a, $b") )


    sendto.foreach(messageManager.send(_, message))
    Ok
  }

  def receive() = Action { implicit request: Request[AnyContent] =>
    val body = request.body.asJson.get
    val sender = (body \ "sender").as[String]
    val message = (body \ "message").as[String]
    messageManager.write(sender, message)
    Ok
  }

  def getMessages() = Action { implicit request: Request[AnyContent] =>
    val lastMessageIndex = request.getQueryString("index").flatMap(_.toIntOption).getOrElse(0)
    val messages = messageManager.getMessages()
    val newMessages = messages.slice(lastMessageIndex, messages.length)
    val response = Json.obj(
      "messages" -> newMessages,
      "index" -> JsNumber(messages.length)
    )
    Ok(response)
  }

}


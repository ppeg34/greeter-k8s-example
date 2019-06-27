package services

import com.google.inject.ImplementedBy

import scala.collection.mutable.ListBuffer

@ImplementedBy(classOf[MessageManagerImpl])
trait MessageManager {
  def send(sendTo: String, message: String): Unit
  def write(sender: String, message: String): Unit
  def getMessages(): ListBuffer[String]
}

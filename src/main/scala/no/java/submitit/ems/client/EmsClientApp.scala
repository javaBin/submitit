package no.java.submitit.ems.client

import no.java.ems.client._
import no.java.submitit.model._

object EmsClientApp {
  def URI = "http://localhost:3000/ems"
 
  val speaker1 = Speaker("Alf Kristian Støyle", "aks@knowit.no", "Developer at KnowIT", null)
  val speaker2 = Speaker("Jon Anders Teigen", "mail@jteigen.com", "Developer at JPro", null)
  val speaker3 = Speaker("Fredrik Vraalsen", "fredrik@vraalsen.no", "Developer at KnowIT", null)
  val speakers = speaker1 :: speaker2 :: speaker3 :: Nil
  val presentation = Presentation("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Cras pellentesque.", 
                                      speakers, 
                                      """Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Ut tortor quam, semper in, aliquet ac, posuere ac, leo. Phasellus justo lectus, interdum eu, molestie vel, rutrum vel, enim. Pellentesque ultrices magna et erat. Integer velit urna, aliquam sed, sodales quis, semper a, sem. Proin convallis metus sit amet sapien. Nunc massa elit, pretium sed, interdum id, placerat vel, tellus. Duis ullamcorper nulla. Mauris eget tortor in enim vestibulum ultrices. 
Sed sapien diam, iaculis non, ullamcorper sagittis, rhoncus ac, eros. Donec semper, sapien a consequat lobortis, nisl arcu elementum metus, ac dapibus sem neque sed diam. Morbi eget nisl. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. In hac habitasse platea dictumst. Vestibulum rhoncus semper justo.""", 
                                      """Lorem
ipsum
dolor
sit
amet""", 
                                      """Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Curabitur consequat, eros vitae feugiat consectetuer, mi risus suscipit est, non tincidunt risus sapien sed tortor. Suspendisse potenti. In ut tortor a arcu mollis mollis. Aliquam magna libero, rhoncus ac, ullamcorper sit amet, faucibus id, arcu. Nulla aliquet porta pede. Mauris posuere nunc. Donec ullamcorper turpis et sapien sollicitudin elementum. Nam nec arcu. In interdum nunc ut risus. In justo erat, semper quis, elementum vel, fringilla id, mauris.
                                        Integer blandit tincidunt arcu. Praesent pede. Quisque lacinia. Fusce mattis mollis nisi. Morbi congue, nunc sed dictum dignissim, enim lacus adipiscing augue, sed tincidunt tortor nulla vel dui. Ut in velit. Pellentesque pulvinar lorem ac dui. Suspendisse consectetuer, urna a eleifend rhoncus, ante massa porta est, vel vulputate sem sapien sit amet nulla. Maecenas id pede. Etiam et orci. Quisque sapien nibh, faucibus in, sodales sit amet, facilisis in, dui. Class aptent taciti sociosqu ad litora torquent per conubia nostra, per inceptos himenaeos. Nam euismod rhoncus leo. """, 
                                      Language.English, 
                                      Level.Beginner, 
                                      60, 
                                      "equipment", 
                                      "required experience", 
                                      "expected audience")

  def main(args : Array[String]) : Unit = {
    val emsService = new RestEmsService(URI, true)
    val emsClient = new EmsClient("JavaZone 2009", emsService)

    emsClient.savePresentation(presentation)
    println(presentation.sessionId)
    val session = emsService.getSession(presentation.sessionId)
    println(session)
    emsService.deleteSession(presentation.sessionId)
  }
}

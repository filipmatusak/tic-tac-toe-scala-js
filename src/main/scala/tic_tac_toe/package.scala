import com.thoughtworks.binding.Binding

package object tic_tac_toe {
  implicit def makeIntellijHappy(x: scala.xml.Node): Binding[org.scalajs.dom.raw.Node] = ???

}

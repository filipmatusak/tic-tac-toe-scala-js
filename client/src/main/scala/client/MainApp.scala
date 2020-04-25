package client

import client.Game.GameParams
import org.scalajs.dom.document
import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.dom
import org.scalajs.dom.document

import scala.scalajs.js.JSApp
import scala.scalajs.js.annotation.JSExport

object MainApp extends JSApp {

  val mainColor = "light-blue"

  def main(): Unit = {
    dom.render(document.getElementById("mainContainer"), base())
  }

  @dom def base() = {
    val isMenuOpen: Var[Boolean] = Var(true)
    val height: Var[Int] = Var(0)
    val width: Var[Int] = Var(0)
    val goal: Var[Int] = Var(0)

    <div>
      {
      if (isMenuOpen.bind) Menu(isMenuOpen, height, width, goal).bind
      else Game.newGame(GameParams(height.get, width.get, goal.get), { _: Unit => isMenuOpen := true }).bind
      }
    </div>
  }
}


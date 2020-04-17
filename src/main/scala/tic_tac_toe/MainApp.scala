package tic_tac_toe

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import tic_tac_toe.Game.GameParams

import scala.xml._
import scala.scalajs.js.JSApp

object MainApp extends JSApp {
  implicit def makeIntellijHappy(x: scala.xml.Node): Binding[org.scalajs.dom.raw.Node] = ???

  def main(): Unit = {
    dom.render(document.getElementById("mainContainer"), base())
  }

  @dom def base() = {
    val menu = Var(true)
    val height = Var(20)
    val width = Var(20)
    val goal = Var(5)

    <div>
      {if (!menu.bind) Game.newGame(GameParams(height.get, width.get, goal.get), {_: Unit => menu := true}).bind
    else {
      <div>
        Height:
        <input type="number" id="heightInput" min="3" onchange={(e: Event) => height := heightInput.value.toInt} value={height.bind.toString}></input>
        Width:
        <input type="number" id="widthInput" min="3" onchange={(e: Event) => width := widthInput.value.toInt} value={width.bind.toString}></input>
        Goal:
        <input type="number" id="goalInput" onchange={(e: Event) => goal := goalInput.value.toInt} value={goal.bind.toString}></input>
        <button onclick={_: Event => menu := false}>Start</button>
      </div>
    }}
    </div>
  }
}

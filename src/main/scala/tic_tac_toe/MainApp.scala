package tic_tac_toe

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.dom
import org.scalajs.dom.document
import org.scalajs.dom.raw.Event
import tic_tac_toe.Game.GameParams

import scala.scalajs.js.JSApp

object MainApp extends JSApp {

  def main(): Unit = {
    dom.render(document.getElementById("mainContainer"), base())
  }

  @dom def base() = {
    val menu = Var(true)
    val height = Var(15)
    val width = Var(15)
    val goal = Var(5)

    <div>
      {
      if (!menu.bind) Game.newGame(GameParams(height.get, width.get, goal.get), { _: Unit => menu := true }).bind
      else {
        <div class="section container">
          <div class="row">
            <div class="col s12 m2">Height:</div>
            <input type="number"
                   class="col s12 m10"
                   id="heightInput"
                   min="3"
                   onchange={(e: Event) => height := heightInput.value.toInt}
                   value={height.get.toString}></input>
          </div>
          <div class="row">
            <div class="col s12 m2">Width:</div>
            <input type="number"
                   class="col s12 m10"
                   id="widthInput"
                   min="3"
                   onchange={(e: Event) => width := widthInput.value.toInt}
                   value={width.get.toString}></input>
          </div>
          <div class="row">
            <div class="col s12 m2">Goal:</div>
            <input type="number"
                   class="col s12 m10"
                   id="goalInput"
                   onchange={(e: Event) => goal := goalInput.value.toInt}
                   value={goal.get.toString}></input>
          </div>
          <div class="row">
            <button class="col s2 waves-effect waves-light btn light-blue" onclick={_: Event => menu := false}>Start</button>
          </div>
        </div>
      }
      }
    </div>
  }
}


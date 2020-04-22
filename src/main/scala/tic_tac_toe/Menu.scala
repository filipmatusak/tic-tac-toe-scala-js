package tic_tac_toe

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom.raw.Event
import tic_tac_toe.MainApp.mainColor

object Menu {
  def stringToInt(str: String): Int = {
    if(str == "") 0 else str.toInt
  }

  @dom def warning(value: Binding[Boolean]) = {
    <div class="col s12 m7">{
      if(value.bind) <p class="warning red-text">Value must be at least 3</p>
      else <div></div>
      }</div>
  }

  @dom def apply(isMenuOpen: Var[Boolean], height: Var[Int], width: Var[Int], goal: Var[Int]) = {
    if(height.get == 0) height := 15
    if(width.get == 0) width := 15
    if(goal.get == 0) goal := 5

    val heightOutOfBound: Binding[Boolean] = Binding{height.bind < 3}
    val widthOutOfBound: Binding[Boolean] = Binding{width.bind < 3}
    val goalOutOfBound: Binding[Boolean] = Binding{goal.bind < 3}

    <div class="section container">
      <div class="row">
        <div class="col s4 m2"><p class="menu-label">Height:</p></div>
        <input type="number"
               class={s"col s3 m3 "}
               id="heightInput"
               onkeyup={(e: Event) => height := stringToInt(heightInput.value)}
               value={height.get.toString}></input>
        {warning(heightOutOfBound).bind}
      </div>
      <div class="row">
        <div class="col s4 m2"><p class="menu-label">Width:</p></div>
        <input type="number"
               class={s"col s3 m3 "}
               id="widthInput"
               onkeyup={(e: Event) => width := stringToInt(widthInput.value)}
               value={width.get.toString}></input>
        {warning(widthOutOfBound).bind}
      </div>
      <div class="row">
        <div class="col s4 m2"><p class="menu-label">Goal:</p></div>
        <input type="number"
               class={s"col s3 m3"}
               id="goalInput"
               onkeyup={(e: Event) => goal := stringToInt(goalInput.value)}
               value={goal.get.toString}></input>
        {warning(goalOutOfBound).bind}
      </div>
      <div class="row">
        {
        val disabled = if(heightOutOfBound.bind || widthOutOfBound.bind || goalOutOfBound.bind) " disabled" else ""
        <button class={s"col s12 m3 waves-effect waves-light btn light-blue $mainColor $disabled"} onclick={_: Event => isMenuOpen := false}>Start</button>}
      </div>
    </div>
  }
}

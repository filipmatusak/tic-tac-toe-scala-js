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
    <div class="col s12">{
      if(value.bind) <p class="warning red-text">Value must be at least 3</p>
      else <div></div>
      }</div>
  }

  def defaultSize(): (Int, Int) = {
    val screenSize = org.scalajs.dom.window.screen.availWidth
    val containerSize = if(screenSize <= 600) .9 else if(screenSize <= 992) .85 else .7
    val width = (org.scalajs.dom.window.screen.availHeight * containerSize / 32 - 1).toInt.min(20)
    val height = (org.scalajs.dom.window.screen.availWidth * containerSize / 32).toInt.min(20)
    (width, height)
  }

  @dom def apply(isMenuOpen: Var[Boolean], height: Var[Int], width: Var[Int], goal: Var[Int]) = {
    if(height.get == 0) {
      val (h, w) = defaultSize()
      height := h
      width := w
      goal := 5
    }

    val heightOutOfBound: Binding[Boolean] = Binding{height.bind < 3}
    val widthOutOfBound: Binding[Boolean] = Binding{width.bind < 3}
    val goalOutOfBound: Binding[Boolean] = Binding{goal.bind < 3}

    <div class="section container">
      <div class="row">
        <div class="col s3 offset-s3"><p class="menu-label">Height:</p></div>
        <input type="number"
               class={s"col s3"}
               id="heightInput"
               onkeyup={(e: Event) => height := stringToInt(heightInput.value)}
               value={height.get.toString}></input>
        {warning(heightOutOfBound).bind}
      </div>
      <div class="row">
        <div class="col s3 offset-s3"><p class="menu-label">Width:</p></div>
        <input type="number"
               class={s"col s3"}
               id="widthInput"
               onkeyup={(e: Event) => width := stringToInt(widthInput.value)}
               value={width.get.toString}></input>
        {warning(widthOutOfBound).bind}
      </div>
      <div class="row">
        <div class="col s3 offset-s3"><p class="menu-label">Goal:</p></div>
        <input type="number"
               class={s"col s3"}
               id="goalInput"
               onkeyup={(e: Event) => goal := stringToInt(goalInput.value)}
               value={goal.get.toString}></input>
        {warning(goalOutOfBound).bind}
      </div>
      <div class="row">
        {
        val disabled = if(heightOutOfBound.bind || widthOutOfBound.bind || goalOutOfBound.bind) " disabled" else ""
        <button class={s"col s6 offset-s3 l4 offset-l5 waves-effect waves-light btn light-blue $mainColor $disabled"} onclick={_: Event => isMenuOpen := false}>Start</button>}
      </div>
    </div>
  }
}

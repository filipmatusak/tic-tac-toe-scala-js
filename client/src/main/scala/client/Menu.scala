package client

import org.scalajs.dom._
import org.scalajs.dom.raw.Event
import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.{Binding, dom}
import org.scalajs.dom._
import org.scalajs.dom.raw.Event
import MainApp.mainColor

object Menu {
  def stringToInt(str: String): Int = if(str == "") 0 else str.toInt

  @dom def input(name: String, value: Var[Int], minValue: Int, isOutOfBound: Binding[Boolean]) = {
    <div class="row">
      <div class="col s3 offset-s3"><p class="menu-label">{name + ":"}</p></div>
      <input type="number"
             class={s"col s3"}
             id={name}
             onkeyup={(e: Event) => value := stringToInt(document.getElementById(name).asInstanceOf[html.Input].value)}
             value={value.get.toString}></input>
      {
      if(isOutOfBound.bind) <p class="col s12 warning red-text">Value must be at least 3</p>
      else <div></div>
      }
    </div>
  }

  def defaultSize(): (Int, Int) = {
    val screenSize = window.screen.availWidth
    val containerSize = if(screenSize <= 600) .9 else if(screenSize <= 992) .85 else .7
    val width = (window.screen.availHeight * containerSize / 32 - 1).toInt.min(20)
    val height = (window.screen.availWidth * containerSize / 32).toInt.min(20)
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
      {input("Height", height, 3, heightOutOfBound).bind}
      {input("Width", width, 3, widthOutOfBound).bind}
      {input("Goal", goal, 3, goalOutOfBound).bind}
      <div class="row">
        {
        val disabled = if(heightOutOfBound.bind || widthOutOfBound.bind || goalOutOfBound.bind) " disabled" else ""
        <button class={s"col s6 offset-s3 l4 offset-l5 waves-effect waves-light btn light-blue $mainColor $disabled"} onclick={_: Event => isMenuOpen := false}>Start</button>}
      </div>
    </div>
  }
}

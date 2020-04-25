package client

import org.scalajs.dom.raw.Event
import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.dom
import org.scalajs.dom.raw.Event

object Game {

  case class GameParams(height: Int, width: Int, goal: Int)
  case class Cell(selected: Var[Option[Int]] = Var(None), winning: Var[Boolean] = Var(false))

  @dom def newGame(params: GameParams, goToMenu: Unit => Unit) = {
    import params._

    val gameWinner: Var[Option[Int]] = Var(None)
    val matrix: Vector[Vector[Cell]] = Vector.fill(height)(Vector.fill(width)(Cell()))
    val playerOnTurn = Var(0)
    val emptyCells = Var(height * width)
    val history: Var[List[Cell]] = Var(Nil)

    def reset(): Unit = {
      for (row <- matrix; cell <- row) {
        cell.selected := None
        cell.winning := false
      }
      playerOnTurn := 0
      emptyCells := params.height * params.width
      history := Nil
      gameWinner := None
    }

    def handleClick(value: Cell, playerOnTurn: Var[Int]): Unit = {
      if (value.selected.get.isEmpty && gameWinner.get.isEmpty) {
        value.selected := Some(playerOnTurn.get + 1)
        emptyCells.:=(emptyCells.get - 1)
        history := (value :: history.get)
        if (findRow()) {
          gameWinner:=Some(playerOnTurn.get)
        } else if(emptyCells.get == 0){
          gameWinner:=Some(-1)
        }
        playerOnTurn.:=((playerOnTurn.get + 1) % 2)
      }
    }

    def handleBack(): Unit = {
      history.get.head.selected := None
      history := history.get.tail
      playerOnTurn.:=((playerOnTurn.get + 1) % 2)
    }

    //not optimal
    def findRow(): Boolean = {
      def find(startX: Int, startY: Int, dx: Int, dy: Int): Boolean = {
        var count = 1
        var last = matrix(startX)(startY).selected.get
        var x = startX + dx
        var y = startY + dy
        while (x >= 0 && x < height && y >= 0 && y < width) {
          val v = matrix(x)(y).selected.get
          if (v == last) {
            count += 1
            if (count >= goal && last.nonEmpty) {
              for(i <- 0 until goal) matrix(x-i*dx)(y-i*dy).winning := true
              return true
            }
          } else {
            count = 1
            last = v
          }
          x += dx
          y += dy
        }
        false
      }

      (0 until height).exists(row => find(row, 0, 0, 1)) ||
        (0 until width).exists(column => find(0, column, 1, 0)) ||
        (0 until height).exists(row => find(row, 0, 1, 1)) ||
        (1 until width).exists(column => find(0, column, 1, 1)) ||
        (0 until height).exists(row => find(row, 0, -1, 1)) ||
        (1 until width).exists(column => find(height - 1, column, -1, 1))
    }

    <div class="container">
      <div class="row">
        <p class="col s12 game-state-label">{
          gameWinner.bind match {
            case None => "Player on turn: " + {playerOnTurn.bind match {
              case 0 => "X"
              case 1 => "O"
            }}
            case Some(0) => "Winner is X"
            case Some(1) => "Winner is O"
            case Some(_) => "Draw"
          }}</p>
        <div class="col s12">
          <table>
            {
            import scalaz.std.vector._
            matrix.map { row =>
              <tr>
                {row.map { value =>
                val text = {value.selected.bind match {
                  case Some(1) => "X"
                  case Some(2) => "O"
                  case _ => " "
                }}
                val color = if(value.winning.bind) "light-blue lighten-4" else ""
                <td class={color} onclick={(_: Event) => handleClick(value, playerOnTurn)}>
                  {text}
                </td>
              }}
              </tr>
            }}
          </table>
        </div>
      </div>
      {
      val buttonClass = "waves-effect waves-light btn light-blue"
      val divClass = "col s4"
      val disabled = if (history.bind.nonEmpty && gameWinner.bind.isEmpty) "" else " disabled"

      <div class="row">
        <div class={divClass}><button class={buttonClass} onclick={_: Event => goToMenu(())}>Menu</button></div>
        <div class={divClass}><button class={buttonClass} onclick={_: Event => reset()}>Reset</button></div>
        <div class={divClass}><button class={buttonClass + disabled} onclick={_: Event => handleBack()}>Back</button></div>
      </div>
      }
    </div>
  }
}

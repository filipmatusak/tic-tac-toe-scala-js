package tic_tac_toe

import com.thoughtworks.binding.Binding.Var
import com.thoughtworks.binding.dom
import org.scalajs.dom.raw.Event

object Game {

  case class GameParams(height: Int, width: Int, goal: Int)

  @dom def newGame(params: GameParams, goToMenu: Unit => Unit) = {
    import params._

    val gameWinner: Var[Option[Int]] = Var(None)
    val matrix: Vector[Vector[Var[Int]]] = Vector.fill(height)(Vector.fill(width)(Var(0)))
    val playerOnTurn = Var(0)
    val emptyCells = Var(height * width)
    val history: Var[List[Var[Int]]] = Var(Nil)

    def reset(): Unit = {
      for (row <- matrix; cell <- row) cell := 0
      playerOnTurn := 0
      emptyCells := params.height * params.width
      history := Nil
      gameWinner := None
    }

    def handleClick(value: Var[Int], playerOnTurn: Var[Int]): Unit = {
      if (value.get == 0 && gameWinner.get.isEmpty) {
        value.:=(playerOnTurn.get + 1)
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
      history.get.head := 0
      history := history.get.tail
      playerOnTurn.:=((playerOnTurn.get + 1) % 2)
    }

    //not optimal
    def findRow(): Boolean = {
      def find(startX: Int, startY: Int, dx: Int, dy: Int): Boolean = {
        var count = 1
        var last = matrix(startX)(startY).get
        var x = startX + dx
        var y = startY + dy
        while (x >= 0 && x < height && y >= 0 && y < width) {
          val v = matrix(x)(y).get
          if (v == last) {
            count += 1
            if (count >= goal && last > 0) return true
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
      </div>
      <div class="row">
        <div class="col s12">
          <table>
            {
            import scalaz.std.vector._
            matrix.map { row =>
              <tr>
                {row.map { value =>
                <td onclick={(_: Event) => handleClick(value, playerOnTurn)}>
                  {value.bind match {
                  case 0 => " "
                  case 1 => "X"
                  case 2 => "O"
                }}
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

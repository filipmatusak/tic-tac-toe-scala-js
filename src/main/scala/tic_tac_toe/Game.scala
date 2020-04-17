package tic_tac_toe

import com.thoughtworks.binding.Binding.{Var, Vars}
import com.thoughtworks.binding.dom
import org.scalajs.dom.raw.Event

object Game {

  case class GameParams(height: Int, width: Int, goal: Int)

  def reset(matrix: Vars[Vars[Var[Int]]], playerOnTurn: Var[Int]): Unit = {
    matrix.get.map(_.get.map(_.:=(0)))
    playerOnTurn := 0
  }

  @dom def newGame(params: GameParams, goToMenu: Unit => Unit) = {
    import params._

    val matrix: Vars[Vars[Var[Int]]] = Vars(Seq.fill(height)(Vars(Seq.fill(width)(Var(0)): _*)): _*)
    val playerOnTurn = Var(0)
    val emptyCells = Var(height * width)

    def handleClick(value: Var[Int], playerOnTurn: Var[Int]): Unit = {
      if (value.get == 0) {
        value.:=(playerOnTurn.get + 1)
        playerOnTurn.:=((playerOnTurn.get + 1) % 2)
        emptyCells.:=(emptyCells.get - 1)
        if (findRow() || emptyCells.get == 0) {
          reset(matrix, playerOnTurn)
        }
      }
    }

    //not optimal
    def findRow(): Boolean = {
      def find(startX: Int, startY: Int, dx: Int, dy: Int): Boolean = {
        var count = 1
        var last = matrix.get(startX).get(startY).get
        var x = startX + dx
        var y = startY + dy
        while (x >= 0 && x < height && y >= 0 && y < width) {
          val v = matrix.get(x).get(y).get
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

    <div>
      <p>Player on turn:
        {playerOnTurn.bind match {
        case 0 => "X"
        case 1 => "O"
      }}
      </p>
      <table>
        {matrix.map { row =>
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
      <button onclick={_: Event => goToMenu(())}>
        Menu
      </button>
      <button onclick={_: Event => reset(matrix, playerOnTurn)}>
        Reset
      </button>
    </div>
  }
}

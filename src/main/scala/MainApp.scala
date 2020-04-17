import scala.scalajs.js.JSApp
import org.scalajs.dom.document
import org.scalajs.dom.raw.{Event, HTMLInputElement}
import com.thoughtworks.binding.{Binding, dom}
import com.thoughtworks.binding.Binding.{Var, Vars}

import scala.xml.Elem

object MainApp extends JSApp {
  implicit def makeIntellijHappy(x: scala.xml.Node): Binding[org.scalajs.dom.raw.Node] = ???

  @dom def game(height: Int, width: Int, goal: Int) = {
    val matrix: Vars[Vars[Var[Int]]] = Vars(Seq.fill(height)(Vars(Seq.fill(width)(Var(0)): _*)): _*)
    val playerOnTurn = Var(0)
    val emptyCells = Var(height*width)

    def handleClick(value: Var[Int], playerOnTurn: Var[Int]): Unit = {
      if (value.get == 0) {
        value.:=(playerOnTurn.get + 1)
        playerOnTurn.:=((playerOnTurn.get + 1) % 2)
        emptyCells.:=(emptyCells.get - 1)
        if (findRow() || emptyCells.get == 0) {
          runNewGame()
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
    </div>
  }

  def main(): Unit = {
    runNewGame()
  }

  def runNewGame(): Unit = {
    dom.render(document.getElementById("mainContainer"), game(20, 20, 5))
  }
}

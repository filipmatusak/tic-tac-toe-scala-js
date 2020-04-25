package pages

import scalatags.Text.all._
import scalatags.Text.tags2.title
import playscalajs.html.scripts

object MainPage {
  val skeleton =
    html(
      head(
        title("Tic tac toe"),
        link(rel:="stylesheet",`type`:="text/css",href:=controllers.routes.Assets.versioned("stylesheets/materialize.min.css").url),
        link(rel:="stylesheet",`type`:="text/css",href:=controllers.routes.Assets.versioned("stylesheets/main.css").url),
        meta(name:="viewport",content:="width=device-width, initial-scale=1.0"),
        meta(charset:="UTF-8")
      ),
      body(
        div(id:="mainContainer"),
        raw(scripts("client").toString())
      )
    )
}
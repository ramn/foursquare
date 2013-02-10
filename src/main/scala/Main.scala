package se.ramn.tetris

import org.newdawn.slick.geom.{Rectangle, Ellipse}
import org.newdawn.slick.AppGameContainer
import org.newdawn.slick.BasicGame
import org.newdawn.slick.Color
import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Input
import org.newdawn.slick.Music
import org.newdawn.slick.fills.GradientFill

import scala.collection.mutable.ArrayBuffer

object Tetris {
  val Width = 800
  val Height = 600

  def main(args: Array[String]) {
    val app = new AppGameContainer(new Tetris)
    app.setDisplayMode(Width, Height, false)
    app.start()
  }
}


class Tetris extends BasicGame("Tetris") {
  import Tetris.{Width, Height}

  private val music = new Music("tetriscala.ogg")
  private val blockSize = 32

  private var entities = List[Entity]()
  private var time = 0
  private var lastMoveTime = 0


  def init(gc: GameContainer) {
    music.loop()
  }

  def update(gc: GameContainer, delta: Int) {
    time += delta
    val addNewEntity = math.random > 0.999

    if (time - lastMoveTime > 300) {
      lastMoveTime = time
      val movedEntities = entities map {
        case ball: Ball => ball.copy(y=ball.y + blockSize)
        case entity => entity
      }
      entities = movedEntities
    }
    entities = entities filterNot (entity => entity.y > Height - blockSize)

    if (addNewEntity) {
      val (x, y) = (util.Random.nextInt(Width-blockSize), 0)
      entities :+= Ball(x, y)
    }

    {
      val keyToColor = Map(
        Input.KEY_B -> Color.blue,
        Input.KEY_C -> Color.cyan,
        Input.KEY_G -> Color.green,
        Input.KEY_M -> Color.magenta,
        Input.KEY_O -> Color.orange,
        Input.KEY_P -> Color.pink,
        Input.KEY_R -> Color.red,
        Input.KEY_Y -> Color.yellow)
      val input = gc.getInput
      val pressedKeys = keyToColor.keys filter (input.isKeyDown)
      val colorsToDelete = keyToColor
        .filter { case (key, color) => input isKeyDown key }
        .map { case (key, color) => color }
        .toSet
      entities = entities filterNot (e => colorsToDelete.contains(e.color))
    }
  }

  def render(gc: GameContainer, g: Graphics) {
    g.setAntiAlias(true)
    for (entity <- entities) entity match {
      case Ball(x, y, color) =>
        g.setColor(color)
        g.fill(new Ellipse(x, y, blockSize, blockSize))
    }
  }
}

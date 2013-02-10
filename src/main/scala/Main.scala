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
  val GridCols = 15
  val GridRows = 25
  val BlockSize = 32

  def main(args: Array[String]) {
    val app = new AppGameContainer(new Tetris)
    app.setDisplayMode(Width, Height, false)
    app.start()
  }
}

sealed trait GameState
case object Ongoing extends GameState
case object GameOver extends GameState

class Tetris extends BasicGame("Tetris") {
  import Tetris.{Width, Height, GridCols, GridRows}

  private val music = new Music("tetriscala.ogg")
  private val gameOverMusic = new Music("11DieVonstantine.ogg")
  private val blockSize = Tetris.BlockSize
  private val fallSpeed = 200

  private var entities = List[Entity]()
  private var time = 0
  private var lastMoveTime = 0
  private var gameState: GameState = Ongoing


  def init(gc: GameContainer) {
    music.loop()
  }

  def update(gc: GameContainer, delta: Int) {
    time += delta

    if (endGameConditionReached) {
      enterGameState(GameOver)
    }
    else {
      if (blockShouldFall) {
        lastMoveTime = time
        entities = blocksMovedDown
      }
      entities = filterEntitiesOnPressedButtons(entities, gc.getInput)
      if (shouldAddNewBlock) {
        entities :+= newBlock
      }
    }
  }

  def render(gc: GameContainer, g: Graphics) {
    g.setAntiAlias(true)
    gameState match {
      case GameOver =>
        g.setColor(Color.red)
        g.setFont(buildFont)
        g.drawString("Game over!", Width/2-60, Height/2-30)
      case Ongoing =>
        for (entity <- entities) entity match {
          case Ball(x, y, color) =>
            g.setColor(color)
            g.fill(new Ellipse(x, y, blockSize, blockSize))
        }
        Block.createRandom(Width/2, 32).render(gc, g)
    }
  }




  private def blockShouldFall = time - lastMoveTime > fallSpeed

  private def blocksMovedDown = entities map {
    case ball: Ball => ball.copy(y=ball.y + blockSize)
    case entity => entity
  }

  private def newBlock = {
    val (x, y) = (util.Random.nextInt(Width-blockSize), 0)
    //entities :+= Ball(x, y)
    Ball(x, y)

    //Block.createRandom(gridX=GridCols/2, gridY=0)
  }

  private def shouldAddNewBlock = math.random > 0.999

  private def endGameConditionReached = {
    entities exists (entity => entity.y > Height - blockSize)
  }

  private def enterGameState(newGameState: GameState) {
    if (gameState != newGameState) {
      gameOverMusic.loop()
    }
    gameState = newGameState
  }

  private def filterEntitiesOnPressedButtons(
    entities: List[Entity],
    input: Input
  ): List[Entity] = {
    val keyToColor = Map(
      Input.KEY_B -> Color.blue,
      Input.KEY_C -> Color.cyan,
      Input.KEY_G -> Color.green,
      Input.KEY_M -> Color.magenta,
      Input.KEY_O -> Color.orange,
      Input.KEY_P -> Color.pink,
      Input.KEY_R -> Color.red,
      Input.KEY_Y -> Color.yellow)
    val colorsToDelete = keyToColor
      .filter { case (key, color) => input isKeyDown key }
      .map { case (key, color) => color }
      .toSet
    entities filterNot (e => colorsToDelete(e.color))
  }

  private def buildFont = {
    import java.awt.Font
    import org.newdawn.slick.UnicodeFont
    import org.newdawn.slick.font.effects.{Effect, ColorEffect}
    val font = new Font("Arial", Font.BOLD, 20)
    val uFont = new UnicodeFont(font)
    uFont.addAsciiGlyphs()
    val fontEffects = uFont.getEffects.asInstanceOf[java.util.List[Effect]]
    val fontEffect = new ColorEffect(java.awt.Color.WHITE)
    fontEffects.add(fontEffect)
    uFont.loadGlyphs()
    uFont
  }
}

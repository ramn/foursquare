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
  val GridCols = 10
  val GridRows = 20
  val BlockSize = 32
  val GridOffsetX = BlockSize * ((Width/BlockSize)/3)
  val GridOffsetY = BlockSize
  val FallSpeed = 200
  val InputReadInterval = 100

  def main(args: Array[String]) {
    val app = new AppGameContainer(new Tetris)
    app.setDisplayMode(Width, Height, false)
    app.start()
  }
}

class Tetris extends BasicGame("Tetris") {
  import Tetris.{Width, Height, GridCols, GridRows, BlockSize}
  import Tetris.{GridOffsetX, GridOffsetY}
  import Tetris.FallSpeed
  import Tetris.InputReadInterval

  private val music = new Music("tetriscala.ogg")
  private val gameOverMusic = new Music("11DieVonstantine.ogg")
  private val grid = new Grid

  private var time = 0
  private var lastMoveTime = 0
  private var lastInputReadTime = 0
  private var gameState: GameState = Ongoing
  private var block: Block = newBlock


  def init(gc: GameContainer) {
    music.loop()
  }

  def update(gc: GameContainer, delta: Int) {
    time += delta
    if (endGameConditionReached)
      enterGameState(GameOver)
    else
      updateBlock(gc)
  }

  def render(gc: GameContainer, g: Graphics) {
    g.setAntiAlias(true)
    gameState match {
      case GameOver =>
        g.setColor(Color.red)
        g.setFont(buildFont)
        g.drawString("Game over!", Width/2-60, Height/2-30)
      case Ongoing =>
        grid.render(gc, g)
        block.render(gc, g)
    }
  }


  private def updateBlock(gc: GameContainer) {
    if (blockHasLanded) {
      meldBlockWithGround()
      block = newBlock
    }
    else {
      if (shouldHandleInput)
        handleInput(gc.getInput)
      if (blockShouldFall)
        moveBlockDown()
    }
  }

  private def shouldHandleInput = time - lastInputReadTime > InputReadInterval

  private def handleInput(input: Input) {
    lastInputReadTime = time
    if (input.isKeyDown(Input.KEY_UP)) {
      block = block.rotateRight
    }
    if (input.isKeyDown(Input.KEY_LEFT)) {
      block = block.moveLeft
    }
    if (input.isKeyDown(Input.KEY_RIGHT)) {
      block = block.moveRight
    }
  }

  private def moveBlockDown() {
    lastMoveTime = time
    block = block.fall
  }

  private def blockHasLanded = {
    block.gridY >= (GridRows-1)
  }

  private def meldBlockWithGround() {
    grid.setFilled(block.gridX, block.gridY)
  }

  //private def coordinatesForBlock(block: Block): (Int, Int) = {
    //(block.gridX + GridOffsetX, block.gridY + GridOffsetY)
  //}

  private def blockShouldFall = time - lastMoveTime > FallSpeed

  private def newBlock = Block.createRandom(gridX=(GridCols/2)-1, gridY=0)

  private def endGameConditionReached = time > 100000

  private def enterGameState(newGameState: GameState) {
    if (gameState != newGameState) {
      gameOverMusic.loop()
    }
    gameState = newGameState
  }

  private def filterBlocksOnPressedButton(
    blocks: List[Block],
    input: Input
  ): List[Block] = {
    val keys = List(
      Input.KEY_T,
      Input.KEY_I,
      Input.KEY_L,
      Input.KEY_J,
      Input.KEY_S,
      Input.KEY_Z,
      Input.KEY_O)
    val keyIsPressed = keys.map { key => (key, input.isKeyDown(key)) }.toMap
    def shouldBeRemoved(block: Block) = block match {
      case b: T => keyIsPressed(Input.KEY_T)
      case b: I => keyIsPressed(Input.KEY_I)
      case b: L => keyIsPressed(Input.KEY_L)
      case b: J => keyIsPressed(Input.KEY_J)
      case b: S => keyIsPressed(Input.KEY_S)
      case b: Z => keyIsPressed(Input.KEY_Z)
      case b: O => keyIsPressed(Input.KEY_O)
    }
    blocks filterNot shouldBeRemoved
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

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


object Tetris {
  val BlockSize = 32
  val Width = 800
  val Height = BlockSize * 22
  val GridCols = 10
  val GridRows = 20
  val GridOffsetX = BlockSize * ((Width/BlockSize)/3)
  val GridOffsetY = BlockSize
  val FallSpeed = 200
  val InputReadInterval = 150

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
    gameState match {
      case Ongoing =>
        if (endGameConditionReached)
          enterGameState(GameOver)
        else
          updateBlock(gc)
      case GameOver =>
    }
  }

  def render(gc: GameContainer, g: Graphics) {
    g.setAntiAlias(true)
    gameState match {
      case GameOver =>
        g.setColor(Color.red)
        g.setFont(FontUtil.buildFont)
        g.drawString("Game over!", Width/2-60, Height/2-30)
      case Ongoing =>
        grid.render(g)
        block.render(g)
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
    grid.nukeCompleteRows
  }

  private def shouldHandleInput = time - lastInputReadTime > InputReadInterval

  private def handleInput(input: Input) {
    val isBlocked = (grid.isFilled _).tupled
    val rotate = () => { block = block.rotateRight(isBlocked) }
    val moveLeft = () => { block = block.moveLeft(isBlocked) }
    val moveRight = () => { block = block.moveRight(isBlocked) }
    val fall = () => { block = block.fall }

    val keyToAction = Map(
      Input.KEY_UP -> rotate,
      Input.KEY_LEFT -> moveLeft,
      Input.KEY_RIGHT -> moveRight,
      Input.KEY_DOWN -> fall
    )

    keyToAction foreach { case (key, action) =>
      if (input.isKeyDown(key)) {
        action()
        lastInputReadTime = time
      }
    }
  }

  private def moveBlockDown() {
    lastMoveTime = time
    block = block.fall
  }

  private def blockHasLanded = {
    type Pred = (Int, Int) => Boolean
    val touchingBottomPredicate: Pred = (x, y) => y == GridRows-1
    val touchingFilledCell: Pred = (x, y) => grid.isFilled(x, y) || grid.isFilled(x, y+1)
    block.gridPiecePositions exists { case (x, y) =>
      List(touchingBottomPredicate, touchingFilledCell) exists (_.apply(x, y))
    }
  }

  private def meldBlockWithGround() {
    block.gridPiecePositions foreach { case (x, y) =>
      grid.setFilled(x, y)
    }
    grid.setFilled(block.gridX, block.gridY)
  }

  private def blockShouldFall = time - lastMoveTime > FallSpeed

  private def newBlock = {
    val initGridX = (GridCols/2)-1
    val candidateBlock = Block.createRandom(gridX=initGridX, gridY=0)
    if (grid.anyIsFilled(candidateBlock.gridPiecePositions))
      enterGameState(GameOver)
    candidateBlock
  }

  private def endGameConditionReached = {
    time > 100000
  }

  private def enterGameState(newGameState: GameState) {
    if (gameState != newGameState) {
      gameOverMusic.loop()
    }
    gameState = newGameState
  }
}

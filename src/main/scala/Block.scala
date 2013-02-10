package se.ramn.tetris

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.fills.GradientFill
import org.newdawn.slick.geom.Rectangle

import Tetris.BlockSize
import Tetris.{GridOffsetX, GridOffsetY}
import Tetris.GridCols

sealed trait Block {
  type Rotations = IndexedSeq[IndexedSeq[(Int, Int)]]

  def gridX: Int

  def gridY: Int

  def fall: Block

  def render(gc: GameContainer, g: Graphics) {
    for ((x, y) <- absolutePiecePositions)
      g.fill(new Rectangle(x, y, BlockSize, BlockSize), gradientFill)
  }

  def rotateLeft: Block = rotate(-1)

  def rotateRight: Block = rotate(1)

  def moveLeft: Block = getMovedIfInside(tryMoveLeft)

  def moveRight: Block = getMovedIfInside(tryMoveRight)

  def gridPiecePositions =
    for {
      (x, y) <- rotation
      newGridX = (x + gridX)
      newGridY = (y + gridY)
    } yield (newGridX, newGridY)



  /// Protected methods ///////////////////////////////

  protected def getMovedIfInside(candidate: Block) =
    if (candidate.isInsideGrid)
      candidate
    else
      this

  protected def tryMoveLeft: Block

  protected def tryMoveRight: Block

  protected def rotate(direction: Int): Block = {
    require(direction == 1 || direction == -1)
    val newRotationNr = {
      val candidate = (currentRotationNr + direction) % 4
      if (candidate < 0) 4 else candidate
    }
    withNewRotation(newRotationNr)
  }

  protected def withNewRotation(newRotationNr: Int): Block

  protected val currentRotationNr: Int

  protected def absolutePiecePositions =
    for {
      (x, y) <- gridPiecePositions
      newGridX = x * BlockSize
      newGridY = y * BlockSize
    } yield (newGridX + GridOffsetX, newGridY + GridOffsetY)

  protected def isInsideGrid = {
    val (leftX, leftY) = leftmostPieceCoord
    val (rightX, rightY) = rightmostPieceCoord
    leftX >= 0 && rightX <= GridCols-1
  }

  protected def leftmostPieceCoord: (Int, Int) = {
    val (x, y) = rotation.minBy { case (x, y) => x }
    coordForPiece(x, y)
  }

  protected def rightmostPieceCoord: (Int, Int) = {
    val (x, y) = rotation.maxBy { case (x, y) => x }
    coordForPiece(x, y)
  }

  protected def coordForPiece(x: Int, y: Int) = (x + gridX, y + gridY)

  protected val rotation = {
    require((0 to 3) contains currentRotationNr)
    possibleRotations(currentRotationNr)
  }

  protected def possibleRotations: Rotations

  protected def gradientFill: GradientFill

  protected def buildGradientFill(startCol: Color, endCol: Color) =
    new GradientFill(0, 0, startCol, 16, 16, endCol, true)
}



case class T(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
  protected def possibleRotations: Rotations = Blocks.T
  protected def gradientFill = buildGradientFill(Color.red, Color.darkGray)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}

case class I(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.I
  protected def gradientFill = buildGradientFill(Color.orange, Color.white)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}

case class L(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.L
  protected def gradientFill = buildGradientFill(Color.yellow, Color.black)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}

case class J(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.J
  protected def gradientFill = buildGradientFill(Color.yellow, Color.black)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}

case class S(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.S
  protected def gradientFill = buildGradientFill(Color.cyan, Color.black)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}

case class Z(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.Z
  protected def gradientFill = buildGradientFill(Color.cyan, Color.black)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}

case class O(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.O
  protected def gradientFill = buildGradientFill(Color.green, Color.white)
  protected def withNewRotation(newRotationNr: Int) = this.copy(currentRotationNr=newRotationNr)
  def fall: Block = this.copy(gridY=gridY + 1)
  def tryMoveLeft = this.copy(gridX=gridX - 1)
  def tryMoveRight = this.copy(gridX=gridX + 1)
}


object Block {
  def createRandom(gridX: Int, gridY: Int): Block = {
    val length = blockConstructors.length
    val blockConstructor = blockConstructors(util.Random.nextInt(length))
    blockConstructor(gridX, gridY, 0)
  }

  private val blockConstructors = List(T, I, L, J, S, Z, O)
}


object Blocks {

  val T = IndexedSeq(
    IndexedSeq((0, 0), (-1, 0), (1, 0), (0, -1)),
    IndexedSeq((0, 0), (0, 1), (0, -1), (-1, 0)),
    IndexedSeq((0, 0), (1, 0), (-1, 0), (0, 1)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (1, 0)))

  val S = IndexedSeq(
    IndexedSeq((0, 0), (1, 0), (0, -1), (-1, -1)),
    IndexedSeq((0, 0), (0, 1), (1, 0), (1, -1)),
    IndexedSeq((0, 0), (1, 0), (0, -1), (-1, -1)),
    IndexedSeq((0, 0), (0, 1), (1, 0), (1, -1)))

  val Z = IndexedSeq(
    IndexedSeq((0, 0), (-1, 0), (0, -1), (1, -1)),
    IndexedSeq((0, 0), (0, 1), (-1, 0), (-1, -1)),
    IndexedSeq((0, 0), (-1, 0), (0, -1), (1, -1)),
    IndexedSeq((0, 0), (0, 1), (-1, 0), (-1, -1)))

  val O = IndexedSeq(
    IndexedSeq((0, 0), (1, 0), (0, -1), (1, -1)),
    IndexedSeq((0, 0), (1, 0), (0, -1), (1, -1)),
    IndexedSeq((0, 0), (1, 0), (0, -1), (1, -1)),
    IndexedSeq((0, 0), (1, 0), (0, -1), (1, -1)))

  val I = IndexedSeq(
    IndexedSeq((0, 0), (-1, 0), (1, 0), (2, 0)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (0, 2)),
    IndexedSeq((0, 0), (-1, 0), (1, 0), (2, 0)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (0, 2)))

  val J = IndexedSeq(
    IndexedSeq((0, 0), (1, 0), (-1, 0), (-1, -1)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (-1, 1)),
    IndexedSeq((0, 0), (-1, 0), (1, 0), (1, 1)),
    IndexedSeq((0, 0), (0, 1), (0, -1), (1, -1)))

  val L = IndexedSeq(
    IndexedSeq((0, 0), (-1, 0), (1, 0), (1, -1)),
    IndexedSeq((0, 0), (0, 1), (0, -1), (-1, -1)),
    IndexedSeq((0, 0), (1, 0), (-1, 0), (-1, 1)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (1, 1)))
}

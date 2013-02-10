package se.ramn.tetris

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.fills.GradientFill
import org.newdawn.slick.geom.Rectangle

sealed trait Block {
  import Tetris.BlockSize

  type Rotations = IndexedSeq[IndexedSeq[(Int, Int)]]

  def gridX: Int

  def gridY: Int

  def render(gc: GameContainer, g: Graphics) {
    for ((x, y) <- absolutePiecePositions)
      g.fill(new Rectangle(x, y, BlockSize, BlockSize), gradientFill)
  }

  def rotateLeft {

  }

  def rotateRight {

  }

  protected val currentRotationNr: Int

  protected def absolutePiecePositions = {
    for ((x, y) <- rotation)
      yield (x * BlockSize + gridX, y * BlockSize + gridY)
  }

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
}

case class I(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.I
  protected def gradientFill = buildGradientFill(Color.orange, Color.white)
}

case class L(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.L
  protected def gradientFill = buildGradientFill(Color.yellow, Color.black)
}

case class J(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.J
  protected def gradientFill = buildGradientFill(Color.yellow, Color.black)
}

case class S(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.S
  protected def gradientFill = buildGradientFill(Color.cyan, Color.black)
}

case class Z(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.Z
  protected def gradientFill = buildGradientFill(Color.cyan, Color.black)
}

case class O(gridX: Int, gridY: Int, currentRotationNr: Int=0) extends Block {
	protected def possibleRotations: Rotations = Blocks.O
  protected def gradientFill = buildGradientFill(Color.green, Color.white)
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

  val L = IndexedSeq(
    IndexedSeq((0, 0), (1, 0), (-1, 0), (-1, -1)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (-1, 1)),
    IndexedSeq((0, 0), (-1, 0), (1, 0), (1, 1)),
    IndexedSeq((0, 0), (0, 1), (0, -1), (1, -1)))

  val J = IndexedSeq(
    IndexedSeq((0, 0), (-1, 0), (1, 0), (1, -1)),
    IndexedSeq((0, 0), (0, 1), (0, -1), (-1, -1)),
    IndexedSeq((0, 0), (1, 0), (-1, 0), (-1, 1)),
    IndexedSeq((0, 0), (0, -1), (0, 1), (1, 1)))
}

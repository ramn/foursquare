package se.ramn.tetris

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.fills.GradientFill
import org.newdawn.slick.geom.Rectangle

import Tetris.BlockSize
import Tetris.{GridCols, GridRows}
import scala.collection.mutable

class Grid {
  private val grid = mutable.IndexedSeq.fill(GridRows, GridCols)(0)

  def render(gc: GameContainer, g: Graphics) {
    for {
      (row, rowIx) <- grid.zipWithIndex
      (cell, colIx) <- row.zipWithIndex
      if cell == 1
      (x, y) = absoluteCoordinate(colIx, rowIx)
    } {
      g.setColor(Color.white)
      g.fill(new Rectangle(x, y-(BlockSize*2), BlockSize, BlockSize))
    }
  }

  def setFilled(col: Int, row: Int) {
    grid(row)(col) = 1
  }

  private def absoluteCoordinate(x: Int, y: Int) = {
    import Tetris.{GridOffsetX, GridOffsetY}
    (x * BlockSize + GridOffsetX, y * BlockSize + GridOffsetY)
  }
}

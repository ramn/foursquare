package se.ramn.tetris

import scala.collection.mutable

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.fills.GradientFill
import org.newdawn.slick.geom.Rectangle

import Tetris.BlockSize
import Tetris.{GridCols, GridRows}
import Tetris.{GridOffsetX, GridOffsetY}

class Grid {
  private val grid = mutable.IndexedSeq.fill(GridRows, GridCols)(0)

  def render(gc: GameContainer, g: Graphics) {
    renderBorder(g)
    renderFilledCells(g)
    renderRowNums(g)
  }

  def setFilled(col: Int, row: Int) {
    var outOfBounds = false
    if (grid.isDefinedAt(row)) {
      if (grid(row).isDefinedAt(col)) {
        grid(row)(col) = 1
      } else {
        outOfBounds = true
      }
    } else {
      outOfBounds = true
    }
    if (outOfBounds) {
      val msg = "Trying to access coords outside of grid, col: %s, row: %s".format(col, row)
      System.err.println(msg)
    }
  }

  private def renderBorder(g: Graphics) {
    val width = GridCols * BlockSize
    val height = GridRows * BlockSize
    val rect = new Rectangle(GridOffsetX, GridOffsetY, width, height)
    g.setColor(Color.orange)
    g.draw(rect)
  }

  private def renderFilledCells(g: Graphics) {
    for {
      (row, rowIx) <- grid.zipWithIndex
      (cell, colIx) <- row.zipWithIndex
      if cell == 1
      (x, y) = absoluteCoordinate(colIx, rowIx)
    } {
      g.setColor(Color.white)
      g.fill(new Rectangle(x, y, BlockSize, BlockSize))
    }
  }

  private def renderRowNums(g: Graphics) {
    for {
      (_, rowIx) <- grid.zipWithIndex
      (x, y) = absoluteCoordinate(0, rowIx)
    } {
      g.setColor(Color.blue)
      g.drawString(rowIx.toString, x, y)
    }
  }

  private def absoluteCoordinate(x: Int, y: Int) = {
    import Tetris.{GridOffsetX, GridOffsetY}
    (x * BlockSize + GridOffsetX, y * BlockSize + GridOffsetY)
  }
}

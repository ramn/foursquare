package se.ramn.foursquare

import scala.collection.mutable

import org.newdawn.slick.GameContainer
import org.newdawn.slick.Graphics
import org.newdawn.slick.Color
import org.newdawn.slick.fills.GradientFill
import org.newdawn.slick.geom.Rectangle

import FourSquare.BlockSize
import FourSquare.{GridCols, GridRows}
import FourSquare.{GridOffsetX, GridOffsetY}

class Grid {
  private var grid = mutable.IndexedSeq.fill(GridRows, GridCols)(0)

  def render(g: Graphics) {
    renderBorder(g)
    renderFilledCells(g)
  }

  def setFilled(col: Int, row: Int) {
    if (withinBounds(col=col, row=row))
      grid(row)(col) = 1
    else {
      val msg = "Trying to fill coords outside of grid, col: %s, row: %s"
        .format(col, row)
      System.err.println(msg)
    }
  }

  def isFilled(col: Int, row: Int) =
    if (withinBounds(col=col, row=row))
      grid(row)(col) == 1
    else
      false

  def anyIsFilled(coordinates: Seq[(Int, Int)]) =
    coordinates exists { case (x, y) => isFilled(x, y) }

  def nukeCompleteRows = {
    def setRowToZero(rowIx: Int) {
      grid(rowIx) = grid(rowIx) map (x => 0)
    }

    while (hasCompleteRows) {
      val completeRowIx = grid lastIndexWhere rowIsCompletelyFilled
      setRowToZero(completeRowIx)
      val rowsToShiftDown = grid.slice(0, completeRowIx)
      val rowsToPatchLength = rowsToShiftDown.length
      grid = grid.patch(1, rowsToShiftDown, rowsToPatchLength)
      setRowToZero(0)
    }
  }


  private def hasCompleteRows = grid exists rowIsCompletelyFilled

  private def rowIsCompletelyFilled(row: IndexedSeq[Int]) = row forall (_ == 1)

  private def withinBounds(col: Int, row: Int) =
    grid.isDefinedAt(row) && grid(row).isDefinedAt(col)

  private def renderBorder(g: Graphics) {
    val width = GridCols * BlockSize
    val height = GridRows * BlockSize
    val rect = new Rectangle(GridOffsetX, GridOffsetY, width, height)
    g.setColor(Color.orange)
    g.draw(rect)
  }

  private def renderFilledCells(g: Graphics) {
    val gradient =
      new GradientFill(0, 0, Color.lightGray, 16, 16, Color.white, true)

    for {
      (row, rowIx) <- grid.zipWithIndex
      (cell, colIx) <- row.zipWithIndex
      if cell == 1
      (x, y) = absoluteCoordinate(colIx, rowIx)
    } {
      g.setColor(Color.white)
      g.fill(new Rectangle(x, y, BlockSize, BlockSize), gradient)
    }
  }

  private def absoluteCoordinate(x: Int, y: Int) = {
    import FourSquare.{GridOffsetX, GridOffsetY}
    (x * BlockSize + GridOffsetX, y * BlockSize + GridOffsetY)
  }
}

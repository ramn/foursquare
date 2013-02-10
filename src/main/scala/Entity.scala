package se.ramn.tetris

import org.newdawn.slick.Color

trait Entity {
  def x: Int
  def y: Int
  def color: Color
}

object RandomColor {  
  def get = {
    import Color._
    val colors = List(red, blue, cyan, magenta, green, orange, pink, yellow)
    val length = colors.length
    colors(util.Random.nextInt(length))
  }
}

case class Ball(x: Int, y: Int, color: Color=RandomColor.get) extends Entity

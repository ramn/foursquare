package se.ramn.tetris

import org.newdawn.slick.geom.Rectangle
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
  def main(args: Array[String]) {
    val tetrisGame = new Tetris
    val app = new AppGameContainer(tetrisGame)
    app.setDisplayMode(800, 600, false)
    app.start()
  }
}

class Tetris extends BasicGame("Tetris") {
  private val music = new Music("tetriscala.ogg")

  def init(gc: GameContainer) {
    music.loop()
  }

  def update(gc: GameContainer, delta: Int) {
  }

  def render(gc: GameContainer, g: Graphics) {
  }
}

package se.ramn.tetris

sealed trait GameState
case object Ongoing extends GameState
case object GameOver extends GameState

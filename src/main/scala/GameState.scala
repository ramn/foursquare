package se.ramn.foursquare

sealed trait GameState
case object Ongoing extends GameState
case object GameOver extends GameState

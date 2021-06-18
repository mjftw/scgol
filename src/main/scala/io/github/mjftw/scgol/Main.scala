package io.github.mjftw.scgol

import scala.annotation.tailrec

object Main {
  def main(args: Array[String]): Unit = {
    val boardSize = 50
    val aliveProbability = 0.25
    val turnPeriodMs = 300

    val start = Game.random(boardSize, boardSize, aliveProbability)

    @tailrec
    def loop(game: Game, delayMs: Int): Game = {
      println(game.board.asString())
      Thread.sleep(delayMs)
      loop(game.advance, delayMs)
    }

    loop(start, turnPeriodMs)

    ()
  }
}

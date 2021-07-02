package io.github.mjftw.scgol

import scala.concurrent.duration._
import cats.effect.IOApp
import cats.effect.{ExitCode, IO}

object Main extends IOApp {
  def runGame(game: Game, view: View[IO], advanceEvery: FiniteDuration): IO[Unit] =
    for {
      nextGame <- IO(game.advance)
      _ <- view.update(game)
      _ <- IO.sleep(advanceEvery)
      _ <- runGame(nextGame, view, advanceEvery)
    } yield ()

  def run(args: List[String]): IO[ExitCode] = {
    val boardSize = 50
    val aliveProbability = 0.5
    val turnLength = 300.millis

    for {
      model <- IO.pure(Game.random(boardSize, boardSize, aliveProbability))
      view <- IO.delay(ConsoleView[IO])
      _ <- view.init(model)
      _ <- runGame(model, view, turnLength)
    } yield ExitCode.Success
  }
}

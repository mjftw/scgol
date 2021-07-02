package io.github.mjftw.scgol

import scala.concurrent.duration._
import cats.effect.IOApp
import cats.effect.{ExitCode, IO}
import pureconfig.ConfigSource
import pureconfig.generic.auto._
import cats.data.EitherT
import cats.implicits._

case class GameConfig(boardSize: Int, aliveProbability: Float, turnLength: FiniteDuration)

object Main extends IOApp {
  def runGame(
      game: Game,
      view: View[IO],
      advanceEvery: FiniteDuration
  ): IO[Either[Throwable, Unit]] =
    (for {
      nextGame <- EitherT(IO(game.advance.asRight))
      _ <- EitherT(view.update(game))
      _ <- EitherT(IO.sleep(advanceEvery).map(_.asRight))
      _ <- EitherT(runGame(nextGame, view, advanceEvery))
    } yield ()).value

  def run(args: List[String]): IO[ExitCode] = {
    val program: IO[Either[Throwable, Unit]] = (for {
      config <- EitherT(
        IO(ConfigSource.default.load[GameConfig].leftMap(e => new RuntimeException(e.toString())))
      )
      model <- EitherT(
        IO(
          Game
            .random(config.boardSize, config.boardSize, config.aliveProbability)
            .asRight[Throwable]
        )
      )
      view <- EitherT(IO(ConsoleView[IO].asRight[Throwable]))
      _ <- EitherT(view.init(model))
      _ <- EitherT(runGame(model, view, config.turnLength))
    } yield ()).value

    program.flatMap {
      case Right(_)    => IO(ExitCode.Success)
      case Left(error) => IO(println(error.getMessage)).as(ExitCode.Error)
    }
  }
}

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
  val program: IO[Either[Throwable, Unit]] = (for {
    config <- EitherT(
      IO(ConfigSource.default.load[GameConfig].leftMap(e => new RuntimeException(e.toString())))
    )
    model <- EitherT(
      IO(Game.random(config.boardSize, config.boardSize, config.aliveProbability).asRight)
    )
    view <- EitherT.pure[IO, Throwable](ConsoleView[IO])
    controller <- EitherT.pure[IO, Throwable](Controller.make)
    _ <- EitherT(view.init(model))
    _ <- EitherT(controller.run(model, view, config.turnLength))
  } yield ()).value

  def run(args: List[String]): IO[ExitCode] =
    program.flatMap {
      case Right(_)    => IO(ExitCode.Success)
      case Left(error) => IO(println(error.getMessage)).as(ExitCode.Error)
    }

}

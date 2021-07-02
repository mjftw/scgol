package io.github.mjftw.scgol

import scala.concurrent.duration.FiniteDuration
import cats.effect.IO
import cats.data.EitherT
import cats.implicits._

trait Controller {
  def run(game: Game, view: View[IO], turnLength: FiniteDuration): IO[Either[Throwable, Unit]]
}

object Controller {
  def make = new Controller {
    def run(
        game: Game,
        view: View[IO],
        advanceEvery: FiniteDuration
    ): IO[Either[Throwable, Unit]] =
      (for {
        nextGame <- EitherT.pure[IO, Throwable](game.advance)
        _ <- EitherT(view.update(game))
        _ <- EitherT(IO.sleep(advanceEvery).map(_.asRight))
        _ <- EitherT(run(nextGame, view, advanceEvery))
      } yield ()).value
  }
}

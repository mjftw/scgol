package io.github.mjftw.scgol

import cats.effect.kernel.Sync
import cats.implicits._

trait View[F[_]] {
  def init(game: Game): F[Unit]
  def update(game: Game): F[Unit]
}

object ConsoleView {
  def apply[F[_]: Sync]: View[F] = new View[F] {
    def init(game: Game): F[Unit] =
      Sync[F].delay(println("Starting Conway's Game of Life!"))

    def update(game: Game): F[Unit] =
      for {
        boardStr <- Sync[F].delay(game.board.asString())
        _ <- Sync[F].delay(print(("\r" * boardStr.length) + boardStr))
      } yield ()
  }
}

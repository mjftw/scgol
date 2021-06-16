package io.github.mjftw.scgol

import cats.effect.IOApp
import cats.effect.{ExitCode, IO}

object Main extends IOApp {
  def run(args: List[String]): IO[ExitCode] =
    for {
      _ <- IO.println("Hello World!")
    } yield ExitCode.Success
}

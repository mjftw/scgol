package io.github.mjftw.scgol

import cats.implicits._

import io.github.mjftw.scgol.LifeState._

case class Dimension(value: Int)
case class Coordinate(x: Int, y: Int)
case class Grid(
    values: Map[Coordinate, LifeState],
    height: Dimension,
    width: Dimension
)

object Grid {
  def make(width: Int, height: Int, isAlive: LifeState = Dead): Grid = {

    val values = (for {
      x <- List.range(0, width)
      y <- List.range(0, height)
    } yield (x, y)).foldLeft(Map.empty[Coordinate, LifeState])((m, xy) =>
      m + (Coordinate.tupled(xy) -> isAlive)
    )

    Grid(
      values,
      Dimension(width),
      Dimension(height)
    )
  }

}

package io.github.mjftw.scgol

import cats.implicits._

import io.github.mjftw.scgol.LifeState._

case class Dimension(value: Int)
case class Location(x: Int, y: Int)

case class Grid(
    values: Map[Location, LifeState],
    height: Dimension,
    width: Dimension
)

object Grid {
  def make(width: Int, height: Int, isAlive: LifeState = Dead): Grid = {

    val values = (for {
      x <- List.range(0, width)
      y <- List.range(0, height)
    } yield (x, y)).foldLeft(Map.empty[Location, LifeState])((m, xy) =>
      m + (Location.tupled(xy) -> isAlive)
    )

    Grid(
      values,
      Dimension(width),
      Dimension(height)
    )
  }
  implicit class LocationOps(xy: Location) {
    def to(endXY: Location): List[Location] =
      for {
        xs <- List.range(xy.x, endXY.x + 1)
        ys <- List.range(xy.y, endXY.y + 1)
      } yield Location(xs, ys)
  }

  implicit class GridOps(grid: Grid) {
    def minX: Int = 0
    def minY: Int = 0
    def maxX: Int = grid.width.value - 1
    def maxY: Int = grid.height.value - 1

    def get(xy: Location): LifeState = grid.values(xy)

    def set(xy: Location, lifeState: LifeState): Grid =
      grid.copy(values = grid.values + (xy -> lifeState))

    def asString(aliveChar: String = "■", deadChar: String = "☐"): String =
      "\n" + (for {
        x <- List.range(0, grid.width.value)
        y <- List.range(0, grid.height.value)
        separator = if (y == grid.maxY) "\n" else ""
        cellChar = if (grid.get(Location(y, x)) == Alive) aliveChar else deadChar
      } yield (cellChar + " " + separator)).mkString("")

    def livingInRange(fromXY: Location, toXY: Location): Int =
      (fromXY to toXY)
        .map(xy => grid.values(xy))
        .filter(_ == Alive)
        .length

    def livingNeighborsAt(xy: Location): Int = {
      val (fromXY, toXY) = xy match {
        // Corners
        case Location(x, y) if x <= minX && y <= minY => (Location(x, y), Location(x + 1, y + 1))
        case Location(x, y) if x <= minX && y >= maxY => (Location(x, y - 1), Location(x + 1, y))
        case Location(x, y) if x >= maxX && y <= minY => (Location(x - 1, y), Location(x, y + 1))
        case Location(x, y) if x >= maxX && y >= maxY => (Location(x - 1, y - 1), Location(x, y))

        // Edges
        case Location(x, y) if x <= minX => (Location(x, y - 1), Location(x + 1, y + 1))
        case Location(x, y) if y <= minY => (Location(x - 1, y), Location(x + 1, y + 1))
        case Location(x, y) if x >= maxX => (Location(x - 1, y - 1), Location(x, y + 1))
        case Location(x, y) if y >= maxY => (Location(x - 1, y - 1), Location(x + 1, y))

        // Middle
        case Location(x, y) => (Location(x - 1, y - 1), Location(x + 1, y + 1))
      }

      // Do not include the cell at xy in the count - it's not a neighbor
      val thisCell = if (grid.get(xy) == Alive) 1 else 0

      livingInRange(fromXY, toXY) - thisCell

    }
  }

}

package io.github.mjftw.scgol

import cats.implicits._

case class Height(value: Int)
case class Width(value: Int)

case class Grid(
    cells: Map[Location, LifeState],
    height: Height,
    width: Width
)

object Grid {
  def make(width: Int, height: Int, isAlive: LifeState = Dead): Grid = {

    val values = (for {
      x <- List.range(0, width)
      y <- List.range(0, height)
    } yield Location(x, y)).foldLeft(Map.empty[Location, LifeState])((m, xy) => m + (xy -> isAlive))

    Grid(
      values,
      Height(width),
      Width(height)
    )
  }

  def random(width: Int, height: Int, aliveProbability: Double = 0.5): Grid = {
    val values = (for {
      x <- List.range(0, width)
      y <- List.range(0, height)
      isAlive = if (math.random() <= aliveProbability) Alive else Dead
    } yield (Location(x, y) -> isAlive)).foldLeft(Map.empty[Location, LifeState])((m, xyAlive) =>
      m + xyAlive
    )

    Grid(
      values,
      Height(width),
      Width(height)
    )
  }

  implicit class GridOps(grid: Grid) {
    def minX: Int = 0
    def minY: Int = 0
    def maxX: Int = grid.width.value - 1
    def maxY: Int = grid.height.value - 1

    def get(xy: Location): LifeState = grid.cells(xy)

    def set(xy: Location, lifeState: LifeState): Grid =
      grid.copy(cells = grid.cells + (xy -> lifeState))

    def asString(aliveChar: String = "■", deadChar: String = "☐"): String =
      "\n" + (for {
        x <- List.range(0, grid.width.value)
        y <- List.range(0, grid.height.value)
        separator = if (y == grid.maxY) "\n" else ""
        cellChar = if (grid.get(Location(y, x)) == Alive) aliveChar else deadChar
      } yield (cellChar + " " + separator)).mkString("")

    def livingInRange(fromXY: Location, toXY: Location): Int =
      (fromXY to toXY)
        .map(xy => grid.cells(xy))
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

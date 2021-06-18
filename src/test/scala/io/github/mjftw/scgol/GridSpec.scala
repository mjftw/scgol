package io.mjftw.github.scgol

import org.scalatest.flatspec.AnyFlatSpec
import io.github.mjftw.scgol.Grid
import io.github.mjftw.scgol.Grid._
import org.scalatest.matchers.should.Matchers
import io.github.mjftw.scgol.{Alive, Dead}
import io.github.mjftw.scgol.Location
import io.github.mjftw.scgol.Height
import io.github.mjftw.scgol.Width

class GridSpec extends AnyFlatSpec with Matchers {
  "make" should "create a new grid" in {
    Grid.make(3, 2, Dead) should be(
      Grid(
        Map(
          Location(0, 0) -> Dead,
          Location(0, 1) -> Dead,
          Location(1, 0) -> Dead,
          Location(1, 1) -> Dead,
          Location(2, 0) -> Dead,
          Location(2, 1) -> Dead
        ),
        Height(3),
        Width(2)
      )
    )
  }

  "gridNeighborsAt" should "give an accurate count away from edges with living cell" in {
    val grid =
      (Location(2, 2) to Location(3, 4)).foldLeft {
        Grid.make(9, 9, Dead)
      } { (grid, xy) =>
        grid.set(xy, Alive)
      }

    grid.livingNeighborsAt(Location(3, 3)) should be(5)
  }

  it should "give an accurate count away from edges with dead cell" in {
    val grid =
      (Location(2, 2) to Location(3, 4))
        .foldLeft {
          Grid.make(9, 9, Dead)
        } { (grid, xy) =>
          grid.set(xy, Alive)
        }
        .set(Location(3, 3), Dead)

    grid.livingNeighborsAt(Location(3, 3)) should be(5)
  }

  it should "give an accurate count on left edge" in {
    val grid =
      (Location(0, 3) to Location(2, 7))
        .foldLeft {
          Grid.make(9, 9, Dead)
        } { (grid, xy) =>
          grid.set(xy, Alive)
        }

    grid.livingNeighborsAt(Location(0, 5)) should be(5)
  }

  it should "give an accurate count on right edge" in {
    val grid =
      (Location(6, 3) to Location(8, 7))
        .foldLeft {
          Grid.make(9, 9, Dead)
        } { (grid, xy) =>
          grid.set(xy, Alive)
        }
        .set(Location(8, 3), Dead)

    grid.livingNeighborsAt(Location(8, 3)) should be(3)
  }

  it should "give an accurate count on top edge" in {
    val grid = Grid
      .make(9, 9, Alive)
      .set(Location(5, 0), Dead)

    grid.livingNeighborsAt(Location(5, 0)) should be(5)
  }

  it should "give an accurate count on bottom edge" in {
    val grid = Grid
      .make(9, 9, Alive)
      .set(Location(5, 8), Dead)

    grid.livingNeighborsAt(Location(5, 8)) should be(5)
  }

  it should "give an accurate count on top left corner" in {
    val grid = Grid.make(9, 9, Alive)

    grid.livingNeighborsAt(Location(0, 0)) should be(3)
  }

  it should "give an accurate count on top right corner" in {
    val grid = Grid.make(9, 9, Alive)

    grid.livingNeighborsAt(Location(8, 0)) should be(3)
  }

  it should "give an accurate count on bottom left corner" in {
    val grid = Grid.make(9, 9, Alive)

    grid.livingNeighborsAt(Location(0, 8)) should be(3)
  }

  it should "give an accurate count on bottom right corner" in {
    val grid = Grid.make(9, 9, Alive)

    grid.livingNeighborsAt(Location(8, 8)) should be(3)
  }
}

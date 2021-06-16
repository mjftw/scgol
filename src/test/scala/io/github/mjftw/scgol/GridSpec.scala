package io.mjftw.github.scgol

import org.scalatest.flatspec.AnyFlatSpec
import io.github.mjftw.scgol.Grid
import org.scalatest.matchers.should.Matchers
import io.github.mjftw.scgol.Dimension
import io.github.mjftw.scgol.Dead
import io.github.mjftw.scgol.Coordinate

class GridSpec extends AnyFlatSpec with Matchers {
  "make" should "create a new grid" in {
    Grid.make(3, 2, Dead) should be(
      Grid(
        Map(
          Coordinate(0, 0) -> Dead,
          Coordinate(0, 1) -> Dead,
          Coordinate(1, 0) -> Dead,
          Coordinate(1, 1) -> Dead,
          Coordinate(2, 0) -> Dead,
          Coordinate(2, 1) -> Dead
        ),
        Dimension(3),
        Dimension(2)
      )
    )
  }
}

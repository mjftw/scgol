package io.github.mjftw.scgol

case class Location(x: Int, y: Int)

object Location {
  implicit class LocationOps(xy: Location) {
    def to(endXY: Location): List[Location] =
      for {
        xs <- List.range(xy.x, endXY.x + 1)
        ys <- List.range(xy.y, endXY.y + 1)
      } yield Location(xs, ys)
  }
}

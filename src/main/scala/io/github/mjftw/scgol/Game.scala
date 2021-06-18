package io.github.mjftw.scgol

case class Game(board: Grid)

object Game {
  def random(width: Int, height: Int, aliveProbability: Double = 0.5): Game =
    Game(Grid.random(width, height, aliveProbability))

  private def cellNextState(lifeState: LifeState, livingNeighbors: Int): LifeState =
    lifeState match {
      case Alive if livingNeighbors == 2 || livingNeighbors == 3 => Alive
      case Dead if livingNeighbors == 3                          => Alive
      case _                                                     => Dead
    }

  implicit class GameOps(game: Game) {
    def advance: Game = {
      val cells = game.board.cells.transform { case ((xy, lifeState)) =>
        cellNextState(lifeState, game.board.livingNeighborsAt(xy))
      }

      Game(game.board.copy(cells = cells))
    }
  }
}

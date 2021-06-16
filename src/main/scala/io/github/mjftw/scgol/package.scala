package io.github.mjftw.scgol

sealed trait LifeState
case object Dead extends LifeState
case object Alive extends LifeState

object LifeState {
  implicit class lifeStateOps(lifeState: LifeState) {
    def toBoolean = lifeState match {
      case Alive => true
      case Dead  => false
    }
  }

  implicit class booleanOps(bool: Boolean) {
    def toLifeState = bool match {
      case true  => Alive
      case false => Dead
    }
  }
}

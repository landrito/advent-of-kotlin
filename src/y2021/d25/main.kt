package y2021.d25

import java.io.File
import java.nio.file.Paths
import y2021.shared.Grid
import y2021.shared.GridPosition
import y2021.shared.getOrThrow
import y2021.shared.map2D
import y2021.shared.mapIndexed2D

enum class SeaCucumber {
  EAST_MOVING, SOUTH_MOVING, EMPTY;

  companion object {
    fun fromString(s: String): SeaCucumber =
      when (s.uppercase()) {
        ">" -> EAST_MOVING
        "V" -> SOUTH_MOVING
        "." -> EMPTY
        else -> throw Error("Unexpected sea cucumber string $s")
      }
  }
}

fun Grid<SeaCucumber>.wrappedNextEast(pos: GridPosition): GridPosition =
  pos.let { (row, column) ->
    if (column + 1 >= get(0).size) {
      row to 0
    } else row to column + 1
  }

fun Grid<SeaCucumber>.wrappedNextSouth(pos: GridPosition): GridPosition =
  pos.let { (row, column) ->
    if (row + 1 >= size) {
      0 to column
    } else row + 1 to column
  }

fun Grid<SeaCucumber>.canMoveEast(pos: GridPosition): Boolean =
  when (getOrThrow(pos)) {
    SeaCucumber.EAST_MOVING -> wrappedNextEast(pos).let(this::getOrThrow) == SeaCucumber.EMPTY
    else -> false
  }

fun Grid<SeaCucumber>.canMoveSouth(pos: GridPosition): Boolean =
  when (getOrThrow(pos)) {
    SeaCucumber.SOUTH_MOVING -> wrappedNextSouth(pos).let(this::getOrThrow) == SeaCucumber.EMPTY
    else -> false
  }

fun Grid<SeaCucumber>.moveEastMoving(): Grid<SeaCucumber> =
  positions().filter(this::canMoveEast).toSet().let { canMoveEast ->
    if (canMoveEast.isEmpty()) return@let this
    canMoveEast.map(this::wrappedNextEast).let { nextEast ->
      mapIndexed2D { indices, it ->
        if (canMoveEast.contains(indices)) {
          SeaCucumber.EMPTY
        } else if (nextEast.contains(indices)) {
          SeaCucumber.EAST_MOVING
        } else it
      }
    }
  }

fun Grid<SeaCucumber>.moveSouthMoving(): Grid<SeaCucumber> =
  positions().filter(this::canMoveSouth).toSet().let { canMoveSouth ->
    if (canMoveSouth.isEmpty()) return@let this
    canMoveSouth.map(this::wrappedNextSouth).let { nextSouth ->
      mapIndexed2D { indices, it ->
        if (canMoveSouth.contains(indices)) {
          SeaCucumber.EMPTY
        } else if (nextSouth.contains(indices)) {
          SeaCucumber.SOUTH_MOVING
        } else it
      }
    }
  }

fun <T> Grid<T>.positions(): List<GridPosition> =
  (0 until size).flatMap { row ->
    (0 until get(0).size).map { col ->
      row to col
    }
  }

data class SeaCucumberStep(val grid: Grid<SeaCucumber>, val noneMoved: Boolean)

fun SeaCucumberStep.step(): SeaCucumberStep {
  val next = grid.moveEastMoving().moveSouthMoving()

  return SeaCucumberStep(
    grid = next,
    noneMoved = grid === next
  )
}

fun solution(grid: Grid<SeaCucumber>): Int {
  (0..Int.MAX_VALUE).fold(SeaCucumberStep(grid, false)) { step, i ->
    if (step.noneMoved) return i
    step.step()
  }
  return -1
}

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d25/input.txt").readLines().filter(String::isNotEmpty)
  val grid = input.map {
    it.split("")
      .filter(String::isNotEmpty)
  }.map2D(SeaCucumber::fromString)

  println(solution(grid))
}


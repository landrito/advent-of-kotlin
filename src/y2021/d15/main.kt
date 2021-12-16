package y2021.d15

import java.io.File
import java.nio.file.Paths
import java.util.PriorityQueue
import y2021.shared.Grid
import y2021.shared.GridPosition
import y2021.shared.adjacentPositions
import y2021.shared.getOrThrow

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d15/input.txt").readLines().filter(String::isNotEmpty)
  val grid = input.map { it.split("").filter(String::isNotEmpty).map(String::toInt) }

  (grid.size to grid[grid.size - 1].size).let { (rowSize, colSize) ->
//    println(partOne(grid, 0 to 0, rowSize - 1 to colSize - 1))
    println(solution(grid, 0 to 0, rowSize - 1 to colSize - 1))
    println(solution(grid, 0 to 0, (rowSize * 5) - 1 to (colSize * 5) - 1))
  }
}

fun solution(grid: Grid<Int>, start: GridPosition, end: GridPosition): Int {
  val seen = mutableSetOf<GridPosition>()
  val pathToRiskQueue =
    PriorityQueue(Comparator<Pair<GridPosition, Int>> { o1, o2 -> o1.second.compareTo(o2.second) })
  pathToRiskQueue.add(start to 0)
  val pathToRiskMap = mutableMapOf(start to 0)

  while (end !in seen) {
    val (current, currentRisk) = pathToRiskQueue.poll()

    current.adjacentPositions().filter { (row, col) ->
      (row >= 0 && row <= end.first) && (col >= 0 && col <= end.second)
    }.forEach {
      val rowCount = grid.size
      val gridRow = it.first % rowCount
      val colCount = grid[rowCount - 1].size
      val gridCol = it.second % colCount
      val riskAtIt = (grid.getOrThrow(gridRow to gridCol) - 1).let { riskBase9 ->
        (it.first / rowCount).let { rowRepeats ->
          (it.second / colCount).let { colRepeats ->
            ((riskBase9 + rowRepeats + colRepeats) % 9) + 1
          }

        }
      }

      val riskToItFromCurrent = riskAtIt + currentRisk
      val itsRisk = pathToRiskMap[it] ?: Int.MAX_VALUE
      if (riskToItFromCurrent < itsRisk) {
        pathToRiskQueue.remove(it to itsRisk)
        pathToRiskQueue.add(it to riskToItFromCurrent)
        pathToRiskMap[it] = riskToItFromCurrent
      }
    }

    seen.add(current)
  }

  return pathToRiskMap.getOrThrow(end)
}
package y2021.d09

import java.io.File
import java.nio.file.Paths
import java.util.ArrayDeque
import y2021.shared.Grid
import y2021.shared.adjacentValues
import y2021.shared.foldIndexed2D
import y2021.shared.getOrNull
import y2021.shared.validAdjacentPositions

fun Grid<Int>.isLow(pos: Pair<Int, Int>): Boolean =
  getOrNull(pos)?.let { height ->
    adjacentValues(pos).fold(true) { isLow, adj ->
      isLow && height < adj
    }
  } ?: throw ArrayIndexOutOfBoundsException()


fun List<Int>.product() = fold(1) { p, i -> p * i }

fun partOne(heightMap: Grid<Int>) = heightMap.foldIndexed2D(0) { sum, pos, height ->
  sum + if (heightMap.isLow(pos)) {
    height + 1
  } else 0
}


fun partTwo(heightMap: Grid<Int>) = mutableSetOf<Pair<Int, Int>>().let { seenLocations ->
  heightMap.foldIndexed2D(listOf<Int>()) { basins, pos, _ ->
    if (seenLocations.contains(pos)) {
      basins
    } else {
      val queue = ArrayDeque(listOf(pos))
      var count = 0
      while (queue.isNotEmpty()) {
        val next = queue.removeFirst()
        if (seenLocations.contains(next)) {
          continue
        }
        seenLocations.add(next)
        if (heightMap[next.first][next.second] == 9) continue
        count++
        queue.addAll(heightMap.validAdjacentPositions((next)))
      }
      basins.plus(count)
    }
  }
}.sortedDescending().take(3).product()

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d9/input.txt").readLines()
  val heightMap = input.map { it.split("").filter(String::isNotEmpty).map(String::toInt) }
  println(partOne(heightMap))
  println(partTwo(heightMap))
}
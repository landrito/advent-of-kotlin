package y2021.d09

import java.io.File
import java.nio.file.Paths
import java.util.ArrayDeque
import y2021.shared.foldIndexed2D
import y2021.shared.getOrNull


typealias HeightMap = List<List<Int>>

fun HeightMap.adjacent(pos: Pair<Int, Int>): List<Int> = pos.let { (row, col) ->
  listOfNotNull(
    getOrNull(row - 1 to col),
    getOrNull(row + 1 to col),
    getOrNull(row to col - 1),
    getOrNull(row to col + 1),
  )
}

fun HeightMap.adjacentPositions(pos: Pair<Int, Int>) = pos.let { (row, col) ->
  listOf(
    row - 1 to col,
    row + 1 to col,
    row to col - 1,
    row to col + 1,
  ).filter { (i, j) ->
    getOrNull(i to j) !== null
  }
}

fun HeightMap.isLow(pos: Pair<Int, Int>): Boolean =
  getOrNull(pos)?.let { height ->
    adjacent(pos).fold(true) { isLow, adj ->
      isLow && height < adj
    }
  }?: throw ArrayIndexOutOfBoundsException();


fun List<Int>.product() = fold(1) {p, i -> p*i}

fun partOne(heightMap: HeightMap) = heightMap.foldIndexed2D(0) { sum, pos, height ->
  sum + if (heightMap.isLow(pos)) {
    height + 1
  } else 0
}


fun partTwo(heightMap: HeightMap) = mutableSetOf<Pair<Int, Int>>().let { seenLocations ->
    heightMap.foldIndexed2D(listOf<Int>()) {basins, pos, _ ->
      if (seenLocations.contains(pos)) {
        basins
      } else {
        val queue = ArrayDeque(listOf(pos))
        var count = 0;
        while (queue.isNotEmpty()) {
          val next = queue.removeFirst()
          if (seenLocations.contains(next)) {
            continue
          }
          seenLocations.add(next)
          if (heightMap[next.first][next.second] == 9) continue
          count++
          queue.addAll(heightMap.adjacentPositions((next)))
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
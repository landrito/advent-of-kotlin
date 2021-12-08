import java.io.File
import java.nio.file.Paths
import kotlin.math.abs

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/2021/day/7/input.txt").readLines().first()
  println(partOne(input))
  println(partTwo(input))
}

fun partOne(input: String): Int {
  return solution(input) { i, j -> abs(i - j) }
}

fun partTwo(input: String): Int {
  return solution(input) { i, j ->
    val delta = abs(i - j) + 1
    (delta * (delta - 1)) / 2
  }
}

fun solution(input: String, costBetweenPositions: (i: Int, j: Int) -> Int): Int {
  val numbers = input.split(',').map {
    it.toInt()
  }.sorted()
  return (numbers.first()..numbers.last()).fold(Int.MAX_VALUE) { minFuel, i ->
    val fuel = numbers.fold(0) { sum, j -> sum + costBetweenPositions(i, j) }
    if (fuel < minFuel) {
      fuel
    } else minFuel
  }
}

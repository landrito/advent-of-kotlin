package y2021.d07


import java.io.File
import java.nio.file.Paths
import kotlin.math.abs

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d07/input.txt").readLines().first()
  val crabs = input.split(',').map(String::toInt)
  println(partOne(crabs))
  println(partTwo(crabs))
}

fun partOne(crabs: List<Int>): Int {
  return solution(crabs) { pos1, pos2 -> abs(pos1 - pos2) }
}

fun partTwo(crabs: List<Int>): Int {
  return solution(crabs) { pos1, pos2: Int ->
    (abs(pos1 - pos2)).let { (it * (it + 1)) / 2 }
  }
}

fun solution(crabs: List<Int>, cost: (pos1: Int, pos2: Int) -> Int): Int {
  val crabSum = {pos: Int -> crabs.sumOf{ crabPos: Int -> cost(pos, crabPos)}}
  return (crabs.minOrNull()!!..crabs.maxOrNull()!!).minByOrNull(crabSum)!!.let(crabSum)
}

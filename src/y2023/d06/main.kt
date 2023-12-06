package y2023.d06

import java.io.File
import java.nio.file.Paths

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2023/d06/input.txt").readLines()
  println(partOne(input))
  println(partTwo(input))
}

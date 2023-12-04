package y2023.d04

import java.io.File
import java.nio.file.Paths
import kotlin.test.expect

class SolutionTest {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2023/d04/example.txt").readLines()

  @org.junit.jupiter.api.Test
  fun partOneExampleTest() {
    expect(13) { partOne(input) }
  }

  @org.junit.jupiter.api.Test
  fun partTwoExampleTest() {
    expect(30) { partTwo(input) }
  }
}
package y2023.d03

import java.io.File
import java.nio.file.Paths
import kotlin.test.expect

class SolutionTest {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2023/d03/example.txt").readLines()

  @org.junit.jupiter.api.Test
  fun partOneExampleTest() {
    expect(4361) { partOne(input) }
  }

  @org.junit.jupiter.api.Test
  fun partTwoExampleTest() {
    expect(467835) { partTwo(input) }
  }
}
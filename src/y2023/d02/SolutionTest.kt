package y2023.d02

import java.io.File
import java.nio.file.Paths
import kotlin.test.expect

class SolutionTest {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2023/d02/example.txt").readLines()

  @org.junit.jupiter.api.Test
  fun partOneExampleTest() {
    expect(8) { partOne(input) }
  }

  @org.junit.jupiter.api.Test
  fun partTwoExampleTest() {
    expect(2286) { partTwo(input) }
  }
}
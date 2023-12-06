package y2023.d06

import java.io.File
import java.math.BigInteger
import java.nio.file.Paths
import kotlin.test.expect

class SolutionTest {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2023/d06/example.txt").readLines()

  @org.junit.jupiter.api.Test
  fun partOneExampleTest() {
    expect(288) { partOne(input) }
  }

  @org.junit.jupiter.api.Test
  fun partTwoExampleTest() {
    expect(71503) { partTwo(input) }
  }
}
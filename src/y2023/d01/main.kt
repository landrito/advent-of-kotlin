package y2023.d01

import java.io.File
import java.nio.file.Paths

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2023/d01/input.txt").readLines()
  println(partOne(input))
  println(partTwo(input))
}

fun partOne(lines: List<String>): Int {
  return lines.map { s ->
    findDigit(s, DigitPosition.FIRST) + findDigit(s, DigitPosition.LAST)
  }.sumOf { digits -> digits.toInt() }
}

fun partTwo(lines: List<String>): Int {
  val findWords = true
  return lines.map { s ->
    findDigit(s, DigitPosition.FIRST, findWords) + findDigit(s, DigitPosition.LAST, findWords)
  }.sumOf { digits -> digits.toInt() };
}

val DIGIT_REPLACEMENTS = mapOf(
  "one" to "1", "1" to "1",
  "two" to "2", "2" to "2",
  "three" to "3", "3" to "3",
  "four" to "4", "4" to "4",
  "five" to "5", "5" to "5",
  "six" to "6", "6" to "6",
  "seven" to "7", "7" to "7",
  "eight" to "8", "8" to "8",
  "nine" to "9", "9" to "9",
  // Digit 0 is not specified as a spelled out digit in the spec.
)

enum class DigitPosition {
  FIRST, LAST
}

fun findDigit(s: String, position: DigitPosition, findWords: Boolean = false): String {
  val query = if (findWords) DIGIT_REPLACEMENTS.keys else DIGIT_REPLACEMENTS.values.toSet()
  val action = if (position == DigitPosition.FIRST) {q: Collection<String> -> s.findAnyOf(q)}
    else {q: Collection<String> -> s.findLastAnyOf(q)}
  return action.invoke(query)!!.let(Pair<Int, String>::second).let{d -> DIGIT_REPLACEMENTS[d]}!!
}


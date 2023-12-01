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
  }
    .map {digits ->  digits.toInt()}
    .reduce(Int::plus)
}

fun partTwo(lines: List<String>): Int {
  val findWords = true
  return lines.map { s ->
    findDigit(s, DigitPosition.FIRST, findWords) + findDigit(s, DigitPosition.LAST, findWords)
  }
    .map {digits ->  digits.toInt()}
    .reduce(Int::plus)
}

data class Replace(val regexString: String, val replacement: String)

fun String.replace(r: Replace): String {
  return this.replace(Regex(r.regexString), r.replacement)
}

val DIGIT_REPLACEMENTS = arrayOf(
  // Digit 0 is not specified as a spelled out digit in the spec.
  Replace("one", "1"),
  Replace("two", "2"),
  Replace("three", "3"),
  Replace("four", "4"),
  Replace("five", "5"),
  Replace("six", "6"),
  Replace("seven", "7"),
  Replace("eight", "8"),
  Replace("nine", "9"),
)

enum class DigitPosition {
  FIRST, LAST
}

fun findDigit(s: String, position: DigitPosition, findWords: Boolean = false): String {
  val queryStrings = if(findWords) {
    DIGIT_REPLACEMENTS.flatMap{ r -> listOf(r.regexString, r.replacement) }
  } else DIGIT_REPLACEMENTS.map{ r -> r.replacement }
  val result = if(position == DigitPosition.FIRST) s.findAnyOf(queryStrings) else s.findLastAnyOf(queryStrings)
  val digitOrWord = result?.second ?: "0"
  return digitOrWord.let(::replaceDigitWordsForDigits)
}

fun replaceDigitWordsForDigits(s: String): String {
  return DIGIT_REPLACEMENTS.fold(s) { result, replacement -> result.replace(replacement) }
}

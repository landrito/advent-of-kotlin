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
  val isDigit = {c: Char -> c.toString().matches(Regex("[0-9]"))}
  return lines.map {line ->  line.first(isDigit).toString() + line.last(isDigit)}
    .map {digits ->  digits.toInt()}
    .reduce(Int::plus)
}

fun partTwo(lines: List<String>): Int {
  // Since we only care about first and last digit we can just concatenate the ltr and rtl and we should get the
  // correct digits at the start and end.
  return lines.map{s -> replaceDigitWordsForDigitsLtr(s) + replaceDigitWordsForDigitsRtl(s)}.let(::partOne)
}

fun replaceDigitWordsForDigits(s: String): String {
  data class Replace(val regexString: String, val replacement: String)
  return arrayOf(
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
  ).fold(s) { result, replace -> result.replace(Regex(replace.regexString), replace.replacement) }
}

fun replaceDigitWordsForDigitsLtr(s: String): String {
  return s.chunked(1).reduce {result, char -> (result + char).let(::replaceDigitWordsForDigits)}
}

fun replaceDigitWordsForDigitsRtl(s: String): String {
  return s.chunked(1).reversed().reduce{result, char -> (char + result).let(::replaceDigitWordsForDigits)}
}
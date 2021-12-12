package y2021.d10

import java.io.File
import java.nio.file.Paths
import java.util.Stack

sealed interface BracketError

data class IncorrectClosingBracket(val foundBracket: String) : BracketError

data class MissingClosingBrackets(val missingBrackets: List<String>) : BracketError

object NoError : BracketError

fun String.toOpeningBracket() =
  when (this) {
    ")" -> "("
    "]" -> "["
    "}" -> "{"
    ">" -> "<"
    else -> throw Error("Invalid closing bracket: `$this`")
  }

fun String.toClosingBracket() =
  when (this) {
    "(" -> ")"
    "[" -> "]"
    "{" -> "}"
    "<" -> ">"
    else -> throw Error("Invalid open bracket: `$this`")
  }

fun determineBracketError(line: List<String>) = Stack<String>().let { stack ->
  line.fold(NoError as BracketError) { error, bracket ->
    if (error == NoError) {
      when (bracket) {
        "(", "[", "{", "<" -> {
          stack.push(bracket)
          NoError
        }
        ")", "]", "}", ">" -> if (stack.pop() != bracket.toOpeningBracket()) {
          IncorrectClosingBracket(bracket)
        } else NoError
        else -> throw Error("Invalid character: $bracket")
      }
    } else error
  }.let { error ->
    if (error == NoError && stack.isNotEmpty()) {
      MissingClosingBrackets(
        stack.asReversed().map(String::toClosingBracket)
      )
    } else error
  }
}


fun partOne(lines: List<List<String>>) =
  lines.map(::determineBracketError).filterIsInstance<IncorrectClosingBracket>()
    .groupBy { it.foundBracket }
    .asSequence()
    .sumOf { (bracket, errors) ->
      errors.size * when (bracket) {
        ")" -> 3
        "]" -> 57
        "}" -> 1197
        ">" -> 25137
        else -> throw Error("Expected bracket but got `$bracket`")
      }
    }

fun partTwo(lines: List<List<String>>) = lines.map(::determineBracketError)
  .filterIsInstance<MissingClosingBrackets>()
  .map { (missingBrackets) ->
    missingBrackets.fold(0L) { acc, bracket ->
      (acc * 5L) + when (bracket) {
        ")" -> 1L
        "]" -> 2L
        "}" -> 3L
        ">" -> 4L
        else -> throw Error("Expected bracket but got `$bracket`")
      }
    }
  }.sorted().let { it[Math.floorDiv(it.size, 2)] }

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d10/input.txt").readLines()
  val lines = input.map { it.split("").filter(String::isNotEmpty) }
  println(partOne(lines))
  println(partTwo(lines))
}

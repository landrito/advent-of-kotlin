package y2021.d13

import java.io.File
import java.nio.file.Paths

typealias Pos2D = Pair<Int, Int>

sealed interface Fold
data class FoldAlongX(val x: Int) : Fold
data class FoldAlongY(val y: Int) : Fold

fun Set<Pos2D>.foldAlong(f: Fold) = when (f) {
  is FoldAlongY ->
    fold(setOf<Pos2D>()) { set, (x, y) ->
      if (y < f.y) {
        set.plus(x to y)
      } else if (y > f.y) {
        set.plus(x to f.y - (y - f.y))
      } else set
    }
  is FoldAlongX ->
    fold(setOf()) { set, (x, y) ->
      if (x < f.x) {
        set.plus(x to y)
      } else if (x > f.x) {
        set.plus(f.x - (x - f.x) to y)
      } else set
    }
}

fun Set<Pos2D>.maxX() = fold(0) {max, (x, _) -> if (x > max) {x} else max }
fun Set<Pos2D>.maxY() = fold(0) {max, (_, y) -> if (y > max) {y} else max }

fun Set<Pos2D>.print() = (0..maxY()).forEach { y ->
  (0..maxX()).fold("") {s, x ->
    s + if(contains(x to y)) {'█'} else ' '
  }.apply(::println)
}

fun partOne(dots: Set<Pos2D>, folds: List<Fold>) = folds.first().let { dots.foldAlong(it) }.size

fun partTwo(dots: Set<Pos2D>, folds: List<Fold>) = folds.fold(dots) {d,f -> d.foldAlong(f)}

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d13/input.txt").readLines().filter(String::isNotEmpty)
  val (dots, folds) = parseInput(input)

  println(partOne(dots, folds))
  partTwo(dots, folds).print()
}

fun parseInput(input: List<String>) = input.fold(setOf<Pos2D>() to listOf<Fold>()) { (dots, folds), line ->
  if (line.startsWith("fold")) {
    line.split(" ").last().split("=").let {
      when (it.first()) {
        "x" -> FoldAlongX(it.last().toInt())
        "y" -> FoldAlongY(it.last().toInt())
        else -> throw Error("Unable to parse line: `$line`")
      }
    }.let {
      dots to folds.plus(it)
    }
  } else {
    line.split(',').map(String::toInt).let {
      dots.plus(it.first() to it.last()) to folds
    }
  }
}
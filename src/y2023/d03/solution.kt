package y2023.d03

import y2021.d09.product

val PART_NUMBER_REGEX = Regex("""\d+""")
val SYMBOL_REGEX = Regex("""[^\d.]""")
val GEAR_REGEX = Regex("""\*""")

fun partOne(lines: List<String>): Int {
  val symbols = lines.map { SYMBOL_REGEX.findAll(it) }
    .map { matches -> matches.flatMap { it.range }.toSet() }

  return lines.map { PART_NUMBER_REGEX.findAll(it) }
    .map { match -> match.map { it.value.toInt() to it.range.pad(1) } }
    .mapIndexed { row, matches ->
      matches.sumOf { (partNumber, searchRange) ->
        if (symbols.withinRangeArea(searchRange, row)) partNumber else 0
      }
    }.sum()
}

fun IntRange.pad(n: Int): IntRange {
  return IntRange(this.first - n, this.last + n)
}

fun List<Set<Int>>.withinRangeArea(searchRange: IntRange, row: Int): Boolean {
  return this.subList(maxOf(0, row - 1), minOf(this.size, row + 2))
    .any { rows -> rows.any { searchRange.contains(it) } }
}

fun partTwo(lines: List<String>): Int {
  val parts = lines.map { PART_NUMBER_REGEX.findAll(it) }
    .map { matches -> matches.map { it.value.toInt() to it.range } }

  return lines.map { GEAR_REGEX.findAll(it) }
    .map { matches -> matches.map { it.range.pad(1) } }
    .mapIndexed { row, gearRanges ->
      gearRanges.sumOf { parts.getConnectedParts(it, row).gearRatio() }
    }.sum()
}

fun List<Sequence<Pair<Int, IntRange>>>.getConnectedParts(gearRange: IntRange, row: Int): List<Int> {
  return this.subList(maxOf(0, row - 1), minOf(this.size, row + 2))
    .flatMap { rows ->
      rows.filter { (_, partRange) -> partRange.intersects(gearRange) }.map { (partNumber) -> partNumber }
    }
}

fun List<Int>.gearRatio(): Int {
  return if (this.size == 2) this.product() else 0
}

fun IntRange.intersects(other: IntRange): Boolean {
  return this.toSet().intersect(other.toSet()).isNotEmpty()
}
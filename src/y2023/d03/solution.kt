package y2023.d03

val PART_NUMBER_REGEX = Regex("""\d+""")
val SYMBOL_REGEX = Regex("""[^\d.]""")

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
  return 0
}

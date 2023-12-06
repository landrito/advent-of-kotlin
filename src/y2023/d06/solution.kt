package y2023.d06

val INT_REGEX = Regex("""\d+""")

typealias Race = Pair<Long, Long>

fun partOne(lines: List<String>): Long =
  races(lines).map { it.waysToWin() }.reduce { a, b -> a * b }

fun partTwo(lines: List<String>): Long =
  lines.map { it.replace(" ", "") }.let(::races).map { it.waysToWin() }.first()

fun races(lines: List<String>): Sequence<Race> =
  lines.first().longs().zip(lines.last().longs())

// This can be reduced to O(logn) if you do a binary search for the first and last times that beat it.
fun Race.waysToWin(): Long = this.let { (time, distance) ->
  (0..time).reversed().first { distance(it, time) > distance } -
      (0..time).first { distance(it, time) > distance } + 1
}

fun String.longs() = INT_REGEX.findAll(this).map { it.value.toLong() }

fun distance(timeButtonHeld: Long, raceTime: Long) = (raceTime - timeButtonHeld) * timeButtonHeld

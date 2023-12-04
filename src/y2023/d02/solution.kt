package y2023.d02

fun partOne(lines: List<String>): Int {
  val bag = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14,
  )

  return lines.map(::gameFrom)
    .map { g -> g.id to g.cubeCounts.reduce(::maxValuesOf) }
    .sumOf { (id, maxCounts) -> if (maxCounts.possibleIn(bag)) id else 0 }
}

fun partTwo(lines: List<String>): Int {
  return lines.map(::gameFrom)
    .map { g -> g.cubeCounts.reduce(::maxValuesOf) }
    .sumOf { counts -> counts.values.reduce { result, current -> result * current } }
}

typealias CubeCounts = Map<String, Int>

data class Game(val id: Int, val cubeCounts: List<CubeCounts>)

fun gameFrom(line: String): Game {
  val id = Regex(""""\\d+""").find(line)!!.value.toInt()
  val cubeCounts = line.split(":")
    .last()
    .split(";")
    .map { round ->
      round.split(",")
        .map { s -> s.trim() }
        .map { s -> s.split(" ") }
        .associate { parts -> parts[1] to parts[0].toInt() }
    }

  return Game(id, cubeCounts)
}

fun maxValuesOf(a: CubeCounts, b: CubeCounts): CubeCounts {
  return b
    .toList()
    .associate { (k, v) -> k to maxOf(v, a.getOrElse(k) { 0 }) }
    .let { maxMap -> a.plus(maxMap) }
}

fun CubeCounts.possibleIn(bagCounts: CubeCounts): Boolean {
  return this.toList()
    .fold(initial = true) { result, (color, count) ->
      result && count <= bagCounts.getOrElse(color) { 0 }
    }
}

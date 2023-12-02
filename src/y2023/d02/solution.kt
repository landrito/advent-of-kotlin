package y2023.d02

fun partOne(lines: List<String>): Int {
  val bag = mapOf(
    "red" to 12,
    "green" to 13,
    "blue" to 14,
  )

  return lines.map(::gameFrom)
    .map { g -> g.id to g.cubeCounts.reduce { result, current -> result.maxValues(current) } }
    .sumOf { (id, maxCounts) -> if (maxCounts.possibleIn(bag)) id else 0 }
}

fun partTwo(lines: List<String>): Int {
  return lines.map(::gameFrom)
    .map { g -> g.cubeCounts.reduce { result, current -> result.maxValues(current) } }
    .sumOf { counts -> counts.values.reduce { result, current -> result * current } }
}

typealias CubeCounts = Map<String, Int>

data class Game(val id: Int, val cubeCounts: List<CubeCounts>)

fun gameFrom(line: String): Game {
  val id = "\\d+".toRegex().find(line)!!.value.toInt()
  val cubeCounts = line.split(":")
    .last()
    .split(";")
    .map { round ->
      round.split(",")
        .map { s -> s.trim() }
        .map { s -> s.split(" ") }
        .map { parts -> Pair(parts[1], parts[0].toInt()) }
        .associate { it }
    }

  return Game(id, cubeCounts)
}

fun CubeCounts.maxValues(other: CubeCounts): CubeCounts {
  return other
    .toList()
    .map { (k, v) -> k to maxOf(v, this.getOrElse(k) { 0 }) }
    .associate { it }
    .let { maxMap -> this.plus(maxMap) }
}

fun CubeCounts.possibleIn(bagCounts: CubeCounts): Boolean {
  return this.toList()
    .fold(true) { result, (color, count) ->
      result && count <= bagCounts.getOrElse(color) { 0 }
    }
}
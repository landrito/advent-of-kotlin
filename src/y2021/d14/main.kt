package y2021.d14

import java.io.File
import java.nio.file.Paths
import y2021.shared.associateCount
import y2021.shared.decrementCount
import y2021.shared.incrementCount

typealias PolymerizationRules = Map<Pair<String, String>, String>
typealias PolymerPairCounts = Map<Pair<String, String>, Long>

fun PolymerizationRules.step(polymerTemplateCounts: PolymerPairCounts): PolymerPairCounts =
  polymerTemplateCounts.entries.fold(polymerTemplateCounts) { pairCounts, entry ->
    get(entry.key)?.let {
      pairCounts
        .decrementCount(entry.key, entry.value)
        .incrementCount(entry.key.first to it, entry.value)
        .incrementCount(it to entry.key.second, entry.value)
    } ?: throw error("Unable to find pair ${entry.key}")
  }

fun PolymerizationRules.step(polymerTemplateCounts: PolymerPairCounts, times: Int) =
  (0 until times).fold(polymerTemplateCounts) { counts, _ -> step(counts) }

fun List<String>.toPolymerTemplateCounts() = windowed(2, partialWindows = true)
  .filter { it.size == 2 }
  .map { it.first() to it.last() }
  .associateCount()

fun <T> min(a: Map.Entry<T, Long>, b: Map.Entry<T, Long>) = if (a.value < b.value) {
  a
} else b

fun <T> max(a: Map.Entry<T, Long>, b: Map.Entry<T, Long>) = if (a.value > b.value) {
  a
} else b

fun solution(
  insertionRules: PolymerizationRules,
  polymerTemplate: List<String>,
  steps: Int,
) =
  polymerTemplate.toPolymerTemplateCounts().let {
    insertionRules.step(it, steps)
      .entries.fold(mapOf(polymerTemplate.last() to 1L)) { map, entry ->
        map.incrementCount(entry.key.first, entry.value)
      }
  }.entries.let {
    it.fold(it.first() to it.last()) { (least, most), cur ->
      min(least, cur) to max(most, cur)
    }
  }.let { (least, most) -> most.value - least.value }

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d14/input.txt").readLines().filter(String::isNotEmpty)
  val polymerTemplate = input.first().split("").filter(String::isNotEmpty)
  val insertionRules = input.slice(1 until input.size).associate { line ->
    line.split("->")
      .map(String::trim)
      .filter(String::isNotEmpty)
      .let {
        it.first().split("").filter(String::isNotEmpty).let { pair ->
          (pair.first() to pair.last()) to it.last()
        }
      }
  }
  println(solution(insertionRules, polymerTemplate, 10))
  println(solution(insertionRules, polymerTemplate, 40))
}

package y2023.d04

import kotlin.math.pow

val INT_REGEX = Regex("""\d+""")

fun partOne(lines: List<String>): Int {
  return lines.map(::cardFrom).sumOf { it.worth() }
}

data class Card(val winningNumbers: Set<Int>, val numbers: Set<Int>)

fun cardFrom(line: String): Card = line.split(":").last().split("|")
  .map { part -> part.trim().let { INT_REGEX.findAll(it) }.map { it.value }.map { it.toInt() }.toSet() }
  .let { Card(it.first(), it.last()) }

fun Card.countMatches(): Int {
  return this.winningNumbers.intersect(this.numbers).size
}

fun Card.worth(): Int {
  return this.countMatches().let { 2.toFloat().pow(it - 1).toInt() }
}

fun partTwo(lines: List<String>): Int {
  val cardCounts = lines.map { 1 }.toTypedArray()
  lines.map(::cardFrom).forEachIndexed { index, card ->
    IntRange(index + 1, index + card.countMatches())
      .forEach { followingIndex -> cardCounts[followingIndex] = cardCounts[followingIndex] + cardCounts[index] }
  }
  return cardCounts.sum()
}
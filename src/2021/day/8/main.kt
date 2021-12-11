package `2021`.day.`8`

import java.io.File
import java.nio.file.Paths

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/2021/day/8/input.txt").readLines()
  println(partOne(input))
  println(partTwo(input))
}

fun partOne(input: List<String>): Int =
  input.map { it.split('|').last().trim().split(' ') }.sumOf { digits ->
    digits.sumOf {
      when (it.length) {
        2, 3, 4, 7 -> 1
        else -> 0
      } as Int
    }
  }

val toCharSet = { s: String ->
  s.split("").filter(String::isNotEmpty).toSet()
}

fun partTwo(input: List<String>): Int = input.map { line ->
  val (signal, output) = line.split('|').map(String::trim).let { parts ->
    parts.map { it.split(' ').map(toCharSet) }.let { digitLists ->
      digitLists.first() to digitLists.last()
    }
  }

  val one = signal.first { it.size == 2 }
  val four = signal.first { it.size == 4 }
  val seven = signal.first { it.size == 3 }
  val eight = signal.first { it.size == 7 }

  val sixSegmentDigits = signal.filter { it.size == 6 }
  val six = disambiguateSix(one, sixSegmentDigits)
  val (zero, nine) = disambiguateZeroNine(four, sixSegmentDigits.filter { it !== six })

  val fiveSegmentDigits = signal.filter { it.size == 5 }
  val three = disambiguateThree(one, fiveSegmentDigits)
  val (two, five) = disambiguateTwoFive(six, fiveSegmentDigits.filter { it !== three })

  output.fold(0) { acc, set ->
    acc * 10 + when (set) {
      one -> 1
      two -> 2
      three -> 3
      four -> 4
      five -> 5
      six -> 6
      seven -> 7
      eight -> 8
      nine -> 9
      zero -> 0
      else -> throw Error("Damn")
    }
  }
}.sumOf { it }

private fun disambiguateSix(
  one: Set<String>,
  sixSegmentDigits: List<Set<String>>,
): Set<String> =
  sixSegmentDigits.first {
    it.intersect(one).size != 2
  }

private data class ZeroNineDisambiguation(val zero: Set<String>, val nine: Set<String>)

private fun disambiguateZeroNine(
  four: Set<String>,
  zeroOrNine: List<Set<String>>,
): ZeroNineDisambiguation = ZeroNineDisambiguation(
  zero = zeroOrNine.first { it.intersect(four).size == 3 },
  nine = zeroOrNine.first { it.intersect(four).size == 4 },
)

private fun disambiguateThree(
  one: Set<String>,
  fiveSegmentDigits: List<Set<String>>,
): Set<String> = fiveSegmentDigits.first { it.intersect(one).size == 2 }

private data class TwoFiveDisambiguation(val two: Set<String>, val five: Set<String>)

private fun disambiguateTwoFive(
  six: Set<String>,
  twoOrFive: List<Set<String>>,
) = TwoFiveDisambiguation(two = twoOrFive.first { it.intersect(six).size != 5 },
                          five = twoOrFive.first { it.intersect(six).size == 5 })
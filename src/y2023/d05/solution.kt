package y2023.d05

val INT_REGEX = Regex("""\d+""")

fun partOne(lines: List<String>): Long =
  Almanac(
    seedConditionsFrom(lines.subList(2, lines.size)), seedsFrom(lines.first())
  ).lowestLocation()

typealias ConditionMap = List<SeedCondition>


data class SeedCondition(val destinationIndex: Long, val sourceIndex: Long, val size: Long)

fun SeedCondition.mapsSource(source: Long): Boolean = source >= sourceIndex && source < sourceIndex + size

fun SeedCondition.destination(source: Long): Long = source - sourceIndex + destinationIndex

fun seedConditionFrom(line: String): SeedCondition =
  line.splitToLongs().let { args -> SeedCondition(args[0], args[1], args[2]) }


data class Almanac(val seedConditions: List<ConditionMap>, val seedRanges: List<LongRange>)

fun Almanac.lowestLocation(): Long = seedRanges.minOfOrNull { seedRange ->
  seedRange.minOf { seed ->
    seedConditions.fold(seed) { result, conditionMap ->
      conditionMap.find { it.mapsSource(result) }
        .let {
          it?.destination(result) ?: result
        }
    }
  }
}!!

fun seedsFrom(line: String): List<LongRange> =
  line.split(":").last().splitToLongs().map { it..it }

fun seedRangesFrom(line: String): List<LongRange> =
  line.split(":").last().splitToLongs().windowed(size = 2, step = 2)
    .map { it.first()..<it.first() + it.last() }

fun seedConditionsFrom(lines: List<String>): List<ConditionMap> =
  mutableListOf<Int>().apply {
    this.add(-1)
    lines.forEachIndexed { index, line -> if (line.isEmpty()) this.add(index) }
    this.add(lines.size - 2)
  }.windowed(2).map { indices ->
    lines.subList(indices.first() + 1, indices.last())
  }.map { it.subList(1, it.size) }.map { specificConditionLines ->
    specificConditionLines.map(::seedConditionFrom)
  }

fun String.splitToLongs(): List<Long> = INT_REGEX.findAll(this).map { it.value.toLong() }.toList()

fun partTwo(lines: List<String>): Long =
  Almanac(
    seedConditionsFrom(lines.subList(2, lines.size)), seedRangesFrom(lines.first())
  ).lowestLocation()
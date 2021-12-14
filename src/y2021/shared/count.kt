package y2021.shared

fun <T> List<T>.associateCount(): Map<T, Long> = fold(mapOf()) { counts, t ->
  counts.incrementCount(t)
}

fun <T> Map<T, Long>.incrementCount(t: T, amount: Long = 1L): Map<T, Long> =
  plus(t to getOrDefault(t, 0) + amount)

fun <T> Map<T, Long>.decrementCount(t: T, amount: Long = 1L): Map<T, Long> = get(t)?.let {
  if (it - amount < 1L) {
    minus(t)
  } else (
    plus(t to it - amount)
    )
} ?: this
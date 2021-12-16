package y2021.shared

fun <K, V> Map<K, V>.getOrThrow(k: K): V = get(k) ?: throw Error("Unable to find K")

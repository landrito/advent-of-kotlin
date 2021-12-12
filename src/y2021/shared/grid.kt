package y2021.shared

typealias GridPosition = Pair<Int, Int>

typealias Grid<T> = List<List<T>>

fun <T, R> Grid<T>.foldIndexed2D(
  initial: R,
  action: (acc: R, indices: GridPosition, it: T) -> R,
): R =
  foldIndexed(initial) { i, outer, row ->
    row.foldIndexed(outer) { j, inner, v -> action(inner, i to j, v) }
  }

fun <T, R> Grid<T>.fold2D(initial: R, action: (acc: R, it: T) -> R): R =
  foldIndexed2D(initial) { acc, _, it -> action(acc, it) }

fun <T, R> Grid<T>.mapIndexed2D(action: (indices: GridPosition, it: T) -> R): Grid<R> =
  mapIndexed { i, row ->
    row.mapIndexed { j, it -> action(i to j, it) }
  }

fun <T, R> Grid<T>.map2D(action: (it: T) -> R): Grid<R> = mapIndexed2D { _, it -> action(it) }

fun <T> Grid<T>.getOrNull(pos: GridPosition) = getOrNull(pos.first)?.getOrNull(pos.second)

fun <T> Grid<T>.surroundingPositions(pos: GridPosition): List<GridPosition> =
  pos.let { (row, col) ->
    listOf(
      row - 1 to col - 1,
      row - 1 to col,
      row - 1 to col + 1,
      row to col - 1,
      row to col + 1,
      row + 1 to col - 1,
      row + 1 to col,
      row + 1 to col + 1,
    ).filter { getOrNull(it) !== null }
  }


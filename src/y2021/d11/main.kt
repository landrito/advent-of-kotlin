package y2021.d11

import java.io.File
import java.nio.file.Paths
import y2021.shared.Grid
import y2021.shared.GridPosition
import y2021.shared.fold2D
import y2021.shared.foldIndexed2D
import y2021.shared.map2D
import y2021.shared.mapIndexed2D
import y2021.shared.surroundingPositions

data class Octopus(val luminosity: Int, val flashed: Boolean) {
  val canFlash: Boolean
    get() = luminosity > 9 && !flashed;

  fun increment() = Octopus(luminosity + 1, flashed)

  fun resetIfFlashed() = if (flashed) {
    Octopus(0, false)
  } else this
}

typealias OctopodesGrid = Grid<Octopus>

fun OctopodesGrid.incrementPos(pos: GridPosition) = mapIndexed2D { indices, octopus ->
  if (indices == pos) {
    octopus.increment()
  } else octopus
}

data class OctopodesGridStep(
  val grid: OctopodesGrid,
  val flashedCount: Int,
  val allFlashed: Boolean,
)

fun OctopodesGrid.step() = increment().flash().let {
  OctopodesGridStep(
    grid = it.resetFlashed(),
    flashedCount = it.countFlashed(),
    allFlashed = it.allFlashed(),
  )
}

fun OctopodesGrid.increment(): OctopodesGrid =
  map { row -> row.map(Octopus::increment) }

fun OctopodesGrid.flash(pos: GridPosition): OctopodesGrid =
  surroundingPositions(pos).fold(this) { g, p -> g.incrementPos(p) }
    .mapIndexed2D { indices, octopus ->
      if (indices == pos) {
        Octopus(octopus.luminosity, true)
      } else octopus
    }

fun OctopodesGrid.flash(): OctopodesGrid = if (canFlash()) {
  foldIndexed2D(this) { g, pos, octopus ->
    if (octopus.canFlash) {
      g.flash(pos)
    } else g
  }.flash()
} else this

fun OctopodesGrid.countFlashed() = fold2D(0) { i, octopus ->
  if (octopus.flashed) {
    i + 1
  } else i
}

fun OctopodesGrid.resetFlashed() = map2D(Octopus::resetIfFlashed)

fun OctopodesGrid.allFlashed() = all { it.all { octopus -> octopus.flashed } }

fun OctopodesGrid.canFlash(): Boolean = any { it.any { octopus -> octopus.canFlash } }

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d11/input.txt").readLines()
  val grid: OctopodesGrid = input.map {
    it.split("")
      .filter(String::isNotEmpty)
      .map(String::toInt)
      .map { i -> Octopus(i, false) }
  }
  println(partOne(grid))
  println(partTwo(grid))
}

fun partOne(grid: OctopodesGrid) =
  (2..100).fold(grid.step()) { (grid, flashedCount), _ ->
    grid.step().let {
      it.copy(flashedCount = it.flashedCount + flashedCount)
    }
  }.flashedCount

fun partTwo(grid: OctopodesGrid): Int {
  (1..Int.MAX_VALUE).fold(grid) { g, step ->
    g.step().also { if (it.allFlashed) return step }.grid
  }
  return -1
}


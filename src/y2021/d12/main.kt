package y2021.d12

import java.io.File
import java.nio.file.Paths

class Graph(edges: List<Pair<String, String>>) {
  data class Node(val name: String, val edges: Set<String>) {
    fun connectedTo(other: String) = this.copy(edges = edges.plus(other))
  }

  private val nodes = edges.fold(setOf<String>()) { set, (first, second) ->
    set.plus(listOf(first, second))
  }.associateWith { Node(it, setOf()) }.let { nodes ->
    val mutNodes = nodes.toMutableMap()
    for ((source, destination) in edges) {
      mutNodes[source]?.also {
        mutNodes[source] = it.connectedTo(destination)
      }
      mutNodes[destination]?.also {
        mutNodes[destination] = it.connectedTo(source)
      }
    }
    mutNodes
  }

  fun pathsBy(
    traversalCondition: (edge: String, path: List<String>) -> Boolean,
  ): List<List<String>> {
    val queue = nodes["start"]?.let { ArrayDeque(listOf(it to listOf("start"))) }
      ?: throw Error("Unable to find node `start`")
    val seenPaths = mutableSetOf<List<String>>()
    while (queue.isNotEmpty()) {
      val (node, path) = queue.removeFirst()
      if (seenPaths.contains(path)) {
        continue
      }

      seenPaths.add(path)

      if (node.name == "end") {
        continue
      }

      node.edges.filter { traversalCondition(it, path) }
        .map { edge ->
          nodes[edge]?.let { node -> node to path.plus(edge) }
            ?: throw Error("Unable to find node $edge")
        }.forEach {
          queue.add(it)
        }
    }

    return seenPaths.filter { it.first() == "start" && it.last() == "end" }
  }
}

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val edges = File("$cwd/src/y2021/d12/input.txt").readLines().map {
    it.split('-').map(String::trim).filter(String::isNotEmpty).let { l -> l[0] to l[1] }
  }
  val graph = Graph(edges)
  println(partOne(graph))
  println(partTwo(graph))
}

fun partOne(graph: Graph) = graph.pathsBy { edge, path ->
  edge == edge.uppercase() || !path.contains(edge)
}.size

fun partTwo(graph: Graph) = graph.pathsBy { edge, path ->
  when (edge) {
    "start", "end" -> path.none { it == edge }
    edge.lowercase() -> {
      path.filter {
        it != "start" && it != "end" && it == it.lowercase()
      }.fold(mapOf<String, Int>()) { m, e ->
        (m[e] ?: 0).let { count ->
          m.plus(e to count + 1)
        }
      }.let { edgeCounts ->
        edgeCounts.all { (_, count) -> count < 2 } || (edgeCounts[edge] ?: 0) == 0
      }
    }
    else -> true
  }
}.size
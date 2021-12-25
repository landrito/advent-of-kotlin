package y2021.d16

import java.io.File
import java.nio.file.Paths

fun main() {
  val cwd = Paths.get("").toAbsolutePath().toString()
  val input = File("$cwd/src/y2021/d16/example_16.txt").readLines().filter(String::isNotEmpty)
  val binary = input.first()
    .split("")
    .filter(String::isNotEmpty)
    .joinToString("") {
      it.toInt(16).toString(2).let { s ->
        "0".repeat(4 - s.length) + s
      }
    }

  println(partOne("110100101111111000101000"))
}

enum class PacketType {
  LITERAL, OPERATOR;

  companion object {
    fun of(i: Int) = when (i) {
      4 -> LITERAL
      else -> OPERATOR
    }
  }
}


/*sealed interface Packet {
  companion object {
    fun fromBinary(binary: String, length: Int? = null): Packet {
      val version = binary.substring(0..2).toInt(2)
      val type = binary.substring(3..6).toInt(2).let(PacketType::of)

      when (type) {
        PacketType.LITERAL ->
          return version + binary.literalValue()
            .let { it.second?.let { rest -> partOne(rest) } ?: 0 }
        PacketType.OPERATOR ->
          return 0
      }
    }
  }
}*/

sealed interface SubPacketType
data class BitLength(val length: Int) : SubPacketType
data class SubPackets(val subPacketCount: Int) : SubPacketType

fun partOne(binary: String): Int = binary.length

fun operatorSubPackets(binary: String, size: Int) {

}

fun String.literalValue() =
  substring(6)
    .split("")
    .filter(String::isNotEmpty)
    .windowed(size = 5, step = 5, partialWindows = true)
    .map { it.joinToString("") }
    .let {
      it.indexOfFirst { s -> s.startsWith("0") }.let { lastValueIndex ->
        it.slice(0..lastValueIndex)
          .joinToString("") { s -> s.substring(1) }
          .toInt(2)
          .let { value ->
            value to if (lastValueIndex + 1 >= it.size) {
              null
            } else it.slice(lastValueIndex + 1 until it.size).joinToString("")
          }
      }
    }



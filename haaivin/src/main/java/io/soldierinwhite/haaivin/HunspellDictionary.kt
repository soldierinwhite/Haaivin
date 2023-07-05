package io.soldierinwhite.haaivin

import java.io.InputStream
import java.nio.charset.Charset
import kotlin.math.max
import kotlin.math.min

class HunspellDictionary(
    val id: String,
    dictionaryProvider: () -> InputStream
) {
    var leftHyphenMin = 1
        private set
    var rightHyphenMin = 2
        private set
    val longestKeyLength: Int
    val patterns = dictionaryProvider().bufferedReader(
        dictionaryProvider().bufferedReader().use {
            Charset.forName(it.readLine())
        }
    ).use { reader ->
        reader.readLines().mapNotNull { line ->
            if (line.none { it.isUpperCase() }) {
                val firstDigitIndex = line.indexOfFirst { it.isDigit() }
                line.substring(firstDigitIndex).windowed(2, 1)
                line.filter { !it.isDigit() } to (firstDigitIndex to line.substring(
                    max(
                        0,
                        firstDigitIndex
                    ),
                    min(max(firstDigitIndex + 2, line.indexOfLast { it.isDigit() } + 2),
                        line.lastIndex + 1))
                    .windowed(2, 1, true) { charPair ->
                        if (charPair[0].isDigit() && charPair.getOrNull(1)?.isDigit() != true) {
                            charPair[0].digitToInt()
                        } else if (charPair.length == 2 && charPair.none { it.isDigit() }) {
                            0
                        } else {
                            null
                        }
                    }.filterNotNull())
            } else {
                if (line.contains("LEFTHYPHENMIN")) {
                    line.substringAfter("LEFTHYPHENMIN").trim().first().digitToIntOrNull()
                        ?.let {
                            leftHyphenMin = it
                        }
                }
                if (line.contains("RIGHTHYPHENMIN")) {
                    line.substringAfter("RIGHTHYPHENMIN").trim().first().digitToIntOrNull()
                        ?.let {
                            rightHyphenMin = it
                        }
                }
                null
            }
        }.toMap().also {
            longestKeyLength = it.keys.maxOf { key -> key.length }
        }
    }
}
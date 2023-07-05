package io.soldierinwhite.haaivin

import androidx.collection.LruCache
import kotlin.math.max
import kotlin.math.min

class Haaivin(
    private val hunspellDictionaries: List<HunspellDictionary>,
    cacheItemSize: Int = 1000
) {
    private val lruCache = LruCache<String, String>(cacheItemSize)
    //https://github.com/hunspell/hyphen/blob/master/README.hyphen
    fun hyphenate(string: String, hyphen: Char = Char(173), dictionaryId: String): String {
        if (hunspellDictionaries.isEmpty()) return string

        val dictionary =
            hunspellDictionaries.find { it.id == dictionaryId } ?: hunspellDictionaries.first()
        val words = string.split(Regex("\\s")).filter { it.isNotEmpty() }
        val hyphenatedWords = words.map { word ->
            lruCache[word]?.let { return@map it }
            val prepWord = ".${word.lowercase()}."
            val scoreArray = IntArray(prepWord.length + 1)
            prepWord.indices.forEach { startIndex ->
                (startIndex + 1 until min(
                    startIndex + dictionary.longestKeyLength,
                    prepWord.length
                ) + 1).forEach { endIndex ->
                    val possibleKey = prepWord.substring(startIndex, endIndex)
                    dictionary.patterns[possibleKey]?.let { pattern ->
                        val (offset, values) = pattern
                        val slice = startIndex + offset - 1 until startIndex + offset + values.size
                        values.zip(scoreArray.sliceArray(slice).toList())
                            .mapIndexed { index, pair ->
                                scoreArray[slice.first + index] = max(pair.first, pair.second)
                            }
                    }
                }
            }
            val scoreList = scoreArray.drop(1).dropLast(1).toList()
            word.mapIndexedNotNull { index, char ->
                if (index + 1 <= dictionary.leftHyphenMin || word.length - (index + 1) < dictionary.rightHyphenMin || scoreList[index] % 2 == 0) {
                    char
                } else {
                    "$char$hyphen"
                }
            }.joinToString("").also { lruCache.put(word, it) }
        }
        val spaces = string.split(Regex("\\S")).filter { it.isNotEmpty() }
        return (((spaces.size - hyphenatedWords.size).takeIf { it > 0 }
            ?.let { (0..it).map { "" } } ?: listOf()) + hyphenatedWords)
            .zip(spaces + ((hyphenatedWords.size - spaces.size).takeIf { it > 0 }
                ?.let { (0..it).map { "" } } ?: listOf())).joinToString("") { (word, space) ->
                    "$word$space"
            }
    }
}

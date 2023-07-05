package io.soldierinwhite.haaivin

import org.junit.Assert.assertEquals
import org.junit.Test
import java.io.File

internal class HaaivinTest {
    private val haaivin = Haaivin(listOf(HunspellDictionary("sv") {
        File("src/test/assets/hyph_sv.dic").inputStream()
    }))

    @Test
    fun testAllWords() {
        val inputStream = File("src/test/assets/swe_wordlist.txt").inputStream().bufferedReader()
        val outputStream = File("src/test/assets/hyphenated_swe_wordlist.txt").inputStream().bufferedReader()

        inputStream.forEachLine { line ->
            assertEquals(outputStream.readLine(), haaivin.hyphenate(line, '-', "sv"))
        }
    }
}
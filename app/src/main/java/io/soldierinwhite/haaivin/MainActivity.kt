package io.soldierinwhite.haaivin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.os.LocaleListCompat
import io.soldierinwhite.haaivin.ui.theme.HaaivinTheme

class MainActivity : ComponentActivity() {
    private lateinit var haaivin: Haaivin
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags("sv"))

        haaivin = Haaivin(hunspellDictionaries = listOf(
            HunspellDictionary("sv") { assets.open("hyph_sv.dic") },
            HunspellDictionary("en") { assets.open("hyph_en_GB.dic") }
        ))
        setContent {
            HaaivinTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val testString = "Bostadsmarknaden"
                    TextTester(
                        text = testString,
                        otherText = haaivin.hyphenate(testString, dictionaryId = "sv")
                    )
                }
            }
        }
    }
}

@Composable
fun TextTester(text: String, otherText: String) {
    Row(modifier = Modifier.verticalScroll(rememberScrollState())) {
        IncreasingSizeTextColumn(
            text = text,
            modifier = Modifier
                .weight(1f)
                .padding(end = 4.dp)
        )
        IncreasingSizeTextColumn(
            text = otherText,
            modifier = Modifier
                .weight(1f)
                .padding(start = 4.dp),
        )
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun IncreasingSizeTextColumn(text: String, modifier: Modifier) {
    Column(
        modifier = modifier
            .padding(16.dp)
    ) {
        var startSize = 12f
        while (startSize < 128) {
            Text(
                text = text,
                fontSize = startSize.sp,
                style = MaterialTheme.typography.bodyLarge.copy(
                    hyphens = Hyphens.Auto,
                    lineHeight = MaterialTheme.typography.bodyLarge.lineHeight * (startSize / 20f)
                )
            )
            startSize *= 1.125f
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HaaivinTheme {
        TextTester("Condominium", "Con\u00ADdo\u00ADmini\u00ADum")
    }
}
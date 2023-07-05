package io.soldierinwhite.haaivin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import io.soldierinwhite.haaivin.ui.theme.HaaivinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HaaivinTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HyphenatedText()
                }
            }
        }
    }
}

@Composable
fun HyphenatedText(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val string = "Mogen som en frukt ligger världen i min famn,\n" +
            "den har mognat i natt,\n" +
            "och skalet är den tunna blå hinnan som spänner sig\n" +
            "                                                    bubblerund,\n" +
            "och saften är det söta och doftande, rinnande, brinnande\n" +
            "                                                    solljusflödet.\n" +
            "\n" +
            "Och ut i det genomskinliga alltet springer jag som simmare,\n" +
            "dränkt i en mognads dop och född till en mognads makt.\n" +
            "Helgad till handling,\n" +
            "lätt som ett skratt\n" +
            "klyver jag ett gyllene honungshav, som begär mina \n" +
            "                                                    hungriga händer."
    Column {
        Text(
            text = string,
            modifier = modifier.padding(bottom = 32.dp)
        )
        Text(
            text = Haaivin(hunspellDictionaries = listOf(HunspellDictionary("sv") {
                context.assets.open("hyph_sv.dic")
            })).hyphenate(string, '-', "sv"),
            modifier = modifier
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    HaaivinTheme {
        HyphenatedText()
    }
}
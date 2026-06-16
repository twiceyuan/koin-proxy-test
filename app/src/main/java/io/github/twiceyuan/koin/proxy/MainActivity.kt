package io.github.twiceyuan.koin.proxy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.twiceyuan.koin.direct.example.directKoinExampleValue
import io.github.twiceyuan.koin.proxy.di.injectSingle
import io.github.twiceyuan.koin.proxy.example.proxyExampleValue
import io.github.twiceyuan.koin.proxy.ui.theme.KoinproxyTheme

class MainActivity : ComponentActivity() {
    private val greetingRepository: GreetingRepository by injectSingle(GreetingRepository::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val text = listOf(
            greetingRepository.greeting("Android"),
            proxyExampleValue(),
            directKoinExampleValue(),
        ).joinToString(separator = "\n")
        setContent {
            KoinproxyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        text = text,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KoinproxyTheme {
        Greeting("Android")
    }
}

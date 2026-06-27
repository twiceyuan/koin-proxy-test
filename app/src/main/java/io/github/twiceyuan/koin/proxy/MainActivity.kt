package io.github.twiceyuan.koin.proxy

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import io.github.twiceyuan.koin.direct.example.directFactoryExampleValue
import io.github.twiceyuan.koin.direct.example.directKoinExampleValue
import io.github.twiceyuan.koin.direct.example.directNamedExampleValue
import io.github.twiceyuan.koin.direct.example.directScopeExampleValue
import io.github.twiceyuan.koin.direct.example.loadDirectFactoryExampleModule
import io.github.twiceyuan.koin.direct.example.loadDirectNamedExampleModule
import io.github.twiceyuan.koin.direct.example.loadDirectScopeExampleModule
import io.github.twiceyuan.koin.proxy.di.createScope
import io.github.twiceyuan.koin.proxy.di.getFactory
import io.github.twiceyuan.koin.proxy.di.getSingle
import io.github.twiceyuan.koin.proxy.di.injectSingle
import io.github.twiceyuan.koin.proxy.di.named
import io.github.twiceyuan.koin.proxy.example.factoryExampleValue
import io.github.twiceyuan.koin.proxy.example.namedExampleValue
import io.github.twiceyuan.koin.proxy.example.proxyExampleValue
import io.github.twiceyuan.koin.proxy.example.scopeExampleModule
import io.github.twiceyuan.koin.proxy.example.scopeExampleValue
import io.github.twiceyuan.koin.proxy.ui.theme.KoinproxyTheme

class MainActivity : ComponentActivity() {

    // single 懒注入
    private val greetingRepository: GreetingRepository by injectSingle()

    // named 懒注入
    private val helloProvider: MessageProvider by injectSingle(named("hello"))
    private val welcomeProvider: MessageProvider by injectSingle(named("welcome"))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 加载 direct 示例的额外模块
        loadDirectFactoryExampleModule()
        loadDirectNamedExampleModule()
        loadDirectScopeExampleModule()

        val texts = listOf(
            // single 示例
            "=== Single ===",
            greetingRepository.greeting("Android"),
            proxyExampleValue(),
            directKoinExampleValue(),

            // named 限定符示例
            "\n=== Named Qualifier ===",
            "hello: ${helloProvider.message()}",
            "welcome: ${welcomeProvider.message()}",
            namedExampleValue(),
            directNamedExampleValue(),

            // factory 示例（每次获取新实例）
            "\n=== Factory ===",
            factoryExampleValue(),
            directFactoryExampleValue(),
            "factory from activity: ${getFactory<TimestampProvider>().timestamp()} vs ${getFactory<TimestampProvider>().timestamp()}",

            // scope 示例
            "\n=== Scope ===",
            scopeExampleValue(),
            directScopeExampleValue(),

            // 动态模块加载示例
            "\n=== Dynamic Module ===",
            "Use loadKoinProxyModules() / unloadKoinProxyModules() for dynamic loading",
        )

        setContent {
            KoinproxyTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        texts = texts,
                        modifier = Modifier.padding(innerPadding),
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(texts: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier) {
        texts.forEach { text ->
            Text(text = text)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    KoinproxyTheme {
        Greeting(listOf("Hello Android"))
    }
}

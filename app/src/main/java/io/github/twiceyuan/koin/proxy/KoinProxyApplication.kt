package io.github.twiceyuan.koin.proxy

import android.app.Application
import io.github.twiceyuan.koin.direct.example.loadDirectKoinExampleModule
import io.github.twiceyuan.koin.proxy.di.startKoinProxy
import io.github.twiceyuan.koin.proxy.example.proxyExampleModule

class KoinProxyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoinProxy(
            modules = listOf(
                appModule,
                proxyExampleModule,
            )
        )
        loadDirectKoinExampleModule()
    }
}

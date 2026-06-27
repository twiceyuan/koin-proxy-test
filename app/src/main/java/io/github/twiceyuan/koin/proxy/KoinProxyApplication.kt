package io.github.twiceyuan.koin.proxy

import android.app.Application
import io.github.twiceyuan.koin.direct.example.loadDirectKoinExampleModule
import io.github.twiceyuan.koin.proxy.di.startKoinProxy
import io.github.twiceyuan.koin.proxy.example.factoryExampleModule
import io.github.twiceyuan.koin.proxy.example.namedExampleModule
import io.github.twiceyuan.koin.proxy.example.proxyExampleModule
import io.github.twiceyuan.koin.proxy.example.scopeExampleModule

class KoinProxyApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoinProxy(
            modules = listOf(
                appModule,
                proxyExampleModule,
                factoryExampleModule,
                namedExampleModule,
                scopeExampleModule,
            )
        )
        loadDirectKoinExampleModule()
    }
}

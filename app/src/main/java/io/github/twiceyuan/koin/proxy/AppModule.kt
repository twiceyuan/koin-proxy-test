package io.github.twiceyuan.koin.proxy

import io.github.twiceyuan.koin.proxy.di.koinProxyModule

val appModule = koinProxyModule {
    single { GreetingRepository() }
}

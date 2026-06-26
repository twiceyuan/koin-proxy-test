package io.github.twiceyuan.koin.proxy.example

import io.github.twiceyuan.koin.proxy.di.getSingle
import io.github.twiceyuan.koin.proxy.di.koinProxyModule

class ProxyExampleService {
    fun value(): String = "value from koin-proxy module"
}

val proxyExampleModule = koinProxyModule {
    single { ProxyExampleService() }
}

fun proxyExampleValue(): String {
    return getSingle<ProxyExampleService>().value()
}

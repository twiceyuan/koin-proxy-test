package io.github.twiceyuan.koin.proxy

import io.github.twiceyuan.koin.proxy.di.koinProxyModule
import io.github.twiceyuan.koin.proxy.di.named

// region 接口和实现（用于演示 named 限定符）

interface MessageProvider {
    fun message(): String
}

class HelloMessageProvider : MessageProvider {
    override fun message(): String = "Hello"
}

class WelcomeMessageProvider : MessageProvider {
    override fun message(): String = "Welcome"
}

// endregion

// region 工厂类（用于演示 factory）

class TimestampProvider {
    private val timestamp = System.currentTimeMillis()
    fun timestamp(): Long = timestamp
}

// endregion

val appModule = koinProxyModule {
    // single：全局单例
    single { GreetingRepository() }

    // single + named：同类型不同限定符
    single<MessageProvider>(named("hello")) { HelloMessageProvider() }
    single<MessageProvider>(named("welcome")) { WelcomeMessageProvider() }

    // factory：每次获取创建新实例
    factory { TimestampProvider() }
}

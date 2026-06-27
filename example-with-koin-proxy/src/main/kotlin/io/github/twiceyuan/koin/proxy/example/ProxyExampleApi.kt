package io.github.twiceyuan.koin.proxy.example

import io.github.twiceyuan.koin.proxy.di.createScope
import io.github.twiceyuan.koin.proxy.di.getFactory
import io.github.twiceyuan.koin.proxy.di.getSingle
import io.github.twiceyuan.koin.proxy.di.koinProxyModule
import io.github.twiceyuan.koin.proxy.di.loadKoinProxyModules
import io.github.twiceyuan.koin.proxy.di.named
import io.github.twiceyuan.koin.proxy.di.unloadKoinProxyModules

// region 基础 single 示例

class ProxyExampleService {
    fun value(): String = "value from koin-proxy module"
}

val proxyExampleModule = koinProxyModule {
    single { ProxyExampleService() }
}

fun proxyExampleValue(): String {
    return getSingle<ProxyExampleService>().value()
}

// endregion

// region factory 示例（每次获取创建新实例）

class RequestIdGenerator {
    private val id = System.nanoTime()
    fun requestId(): String = "request-$id"
}

val factoryExampleModule = koinProxyModule {
    factory { RequestIdGenerator() }
}

fun factoryExampleValue(): String {
    val gen1 = getFactory<RequestIdGenerator>()
    val gen2 = getFactory<RequestIdGenerator>()
    return "factory: ${gen1.requestId()} vs ${gen2.requestId()} (different = ${gen1.requestId() != gen2.requestId()})"
}

// endregion

// region named 限定符示例

interface ConfigProvider {
    fun config(): String
}

class DevConfigProvider : ConfigProvider {
    override fun config(): String = "dev-config"
}

class ProdConfigProvider : ConfigProvider {
    override fun config(): String = "prod-config"
}

val namedExampleModule = koinProxyModule {
    single<ConfigProvider>(named("dev")) { DevConfigProvider() }
    single<ConfigProvider>(named("prod")) { ProdConfigProvider() }
}

fun namedExampleValue(): String {
    val devConfig = getSingle<ConfigProvider>(named("dev")).config()
    val prodConfig = getSingle<ConfigProvider>(named("prod")).config()
    return "named: dev=$devConfig, prod=$prodConfig"
}

// endregion

// region scope 示例

interface SessionData {
    fun sessionId(): String
}

class UserSession(private val userId: String) : SessionData {
    private val timestamp = System.currentTimeMillis()
    override fun sessionId(): String = "session-$userId-$timestamp"
}

class CartSession(private val userId: String) : SessionData {
    private val items = mutableListOf<String>()
    override fun sessionId(): String = "cart-$userId-${items.size}"

    fun addItem(item: String) {
        items.add(item)
    }
}

val scopeExampleModule = koinProxyModule {
    scope(named("user-session")) {
        scoped(SessionData::class) { UserSession("user-123") }
    }
    scope(named("cart-session")) {
        scoped(SessionData::class) { CartSession("user-123") }
    }
}

fun scopeExampleValue(): String {
    // 创建用户会话作用域
    val userScope = createScope("user-session-1", named("user-session"))
    val userSession = userScope.get(SessionData::class)

    // 创建购物车作用域
    val cartScope = createScope("cart-session-1", named("cart-session"))
    val cartSession = cartScope.get(SessionData::class)

    val result = "scope: user=${userSession.sessionId()}, cart=${cartSession.sessionId()}"

    // 关闭作用域
    userScope.close()
    cartScope.close()

    return result
}

// endregion

// region 动态模块加载示例

class DynamicService {
    fun value(): String = "dynamic module loaded!"
}

val dynamicModule = koinProxyModule {
    single { DynamicService() }
}

fun loadDynamicModule() {
    loadKoinProxyModules(dynamicModule)
}

fun unloadDynamicModule() {
    unloadKoinProxyModules(dynamicModule)
}

fun dynamicExampleValue(): String {
    return getSingle<DynamicService>().value()
}

// endregion

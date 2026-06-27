package io.github.twiceyuan.koin.direct.example

import org.koin.core.context.GlobalContext
import org.koin.core.qualifier.named
import org.koin.dsl.module

// region 基础 single 示例

class DirectKoinExampleService {
    fun value(): String = "value from direct Koin module"
}

private val directKoinExampleModule = module {
    single { DirectKoinExampleService() }
}

fun loadDirectKoinExampleModule() {
    GlobalContext.get().loadModules(listOf(directKoinExampleModule))
}

fun directKoinExampleValue(): String {
    return GlobalContext.get().get<DirectKoinExampleService>().value()
}

// endregion

// region factory 示例（每次获取创建新实例）

class DirectRequestIdGenerator {
    private val id = System.nanoTime()
    fun requestId(): String = "request-$id"
}

private val directFactoryExampleModule = module {
    factory { DirectRequestIdGenerator() }
}

fun loadDirectFactoryExampleModule() {
    GlobalContext.get().loadModules(listOf(directFactoryExampleModule))
}

fun directFactoryExampleValue(): String {
    val gen1 = GlobalContext.get().get<DirectRequestIdGenerator>()
    val gen2 = GlobalContext.get().get<DirectRequestIdGenerator>()
    return "factory: ${gen1.requestId()} vs ${gen2.requestId()} (different = ${gen1.requestId() != gen2.requestId()})"
}

// endregion

// region named 限定符示例

interface DirectConfigProvider {
    fun config(): String
}

class DirectDevConfigProvider : DirectConfigProvider {
    override fun config(): String = "dev-config"
}

class DirectProdConfigProvider : DirectConfigProvider {
    override fun config(): String = "prod-config"
}

private val directNamedExampleModule = module {
    single<DirectConfigProvider>(named("dev")) { DirectDevConfigProvider() }
    single<DirectConfigProvider>(named("prod")) { DirectProdConfigProvider() }
}

fun loadDirectNamedExampleModule() {
    GlobalContext.get().loadModules(listOf(directNamedExampleModule))
}

fun directNamedExampleValue(): String {
    val devConfig = GlobalContext.get().get<DirectConfigProvider>(named("dev")).config()
    val prodConfig = GlobalContext.get().get<DirectConfigProvider>(named("prod")).config()
    return "named: dev=$devConfig, prod=$prodConfig"
}

// endregion

// region scope 示例

interface DirectSessionData {
    fun sessionId(): String
}

class DirectUserSession(private val userId: String) : DirectSessionData {
    private val timestamp = System.currentTimeMillis()
    override fun sessionId(): String = "session-$userId-$timestamp"
}

class DirectCartSession(private val userId: String) : DirectSessionData {
    private val items = mutableListOf<String>()
    override fun sessionId(): String = "cart-$userId-${items.size}"

    fun addItem(item: String) {
        items.add(item)
    }
}

private val directScopeExampleModule = module {
    scope(named("user-session")) {
        scoped<DirectSessionData> { DirectUserSession("user-123") }
    }
    scope(named("cart-session")) {
        scoped<DirectSessionData> { DirectCartSession("user-123") }
    }
}

fun loadDirectScopeExampleModule() {
    GlobalContext.get().loadModules(listOf(directScopeExampleModule))
}

fun directScopeExampleValue(): String {
    val koin = GlobalContext.get()

    // 创建用户会话作用域
    val userScope = koin.createScope("user-session-1", named("user-session"))
    val userSession = userScope.get<DirectSessionData>()

    // 创建购物车作用域
    val cartScope = koin.createScope("cart-session-1", named("cart-session"))
    val cartSession = cartScope.get<DirectSessionData>()

    val result = "scope: user=${userSession.sessionId()}, cart=${cartSession.sessionId()}"

    // 关闭作用域
    userScope.close()
    cartScope.close()

    return result
}

// endregion

// region 动态模块加载示例

class DirectDynamicService {
    fun value(): String = "dynamic module loaded!"
}

private val directDynamicModule = module {
    single { DirectDynamicService() }
}

fun loadDirectDynamicModule() {
    GlobalContext.get().loadModules(listOf(directDynamicModule))
}

fun unloadDirectDynamicModule() {
    GlobalContext.get().unloadModules(listOf(directDynamicModule))
}

fun directDynamicExampleValue(): String {
    return GlobalContext.get().get<DirectDynamicService>().value()
}

// endregion

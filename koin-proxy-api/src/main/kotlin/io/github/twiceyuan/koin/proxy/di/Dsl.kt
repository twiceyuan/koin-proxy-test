package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

// region 模块定义 DSL

fun koinProxyModule(declaration: KoinProxyModuleBuilder.() -> Unit): KoinProxyModule {
    return KoinProxyModuleBuilder().apply(declaration).build()
}

// endregion

// region 获取实例 - Single

fun <T : Any> getSingle(type: KClass<T>, qualifier: KoinProxyQualifier? = null): T {
    return KoinProxyRuntime.get(type, qualifier)
}

inline fun <reified T : Any> getSingle(qualifier: KoinProxyQualifier? = null): T {
    return getSingle(T::class, qualifier)
}

fun <T : Any> injectSingle(type: KClass<T>, qualifier: KoinProxyQualifier? = null): Lazy<T> {
    return lazy { getSingle(type, qualifier) }
}

inline fun <reified T : Any> injectSingle(qualifier: KoinProxyQualifier? = null): Lazy<T> {
    return injectSingle(T::class, qualifier)
}

// endregion

// region 获取实例 - Factory

fun <T : Any> getFactory(type: KClass<T>, qualifier: KoinProxyQualifier? = null): T {
    return KoinProxyRuntime.get(type, qualifier)
}

inline fun <reified T : Any> getFactory(qualifier: KoinProxyQualifier? = null): T {
    return getFactory(T::class, qualifier)
}

fun <T : Any> injectFactory(type: KClass<T>, qualifier: KoinProxyQualifier? = null): Lazy<T> {
    return lazy { getFactory(type, qualifier) }
}

inline fun <reified T : Any> injectFactory(qualifier: KoinProxyQualifier? = null): Lazy<T> {
    return injectFactory(T::class, qualifier)
}

// endregion

// region Scope 管理

/** 创建一个新的作用域实例 */
fun createScope(scopeId: String, qualifier: KoinProxyQualifier): KoinProxyScope {
    return KoinProxyRuntime.getScopeRegistry().createScope(scopeId, qualifier)
}

/** 获取已存在的作用域 */
fun getScope(scopeId: String): KoinProxyScope? {
    return KoinProxyRuntime.getScopeRegistry().getScope(scopeId)
}

// endregion

// region 动态模块管理

fun loadKoinProxyModules(vararg modules: KoinProxyModule) {
    KoinProxyRuntime.loadModules(modules.toList())
}

fun unloadKoinProxyModules(vararg modules: KoinProxyModule) {
    KoinProxyRuntime.unloadModules(modules.toList())
}

// endregion

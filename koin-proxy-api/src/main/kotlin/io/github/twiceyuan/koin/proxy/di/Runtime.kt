package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

/** 全局运行时，持有 resolver 引用 */
object KoinProxyRuntime {
    @Volatile
    private var resolver: KoinProxyResolver? = null

    fun installResolver(resolver: KoinProxyResolver) {
        this.resolver = resolver
    }

    fun <T : Any> get(type: KClass<T>, qualifier: KoinProxyQualifier? = null): T {
        return checkNotNull(resolver) { "KoinProxyRuntime has not been started." }
            .get(type, qualifier)
    }

    fun loadModules(modules: List<KoinProxyModule>) {
        checkNotNull(resolver) { "KoinProxyRuntime has not been started." }
            .loadModules(modules)
    }

    fun unloadModules(modules: List<KoinProxyModule>) {
        checkNotNull(resolver) { "KoinProxyRuntime has not been started." }
            .unloadModules(modules)
    }

    fun getScopeRegistry(): KoinProxyScopeRegistry {
        return checkNotNull(resolver) { "KoinProxyRuntime has not been started." }
            .getScopeRegistry()
    }
}

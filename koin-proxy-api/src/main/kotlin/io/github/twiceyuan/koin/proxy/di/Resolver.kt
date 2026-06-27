package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

/** 解析器接口，由 koin-3-proxy 或 koin-4-proxy 实现 */
interface KoinProxyResolver {
    fun <T : Any> get(type: KClass<T>, qualifier: KoinProxyQualifier? = null): T
    fun loadModules(modules: List<KoinProxyModule>)
    fun unloadModules(modules: List<KoinProxyModule>)
    fun getScopeRegistry(): KoinProxyScopeRegistry
}

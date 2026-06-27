package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

/** 作用域定义 */
class KoinProxyScopeDefinition(
    val qualifier: KoinProxyQualifier,
    val definitions: List<KoinProxyDefinition<*>>,
)

/** 作用域构建器 */
class KoinProxyScopeBuilder(val scopeQualifier: KoinProxyQualifier) {
    private val definitions = mutableListOf<KoinProxyDefinition<*>>()

    fun <T : Any> scoped(
        type: KClass<T>,
        factory: () -> T,
    ) {
        definitions += KoinProxyDefinition(type, factory, DefinitionKind.SCOPED)
    }

    inline fun <reified T : Any> scoped(noinline factory: () -> T) {
        scoped(T::class, factory)
    }

    fun build(): KoinProxyScopeDefinition = KoinProxyScopeDefinition(
        qualifier = scopeQualifier,
        definitions = definitions.toList(),
    )
}

/** 已创建的作用域实例 */
interface KoinProxyScope {
    /** 从作用域获取实例 */
    fun <T : Any> get(type: KClass<T>, qualifier: KoinProxyQualifier? = null): T

    /** 关闭作用域，释放所有 scoped 实例 */
    fun close()
}

/** 作用域注册表，用于创建和管理作用域 */
interface KoinProxyScopeRegistry {
    /** 创建一个新的作用域实例 */
    fun createScope(scopeId: String, qualifier: KoinProxyQualifier): KoinProxyScope

    /** 获取已存在的作用域 */
    fun getScope(scopeId: String): KoinProxyScope?
}

// region 顶层便捷函数

inline fun <reified T : Any> KoinProxyScope.get(): T {
    return get(T::class)
}

// endregion

package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

/** 模块容器 */
class KoinProxyModule(
    val definitions: List<KoinProxyDefinition<*>>,
    val scopeDefinitions: List<KoinProxyScopeDefinition>,
)

/** 模块构建器 */
class KoinProxyModuleBuilder {
    private val definitions = mutableListOf<KoinProxyDefinition<*>>()
    private val scopeDefinitions = mutableListOf<KoinProxyScopeDefinition>()

    // region single 注册

    fun <T : Any> single(
        type: KClass<T>,
        qualifier: KoinProxyQualifier? = null,
        factory: () -> T,
    ) {
        definitions += KoinProxyDefinition(type, factory, DefinitionKind.SINGLE, qualifier)
    }

    inline fun <reified T : Any> single(
        qualifier: KoinProxyQualifier? = null,
        noinline factory: () -> T,
    ) {
        single(T::class, qualifier, factory)
    }

    // endregion

    // region factory 注册

    fun <T : Any> factory(
        type: KClass<T>,
        qualifier: KoinProxyQualifier? = null,
        factory: () -> T,
    ) {
        definitions += KoinProxyDefinition(type, factory, DefinitionKind.FACTORY, qualifier)
    }

    inline fun <reified T : Any> factory(
        qualifier: KoinProxyQualifier? = null,
        noinline factory: () -> T,
    ) {
        factory(T::class, qualifier, factory)
    }

    // endregion

    // region scope 注册

    fun scope(
        qualifier: KoinProxyQualifier,
        block: KoinProxyScopeBuilder.() -> Unit,
    ) {
        scopeDefinitions += KoinProxyScopeBuilder(qualifier).apply(block).build()
    }

    // endregion

    fun build(): KoinProxyModule = KoinProxyModule(
        definitions = definitions.toList(),
        scopeDefinitions = scopeDefinitions.toList(),
    )
}

package io.github.twiceyuan.koin.proxy.di

import kotlin.reflect.KClass

/** 服务定义 */
class KoinProxyDefinition<T : Any>(
    val type: KClass<T>,
    val factory: () -> T,
    val kind: DefinitionKind = DefinitionKind.SINGLE,
    val qualifier: KoinProxyQualifier? = null,
)
